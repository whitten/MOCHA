/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck.DrugCheckXmlUtility;
/**
 * Verify that order checks generated randomly are valid
 */
public class RandomOrderCheckTestCaseTest extends RandomOrderCheckTestCase {
    /**
     * Get a random order check 100 times and verify all are valid
     * 
     * @throws Exception if error validating random order check
     */
    public void testGetRandomOrderCheck() throws Exception {
        for (int i = 1; i <= 100; i++) {
            System.out.println("--------------- Random Order Check " + i + " ---------------");
            String orderCheck = getRandomOrderCheck(DrugCheckType.RANDOM);
            System.out.println(orderCheck);
            assertValid(orderCheck);
        }
    }
    /**
     * Verify the given request is valid.
     * 
     * @param xmlRequest String XML request
     * 
     * @throws Exception if error validating random order check
     */
    private void assertValid(String xmlRequest) throws Exception {
        assertTrue("XML request invalid", DrugCheckXmlUtility.validatePepsRequest(xmlRequest));
    }
