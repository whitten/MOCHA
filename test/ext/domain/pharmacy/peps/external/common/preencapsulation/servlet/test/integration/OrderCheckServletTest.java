/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet.test.integration;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.OrderCheckTestCase;
/**
 * Test the Order Check operation using the deployed servlet as an integration test.
 */
public class OrderCheckServletTest extends OrderCheckTestCase {
    /**
     * Test web service order check
     * 
     * @throws Exception
     */
    public void testSendXMLCall() throws Exception {
        assertActualResponseEqual("xml/test/messages/orderCheckBig.xml", "xml/test/messages/orderCheckBigResponse.xml");
    }
    /**
     * Test order check 1
     * 
     * @throws Exception
     */
    public void testOrderCheck1() throws Exception {
        assertActualResponseEqual("xml/test/messages/orderCheck1.xml", "xml/test/messages/orderCheckResponse1.xml");
    }
    /**
     * Test order check 2
     * 
     * @throws Exception
     */
    public void testOrderCheck2() throws Exception {
        assertActualResponseEqual("xml/test/messages/orderCheck2.xml", "xml/test/messages/orderCheckResponse2.xml");
    }
    /**
     * Test order check 4
     * 
     * @throws Exception
     */
    public void testOrderCheck4() throws Exception {
        assertActualResponseEqual("xml/test/messages/orderCheck4.xml", "xml/test/messages/orderCheckResponse4.xml");
    }
    /**
     * Test order check 7
     * 
     * @throws Exception
     */
    public void testOrderCheck7() throws Exception {
        assertActualResponseEqual("xml/test/messages/orderCheck7.xml", "xml/test/messages/orderCheckResponse7.xml");
    }
    /**
     * Test typical order check
     * 
     * @throws Exception
     */
    public void testTypicalOrderCheck() throws Exception {
        assertActualResponseEqual("xml/test/messages/typicalOrderCheck.xml",
            "xml/test/messages/typicalOrderCheckResponse.xml");
    }
    /**
     * Test an invalid request for an order check
     * 
     * @throws Exception
     */
    public void testMissingAttribute() throws Exception {
        assertActualExceptionEqual("xml/test/messages/drugCheckInputMissingAttribute.xml",
            "xml/test/messages/drugcheckInputMissingAttributeResponse.xml");
    }
    /**
     * Test an invalid request for an order check
     * 
     * @throws Exception
     */
    public void testMissingElement() throws Exception {
        assertActualExceptionEqual("xml/test/messages/drugCheckInputMissingElement.xml",
            "xml/test/messages/drugcheckInputMissingElementResponse.xml");
    }
    /**
     * Test an invalid request for an order check
     * 
     * @throws Exception
     */
    public void testWrongEnumeration() throws Exception {
        assertActualExceptionEqual("xml/test/messages/drugCheckInputWrongEnumeration.xml",
            "xml/test/messages/drugCheckInputWrongEnumerationResponse.xml");
    }
    /**
     * Test that duplicate GCNs are handled properly, and that the right drug is associated with the right response. We used
     * to use GCNs to map back from the FDB results to the drugs, but the GCNs may not be unique in a check, therefore we
     * needed to use a combination of IEN and order number.
     * 
     * @throws Exception
     */
    public void testDuplicateGcn() throws Exception {
        assertActualResponseEqual("xml/test/messages/duplicateGcn.xml", "xml/test/messages/duplicateGcnResponse.xml");
    }
    /**
     * Test ping request
     * 
     * @throws Exception if error
     */
    public void testPing() throws Exception {
        assertActualResponseEqual("xml/test/messages/ping.xml", "xml/test/messages/pingResponse.xml");
    }
    /**
     * Test that a non-ping request without a body produces an error
     * 
     * @throws Exception if error
     */
    public void testNoBody() throws Exception {
        assertActualExceptionEqual("xml/test/messages/noBody.xml", "xml/test/messages/noBodyResponse.xml");
    }
    /**
     * Test that a request without any order check types throws an exception
     * 
     * @throws Exception if error
     */
    public void testNoChecks() throws Exception {
        assertActualExceptionEqual("xml/test/messages/noChecks.xml", "xml/test/messages/noChecksResponse.xml");
    }
    /**
     * Test that a prospectiveOnly="true" that has no prospective drugs throws an exception
     * 
     * @throws Exception if error
     */
    public void testNoProspectives() throws Exception {
        assertActualExceptionEqual("xml/test/messages/noProspectives.xml", "xml/test/messages/noProspectivesResponse.xml");
    }
    /**
     * Test that a non-ping request without a body produces an error
     * 
     * @throws Exception if error
     */
    public void testNoDrugs() throws Exception {
        assertActualExceptionEqual("xml/test/messages/noDrugs.xml", "xml/test/messages/noDrugsResponse.xml");
    }
    /**
     * Test ping request
     * 
     * @throws Exception if error
     */
    public void testProfileOnly() throws Exception {
        assertActualResponseEqual("xml/test/messages/profileOnly.xml", "xml/test/messages/profileOnlyResponse.xml");
    }
    /**
     * Test that the duplicate therapy bug is resolved.
     * 
     * The FDB DIF API prior to version 3.2.2 had a bug where if custom tables and duplicate allowance were requested to be
     * used, the duplicate allowance would be ignored.
     * 
     * @throws Exception
     */
    public void testDuplicateTherapyBug() throws Exception {
        assertActualResponseEqual("xml/test/messages/duplicateTherapyBug.xml",
            "xml/test/messages/duplicateTherapyBugResponse.xml");
    }
    /**
     * Test that when duplicate drugs are sent in that would cause duplicate FDB interactions to be returned, that the
     * duplicate FDB interactions aren't filtered out.
     * <p>
     * VA CR 1718 had a problem where we were filtering these out.
     * 
     * @throws Exception if error
     */
    public void testDuplicateDrugs() throws Exception {
        assertActualResponseEqual("xml/test/messages/duplicateDrugs.xml", "xml/test/messages/duplicateDrugsResponse.xml");
    }
