package EXT.DOMAIN.pharmacy.peps.updater.national.test;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.TimeZone;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.SiteUpdate;
import EXT.DOMAIN.pharmacy.peps.updater.common.database.test.SiteUpdateTest;
import EXT.DOMAIN.pharmacy.peps.updater.national.session.DifReportService;
import junit.framework.TestCase;
/**
 * Test the report generation.
 */
public class ReportTest extends TestCase {
    DifReportService reportService;
    SiteUpdate siteUpdate;
    JdbcTemplate jdbcTemplate;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        ApplicationContext context = new ClassPathXmlApplicationContext(
            "xml/spring/datup/national/test/InterfaceContext.xml");
        this.reportService = (DifReportService) context.getBean("difReportService");
        this.siteUpdate = (SiteUpdate) context.getBean("siteUpdate");
        jdbcTemplate = new JdbcTemplate((DataSource) context.getBean("datupDataSource"));
        SiteUpdateTest.clearFdbVersionTable(jdbcTemplate);
        siteUpdate.insertFdbVersion(false, "20090902", new Date(System.currentTimeMillis() - (1000l * 60 * 60 * 24 * 365)));
        siteUpdate.insertFdbVersion(false, "20091002", new Date(System.currentTimeMillis() - (1000l * 60 * 60 * 24 * 7)));
        siteUpdate.insertFdbVersion(true, "20091002", new Date());
        long v1 = siteUpdate.retrieveFdbVersion(false, "20090902");
        long v2 = siteUpdate.retrieveFdbVersion(true, "20091002");
        long v3 = siteUpdate.retrieveFdbVersion(false, "20091002");
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
        siteUpdate.insertFdbSiteUpdate(436, "VA Region 3", v1, true, "success", new java.util.Date(System
            .currentTimeMillis()
            + (1000 * 60 * 15)), TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
        siteUpdate.insertFdbSiteUpdate(436, "VA Region 3", v3, true, "success", new java.util.Date(System
            .currentTimeMillis()
            + (1000 * 60 * 46)), TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
        siteUpdate.insertFdbSiteUpdate(436, "VA Region 3", v2, true, "success", new java.util.Date(System
            .currentTimeMillis()
            + (1000 * 60 * 59)), TimeZone.getTimeZone(TimeZone.getAvailableIDs()[0]));
    }
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
    }
    /**
     * Test site version report.
     * 
     * @throws Exception
     */
    public void testSiteVersionReport() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        reportService.generateSiteVersionReport(writer, true, false);
        writer.flush();
        out.close();
        System.out.println(out.toString());
        assertTrue("Bad report", out.toString() != null);
    }
    /**
     * Test export site version report.
     * 
     * @throws Exception
     */
    public void testExportSiteVersionReport() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        reportService.exportSiteVersionReport(writer, true, false);
        writer.flush();
        out.close();
        System.out.println(out.toString());
        assertTrue("Bad report", out.toString() != null);
    }
    /**
     * Test site version report.
     * 
     * @throws Exception
     */
    public void testSiteHistoryReport() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        reportService.generateSiteHistoryReport(writer, 436, 100, 3, 2009, 12, 2040);
        writer.flush();
        out.close();
        System.out.println(out.toString());
        assertTrue("Bad report", out.toString() != null);
    }
    /**
     * Test export site version report.
     * 
     * @throws Exception
     */
    public void testExportSiteHistoryReport() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        reportService.exportSiteHistoryReport(writer, 436, 100, 3, 2009, 12, 2040);
        writer.flush();
        out.close();
        System.out.println(out.toString());
        assertTrue("Bad report", out.toString() != null);
    }
