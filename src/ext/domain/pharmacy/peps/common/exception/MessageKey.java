/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.exception;
import java.io.Serializable;
/**
 * Represents a key to an exception message.
 * 
 * In WebLogic but not in Eclipse, the JVM calls into Throwable(String), 
 * and passes the localized message; however, our classes
 * set the string to the key, ultimately causing our
 * exceptions to fail when trying to lookup the resource
 * bundle. Using the MessageKey class prevents our
 * exceptions from having a PharmacyException(String) constructor,
 * rather we have a PharmacyException(MessageKey) constructor.
 */
public class MessageKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private String key;
    /**
     * Set the key
     * 
     * @param key String
     */
    public MessageKey(String key) {
        this.key = key;
    }
    /**
     * Return the key for the current message
     * 
     * @return String key
     */
    public String getKey() {
        return key;
    }
    /**
     * Return the key
     * 
     * @return String key
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return key;
    }
