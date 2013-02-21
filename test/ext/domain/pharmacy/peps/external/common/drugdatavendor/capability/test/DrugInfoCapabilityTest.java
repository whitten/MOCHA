/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.DrugInfoCapability;
import junit.framework.TestCase;
/**
 * Verify we can get dose routes for a GCN
 */
public class DrugInfoCapabilityTest extends TestCase {
    private DrugInfoCapability capability;
    /**
     * Setup the capability
     * 
     * @throws Exception id error
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        System.out.println("--------------------------" + getName() + "--------------------------");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/mocha/test/InterfaceContext.xml");
        this.capability = (DrugInfoCapability) context.getBean("drugInfoCapability");
    }
    /**
     * Test retrieving dose routes and types for no GCN sequence numbers
     */
    public void testZeroGcn() {
        Collection<DrugInfoVo> gcnSeqNos = Collections.emptyList();
        DrugInfoResultsVo results = capability.processDrugInfoRequest(gcnSeqNos);
        assertEquals("No drugs should be returned", 0, results.getDrugs().size());
        assertEquals("All drugs should be found", 0, results.getDrugsNotFound().size());
    }
    /**
     * Test retrieving dose routes and types for one GCN sequence number
     */
    public void testSingleGcn() {
        Collection<DrugInfoVo> gcnSeqNos = new ArrayList<DrugInfoVo>();
        DrugInfoVo one = new DrugInfoVo();
        one.setGcnSeqNo("12000");
        gcnSeqNos.add(one);
        DrugInfoResultsVo results = capability.processDrugInfoRequest(gcnSeqNos);
        System.out.println(results);
        assertEquals("Incorrect number of drugs returned", 1, results.getDrugs().size());
        assertEquals("All drugs should be found", 0, results.getDrugsNotFound().size());
        for (DrugInfoVo drugInfoVo : results.getDrugs()) {
            assertEquals("Incorrect number of routes for 12000", 1, drugInfoVo.getDoseRoutes().size());
            assertEquals("Incorrect number of types for 12000", 2, drugInfoVo.getDoseTypes().size());
        }
    }
    /**
     * Test retrieving dose routes and types for multiple GCN sequence numbers
     */
    public void testMultipleGcn() {
        Collection<DrugInfoVo> gcnSeqNos = new ArrayList<DrugInfoVo>();
        DrugInfoVo one = new DrugInfoVo();
        one.setGcnSeqNo("12000");
        gcnSeqNos.add(one);
        DrugInfoVo two = new DrugInfoVo();
        two.setGcnSeqNo("6559");
        gcnSeqNos.add(two);
        DrugInfoResultsVo results = capability.processDrugInfoRequest(gcnSeqNos);
        System.out.println(results);
        assertEquals("Incorrect number of drugs returned", 2, results.getDrugs().size());
        assertEquals("All drugs should be found", 0, results.getDrugsNotFound().size());
        for (DrugInfoVo drugInfoVo : results.getDrugs()) {
            if (one.getGcnSeqNo().equals(drugInfoVo.getGcnSeqNo())) {
                assertEquals("Incorrect number of routes for 12000", 1, drugInfoVo.getDoseRoutes().size());
                assertEquals("Incorrect number of types for 12000", 2, drugInfoVo.getDoseTypes().size());
            }
            else {
                assertEquals("Incorrect number of routes for 6559", 1, drugInfoVo.getDoseRoutes().size());
                assertEquals("Incorrect number of types for 6559", 3, drugInfoVo.getDoseTypes().size());
            }
        }
    }
    /**
     * Test sending an invalid GCN sequence number
     */
    public void testInvalidGcn() {
        Collection<DrugInfoVo> gcnSeqNos = new ArrayList<DrugInfoVo>();
        DrugInfoVo one = new DrugInfoVo();
        one.setGcnSeqNo("12000");
        gcnSeqNos.add(one);
        DrugInfoVo two = new DrugInfoVo();
        two.setGcnSeqNo("6559");
        gcnSeqNos.add(two);
        DrugInfoVo three = new DrugInfoVo();
        three.setGcnSeqNo("0");
        gcnSeqNos.add(three);
        DrugInfoResultsVo results = capability.processDrugInfoRequest(gcnSeqNos);
        System.out.println(results);
        assertEquals("Incorrect number of drugs returned", 2, results.getDrugs().size());
        assertEquals("One drug should not be found", 1, results.getDrugsNotFound().size());
        assertTrue("Drug 0 should not be found", results.getDrugsNotFound().contains(three));
        for (DrugInfoVo drugInfoVo : results.getDrugs()) {
            if (one.getGcnSeqNo().equals(drugInfoVo.getGcnSeqNo())) {
                assertEquals("Incorrect number of routes for 12000", 1, drugInfoVo.getDoseRoutes().size());
                assertEquals("Incorrect number of types for 12000", 2, drugInfoVo.getDoseTypes().size());
            }
            else {
                assertEquals("Incorrect number of routes for 6559", 1, drugInfoVo.getDoseRoutes().size());
                assertEquals("Incorrect number of types for 6559", 3, drugInfoVo.getDoseTypes().size());
            }
        }
    }
