package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * Types of mailing lists and associated configuration values.
 */
public enum MailingListType {
    
    NATIONAL_SUCCESS(Configuration.EMAIL_LIST_SUCCESS, "template/emails/national.success.template",Configuration.EMAIL_TEMPLATE_SUCCESS), 
    NATIONAL_FAILURE(Configuration.EMAIL_LIST_FAILURE, "template/emails/national.failure.template", Configuration.EMAIL_TEMPLATE_FAILURE),
    LOCAL_SUCCESS(Configuration.EMAIL_LIST_SUCCESS, "template/emails/local.success.template", Configuration.EMAIL_TEMPLATE_SUCCESS),
    LOCAL_FAILURE(Configuration.EMAIL_LIST_FAILURE, "template/emails/local.failure.template", Configuration.EMAIL_TEMPLATE_FAILURE),
    UPDATE_AVAILABLE(Configuration.EMAIL_LIST_UPDATE_AVAILABLE, "template/emails/update.available.template", Configuration.EMAIL_TEMPLATE_UPDATE_AVAILABLE);
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MailingListType.class);
    
    private String toKey;
    private String templatePath;
    private String templateOverride;
    /**
     * private constructor to preven additional enums
     * 
     * @param toValue
     * @param templateValue
     */
    private MailingListType(String toValue, String templateValue, String templateOverride) {
        this.toKey = toValue;
        this.templatePath = templateValue;
        this.templateOverride = templateOverride;
    }
    /**
     * Gets the mailing list from the given configuration
     * 
     * @param config Configuration file to help
     * @return
     */
    public String getTo(Configuration config) {
        return config.load().getString(toKey);
    }
    /**
     * Gets the template path location
     *
     * @param config Configuration file to help
     * @return
     */
    public URL getTemplate(Configuration config) {
        if(config.load().containsKey(templateOverride)) {
            File file = new File(config.load().getString(templateOverride));
            
            if(file.exists() && file.canRead()) {
                try {
                    return file.toURI().toURL();
                }
                catch(MalformedURLException e) {
                    LOG.error("Internal error", e);
                }
            }
            else {
                LOG.warn("Defaulting to the bundled email template. Unable to read the template file at: " + file.getAbsolutePath());
            }
        }
        
        return getClass().getClassLoader().getResource(templatePath);
    }
    /**
     * String representation.
     * 
     * @param config configuration
     * @return string
     */
    public String toString(Configuration config) {
        ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        b.append("template", templatePath);
        b.append("templateOverride", templateOverride);
        b.append("to", getTo(config));
        return b.toString();
    }
    /**
     * String representation.
     * 
     * @return string
     * 
     * @see java.lang.Enum#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
