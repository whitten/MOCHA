/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.messaging.test.stub;
import java.io.Serializable;
import java.util.Enumeration;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
/**
 * Stub of javax.jms.ObjectMessage that allows us to test outside of the container.
 * 
 * NOTE: This ObjectMessage instance cannot be sent successfully to a Queue or Topic. It only implements the get/setObject
 * methods!
 */
public class ObjectMessageStub implements ObjectMessage {
    private Serializable object;
    /**
     * Empty constructor
     */
    public ObjectMessageStub() {
        super();
    }
    /**
     * *
     * 
     * @param object Object
     * @throws JMSException
     * 
     * @see javax.jms.ObjectMessage#setObject(java.io.Serializable)
     */
    public void setObject(Serializable object) throws JMSException {
        this.object = object;
    }
    /**
     * 
     * @return Serializable
     * @throws JMSException
     * 
     * @see javax.jms.ObjectMessage#getObject()
     */
    public Serializable getObject() throws JMSException {
        return object;
    }
    /**
     * 
     * @return String
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSMessageID()
     */
    public String getJMSMessageID() throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 ID
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSMessageID(java.lang.String)
     */
    public void setJMSMessageID(String arg0) throws JMSException {
    }
    /**
     * 
     * @return long
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSTimestamp()
     */
    public long getJMSTimestamp() throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 timestamp
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSTimestamp(long)
     */
    public void setJMSTimestamp(long arg0) throws JMSException {
    }
    /**
     * 
     * @return byte array
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSCorrelationIDAsBytes()
     */
    public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 byte array
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSCorrelationIDAsBytes(byte[])
     */
    public void setJMSCorrelationIDAsBytes(byte[] arg0) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSCorrelationID(java.lang.String)
     */
    public void setJMSCorrelationID(String arg0) throws JMSException {
    }
    /**
     * 
     * @return string
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSCorrelationID()
     */
    public String getJMSCorrelationID() throws JMSException {
        return null;
    }
    /**
     * 
     * @return destination
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSReplyTo()
     */
    public Destination getJMSReplyTo() throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 destination
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSReplyTo(javax.jms.Destination)
     */
    public void setJMSReplyTo(Destination arg0) throws JMSException {
    }
    /**
     * 
     * @return destination
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSDestination()
     */
    public Destination getJMSDestination() throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 destination
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSDestination(javax.jms.Destination)
     */
    public void setJMSDestination(Destination arg0) throws JMSException {
    }
    /**
     * 
     * @return int
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSDeliveryMode()
     */
    public int getJMSDeliveryMode() throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 int
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSDeliveryMode(int)
     */
    public void setJMSDeliveryMode(int arg0) throws JMSException {
    }
    /**
     * 
     * @return boolean
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSRedelivered()
     */
    public boolean getJMSRedelivered() throws JMSException {
        return false;
    }
    /**
     * 
     * @param arg0 boolean
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSRedelivered(boolean)
     */
    public void setJMSRedelivered(boolean arg0) throws JMSException {
    }
    /**
     * 
     * @return string
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSType()
     */
    public String getJMSType() throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 string
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSType(java.lang.String)
     */
    public void setJMSType(String arg0) throws JMSException {
    }
    /**
     * 
     * @return long
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSExpiration()
     */
    public long getJMSExpiration() throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 long
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSExpiration(long)
     */
    public void setJMSExpiration(long arg0) throws JMSException {
    }
    /**
     * 
     * @return int
     * @throws JMSException
     * 
     * @see javax.jms.Message#getJMSPriority()
     */
    public int getJMSPriority() throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 int
     * @throws JMSException
     * 
     * @see javax.jms.Message#setJMSPriority(int)
     */
    public void setJMSPriority(int arg0) throws JMSException {
    }
    /**
     * 
     * @throws JMSException
     * 
     * @see javax.jms.Message#clearProperties()
     */
    public void clearProperties() throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @return boolean
     * @throws JMSException
     * 
     * @see javax.jms.Message#propertyExists(java.lang.String)
     */
    public boolean propertyExists(String arg0) throws JMSException {
        return false;
    }
    /**
     * 
     * @param arg0 string
     * @return boolean
     * @throws JMSException
     * 
     * @see javax.jms.Message#getBooleanProperty(java.lang.String)
     */
    public boolean getBooleanProperty(String arg0) throws JMSException {
        return false;
    }
    /**
     * 
     * @param arg0 string
     * @return byte
     * @throws JMSException
     * 
     * @see javax.jms.Message#getByteProperty(java.lang.String)
     */
    public byte getByteProperty(String arg0) throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 string
     * @return short
     * @throws JMSException
     * 
     * @see javax.jms.Message#getShortProperty(java.lang.String)
     */
    public short getShortProperty(String arg0) throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 string
     * @return int
     * @throws JMSException
     * 
     * @see javax.jms.Message#getIntProperty(java.lang.String)
     */
    public int getIntProperty(String arg0) throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 string
     * @return long
     * @throws JMSException
     * 
     * @see javax.jms.Message#getLongProperty(java.lang.String)
     */
    public long getLongProperty(String arg0) throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 string
     * @return float
     * @throws JMSException
     * 
     * @see javax.jms.Message#getFloatProperty(java.lang.String)
     */
    public float getFloatProperty(String arg0) throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 string
     * @return double
     * @throws JMSException
     * 
     * @see javax.jms.Message#getDoubleProperty(java.lang.String)
     */
    public double getDoubleProperty(String arg0) throws JMSException {
        return 0;
    }
    /**
     * 
     * @param arg0 string
     * @return string
     * @throws JMSException
     * 
     * @see javax.jms.Message#getStringProperty(java.lang.String)
     */
    public String getStringProperty(String arg0) throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 string
     * @return object
     * @throws JMSException
     * 
     * @see javax.jms.Message#getObjectProperty(java.lang.String)
     */
    public Object getObjectProperty(String arg0) throws JMSException {
        return null;
    }
    /**
     * 
     * @return enumeration
     * @throws JMSException
     * 
     * @see javax.jms.Message#getPropertyNames()
     */
    public Enumeration getPropertyNames() throws JMSException {
        return null;
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 boolean
     * @throws JMSException
     * 
     * @see javax.jms.Message#setBooleanProperty(java.lang.String, boolean)
     */
    public void setBooleanProperty(String arg0, boolean arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 byte
     * @throws JMSException
     * 
     * @see javax.jms.Message#setByteProperty(java.lang.String, byte)
     */
    public void setByteProperty(String arg0, byte arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 short
     * @throws JMSException
     * 
     * @see javax.jms.Message#setShortProperty(java.lang.String, short)
     */
    public void setShortProperty(String arg0, short arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 int
     * @throws JMSException
     * 
     * @see javax.jms.Message#setIntProperty(java.lang.String, int)
     */
    public void setIntProperty(String arg0, int arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 long
     * @throws JMSException
     * 
     * @see javax.jms.Message#setLongProperty(java.lang.String, long)
     */
    public void setLongProperty(String arg0, long arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 float
     * @throws JMSException
     * 
     * @see javax.jms.Message#setFloatProperty(java.lang.String, float)
     */
    public void setFloatProperty(String arg0, float arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 double
     * @throws JMSException
     * 
     * @see javax.jms.Message#setDoubleProperty(java.lang.String, double)
     */
    public void setDoubleProperty(String arg0, double arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 string
     * @throws JMSException
     * 
     * @see javax.jms.Message#setStringProperty(java.lang.String, java.lang.String)
     */
    public void setStringProperty(String arg0, String arg1) throws JMSException {
    }
    /**
     * 
     * @param arg0 string
     * @param arg1 object
     * @throws JMSException
     * 
     * @see javax.jms.Message#setObjectProperty(java.lang.String, java.lang.Object)
     */
    public void setObjectProperty(String arg0, Object arg1) throws JMSException {
    }
    /**
     * 
     * @throws JMSException
     * 
     * @see javax.jms.Message#acknowledge()
     */
    public void acknowledge() throws JMSException {
    }
    /**
     * 
     * @throws JMSException
     * 
     * @see javax.jms.Message#clearBody()
     */
    public void clearBody() throws JMSException {
    }
