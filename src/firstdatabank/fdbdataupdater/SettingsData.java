package dbank.fdbdataupdater;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.DifUpdater;
/**
 * Overridden FDB Updater settings. Provides code level integration and avoids security issues by absolving the need to write
 * the database username/password settings to a temporary file.
 * 
 * The update settings have been detuned performance wise to support Cache. For example, to throws deadlock errors if more
 * than one connection is used and throws an "undefined" transaction error if prepared statements are reused or the batch
 * commit size set high. Oracle displays no such errors.
 */
public class SettingsData {
    private static SettingsData INSTANCE;
    private DataSource dataSource;
    private File source;
    private int batchCommitSize;
    private DifUpdater.DifUpdateCallback callback;
    /**
     * Singleton instance.
     * 
     * @return instance
     */
    public static SettingsData getInstance() {
        return INSTANCE;
    }
    /**
     * Private constructor.
     */
    private SettingsData() {
        INSTANCE = this;
    }
    /**
     * Set the log callback.
     * 
     * @param callback log callback
     */
    public void setCallback(DifUpdater.DifUpdateCallback callback) {
        this.callback = callback;
    }
    /**
     * Set the batch commit size.
     * 
     * size <= 0: single commit size >= 1: batch commit
     * 
     * @param size batch commit size
     */
    public void setBatchCommitSize(int size) {
        this.batchCommitSize = size;
    }
    public String getDriver() {
        return null; // not used
    }
    public String getURL() {
        return null; // not used
    }
    public String getUserName() {
        return null; // not used
    }
    public String getPassword() {
        return null; // not used
    }
    public int getConnections() {
        /* Note: Cache throws locking errors with more than one connection */
        return 1;
    }
    public EnumCommitType getCommitType() {
        return (batchCommitSize >= 1) ? EnumCommitType.enuBatch : EnumCommitType.enuSingleTransaction;
    }
    public int getActionType() {
        return 0; // incremental update
    }
    public void setProductSourceFile(File source) {
        this.source = source;
    }
    public File getProductSourceFile() throws FileNotFoundException {
        if ((source == null) || !source.exists() || !source.canRead()) {
            throw new FileNotFoundException("Unable to locate and/or read DIF update file: " + source);
        }
        return source;
    }
    public int getCommitIntervalConn() {
        return batchCommitSize; // should be low (e.g., 200) to support Cache
    }
    public int getStatementsPerConnFull() {
        return 1; // not used
    }
    public int getStatementsPerConnInc() {
        return 1; // not used
    }
    public boolean closeStatementOnCommit() {
        return true; // to support Cache
    }
    public boolean commitOnNewStatement() {
        return false;
    }
    public int getLoginTimeout() {
        return -1;
    }
    public int getQueryTimeout() {
        return -1;
    }
    public int getTraceLevel() {
        return 0;
    }
    public boolean getReuseStatements() {
        return false; // Cache throws random transaction errors if statements are reused
    }
    public boolean getAvoidDeadlock() {
        return false;
    }
    public boolean usePreparedStatements() {
        return true;
    }
    public boolean useHybridPS() {
        return false;
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public void addTrace(int level, String message) {
        if (callback != null) {
            callback.log(message);
        }
    }
    public String[] headLines() {
        return new String[] {};
    }
    public void validate() throws IllegalArgumentException {
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public String toString() {
        ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        b.append("Source", source);
        b.append("Connections", getConnections());
        b.append("Commit Type", EnumCommitType.enuBatch.equals(getCommitType()) ? "BATCH_COMMIT" : "SINGLE_COMMIT");
        b.append("Batch Size", getCommitIntervalConn());
        return b.toString();
    }
