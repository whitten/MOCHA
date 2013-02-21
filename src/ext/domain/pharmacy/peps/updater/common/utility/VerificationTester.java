package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.ProcessOrderChecksCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.RandomOrderCheckCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.impl.RandomOrderCheckCapabilityImpl.DrugCheckType;
public class VerificationTester {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(VerificationTester.class);
    private Configuration configuration;
    private ProcessOrderChecksCapability orderChecksCapability;
    private RandomOrderCheckCapability randomOrderCheckCapability;
    /**
     * Default constructor.
     */
    protected VerificationTester() {
    }
    /**
     * Execute the PEPS 0.5 unit tests to verify the FDB installation.
     * 
     * @throws CommonException if the unit test fails to execute
     */
    public boolean executeOrderCheckTests() {
        int testCount = configuration.load().getInt(Configuration.FDB_TEST_COUNT);
        int testFailureHeuristic = (int) Math.ceil(testCount * 0.2);
        LOG.debug("Running " + testCount + " random FDB-DIF verification tests...");
        // execute n random order checks
        for (int i = 0, errorCount = 0; i < testCount;) {
            try {
                String xmlInput;
                try {
                    // If we fail on the order check, we'll ignore a few in case of minor schema changes
                    xmlInput = randomOrderCheckCapability.getRandomOrderCheck(DrugCheckType.ALL);
                }
                catch (Throwable t) {
                    LOG.warn("Random verification test generation failed", t);
                    if (++errorCount > testFailureHeuristic) {
                        // If more than 20% of the tests fail generation, we should stop the code here
                        throw t;
                    }
                    else {
                        continue;
                    }
                }
                String xmlOutput = orderChecksCapability.handleRequest(xmlInput);
                if (xmlOutput.indexOf("<exception") != -1) {
                    LOG.error("Randomly generated XML #" + i + " FAILED.\nInput: " + xmlInput + "\nOutput: " + xmlOutput);
                    return false;
                }
                i++;
            }
            catch (Throwable t) {
                LOG.error("Randomly generated XML #" + i + " FAILED", t);
                return false;
            }
        }
        return true;
    }
    /**
     * Set the order check capability.
     * 
     * @param capability capability
     */
    public void setProcessOrderChecksCapability(ProcessOrderChecksCapability capability) {
        this.orderChecksCapability = capability;
    }
    /**
     * Configuration
     * 
     * @param configuration configuration
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    /**
     * Capability.
     * 
     * @param randomOrderCheckCapability capability
     */
    public void setRandomOrderCheckCapability(RandomOrderCheckCapability randomOrderCheckCapability) {
        this.randomOrderCheckCapability = randomOrderCheckCapability;
    }
