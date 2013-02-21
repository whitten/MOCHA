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
 * Validate drug-therapy check returns some results
 */
public class PerformDrugTherapyCheckCommandTest extends TestCase {
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
        aspirin.setGcnSeqNo("12000");
        aspirin.setProspective(false);
        DrugCheckVo warfarin = new DrugCheckVo();
        warfarin.setGcnSeqNo("12000");
        warfarin.setProspective(false);
        drugsToScreen.add(aspirin);
        drugsToScreen.add(warfarin);
        this.checkVo = new OrderCheckVo();
        checkVo.setDrugsToScreen(drugsToScreen);
        checkVo.setDuplicateAllowance(false);
        checkVo.setProspectiveOnly(false);
        checkVo.setDrugTherapyCheck(true);
    }
    /**
     * Verify execute method returns some results
     */
    public void testDrugTherapyCheck() {
        OrderCheckResultsVo results = capability.performDrugChecks(checkVo);
        assertTrue("dose check returned results", results.getDrugTherapyCheckResults().getChecks().size() > 0);
    }
