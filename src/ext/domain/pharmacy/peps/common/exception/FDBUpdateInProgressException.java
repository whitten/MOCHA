/**
 * Copyright 2010, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.exception;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
/**
 *
 *  Exception to be handled slightly differently than the usual Pharmacy Exceptions, thus is not using the usual heirarchy
 */
public class FDBUpdateInProgressException extends Exception {
    private static final long serialVersionUID = 1L;  
    private static final Logger LOG = Logger.getLogger(PharmacyException.class);
    private MessageKey key;
    private Object[] arguments;
    /**
     * Create a new exception with a no-argument message.
     * 
     * @param key String key to the exception message
     */
    public FDBUpdateInProgressException(MessageKey key) {
        this.key = key;
        this.arguments = new Object[] {};
        
        log();
    }
    
    /**
     * Return the message localized for the default locale. The localized message is derived from the ExceptionMessage
     * provided in the constructor.
     * 
     * @return String localized message
     * 
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("properties/" + getClass().getName(), Locale.getDefault(), Thread
            .currentThread().getContextClassLoader());
        String message;
        try {
            message = bundle.getString(key.getKey());
            if (arguments.length > 0) {
                message = MessageFormat.format(message, arguments);
            }
        }
        catch (MissingResourceException e) {
            message = e.getLocalizedMessage();
        }
        return message;
    }
    /**
     * Return the message localized for the default locale. The localized message is derived from the ExceptionMessage
     * provided in the constructor.
     * 
     * @return String localized message
     * 
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return getLocalizedMessage();
    }
    /**
     * Log the message via Log4j
     */
    protected void log() {
        LOG.error("", this);
    }
