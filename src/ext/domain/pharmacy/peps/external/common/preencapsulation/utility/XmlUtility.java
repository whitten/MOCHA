/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.xml.sax.SAXException;
import EXT.DOMAIN.pharmacy.peps.common.exception.FDBUpdateInProgressException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.common.exception.PharmacyException;
import EXT.DOMAIN.pharmacy.peps.common.exception.PharmacyRuntimeException;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.exception.Exception;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.exception.ExceptionCodeType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.exception.ObjectFactory;
import dbank.database.FDBException;
import dbank.dif.FDBUnknownTypeException;
/**
 * Provide common functionality between all XML utilities
 */
public class XmlUtility {
    protected static final String EXCEPTION_PACKAGE = Exception.class.getPackage().getName();
    protected static final JAXBContext EXCEPTION_CONTEXT = initializeJaxbContext(EXCEPTION_PACKAGE);
    protected static final String EXCEPTION_NAMESPACE = EXCEPTION_PACKAGE.replace('.', '/');
    protected static final String EXCEPTION_SCHEMA_LOCATION = EXCEPTION_NAMESPACE + " " + "exception.xsd";
    protected static final String EXCEPTION_SCHEMA_FILE = "xml/schema/exception.xsd";
    protected static final String[] EXCEPTION_CDATA_ELEMENTS = {EXCEPTION_NAMESPACE + "^detailedMessage"};
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
     * Unmarshal the XML object into the given type.
     * 
     * @param <T> type to return when unmarshalling
     * @param xml String XML request
     * @param validate boolean true if should validate XML String
     * @param jaxbContext JAXBContext instance to get unmarshaller
     * @param schemaFile String location on class path to get schema file
     * @param returnType Class type to unmarshal
     * @return DrugInfoRequest created from the XML request
     */
    protected static <T> T unmarshal(String xml, boolean validate, JAXBContext jaxbContext, String schemaFile,
                                     Class<T> returnType) {
        ValidationEventCollector validationEventCollector = new ValidationEventCollector();
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setEventHandler(validationEventCollector);
            if (validate) {
                // SchemaFactory is not thread-safe
                synchronized (SCHEMA_FACTORY) {
                    Schema schema = SCHEMA_FACTORY.newSchema(new StreamSource(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(schemaFile)));
                    unmarshaller.setSchema(schema);
                }
            }
            BufferedReader reader = new BufferedReader(new StringReader(xml));
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal(new StreamSource(reader), returnType);
            T request = jaxbElement.getValue();
            try {
                reader.close();
            }
            catch (IOException e) {
                LOG.error("Due to implementation of StringReader, this exception should never be thrown.", e);
            }
            handleValidationEvents(validationEventCollector, getClassName(returnType));
            return request;
        }
        catch (JAXBException e) {
            handleValidationEvents(e, validationEventCollector, getClassName(returnType));
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
        catch (SAXException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Marshal an Exception into an XML String
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
     * Return true if the values of the attributes in the expected and actual responses are equal.
     * <p>
     * Only tests equality of the code and message attributes as the stack trace (contained in the detailed message) changes
     * how Java reflection chooses to operate.
     * 
     * @param expectedResponse String XML expected response
     * @param actualResponse String XML actual response
     * @return true if the expected response equals the actual response
     */
    public static boolean exceptionEquals(String expectedResponse, String actualResponse) {
        try {
            Unmarshaller unmarshaller = EXCEPTION_CONTEXT.createUnmarshaller();
            Exception expected = (Exception) unmarshaller.unmarshal(new StreamSource(new StringReader(expectedResponse)));
            Exception actual = (Exception) unmarshaller.unmarshal(new StreamSource(new StringReader(actualResponse)));
            return expected.getCode().equals(actual.getCode()) && expected.getMessage().equals(actual.getMessage());
        }
        catch (JAXBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Creates an error XML response message
     * 
     * @param throwable Throwable to create XML error message from
     * @return String the XML error message
     */
    public static String createXmlErrorMessage(Throwable throwable) {
        ObjectFactory objectFactory = new ObjectFactory();
        Exception exception = objectFactory.createException();
        // All FDB Exceptions extend FDBException, except for FDBUnknownTypeException which extends RuntimeException
        if (throwable instanceof FDBException || throwable instanceof FDBUnknownTypeException) {
            exception.setCode(ExceptionCodeType.FDB);
        }
        else if (throwable instanceof FDBUpdateInProgressException) {
            exception.setCode(ExceptionCodeType.FDBUPDATE);
        }
        else if (throwable instanceof PharmacyException || throwable instanceof PharmacyRuntimeException) {
            exception.setCode(ExceptionCodeType.PRE);
        }
        else if (throwable instanceof java.lang.Exception) {
            exception.setCode(ExceptionCodeType.JAVA);
        }
        else {
            exception.setCode(ExceptionCodeType.SYSTEM);
        }
        if (throwable.getLocalizedMessage() == null) {
            exception.setMessage("");
        }
        else {
            exception.setMessage(throwable.getLocalizedMessage());
        }
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        exception.setDetailedMessage(stackTrace.toString());
        return marshal(exception, false, EXCEPTION_CONTEXT, EXCEPTION_SCHEMA_LOCATION, EXCEPTION_SCHEMA_FILE,
            EXCEPTION_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param <T> JAXB type to pretty print
     * @param xmlObject JAXB object to pretty print
     * @param jaxbContext JAXBContext to use to create marshaller
     * @param schemaLocation String location on class path of schema
     * @param schemaFile String location on class path to get schema file
     * @param cdataElements String array of XML elements to wrap in CDATA blocks
     * @return String formatted XML
     */
    protected static <T> String prettyPrint(T xmlObject, JAXBContext jaxbContext, String schemaLocation, String schemaFile,
                                            String[] cdataElements) {
        return marshal(xmlObject, true, jaxbContext, schemaLocation, schemaFile, cdataElements);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param <T> JAXB type to pretty print
     * @param xml String XML to pretty print
     * @param jaxbContext JAXBContext to use to create marshaller
     * @param schemaLocation String XML namespace location
     * @param schemaFile String location on class path of schema
     * @param returnType JAXB Class to pretty print
     * @param cdataElements String array of XML elements to wrap in CDATA blocks
     * @return String formatted XML
     */
    protected static <T> String prettyPrint(String xml, JAXBContext jaxbContext, String schemaLocation, String schemaFile,
                                            Class<T> returnType, String[] cdataElements) {
        try {
            return prettyPrint(unmarshal(xml, false, jaxbContext, schemaFile, returnType), jaxbContext, schemaLocation,
                schemaFile, cdataElements);
        }
        catch (java.lang.Exception e) {
            LOG.error("Unable to pretty print XML. Returning original XML String.");
            return xml;
        }
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param xmlException String XML exception to pretty print
     * @return String formatted XML exception
     */
    public static String prettyPrintException(String xmlException) {
        return prettyPrint(xmlException, EXCEPTION_CONTEXT, EXCEPTION_SCHEMA_LOCATION, EXCEPTION_SCHEMA_FILE,
            Exception.class, EXCEPTION_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param exception Exception to pretty print
     * @return String formatted XML exception
     */
    public static String prettyPrintException(Exception exception) {
        return prettyPrint(exception, EXCEPTION_CONTEXT, EXCEPTION_SCHEMA_LOCATION, EXCEPTION_SCHEMA_FILE,
            EXCEPTION_CDATA_ELEMENTS);
    }
    /**
     * Cannot instantiate
     */
    protected XmlUtility() {
        super();
    }
