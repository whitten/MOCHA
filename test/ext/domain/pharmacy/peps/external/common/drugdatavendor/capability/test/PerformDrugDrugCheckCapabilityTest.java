/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugChecksCapability;
import junit.framework.TestCase;
/**
 * Verify the drug-drug check returns values
 */
public class PerformDrugDrugCheckCapabilityTest extends TestCase {
    private PerformDrugChecksCapability capability;
    private OrderCheckVo checkVo;
    /**
     * Instantiate and add drugs to drugsToScreen collection
     * 
     * @throws Exception If error in setting up collection
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        System.out.println("--------------------------" + getName() + "--------------------------");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/mocha/test/InterfaceContext.xml");
        this.capability = (PerformDrugChecksCapability) context.getBean("performDrugChecksCapability");
        Collection<DrugCheckVo> drugsToScreen = new ArrayList<DrugCheckVo>();
        DrugCheckVo aspirin = new DrugCheckVo();
        aspirin.setGcnSeqNo("12000"); // GCN sequence number
        aspirin.setProspective(false);
        DrugCheckVo warfarin = new DrugCheckVo();
        warfarin.setGcnSeqNo("6559"); // GCN sequence number
        warfarin.setProspective(false);
        drugsToScreen.add(aspirin);
        drugsToScreen.add(warfarin);
        this.checkVo = new OrderCheckVo();
        checkVo.setDrugsToScreen(drugsToScreen);
        checkVo.setProspectiveOnly(false);
        checkVo.setDrugDrugCheck(true);
    }
    /**
     * Verify check method returns some results
     */
    public void testDrugDrugCheck() {
        OrderCheckResultsVo results = capability.performDrugChecks(checkVo);
        assertTrue("dose check returned results", results.getDrugDrugCheckResults().getChecks().size() > 0);
    }
