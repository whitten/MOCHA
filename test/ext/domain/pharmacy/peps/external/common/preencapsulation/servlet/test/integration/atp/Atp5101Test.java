/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet.test.integration.atp;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.DrugInfoTestCase;
/**
 * Test the message bean using the MockVistACall object.
 */
public class Atp5101Test extends DrugInfoTestCase {
    /**
     * ATP test case 5101
     * 
     * @throws Exception
     */
    public void testSendXMLCall() throws Exception {
        assertActualResponseEqual("xml/test/messages/atp/5101.xml", "xml/test/messages/atp/5101Response.xml");
    }
