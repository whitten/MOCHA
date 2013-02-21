/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.exception;
/**
 * Exception thrown when value/data objects used by interfaces are invalid
 */
public class InterfaceValidationException extends InterfaceException {
    public static final MessageKey VALIDATION_ERROR = new MessageKey("VALIDATION_ERROR");
    public static final MessageKey DRUGS_TO_SCREEN_REQUIRED = new MessageKey("DRUGS_TO_SCREEN_REQUIRED");
    public static final MessageKey ORDER_CHECK_REQUERED = new MessageKey("ORDER_CHECK_REQUERED");
    public static final MessageKey PROSPECTIVE_DRUGS_REQUIRED = new MessageKey("PROSPECTIVE_DRUGS_REQUIRED");
    public static final MessageKey XML_REQUEST_REQUIRED = new MessageKey("XML_REQUEST_REQUIRED");
    public static final MessageKey FDB_VERIFICATION = new MessageKey("FDB_VERIFICATION");
    private static final long serialVersionUID = 1L;
    /**
     * Create a new exception with a no-argument message.
     * 
     * @param key String key to the exception message
     */
    public InterfaceValidationException(MessageKey key) {
        super(key);
    }
    /**
     * Create a new exception with a parameterized message.
     * 
     * @param key String key to the exception message
     * @param arguments Arguments to insert into the message
     */
    public InterfaceValidationException(MessageKey key, Object... arguments) {
        super(key, arguments);
    }
    /**
     * Create a new exception with a parameterized message.
     * 
     * @param e Exception that caused this exception
     * @param key String key to the exception message
     * @param arguments Arguments to insert into the message
     */
    public InterfaceValidationException(Throwable e, MessageKey key, Object... arguments) {
        super(e, key, arguments);
    }
    /**
     * Create a new exception with a no-argument message.
     * 
     * @param e Exception that caused this exception
     * @param key String key to the exception message
     */
    public InterfaceValidationException(Throwable e, MessageKey key) {
        super(e, key);
    }
