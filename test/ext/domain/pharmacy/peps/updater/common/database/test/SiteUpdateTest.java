package EXT.DOMAIN.pharmacy.peps.updater.common.database.test;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import EXT.DOMAIN.pharmacy.peps.common.test.integration.IntegrationTestCase;
import EXT.DOMAIN.pharmacy.peps.common.vo.FdbVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateReportVo;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.SiteUpdate;
public class SiteUpdateTest extends IntegrationTestCase {
    SiteUpdate siteUpdate;
    JdbcTemplate jdbcTemplate;
    public SiteUpdateTest() {
        super();
        setUp();
    }
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
            "xml/spring/datup/national/test/InterfaceContext.xml");
        this.siteUpdate = (SiteUpdate) context.getBean("siteUpdate");
        this.jdbcTemplate = new JdbcTemplate((DataSource) context.getBean("datupDataSource"));
    }
    /**
     * Test fdb version insert
     * 
     * @throws Exception
     */
    public void testFdbVersionInsert() throws Exception {
        clearFdbVersionTable(jdbcTemplate);
        siteUpdate.insertFdbVersion(false, "20081210", new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 14)));
        siteUpdate.insertFdbVersion(false, "20090101", new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7)));
        siteUpdate.insertFdbVersion(true, "20090102", new Date());
    }
    /**
     * Test fd site update insert
     * 
     * @throws Exception
     */
    public void testFdbSiteUpdateInsert() throws Exception {
        /* @todo (mspears:Feb 25, 2010) Use java.util.TimeZone type */
        long v1 = siteUpdate.retrieveFdbVersion(false, "20081210");
        long v2 = siteUpdate.retrieveFdbVersion(true, "20090102");
        long v3 = siteUpdate.retrieveFdbVersion(false, "20090101");
        siteUpdate.insertFdbSiteUpdate(405, "VA Region 4", v1, true, "success", new java.util.Date(System
            .currentTimeMillis()
            - (1000 * 60 * 60 * 24 * 6)), TimeZone.getDefault());
        siteUpdate.insertFdbSiteUpdate(402, "VA Region 3", v2, false, "Bad DIF-Custom export!", new java.util.Date(System
            .currentTimeMillis()
            + (1000 * 60 * 36)), TimeZone.getDefault());
        siteUpdate.insertFdbSiteUpdate(358, "VA Region 2", v2, true, "was successful!", new java.util.Date(System
            .currentTimeMillis()
            + (1000 * 60 * 15)), TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
        siteUpdate.insertFdbSiteUpdate(679, "VA Region 1", v2, true, "t", new java.util.Date(System.currentTimeMillis()
            + (1000 * 60 * 24)), TimeZone.getDefault());
        siteUpdate.insertFdbSiteUpdate(679, "VA Region 1", v3, false, "FDB database is unavailable!", new java.util.Date(
            System.currentTimeMillis() + (1000 * 60 * 12)), TimeZone.getDefault());
        siteUpdate.insertFdbSiteUpdate(358, "VA Region 2", v3, false, "Unable to write to disk!", new java.util.Date(System
            .currentTimeMillis()
            + (1000 * 60 * 15)), TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
    }
    /**
     * Test retrive All sites
     * 
     * @throws Exception
     */
    public void testRetrieveAll() throws Exception {
        List<SiteUpdateReportVo> list = siteUpdate.retrieveSiteUpdates();
        for (SiteUpdateReportVo vo : list) {
            System.out.println(vo);
        }
        assertEquals("wrong count", 127, list.size());
    }
    /**
     * Test retrieve failed updates
     * 
     * @throws Exception
     */
    public void testRetrieveFailed() throws Exception {
        List<SiteUpdateReportVo> list = siteUpdate.retrieveAbnormalSiteUpdates(true, "20090101", true, "20090102");
        for (SiteUpdateReportVo vo : list) {
            System.out.println(vo);
        }
        assertEquals("wrong count", 4, list.size());
    }
    /**
     * Test retrieve fdb id
     * 
     * @throws Exception
     */
    public void testRetrieveFdbVersion() throws Exception {
        long id = siteUpdate.retrieveFdbVersion(false, "20090101");
        assertTrue("bag id", id > 0);
        FdbVersionVo latestFdbVersion = siteUpdate.retrieveLastFdbVersion(false);
        System.out.println(latestFdbVersion);
        assertEquals("wrong date", "20090101", latestFdbVersion.getFdbSessionDate());
        FdbVersionVo latestFdbCustomVersion = siteUpdate.retrieveLastFdbVersion(true);
        System.out.println(latestFdbCustomVersion);
        assertEquals("wrong date", "20090102", latestFdbCustomVersion.getFdbSessionDate());
    }
    /**
     * Clear tables.
     * 
     * @param template template
     */
    public static void clearFdbVersionTable(JdbcTemplate template) {
        template.execute("delete from DATUP.FDB_SITE_UPDATE");
        template.execute("delete from DATUP.FDB_VERSION");
    }
