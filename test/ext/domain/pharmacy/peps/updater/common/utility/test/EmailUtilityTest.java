/**
 * Copyright 2010, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.Configuration;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.EmailUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.MailingListType;
import junit.framework.TestCase;
/**
 * Unit test for the email interface.
 */
public class EmailUtilityTest extends TestCase {
    Configuration config;
    EmailUtility utility;
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
        ApplicationContext context = new ClassPathXmlApplicationContext("xml/spring/datup/local/test/InterfaceContext.xml");
        this.config = (Configuration) context.getBean("datupConfiguration");
        this.utility = (EmailUtility) context.getBean("emailUtility");
        config.load().setProperty(Configuration.EMAIL_HOSTNAME, "mail.datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_USERNAME, "");
        config.load().setProperty(Configuration.EMAIL_PASSWORD, "");
        config.load().setProperty(Configuration.EMAIL_SENDER, "noreply@datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_LIST_SUCCESS, "xxx@datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_LIST_FAILURE, "xxx@datasys.swri.edu");
        config.load().setProperty(Configuration.EMAIL_LIST_UPDATE_AVAILABLE, "xxx@datasys.swri.edu");
    }
    public void testBuildEmail() {
        Map<String, String> values = new HashMap<String, String>();
        values.put(EmailUtility.TYPE_KEY, "Incremental");
        values.put(EmailUtility.DATE_TIME_KEY, "12/25/2010");
        values.put(EmailUtility.VERSION_KEY, "3.2");
        values.put(EmailUtility.SITE_KEY, "112");
        values.put(EmailUtility.REASON_KEY, "Unable to connect to database.");
        String email = utility.buildEmailFromTemplate(MailingListType.NATIONAL_SUCCESS.getTemplate(config), values);
        System.out.println(email);
        assertTrue("Email template null", !"".equals(email));
    }
    public void testGetTemplateParameters() {
        String[] params = utility.getParametersFromPath(MailingListType.NATIONAL_SUCCESS.getTemplate(config));
        assertTrue("First Parameter not ${TYPE} as expected, was " + params[0], params[0].equals("${TYPE}"));
        assertTrue("First Parameter not ${VERSION} as expected, was " + params[1], params[1].equals("${VERSION}"));
        assertTrue("First Parameter not ${TIME} as expected, was " + params[2], params[2].equals("${TIME}"));
    }
    public void testGetTemplateSubject() {
        String subject = utility.getSubjectForTemplate(MailingListType.NATIONAL_SUCCESS.getTemplate(config),
            new HashMap<String, String>());
        System.out.println(subject);
        assertTrue("Email subject null", !"".equals(subject));
    }
    public void testSendMail() {
        Map<String, String> values = new HashMap<String, String>();
        values.put(EmailUtility.TYPE_KEY, "FDB-DIF Incremental Data");
        values.put(EmailUtility.DATE_TIME_KEY, "12/25/2010");
        values.put(EmailUtility.VERSION_KEY, "3.2");
        values.put(EmailUtility.SITE_KEY, "112");
        values.put(EmailUtility.REASON_KEY, "Unable to connect to database.");
        assertTrue("National success email send failed", utility.sendEmail(MailingListType.NATIONAL_SUCCESS, values));
        assertTrue("National failure email send failed", utility.sendEmail(MailingListType.NATIONAL_FAILURE, values));
        assertTrue("National update available email send failed", utility
            .sendEmail(MailingListType.UPDATE_AVAILABLE, values));
        assertTrue("Local failure email send failed", utility.sendEmail(MailingListType.LOCAL_FAILURE, values));
        assertTrue("Local success email send failed", utility.sendEmail(MailingListType.LOCAL_SUCCESS, values));
    }
    
    public void testCustomTemplateMail() {
        Map<String, String> values = new HashMap<String, String>();
        values.put(EmailUtility.TYPE_KEY, "VA-Custom Incremental Data");
        values.put(EmailUtility.DATE_TIME_KEY, "12/25/2010");
        values.put(EmailUtility.VERSION_KEY, "3.2");
        values.put(EmailUtility.SITE_KEY, "112");
        values.put(EmailUtility.REASON_KEY, "Unable to connect to database.");
        config.load().setProperty(Configuration.EMAIL_TEMPLATE_SUCCESS, "etc/datup/overriden.national.success.template");
        
        String email = utility.buildEmailFromTemplate(MailingListType.NATIONAL_SUCCESS.getTemplate(config), values);
        System.out.println(email);
        assertTrue("Email template incorrect", email.contains("OVERRIDEN EMAIL TEMPLATE"));        
        assertTrue("National custom success email send failed", utility.sendEmail(MailingListType.NATIONAL_SUCCESS, values));
        
        config.load().setProperty(Configuration.EMAIL_TEMPLATE_SUCCESS, "");
        assertTrue("National custom success email send failed", utility.sendEmail(MailingListType.NATIONAL_SUCCESS, values));
    }
