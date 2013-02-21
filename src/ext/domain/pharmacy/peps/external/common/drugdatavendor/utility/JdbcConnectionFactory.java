/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import dbank.database.JDBCConnectionFactory;
/**
 * <p>Title: Create a custom JDBCConnectionFactory to allow the DIFWK to use
 *  connections from a DataSource.  Must define getConnection() and returnConnection()</p>  
 */
public class JdbcConnectionFactory implements JDBCConnectionFactory {
    private DataSource dataSource;
    /**
     * Return a usable JDBC Connection from this factory
     * 
     * @return Connection to the database
     * @throws SQLException trouble with the database
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    /**
     * Return the connection through the factory
     * 
     * @param sqlConnection the connection to close
     * @throws SQLException if unable to close connection
     */
    public void returnConnection(Connection sqlConnection) throws SQLException {
        sqlConnection.close();
    }
    /**
     * 
     * @param dataSource property
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
