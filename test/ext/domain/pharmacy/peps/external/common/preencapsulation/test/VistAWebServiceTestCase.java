/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.test.integration.IntegrationTestCase;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.stub.VistAWebServiceCallStub;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.XmlUtility;
/**
 * Send a request to the PRE Encapsulation servlet and verify the actual response is equal to the expected response.
 * 
 * This super class of web service (servlet) test cases should not be instantiated and has no test methods, therefor it is
 * marked as abstract.
 */
public abstract class VistAWebServiceTestCase extends IntegrationTestCase {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private VistAWebServiceCallStub call;
    /**
     * Empty constructor
     */
    public VistAWebServiceTestCase() {
        super();
    }
    /**
     * Give the test case a name
     * 
     * @param name String
     */
    public VistAWebServiceTestCase(String name) {
        super(name);
    }
    /**
     * initialize MockVistACall
     * 
     * @throws Exception if error instantiating VistAWebServiceCallStub
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("\n------------------------- " + getName() + " -------------------------\n");
    }
    /**
     * Setup the URL and instantiate the call stub
     */
    @Override
    protected void initialize() {
        super.initialize();
        
        String url = "http://" + getLocalHost() + ":" + getLocalPort() + "/MOCHA/" + getRequestURL();
        try {
            this.call = new VistAWebServiceCallStub(url);
        }
        catch (MalformedURLException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Return the end of the URL for a given Vista web service call (e.g., 'ordercheck' or 'druginfo')
     * 
     * @return String end of URL
     */
    public abstract String getRequestURL();
    /**
     * Send a request to the PRE Encapsulation servlet.
     * 
     * @param requestPath String file path to request
     * @param responsePath String file path to expected response
     * @throws Exception if error
     */
    public abstract void assertActualResponseEqual(String requestPath, String responsePath) throws Exception;
    /**
     * Send a request to the PRE Encapsulation servlet.
     * 
     * @param requestPath String file path to request
     * @param responsePath String file path to expected response
     * @throws Exception if error
     */
    public void assertActualExceptionEqual(String requestPath, String responsePath) throws Exception {
        String request = readInputStream(requestPath);
        System.out.println("Sending request to: " + getUrl());
        System.out.println("XML Request:");
        System.out.println(request);
        long start = System.currentTimeMillis();
        String actual = sendRequest(request);
        long stop = System.currentTimeMillis();
        System.out.println("Actual XML Response:");
        System.out.println(XmlUtility.prettyPrintException(actual));
        String expected = readInputStream(responsePath);
        System.out.println("Expected XML Response:");
        System.out.println(expected);
        System.out.println("Start Time: " + new Date(start));
        System.out.println("Stop Time: " + new Date(stop));
        System.out.println("Execution Time: " + (stop - start));
        assertTrue("Response XML is not correct", XmlUtility.exceptionEquals(expected, actual));
    }
    /**
     * Verify the response is not an exception
     * 
     * @param result XML string representing the result of the test case
     * @throws Exception
     */
    public void assertNotError(String result) throws Exception {
        assertTrue("Received error from PRE v0.5", !result.substring(result.indexOf("<")).trim().startsWith("<exception"));
    }
    /**
     * Read the text from the given path to a file.
     * 
     * @param path String file path from which to read text
     * @return String text contained in the file represented by the file
     */
    public String readInputStream(String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer xmlRequest = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                xmlRequest.append(line);
                xmlRequest.append(LINE_SEPARATOR);
                line = reader.readLine();
            }
            reader.close();
            inputStream.close();
            return xmlRequest.toString();
        }
        catch (IOException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * 
     * @return the URL this call will send a request to
     */
    public String getUrl() {
        return call.getUrl();
    }
    /**
     * Send a request to the servlet
     * 
     * @param xmlRequest String XML request to send
     * @return String response from servlet
     * @throws IOException if error sending request
     */
    public String sendRequest(String xmlRequest) throws IOException {
        return call.sendRequest(xmlRequest);
    }
