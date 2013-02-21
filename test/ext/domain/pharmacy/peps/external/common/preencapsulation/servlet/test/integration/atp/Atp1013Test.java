/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet.test.integration.atp;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.OrderCheckTestCase;
/**
 * Test the message bean using the MockVistACall object.
 */
public class Atp1013Test extends OrderCheckTestCase {
    /**
     * ATP test case 1013
     * 
     * @throws Exception
     */
    public void testSendXMLCall() throws Exception {
        assertActualResponseEqual("xml/test/messages/atp/1013.xml", "xml/test/messages/atp/1013Response.xml");
    }
