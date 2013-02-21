/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet.test.integration;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.DrugInfoTestCase;
/**
 * Test requests to retrieve dose routes and types for GCN sequence numbers
 */
public class DrugInfoServletTest extends DrugInfoTestCase {
    /**
     * Verify a request with a single drug works properly
     * 
     * @throws Exception
     */
    public void testSingleDrug() throws Exception {
        assertActualResponseEqual("xml/test/messages/drugInfoSingle.xml", "xml/test/messages/drugInfoSingleResponse.xml");
    }
    /**
     * Verify a request with multiple drugs works properly
     * 
     * @throws Exception
     */
    public void testMultipleDrug() throws Exception {
        assertActualResponseEqual("xml/test/messages/drugInfoMultiple.xml",
            "xml/test/messages/drugInfoMultipleResponse.xml");
    }
    /**
     * Verify unknown GCN sequence numbers are handled properly
     * 
     * @throws Exception
     */
    public void testDrugNotFound() throws Exception {
        assertActualResponseEqual("xml/test/messages/drugInfoUnknownGcn.xml",
            "xml/test/messages/drugInfoUnknownGcnResponse.xml");
    }
