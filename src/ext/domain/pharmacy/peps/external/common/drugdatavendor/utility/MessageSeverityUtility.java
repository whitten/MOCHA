/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
import EXT.DOMAIN.pharmacy.peps.common.utility.PropertyUtility;
import dbank.dif.FDBMessageSeverity;
/**
 * Strip any reference to FDB from the message severity
 */
public class MessageSeverityUtility {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MessageSeverityUtility.class);
    /**
     * Map between the toString() return of FDBMessageSeverity and the message returned to VistA via a properties file.
     * 
     * @param severity FDBMessageSeverity to convert
     * @return String severity without reference to FDB
     */
    public static String convert(FDBMessageSeverity severity) {
        String result = PropertyUtility.getProperty(MessageSeverityUtility.class, severity.toString());
        if (result == null) {
            LOG.error(severity.toString() + " FDBMessageSeverity did not map successfully!");
            return severity.toString();
        }
        else {
            return result;
        }
    }
    /**
     * Cannot instantiate
     */
    private MessageSeverityUtility() {
        super();
    }
