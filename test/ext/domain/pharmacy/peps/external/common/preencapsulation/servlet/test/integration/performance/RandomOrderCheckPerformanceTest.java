/**
 * Copyright 2009, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.servlet.test.integration.performance;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.RandomOrderCheckTestCase;
/**
 * 
 */
public class RandomOrderCheckPerformanceTest extends RandomOrderCheckTestCase {
    private static final int ITERATIONS = 200;
    private static final long MAX_TIME = 1250;
    /**
     * Test that {@link #ITERATIONS} random drug dose checks finish within {@link #MAX_TIME} milliseconds.
     * 
     * @throws Exception if error
     */
    public void testDrugDose() throws Exception {
        assertPerformance(DrugCheckType.DRUG_DOSE);
    }
    /**
     * Test that {@link #ITERATIONS} random drug drug checks finish within {@link #MAX_TIME} milliseconds.
     * 
     * @throws Exception if error
     */
    public void testDrugDrug() throws Exception {
        assertPerformance(DrugCheckType.DRUG_DRUG);
    }
    /**
     * Test that {@link #ITERATIONS} random drug therapy checks finish within {@link #MAX_TIME} milliseconds.
     * 
     * @throws Exception if error
     */
    public void testDrugTherapy() throws Exception {
        assertPerformance(DrugCheckType.DRUG_THERAPY);
    }
    /**
     * Test that {@link #ITERATIONS} random all order check types finish within {@link #MAX_TIME} milliseconds.
     * 
     * @throws Exception if error
     */
    public void testAll() throws Exception {
        assertPerformance(DrugCheckType.ALL);
    }
    /**
     * Assert that a PEPSResponse was received for 200 calls of the given type.
     * 
     * @param drugCheckType {@link DrugCheckType} to send
     * @throws Exception
     */
    private void assertPerformance(DrugCheckType drugCheckType) throws Exception {
        for (int i = 1; i <= ITERATIONS; i++) {
            String orderCheck = getRandomOrderCheck(drugCheckType);
            System.out.println(drugCheckType + " " + i + " of 200");
            assertPerformance(orderCheck, MAX_TIME);
        }
    }
