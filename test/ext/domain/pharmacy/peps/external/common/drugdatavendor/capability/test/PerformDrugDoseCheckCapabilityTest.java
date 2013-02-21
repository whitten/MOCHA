/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDoseCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugChecksCapability;
import junit.framework.TestCase;
/**
 * Validate drug-dose check returns some results
 */
public class PerformDrugDoseCheckCapabilityTest extends TestCase {
    private static final String UNIT_MG = "MG";
    private static final String RATE_DAY = "DAY";
    private static final String RATE_HOUR = "HOUR";
    private static final String ROUTE_ORAL = "ORAL";
    private static final String ROUTE_INTRAVENOUS = "INTRAVENOUS";
    private static final String MAINTENANCE_DOSE = "MAINTENANCE";
    private static final String SINGLE_DOSE = "SINGLE DOSE";
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
        aspirin.setDoseRate(RATE_DAY);
        aspirin.setDoseUnit(UNIT_MG);
        aspirin.setDuration(100);
        aspirin.setDurationRate(RATE_DAY);
        aspirin.setFrequency("5");
        aspirin.setSingleDoseAmount(0.0001);
        aspirin.setDoseType(MAINTENANCE_DOSE);
        aspirin.setDoseRoute(ROUTE_ORAL);
        aspirin.setProspective(true);
        aspirin.setDoseCheck(true);
        DrugCheckVo warfarin = new DrugCheckVo();
        warfarin.setGcnSeqNo("6559");
        warfarin.setDoseRate(RATE_DAY);
        warfarin.setDoseUnit(UNIT_MG);
        warfarin.setDuration(100);
        warfarin.setDurationRate(RATE_DAY);
        warfarin.setFrequency("5");
        warfarin.setSingleDoseAmount(0.0001);
        warfarin.setDoseType(MAINTENANCE_DOSE);
        warfarin.setDoseRoute(ROUTE_ORAL);
        warfarin.setProspective(true);
        warfarin.setDoseCheck(true);
        drugsToScreen.add(aspirin);
        drugsToScreen.add(warfarin);
        this.checkVo = new OrderCheckVo();
        checkVo.setDrugsToScreen(drugsToScreen);
        checkVo.setProspectiveOnly(false);
        checkVo.setAgeInDays(27200);
        checkVo.setBodySurfaceAreaInSqM(2.01);
        checkVo.setWeightInKg(90);
        checkVo.setDrugDoseCheck(true);
    }
    /**
     * Verify check method returns some results
     */
    public void testDrugDoseCheck() {
        OrderCheckResultsVo results = capability.performDrugChecks(checkVo);
        System.out.println(results);
        assertTrue("Dose check should return at least one result", results.getDrugDoseCheckResults().getChecks().size() > 0);
    }
    /**
     * Verify flagging a chemo injectable drug
     */
    public void testChemoInjectable() {
        checkVo.getDrugsToScreen().clear();
        DrugCheckVo fluorouracil = new DrugCheckVo();
        fluorouracil.setGcnSeqNo("8818");
        fluorouracil.setDoseRate(RATE_HOUR);
        fluorouracil.setDoseUnit(UNIT_MG);
        fluorouracil.setDuration(1);
        fluorouracil.setDurationRate(RATE_HOUR);
        fluorouracil.setFrequency("1");
        fluorouracil.setSingleDoseAmount(50);
        fluorouracil.setDoseType(SINGLE_DOSE);
        fluorouracil.setDoseRoute(ROUTE_INTRAVENOUS);
        fluorouracil.setProspective(true);
        fluorouracil.setDoseCheck(true);
        checkVo.getDrugsToScreen().add(fluorouracil);
        OrderCheckResultsVo results = capability.performDrugChecks(checkVo);
        System.out.println(results);
        assertFalse("Should have dose result", results.getDrugDoseCheckResults().getChecks().isEmpty());
        DrugDoseCheckResultVo result = results.getDrugDoseCheckResults().getChecks().iterator().next();
        assertTrue("Should be chemo injectable", result.isChemoInjectable());
    }
