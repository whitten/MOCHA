/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test;
import java.text.SimpleDateFormat;
import java.util.Date;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.druginfo.DrugInfoXmlUtility;
/**
 * Send a request to the drug info servlet.
 * 
 * This super class of web service (servlet) test cases should not be instantiated and has no test methods, therefore it is
 * marked as abstract.
 */
public abstract class DrugInfoTestCase extends VistAWebServiceTestCase { // NOPMD - see javadoc
    /**
     * Empty constructor
     */
    public DrugInfoTestCase() {
        super();
    }
    /**
     * Give the test case a name
     * 
     * @param name String
     */
    public DrugInfoTestCase(String name) {
        super(name);
    }
    /**
     * Return 'druginfo'
     * 
     * @return String 'druginfo'
     */
    public String getRequestURL() {
        return "druginfo";
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
        System.out.println(DrugInfoXmlUtility.prettyPrint(actual));
        String expected = readInputStream(responsePath);
        System.out.println("Expected XML Response (" + responsePath + "):");
        System.out.println(expected);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm:ss.SSS a z");
        System.out.println("Start Time: " + dateFormat.format(new Date(start)));
        System.out.println("Stop Time: " + dateFormat.format(new Date(stop)));
        System.out.println("Execution Time: " + (stop - start) + " milliseconds");
        assertTrue("Response XML is not correct", DrugInfoXmlUtility.responseEquals(expected, actual));
    }
