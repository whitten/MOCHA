/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.xml.sax.SAXException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
/**
 * Provide common functionality between all XML utilities
 */
public class XmlUtility {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(XmlUtility.class);
    private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    /**
     * Initialize a JAXBContext for the given package name. Wrap any caught exception in a InterfaceException (runtime).
     * 
     * @param packageName String package name
     * @return JAXBContext
     */
    protected static JAXBContext initializeJaxbContext(String packageName) {
        try {
            return JAXBContext.newInstance(packageName, Thread.currentThread().getContextClassLoader());
        }
        catch (JAXBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Set String fields to be wrapped in CDATA blocks.
     * <p>
     * NOTE:
     * <p>
     * This method uses Sun proprietary classes in the com.sun.org.apache.xml.internal.serialize package! We could have used
     * the Xerces library, but all classes in the org.apache.xml.serialize package are deprecated as of Xerces version 2.9.0!
     * The Xerces FAQ mentions using the LSSerialzier or Transformer classes, but neither implement the ContentHandler class
     * required by JAXB! For now, we'll continue using the proprietary classes provided in the JDK.
     * 
     * @see https://jaxb.dev.java.net/issues/show_bug.cgi?id=682
     * @see http://xerces.apache.org/xerces2-j/faq-general.html#faq-6
     * 
     * @param writer Writer to send output to
     * @param cdataElements String array of XML elements to wrap in CDATA blocks
     * @param prettyPrint boolean true if output should be indented/pretty printed
     * @return XMLSerializer to handle wrapping String fields in CDATA blocks
     */
    protected static XMLSerializer getXmlSerializer(Writer writer, String[] cdataElements, boolean prettyPrint) {
        OutputFormat of = new OutputFormat();
        of.setCDataElements(cdataElements);
        of.setIndenting(prettyPrint);
        return new XMLSerializer(writer, of);
    }
    /**
     * If there are any events, convert the events into an error message string and throw a new InterfaceValidationException.
     * 
     * @param handler ValidationEventController with any validation events
     * @param messageType String describing what type of message caused the error (Request, Response, or exception message)
     */
    protected static void handleValidationEvents(ValidationEventCollector handler, String messageType) {
        handleValidationEvents(null, handler, messageType);
    }
    /**
     * If there are any events, convert the events into an error message string and throw a new InterfaceValidationException.
     * 
     * @param jaxbException that prompted the check for validation events
     * @param handler ValidationEventController with any validation events
     * @param messageType String describing what type of message caused the error (Request, Response, or exception message)
     */
    protected static void handleValidationEvents(JAXBException jaxbException, ValidationEventCollector handler,
                                                 String messageType) {
        if (handler.hasEvents()) {
            ValidationEvent[] events = handler.getEvents();
            StringBuffer errorMessage = new StringBuffer();
            for (int i = 0; i < events.length; i++) {
                if (i > 0) {
                    errorMessage.append(" ");
                }
                errorMessage.append(events[i].getMessage());
            }
            if (jaxbException == null) {
                throw new InterfaceValidationException(InterfaceValidationException.VALIDATION_ERROR,
                    InterfaceException.PRE_ENCAPSULATION, messageType, errorMessage);
            }
            else {
                throw new InterfaceValidationException(jaxbException, InterfaceValidationException.VALIDATION_ERROR,
                    InterfaceException.PRE_ENCAPSULATION, messageType, errorMessage);
            }
        }
    }
    /**
     * Marshal into an XML String
     * 
     * @param <T> type of JAXB object to marshal
     * @param xmlObject JAXB XML object to marshal
     * @param prettyPrint boolean true if indenting/pretty printing should be used
     * @param jaxbContext JAXBContext with which to create marshaller
     * @param schemaLocation String location on class path to find schema
     * @param schemaFile String location on class path to get schema file
     * @param cdataElements String array of XML elements to wrap in CDATA blocks
     * @return XML String representing given Exception
     */
    protected static <T> String marshal(T xmlObject, boolean prettyPrint, JAXBContext jaxbContext, String schemaLocation,
                                        String schemaFile, String[] cdataElements) {
        try {
            StringWriter xmlResponse = new StringWriter();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setEventHandler(new ValidationEventCollector());
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
            // SchemaFactory is not thread-safe
            synchronized (SCHEMA_FACTORY) {
                Schema schema = SCHEMA_FACTORY.newSchema(new StreamSource(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(schemaFile)));
                marshaller.setSchema(schema);
            }
            marshaller.marshal(xmlObject, getXmlSerializer(xmlResponse, cdataElements, prettyPrint));
            handleValidationEvents((ValidationEventCollector) marshaller.getEventHandler(), getClassName(xmlObject
                .getClass()));
            return xmlResponse.toString();
        }
        catch (JAXBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
        catch (SAXException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Get just the class name from a fully qualified class
     * 
     * @param clazz Class
     * @return String name of the class without its package
     */
    private static String getClassName(Class<?> clazz) {
        int beginIndex = clazz.getName().lastIndexOf(".") + 1;
        return clazz.getName().substring(beginIndex);
    }
    /**
     * Cannot instantiate
     */
    protected XmlUtility() {
        super();
    }
