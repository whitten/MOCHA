/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility;
import EXT.DOMAIN.pharmacy.peps.common.utility.PropertyUtility;
import dbank.dif.FDBMessageType;
/**
 * Strip any reference to FDB from the message type
 */
public class MessageTypeUtility {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MessageTypeUtility.class);
    /**
     * Call toString on the type and strip any reference to FDB
     * 
     * @param type FDBMessageType to convert
     * @return String type without reference to FDB
     */
    public static String convert(FDBMessageType type) {
        String result = PropertyUtility.getProperty(MessageTypeUtility.class, type.toString());
        if (result == null) {
            LOG.error(type.toString() + " FDBMessageType did not map successfully!");
            return type.toString();
        }
        else {
            return result;
        }
    }
    /**
     * Cannot instantiate
     */
    private MessageTypeUtility() {
        super();
    }
