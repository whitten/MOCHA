/**
 * Copyright 2010, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import EXT.DOMAIN.pharmacy.peps.common.exception.CommonException;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
/**
 * Email utility for sending notifications.
 */
public class EmailUtility {
    public static final String TYPE_KEY = "${TYPE}";
    public static final String DATE_TIME_KEY = "${TIME}";
    public static final String VERSION_KEY = "${VERSION}";
    public static final String SITE_KEY = "${SITE}";
    public static final String REASON_KEY = "${REASON}";
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EmailUtility.class);
    private static final String LINE_SEPARATOR = "\n";
    private Configuration config;
    /**
     * 
     * 
     * @param mailingList
     * @param subject
     * @param templathPath
     * @param values
     */
    public boolean sendEmail(MailingListType mailingList, Map<String, String> values) {
        LOG.debug("Sending email: " + mailingList + "\n" + values);
        if ((mailingList.getTo(config) == null) || "".equals(mailingList.getTo(config))) {
            LOG.error("Unable to send email, misconfigured mailing list key: " + mailingList);
            return false;
        }
        try {
            String email = buildEmailFromTemplate(mailingList.getTemplate(config), values);
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", config.load().getString(Configuration.EMAIL_HOSTNAME));
            Authenticator auth = new EmailAuthenticator(config.load().getString(Configuration.EMAIL_USERNAME), config.load()
                .getString(Configuration.EMAIL_PASSWORD));
            Session session = Session.getInstance(props, auth);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.load().getString(Configuration.EMAIL_SENDER)));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailingList.getTo(config)));
            message.setSubject(getSubjectForTemplate(mailingList.getTemplate(config), values));
            message.setText(stripHtml(email));
            message.setDataHandler(new DataHandler(new HTMLDataSource(email)));
            // Set the email as high priority so it is less likely to be ignored
            message.addHeader("X-Priority", "1");
            message.addHeader("X-MSMail-Priority", "High");
            message.addHeader("Importance", "High");
            message.setSentDate(new Date(System.currentTimeMillis()));
            // This is simply the default send, we may need to customize
            Transport.send(message);
            return true;
        }
        catch (Throwable t) {
            LOG.error("Unable to send email notification to: " + mailingList.getTo(config), t);
        }
        return false;
    }
    /**
     * 
     * Applies templated values to the email
     * 
     * @param template
     * @param values
     * @return
     */
    private String finalizeEmail(String template, Map<String, String> values) {
        String[] parsed = template.split("<END>");
        String[] args = parsed[0].split("[ \t\n\f\r]");
        String email = parsed[2];
        for (String arg : args) {
            if ("".equals(arg)) {
                continue;
            }
            if (values.get(arg) == null) {
                LOG.warn("Missing email template value for key: " + arg);
                values.put(arg, "?");
            }
            email = email.replace(arg, values.get(arg));
        }
        return email;
    }
    /**
     * Strips HTML Tags
     * 
     * Finds the area between the body tags, and removes any tags in that range.
     * 
     * This method must be run after the templated are populated with data
     * 
     * @param email
     * @return
     */
    private String stripHtml(String email) {
        String[] split = email.split("</?BODY*?>");
        return split[1].replaceAll("<(.|\n)*?>", "");
    }
    /**
     * Reads the template from the given path directory.
     * 
     * Template returned has no formatting done and still needs to be split into parameters, subject, and html text
     * 
     * @param templatePath location of the file on the classpath
     * @return
     */
    private String readTemplate(URL templatePath) {
        try {
            InputStream inputStream = templatePath.openStream();
            if (inputStream == null) {
                throw new CommonException(CommonException.RESOURCE_UNAVAILABLE, templatePath);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                StringBuffer xmlRequest = new StringBuffer(2048);
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    xmlRequest.append(line);
                    xmlRequest.append(LINE_SEPARATOR);
                }
                return xmlRequest.toString();
            }
            finally {
                reader.close();
                inputStream.close();
            }
        }
        catch (IOException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Gets only the parameters from the path location.
     * 
     * @param templatePath
     * @return
     */
    public String[] getParametersFromPath(URL templatePath) {
        String[] parsed = readTemplate(templatePath).split("<END>");
        return parsed[0].split("[ \t\n\f\r]");
    }
    /**
     * There may be templates in the subject, so we'll parse that
     * 
     * @param templatePath
     * @param values
     * @return
     */
    public String getSubjectForTemplate(URL templatePath, Map<String, String> values) {
        String[] parsed = readTemplate(templatePath).split("<END>");
        String[] args = parsed[0].split("[ \t\n\f\r]");
        String subject = parsed[1];
        for (String arg : args) {
            if ("".equals(arg)) {
                continue;
            }
            if (values.get(arg) == null) {
                LOG.warn("Missing email template value for key: " + arg);
                values.put(arg, "?");
            }
            subject = subject.replace(arg, values.get(arg));
        }
        return subject.trim();
    }
    /**
     * Sets configuration
     * 
     * @param config
     */
    public void setConfiguration(Configuration config) {
        this.config = config;
    }
    /**
     * creates the text body of the email from the template location and the values
     * 
     * @param templatePath
     * @param values
     * @return
     */
    public String buildEmailFromTemplate(URL templatePath, Map<String, String> values) {
        String template = readTemplate(templatePath);
        String email = finalizeEmail(template, values);
        return email.trim();
    }
    /*
     * Inner class to act as a JAF datasource to send HTML e-mail content
     */
    private static class HTMLDataSource implements DataSource {
        private String html;
        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }
        // Return html string in an InputStream.
        // A new stream must be returned each time.
        public InputStream getInputStream() throws IOException {
            if (html == null) {
                throw new IOException("Missing HTML content");
            }
            return new ByteArrayInputStream(html.getBytes("UTF-8"));
        }
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }
        public String getContentType() {
            return "text/html";
        }
        public String getName() {
            return "DATUP HTML Data Source";
        }
    }
    /**
     * Inner class for the Authenticator.
     * 
     * I'm not sure if we'll need it, but seems like the proper design here, ultimately
     */
    private class EmailAuthenticator extends Authenticator {
        private String username;
        private String password;
        protected EmailAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }
