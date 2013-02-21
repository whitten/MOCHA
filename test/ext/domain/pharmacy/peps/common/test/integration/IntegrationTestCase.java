/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.test.integration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.naming.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jndi.JndiTemplate;
import junit.framework.TestCase;
/**
 * Loads Spring ApplicationContext and InitialContext properties for use in integration test cases.
 */
public abstract class IntegrationTestCase extends TestCase { // NOPMD - abstract class, so no test methods
    private static final String PROPERTIES_FILE = "properties/EXT/DOMAIN/pharmacy/peps/common/test/integration/IntegrationTestCase.properties";
    private String localContextFactory;
    private String localHost;
    private int localPort;
    private String localProviderUrl;
    private String fdbDifJdbcUrl;
    private String fdbDifUser;
    private String fdbDifPassword;
    private String fdbDifDriver;
    private String datupJdbcUrl;
    private String datupUser;
    private String datupDriver;
    private String datupPassword;
    private SingleConnectionDataSource fdbDifDataSource;
    private SingleConnectionDataSource datupDataSource;
    private JdbcTemplate fdbDifJdbcTemplate;
    private JdbcTemplate datupJdbcTemplate;
    private HostProperties national = new HostProperties();
    /**
     * Setup properties
     */
    public IntegrationTestCase() {
        super();
        initialize();
    }
    /**
     * Setup properties
     * 
     * @param name TestCase name
     */
    public IntegrationTestCase(String name) {
        super(name);
        initialize();
    }
    /**
     * Read properties and instantiate ApplicationContext
     */
    protected void initialize() {
        try {
            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE));
            this.localContextFactory = properties.getProperty("local.context.factory");
            this.localHost = properties.getProperty("local.host");
            this.localPort = Integer.parseInt(properties.getProperty("local.port"));
            this.localProviderUrl = properties.getProperty("local.provider.url");
            this.fdbDifJdbcUrl = properties.getProperty("local.fdb.dif.jdbc.url");
            this.fdbDifUser = properties.getProperty("local.fdb.dif.user");
            this.fdbDifPassword = properties.getProperty("local.fdb.dif.password");
            this.fdbDifDriver = properties.getProperty("local.fdb.dif.driver");
            this.datupJdbcUrl = properties.getProperty("national.datup.jdbc.url");
            this.datupUser = properties.getProperty("national.datup.user");
            this.datupPassword = properties.getProperty("national.datup.password");
            this.datupDriver = properties.getProperty("national.datup.driver");
            // Register the JDBC driver and load the data source
            Class.forName(getFdbDifDriver()).newInstance();
            this.fdbDifDataSource = new SingleConnectionDataSource(getFdbDifJdbcUrl(), getFdbDifUser(), getFdbDifPassword(),
                true);
            this.fdbDifJdbcTemplate = new JdbcTemplate(fdbDifDataSource);
            Class.forName(getDatupDriver()).newInstance();
            this.datupDataSource = new SingleConnectionDataSource(getDatupJdbcUrl(), getDatupUser(), getDatupPassword(),
                true);
            this.datupJdbcTemplate = new JdbcTemplate(datupDataSource);
            national.providerUrl = properties.getProperty("national.provider.url");
            national.contextFactory = properties.getProperty("national.context.factory");
        }
        catch (Exception e) {
            throw new RuntimeException(e); // NOPMD - we're in a test case anyway
        }
    }
    /**
     * Context factory for remote lookup, e.g. weblogic.jndi.WLInitialContextFactory
     * 
     * @return String context factory
     */
    public String getLocalContextFactory() {
        return localContextFactory;
    }
    /**
     * Provider URL, e.g. t3://localhost:8010
     * 
     * @return String provider URL
     */
    public String getLocalProviderUrl() {
        return localProviderUrl;
    }
    /**
     * String host name, e.g. localhost
     * 
     * @return String host name
     */
    public String getLocalHost() {
        return localHost;
    }
    /**
     * Port the server is running on, e.g. 8010
     * 
     * @return int port number
     */
    public int getLocalPort() {
        return localPort;
    }
    /**
     * JDBC URL for FDB DIF
     * 
     * @return fdbDifJdbcUrl property
     */
    public String getFdbDifJdbcUrl() {
        return fdbDifJdbcUrl;
    }
    /**
     * Password for FDB DIF user
     * 
     * @return fdbDifPassword property
     */
    public String getFdbDifPassword() {
        return fdbDifPassword;
    }
    /**
     * FDB DIF user name
     * 
     * @return fdbDifUser property
     */
    public String getFdbDifUser() {
        return fdbDifUser;
    }
    /**
     * FDB DIF JDB driver class name
     * 
     * @return fdbDifDriver property
     */
    public String getFdbDifDriver() {
        return fdbDifDriver;
    }
    /**
     * Datup JDBC driver class name
     * 
     * @return datupDriver property
     */
    public String getDatupDriver() {
        return datupDriver;
    }
    /**
     * JDBC URL for Datup
     * 
     * @return datupJdbcUrl property
     */
    public String getDatupJdbcUrl() {
        return datupJdbcUrl;
    }
    /**
     * Password for Datup user
     * 
     * @return datupPassword property
     */
    public String getDatupPassword() {
        return datupPassword;
    }
    /**
     * Datup user name
     * 
     * @return datupUser property
     */
    public String getDatupUser() {
        return datupUser;
    }
    /**
     * Read the text from the given path to a file.
     * 
     * @param path String file path from which to read text
     * @return String text contained in the file represented by the file
     * @throws IOException if error reading from the file
     */
    public String readInputStream(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer xmlRequest = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            xmlRequest.append(line);
            line = reader.readLine();
        }
        reader.close();
        inputStream.close();
        return xmlRequest.toString();
    }
    /**
     * 
     * @return dataSource property
     */
    public SingleConnectionDataSource getFdbDifDataSource() {
        return fdbDifDataSource;
    }
    /**
     * 
     * @return jdbcTemplate property
     */
    public JdbcTemplate getFdbDifJdbcTemplate() {
        return fdbDifJdbcTemplate;
    }
    /**
     * 
     * @return dataSource property
     */
    public SingleConnectionDataSource getDatupDataSource() {
        return datupDataSource;
    }
    /**
     * 
     * @return jdbcTemplate property
     */
    public JdbcTemplate getDatupJdbcTemplate() {
        return datupJdbcTemplate;
    }
    /**
     * Create a Spring {@link JndiTemplate} for the given context factory and provider URL.
     * 
     * @param contextFactory String initial context factory
     * @param providerUrl String provider URL
     * @return JndiTemplate for the given context factory and provider URL
     */
    private JndiTemplate getJndiTemplate(String contextFactory, String providerUrl) {
        Properties environment = new Properties();
        environment.setProperty(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        environment.setProperty(Context.PROVIDER_URL, providerUrl);
        return new JndiTemplate(environment);
    }
    /**
     * Get the Spring {@link JndiTemplate} for NationalPharmacyServer.
     * 
     * @return JndiTemplate to NationalPharmacyServer
     */
    public JndiTemplate getNationalJndiTemplate() {
        return getJndiTemplate(getNationalContextFactory(), getNationalProviderUrl());
    }
    /**
     * Context factory for remote lookup, e.g. weblogic.jndi.WLInitialContextFactory
     * 
     * @return String context factory
     */
    public String getNationalContextFactory() {
        return national.contextFactory;
    }
    /**
     * Provider URL, e.g. t3://localhost:8012
     * 
     * @return String provider URL
     */
    public String getNationalProviderUrl() {
        return national.providerUrl;
    }
    /**
     * 
     * @throws Throwable
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        getFdbDifDataSource().destroy();
        getDatupDataSource().destroy();
    }
    /**
     * Holds properties for each host. Supports test cases to iterate over all locals in a for loop.
     */
    private class HostProperties {
        public String contextFactory;
        public String providerUrl;
        public String host;
        public int port;
        /**
         * Empty constructor
         */
        public HostProperties() {
            super();
        }
    }
