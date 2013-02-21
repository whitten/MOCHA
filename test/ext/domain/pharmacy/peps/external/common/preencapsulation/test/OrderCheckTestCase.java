/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test;
import java.text.SimpleDateFormat;
import java.util.Date;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck.DrugCheckXmlUtility;
/**
 * Send a request to the order check servlet.
 * 
 * This super class of web service (servlet) test cases should not be instantiated and has no test methods, therefore it is
 * marked as abstract.
 */
public abstract class OrderCheckTestCase extends VistAWebServiceTestCase { // NOPMD - see javadoc
    /**
     * Empty constructor
     */
    public OrderCheckTestCase() {
        super();
    }
    /**
     * Give the test case a name
     * 
     * @param name String
     */
    public OrderCheckTestCase(String name) {
        super(name);
    }
    /**
     * Return 'ordercheck'
     * 
     * @return String 'ordercheck'
     */
    public String getRequestURL() {
        return "ordercheck";
    }
    /**
     * Send a request to the PRE Encapsulation servlet.
     * 
     * @param requestPath String file path to request
     * @param responsePath String file path to expected response
     * @throws Exception if error
     */
    public void assertActualResponseEqual(String requestPath, String responsePath) throws Exception {
        String request = readInputStream(requestPath);
        System.out.println("Sending request to: " + getUrl());
        System.out.println("XML Request (" + requestPath + "):");
        System.out.println(request);
        long start = System.currentTimeMillis();
        String actual = sendRequest(request);
        long stop = System.currentTimeMillis();
        System.out.println("Actual XML Response:");
        System.out.println(DrugCheckXmlUtility.prettyPrint(actual));
        String expected = readInputStream(responsePath);
        System.out.println("Expected XML Response (" + responsePath + "):");
        System.out.println(expected);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm:ss.SSS a z");
        System.out.println("Start Time: " + dateFormat.format(new Date(start)));
        System.out.println("Stop Time: " + dateFormat.format(new Date(stop)));
        System.out.println("Execution Time: " + (stop - start) + " milliseconds");
        assertTrue("Response XML is not correct", DrugCheckXmlUtility.responseEquals(expected, actual));
    }
    /**
     * Send a request to the PRE Encapsulation servlet and verify a PEPSResponse is received (not an exception).
     * 
     * @param xmlRequest String XML request (not a file path to the request)
     * @throws Exception if error
     */
    public void assertReceivedResponse(String xmlRequest) throws Exception {
        System.out.println("Sending request to: " + getUrl());
        System.out.println("XML Request:");
        System.out.println(xmlRequest);
        long start = System.currentTimeMillis();
        String actual = sendRequest(xmlRequest);
        long stop = System.currentTimeMillis();
        System.out.println("Actual XML Response:");
        System.out.println(DrugCheckXmlUtility.prettyPrint(actual));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm:ss.SSS a z");
        System.out.println("Start Time: " + dateFormat.format(new Date(start)));
        System.out.println("Stop Time: " + dateFormat.format(new Date(stop)));
        System.out.println("Execution Time: " + (stop - start) + " milliseconds");
        System.out.println();
        assertTrue("PEPSResponse not received", actual.indexOf("<PEPSResponse") != -1);
    }
    /**
     * Send a request to the PRE Encapsulation servlet and verify a PEPSResponse is received (not an exception). Then verify
     * that the response was received in less than or equal to the given number of milliseconds.
     * <p>
     * Output is "silent" meaning that the requests and responses are not output to System.out.
     * 
     * @param xmlRequest String XML request (not a file path to the request)
     * @param milliseconds Performance requirement (less than or equal to)
     * @throws Exception if error
     */
    public void assertPerformance(String xmlRequest, long milliseconds) throws Exception {
        long start = System.currentTimeMillis();
        String actual = sendRequest(xmlRequest);
        long stop = System.currentTimeMillis();
        boolean hasResponse = actual.indexOf("<PEPSResponse") != -1;
        if (!hasResponse) {
            System.out.println("PEPSReponse not received.");
            System.out.println("XML Request:");
            System.out.println(xmlRequest);
            System.out.println("XML Response:");
            System.out.println(DrugCheckXmlUtility.prettyPrint(actual));
        }
        assertTrue("PEPSResponse not received", hasResponse);
        System.out.println("Execution Time: " + (stop - start) + " milliseconds");
        assertTrue("Exceeded performance requirement", (stop - start) <= milliseconds);
    }
