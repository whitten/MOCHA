/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.ProcessOrderChecksCapability;
import junit.framework.TestCase;
/**
 * 
 */
public class ProcessOrderChecksCapabilityTest extends TestCase {
    private ProcessOrderChecksCapability capability;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/mocha/test/InterfaceContext.xml");
        this.capability = (ProcessOrderChecksCapability) context.getBean("processOrderChecksCapability");
    }
    /**
     * Test sending a valid XML message from M to Java
     * 
     * @throws Exception
     */
    public void testSendXMLCall() throws Exception {
        String response = capability.handleRequest(readInputStream("xml/test/messages/orderCheck1.xml"));
        assertTrue("Response XML is incorrect", response.indexOf("<PEPSResponse") != -1);
    }
    /**
     * Test sending an invalid XML message from M to Java
     * 
     * @throws Exception
     */
    public void testMissingAttribute() throws Exception {
        try {
            capability.handleRequest(readInputStream("xml/test/messages/drugCheckInputMissingAttribute.xml"));
            fail();
        }
        catch (InterfaceValidationException ex) {
            ex.printStackTrace(System.out);
        }
    }
    /**
     * Test sending an invalid XML message from M to Java
     * 
     * @throws Exception
     */
    public void testMissingElement() throws Exception {
        try {
            capability.handleRequest(readInputStream("xml/test/messages/drugCheckInputMissingElement.xml"));
            fail();
        }
        catch (InterfaceValidationException ex) {
            ex.printStackTrace(System.out);
        }
    }
    /**
     * Test sending an invalid XML message from M to Java
     * 
     * @throws Exception
     */
    public void testWrongEnumeration() throws Exception {
        try {
            capability.handleRequest(readInputStream("xml/test/messages/drugCheckInputWrongEnumeration.xml"));
            fail();
        }
        catch (InterfaceValidationException ex) {
            ex.printStackTrace(System.out);
        }
    }
    /**
     * Read the text from the given path to a file.
     * 
     * @param path String file path from which to read text
     * @return String text contained in the file represented by the file
     * @throws IOException if error reading from the file
     */
    private String readInputStream(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer xmlRequest = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            xmlRequest.append(line);
            line = reader.readLine();
        }
        reader.close();
        inputStream.close();
        return xmlRequest.toString();
    }
