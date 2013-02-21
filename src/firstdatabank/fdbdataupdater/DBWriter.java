package dbank.fdbdataupdater;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdater;
import dbank.utils.SynchronizedQueue;
/**
 * Overridden FDB Updater DBWriter. Allows for direct use of an existing datasource, thus avoiding the security and
 * maintenance problems associated with storing the database username and password in a configuration file.
 */
public class DBWriter extends Thread {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DBWriter.class);
    private final SynchronizedQueue m_objQueue;
    private final Connection m_objConnection;
    private final int m_intCommitFactor;
    private final String m_arrSQL[];
    private final PreparedStatement m_arrStatements[];
    private final long m_arrStatementAge[];
    private Statement m_objStmt;
    private boolean m_bClosed;
    private long m_lngStatements;
    private long m_lngStatementsSinceCommit;
    private int m_intCommits;
    public static String transIsoToString(int iso) {
        switch (iso) {
            case 0: // '\0'
                return "TRANSACTION_NONE";
            case 1: // '\001'
                return "TRANSACTION_READ_UNCOMMITTED";
            case 2: // '\002'
                return "TRANSACTION_READ_COMMITTED";
            case 4: // '\004'
                return "TRANSACTION_REPEATABLE_READ";
            case 8: // '\b'
                return "TRANSACTION_SERIALIZABLE";
            case 3: // '\003'
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            default:
                return "(UNKNOWN)";
        }
    }
    public DBWriter(String URL, String userName, String password, int numStatements, int commitFactor,
                    SynchronizedQueue queue) throws SQLException {
        this(false, URL, userName, password, numStatements, commitFactor, queue);
    }
    public DBWriter(boolean forceAuto, String URL, String userName, String password, int numStatements, int commitFactor,
                    SynchronizedQueue queue) throws SQLException {
        m_objStmt = null;
        m_bClosed = false;
        m_lngStatements = 0L;
        m_lngStatementsSinceCommit = 0L;
        m_intCommits = 0;
        m_objQueue = queue;
        m_intCommitFactor = forceAuto ? 1 : commitFactor;
        // overridden to use datasource instead of a separate JDBC connection
        m_objConnection = SettingsData.getInstance().getConnection();
        m_objConnection.setAutoCommit(isAutoCommit());
        m_arrSQL = new String[numStatements];
        m_arrStatements = new PreparedStatement[numStatements];
        m_arrStatementAge = new long[numStatements];
    }
    public long getStatements() {
        return m_lngStatements;
    }
    public int getCommits() {
        return m_intCommits;
    }
    public boolean isAutoCommit() {
        return m_intCommitFactor == 1;
    }
    public int getTransactionIsolation() {
        int retval = -1;
        if (m_objConnection == null) {
            return retval;
        }
        try {
            retval = m_objConnection.getTransactionIsolation();
        }
        catch (SQLException sqlexception) {
        }
        return retval;
    }
    public boolean isSingleTransaction() {
        return m_intCommitFactor < 1;
    }
    public boolean isClosed() {
        return m_bClosed;
    }
    public void run() {
        do {
            if (isClosed()) {
                return;
            }
            if (m_objQueue.isAborting()) {
                close();
                return;
            }
            DMLRowData o = (DMLRowData) m_objQueue.dequeue();
            if (o == null) {
                close();
                return;
            }
            try {
                if (o.getBindCount() <= 0) {
                    if (m_objStmt == null) {
                        m_objStmt = m_objConnection.createStatement();
                    }
                    m_objStmt.executeUpdate(o.getSQL());
                    m_lngStatementsSinceCommit++;
                    m_lngStatements++;
                    if (!SettingsData.getInstance().getReuseStatements()) {
                        m_objStmt.close();
                        m_objStmt = null;
                    }
                }
                else {
                    PreparedStatement stmt = getStatement(o.getSQL());
                    for (int i = 0; i < o.getBindCount(); i++) {
                        o.getType(i).SQLBind(stmt, i + 1, o.getBind(i));
                    }
                    stmt.executeUpdate();
                    if (!SettingsData.getInstance().getReuseStatements()) {
                        closeStatement(stmt);
                    }
                }
                if ((m_lngStatementsSinceCommit > m_intCommitFactor) && !isSingleTransaction() && !isAutoCommit()) {
                    doCommit();
                }
            }
            catch (Exception ex) {
                // callback
                DifUpdater.setException(ex);
                
                LOG.error("FDB DB Writer failed", ex);
                LOG.error("Failed SQL :" + o.getSQL());
                
                m_objQueue.abort(ex);
                close();
                return;
            }
        }
        while (true);
    }
    public synchronized void close() {
        if (m_bClosed) {
            return;
        }
        m_bClosed = true;
        if (m_objStmt != null) {
            try {
                m_objStmt.close();
            }
            catch (SQLException sqlexception) {
            }
            finally {
                m_objStmt = null;
            }
        }
        for (int i = 0; i < m_arrStatements.length; i++) {
            try {
                if (m_arrStatements[i] != null) {
                    m_arrStatements[i].close();
                }
            }
            catch (SQLException sqlexception1) {
            }
            m_arrStatements[i] = null;
            m_arrSQL[i] = null;
        }
        try {
            if (!isAutoCommit()) {
                if (m_objQueue.isAborting()) {
                    m_objConnection.rollback();
                }
                else {
                    doCommit();
                }
            }
        }
        catch (SQLException ex1) {
            m_objQueue.abort(ex1);
        }
        finally {
            try {
                m_objConnection.close();
            }
            catch (SQLException sqlexception2) {
            }
        }
    }
    private void closeStatement(Statement stmt) throws SQLException {
        if (stmt == null) {
            return;
        }
        for (int i = 0; i < m_arrStatements.length; i++) {
            if (m_arrStatements[i] == stmt) {
                m_arrStatements[i].close();
                m_arrStatements[i] = null;
                m_arrSQL[i] = null;
                m_arrStatementAge[i] = 0L;
                return;
            }
        }
    }
    private PreparedStatement getStatement(String SQL) throws SQLException {
        int bestIndex = 0;
        long bestAge = 0x7fffffffffffffffL;
        for (int i = 1; i < m_arrStatements.length; i++) {
            if (SQL.equals(m_arrSQL[i])) {
                bestIndex = i;
                break;
            }
            if (m_arrStatements[i] == null) {
                bestIndex = i;
                break;
            }
            if (m_arrStatementAge[i] < bestAge) {
                bestAge = m_arrStatementAge[i];
                bestIndex = i;
            }
        }
        m_arrStatementAge[bestIndex] = ++m_lngStatements;
        m_lngStatementsSinceCommit++;
        if (!SQL.equals(m_arrSQL[bestIndex])) {
            SettingsData sd = SettingsData.getInstance();
            if (sd.getTraceLevel() >= 4) {
                sd.addTrace(4, String.valueOf(String.valueOf(this)).concat(" New Statement."));
            }
            if (sd.commitOnNewStatement() && (getStatements() > 0) && !isAutoCommit() && !isSingleTransaction()) {
                doCommit();
            }
            if (m_arrStatements[bestIndex] != null) {
                m_arrStatements[bestIndex].close();
            }
            m_arrStatements[bestIndex] = m_objConnection.prepareStatement(SQL);
            if (sd.getQueryTimeout() >= 0) {
                m_arrStatements[bestIndex].setQueryTimeout(sd.getQueryTimeout());
            }
            m_arrSQL[bestIndex] = SQL;
        }
        return m_arrStatements[bestIndex];
    }
    void doCommit() throws SQLException {
        m_intCommits++;
        m_lngStatementsSinceCommit = 0;
        m_objConnection.commit();
        if (SettingsData.getInstance().getTraceLevel() >= 4) {
            SettingsData.getInstance().addTrace(4, String.valueOf(String.valueOf(this)).concat(" Commit."));
        }
        if (SettingsData.getInstance().closeStatementOnCommit()) {
            if (SettingsData.getInstance().getTraceLevel() >= 4) {
                SettingsData.getInstance().addTrace(4,
                    String.valueOf(String.valueOf(this)).concat(" Closing Statement(s)..."));
            }
            if (m_objStmt != null) {
                try {
                    m_objStmt.close();
                }
                catch (SQLException sqlexception) {
                }
                m_objStmt = null;
            }
            for (int i = 0; i < m_arrStatements.length; i++) {
                m_arrSQL[i] = null;
                m_arrStatementAge[i] = 0L;
                if (m_arrStatements[i] == null) {
                    continue;
                }
                try {
                    m_arrStatements[i].close();
                }
                catch (SQLException sqlexception1) {
                }
                m_arrStatements[i] = null;
            }
        }
    }
