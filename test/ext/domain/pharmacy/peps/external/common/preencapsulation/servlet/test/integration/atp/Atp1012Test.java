/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet.test.integration.atp;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.OrderCheckTestCase;
/**
 * Test the message bean using the MockVistACall object.
 */
public class Atp1012Test extends OrderCheckTestCase {
    /**
     * ATP test case 1011
     * 
     * @throws Exception
     */
    public void testSendXMLCall() throws Exception {
        assertActualExceptionEqual("xml/test/messages/atp/1012.xml", "xml/test/messages/atp/1012Response.xml");
    }
