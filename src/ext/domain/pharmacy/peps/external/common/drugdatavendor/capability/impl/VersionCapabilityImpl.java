/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.impl;
import java.util.Collections;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.VersionCapability;
import dbank.database.FDBException;
import dbank.dif.FWStatus;
import dbank.dif.Navigation;
/**
 * Retrieve drug data vendor version information
 */
public class VersionCapabilityImpl implements VersionCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(VersionCapabilityImpl.class);
    private static final String BUILD_VERSION = "BUILDVERSION";
    private static final String DB_VERSION = "DBVERSION";
    private static final String ISSUE_DATE = "ISSUEDATE";
    private static final String SQL = "SELECT " + BUILD_VERSION + ", " + DB_VERSION + ", " + ISSUE_DATE + " FROM CT_VERSION";
    private static final String NOT_AVAILABLE = "NOT AVAILABLE";
    private Navigation navigation;
    private JdbcTemplate jdbcTemplate;
    /**
     * Retrieve drug data vendor version information.
     * 
     * @return DrugDataVendorVersionVo with version information
     */
    @SuppressWarnings("unchecked")
    public DrugDataVendorVersionVo retrieveDrugDataVendorVersion() {
        DrugDataVendorVersionVo version = new DrugDataVendorVersionVo();
        FWStatus status = null;
        try {
            status = navigation.getStatus();
        }
        catch (FDBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.DRUG_DATA_VENDOR);
        }
        version.setBuildVersion(!"".equals(status.getDatabaseBuildVersion()) ? status.getDatabaseBuildVersion()
            : NOT_AVAILABLE);
        version
            .setDataVersion(!"".equals(status.getDatabaseDataVersion()) ? status.getDatabaseDataVersion() : NOT_AVAILABLE);
        version.setIssueDate(!"".equals(status.getDatabaseIssueDate()) ? status.getDatabaseIssueDate() : NOT_AVAILABLE);
        Map map = Collections.EMPTY_MAP;
        try {
            map = jdbcTemplate.queryForMap(SQL);
        }
        catch (DataAccessException e) {
            LOG.warn("Unable to query CT_VERSION table for custom data version information. " + "Returning '"
                + NOT_AVAILABLE + "' for all values!", e);
        }
        version.setCustomBuildVersion(getValue(map, BUILD_VERSION));
        version.setCustomDataVersion(getValue(map, DB_VERSION));
        version.setCustomIssueDate(getValue(map, ISSUE_DATE));
        return version;
    }
    /**
     * Return the value in the the map with the given key (all caps). If it does not have the value, look for it with a lower
     * case version of the key.
     * 
     * @param map Map from JdbcTemplate
     * @param key all CAPS column name
     * @return String value, if no value return {@link #NOT_AVAILABLE}
     */
    private String getValue(Map map, String key) {
        Object value = map.get(key);
        if (value == null) {
            value = map.get(key.toLowerCase());
        }
        if (value == null) {
            value = NOT_AVAILABLE;
        }
        return (String) value;
    }
    /**
     * 
     * @param navigation property
     */
    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }
    /**
     * 
     * @param dataSource property
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
