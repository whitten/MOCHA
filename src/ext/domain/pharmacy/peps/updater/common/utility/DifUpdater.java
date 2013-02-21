package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.naming.NamingException;
import org.springframework.jndi.JndiTemplate;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.session.impl.OrderCheckServiceImpl;
import dbank.fdbdataupdater.SettingsData;
/**
 * Interface with the FDB provided DIF update utility.
 */
public class DifUpdater {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DifUpdater.class);
    private static final String DATUP_UPDATE_IN_PROGRESS = "DATUP_UPDATE_IN_PROGRESS";
    private static final String INCREMENTAL_UPDATER_CLASS = "dbank.fdbdataupdater.DoIncremental";
    private static int RUNCOUNTER;
    private static Exception threadException; 
   
    private Configuration config;
    private DifUpdateCallback callback;
    private JndiTemplate jndiTemplate;
    /**
     * Atomic technique to check if a DIF update is active.
     * 
     * @return true if active, false otherwise
     */
    public static boolean isRunning() {
        return (RUNCOUNTER > 0);
    }
    
    /**
     * Callback for setting an arbitrary exception from a separate thread.
     * 
     * @param e exception
     */
    public static void setException(Exception e) {
        threadException = e;
    }
    /**
     * Constructor.
     */
    protected DifUpdater() {
        this.callback = new DifUpdateCallback();
    }
    
    /**
     * Instantiate and run the FDB updater tool. Report any errors as exceptions.
     * 
     * @param updateFile ZIP file
     */
    public synchronized void runUpdater(File updateFile) {
        try {
            RUNCOUNTER++; // obtain DIF update lock
            threadException = null; // reset thread exception value
            try {
                jndiTemplate.rebind(DATUP_UPDATE_IN_PROGRESS, Boolean.TRUE);
                
                Thread.sleep(1000 * 60); // wait 60 seconds for pending order checks to complete
            }
            catch (NamingException e) {
                LOG.warn("Unable to register update notification", e);
            }
            catch (InterruptedException e) {
                LOG.warn("Unable to wait for mandatory update notification grace period", e);
            }
            SettingsData.getInstance().setCallback(callback);
            SettingsData.getInstance().setProductSourceFile(updateFile);
            SettingsData.getInstance().setBatchCommitSize(config.load().getInt(Configuration.FDB_BATCH_COMMIT_SIZE));
            LOG.debug("Running FDB-DIF updater with settings: " + SettingsData.getInstance());
            dbank.fdbdataupdater.DoIncremental incUpdater = instantiateUpdater();
            incUpdater.run();
            // check for error and throw if present
            if (incUpdater.getException() != null) {
                throw new InterfaceException(incUpdater.getException(), InterfaceException.INTERFACE_ERROR,
                    "FDB-DIF Updater");
            }
            else if(threadException != null) {
                throw new InterfaceException(threadException, InterfaceException.INTERFACE_ERROR,
                    "FDB-DIF Updater");
            }
        }
        finally {
            RUNCOUNTER--; // release DIF update lock
            
            try {
                jndiTemplate.rebind(DATUP_UPDATE_IN_PROGRESS, Boolean.FALSE);
            }
            catch (NamingException e) {
                LOG.warn("Unable to close update notification", e);
            }
        }
    }
    /**
     * Locate and instantiate the FDB updater tool.
     * 
     * @return
     */
    private dbank.fdbdataupdater.DoIncremental instantiateUpdater() {
        try {
            Class<dbank.fdbdataupdater.DoIncremental> incUpdaterClass = (Class<dbank.fdbdataupdater.DoIncremental>) Thread
                .currentThread().getContextClassLoader().loadClass(INCREMENTAL_UPDATER_CLASS);
            Constructor<dbank.fdbdataupdater.DoIncremental> constructor = incUpdaterClass
                .getConstructor(new Class[] {dbank.fdbdataupdater.Status.class, dbank.utils.Logger.class});
            return constructor.newInstance(new dbank.fdbdataupdater.NullStatus(),
                new dbank.utils.SimpleLoggerAscii(null));
        }
        catch (InvocationTargetException e) {
            throw new InterfaceException(e.getCause(), InterfaceException.INTERFACE_ERROR, "FDB-DIF Updater");
        }
        catch (Exception e) {
            throw new InterfaceException(e.getCause(), InterfaceException.INTERFACE_UNAVAILABLE, "FDB-DIF Updater");
        }
    }
    /**
     * Set configuration.
     * 
     * @param config configuration
     */
    public void setConfiguration(Configuration config) {
        this.config = config;
    }
    /**
     * 
     * @param jndiTemplate jndiTemplate property
     */
    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
        
        try {
            jndiTemplate.bind(DATUP_UPDATE_IN_PROGRESS, Boolean.FALSE);
        }
        catch (NamingException e) {
            LOG.warn("Unable to register DATUP with JNDI", e);
        }
    }
    /**
     * 
     * @return jndiTemplate property
     */
    public JndiTemplate getJndiTemplate() {
        return jndiTemplate;
    }
    /**
     * Callback to listen for log messages.
     */
    public class DifUpdateCallback {
        /**
         * Log messages.
         * 
         * @param message update message
         */
        public void log(String message) {
            LOG.trace("FDB-DIF Update Tool: " + message);
        }
    }
