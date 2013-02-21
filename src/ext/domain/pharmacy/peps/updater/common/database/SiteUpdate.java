/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import EXT.DOMAIN.pharmacy.peps.common.vo.FdbVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateReportVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteVo;
public class SiteUpdate {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SiteUpdate.class);
    private JdbcTemplate jdbcTemplate;
    /**
     * Insert new FDB site update record.
     * 
     * @param siteId site ID
     * @param regionName region name
     * @param fdbId FDB Version ID
     * @param successful true if update successful
     * @param message update status message
     * @param lastUpdate when update occurred
     * @param timeZone corresponding timezone
     */
    public void insertFdbSiteUpdate(long siteId, String regionName, long fdbId, boolean successful, String message,
                                    Date lastUpdate, TimeZone timeZone) {
        String sql = "insert into datup.fdb_site_update(site_id, region_name, fdb_id, is_successful, message, Last_Update_Time, Last_Update_Timezone)"
            + " values (?,?,?,?,?,?,?)";
        Object[] params = new Object[] {siteId, regionName, fdbId, successful ? "Y" : "N", message, lastUpdate,
                                        timeZone.getID()};
        int[] types = new int[] {Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP,
                                 Types.VARCHAR};
        jdbcTemplate.update(sql, params, types);
    }
    public void insertFdbVersion(boolean isCustom, String versionNumber, Date when) {
        String sql = "insert into datup.fdb_version(is_custom, version_number, when_created)" + " values (?, ?, ?)";
        Object[] params = new Object[] {isCustom ? "Y" : "N", versionNumber, when};
        int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP};
        jdbcTemplate.update(sql, params, types);
    }
    /**
     * Retrieve all site updates.
     * 
     * @return list of updates
     */
    @SuppressWarnings("unchecked")
    public List<SiteUpdateReportVo> retrieveSiteUpdates() {
        String sql = " SELECT site.site_id, site.site_name, "
            + "          version.region_name, version.fdb_id, version.is_successful, version.message, version.last_update_time, "
            + "          version.last_update_timezone, fdb.is_custom, fdb.version_number "
            + "        FROM datup.fdb_site_update version JOIN datup.fdb_version FDB "
            + "        ON FDB.fdb_id = version.fdb_id JOIN "
            + "          (select site_id,  max(last_update_time)  maxdate FROM "
            + "          (select * from  datup.fdb_site_update fupdate, datup.fdb_version fdb where fdb.fdb_id = fupdate.fdb_id "
            + "          and fdb.is_custom = ?) fdb_site_update group by site_id)"
            + "          MAXRESULTS ON version.site_id = MAXRESULTS.site_id "
            + "        AND version.last_update_time = MAXRESULTS.maxdate RIGHT OUTER JOIN datup.site site "
            + "        ON site.site_id = version.site_id AND fdb.is_custom = ? ORDER BY 1 ASC, 7 DESC";
        int[] types = new int[] {Types.VARCHAR, Types.VARCHAR};
        jdbcTemplate.setMaxRows(Integer.MAX_VALUE);
        List<SiteUpdateReportVo> fdbUpdateResults = jdbcTemplate.query(sql, new Object[] {"N", "N"}, types,
            new SiteUpdateMapper());
        List<SiteUpdateReportVo> custom = jdbcTemplate.query(sql, new Object[] {"Y", "Y"}, types, new SiteUpdateMapper());
        // align the fdbUpdateResults and fdbCustomResults
        for (int i = 0, size = fdbUpdateResults.size(); i < size; i++) {
            fdbUpdateResults.get(i).setFdbCustomMessage(custom.get(i).getFdbCustomMessage());
            fdbUpdateResults.get(i).setFdbCustomSuccessful(custom.get(i).isFdbCustomSuccessful());
            fdbUpdateResults.get(i).setFdbCustomLastUpdateTime(custom.get(i).getFdbCustomLastUpdateTime());
            fdbUpdateResults.get(i).setFdbCustomLastUpdateTimeZone(custom.get(i).getFdbCustomLastUpdateTimeZone());
            fdbUpdateResults.get(i).setFdbCustomSessionDate(custom.get(i).getFdbCustomSessionDate());
            fdbUpdateResults.get(i).setFdbCustomId(custom.get(i).getFdbCustomId());
            if (fdbUpdateResults.get(i).getRegionName() == null) {
                fdbUpdateResults.get(i).setRegionName(custom.get(i).getRegionName());
            }
        }
        return fdbUpdateResults;
    }
    /**
     * Retrieve site update history.
     * 
     * @param site site number
     * @param limit number of records
     * @param startMonth start month
     * @param startYear start year
     * @param endMonth end month
     * @param endYear end year
     * @return site history
     */
    @SuppressWarnings("unchecked")
    public List<SiteUpdateReportVo> retrieveSiteHistory(long site, int limit, int startMonth, int startYear, int endMonth,
                                                        int endYear) {
        String sql = " SELECT site.site_id, site.site_name, version.region_name, version.fdb_id, version.is_successful,"
            + "        version.message, version.last_update_time, version.last_update_timezone, fdb.is_custom, fdb.version_number"
            + "        FROM datup.fdb_site_update version, datup.fdb_version fdb, datup.site site"
            + "        WHERE fdb.fdb_id = version.fdb_id AND site.site_id = version.site_id AND site.site_id = ?"
            + "        AND fdb.when_created >= ? AND fdb.when_created <= ?              "
            + "        ORDER BY 10 DESC, 7 DESC";
        int[] types = new int[] {Types.INTEGER, Types.TIMESTAMP, Types.TIMESTAMP};
        Calendar startDate = new GregorianCalendar(startYear, startMonth, 1);
        startDate.set(Calendar.DAY_OF_MONTH, startDate.getMinimum(Calendar.DAY_OF_MONTH));
        startDate.set(Calendar.HOUR_OF_DAY, startDate.getMinimum(Calendar.HOUR_OF_DAY));
        startDate.set(Calendar.MINUTE, startDate.getMinimum(Calendar.MINUTE));
        startDate.set(Calendar.SECOND, startDate.getMinimum(Calendar.SECOND));
        Calendar endDate = new GregorianCalendar(endYear, endMonth, 1);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getMaximum(Calendar.DAY_OF_MONTH));
        endDate.set(Calendar.HOUR_OF_DAY, endDate.getMaximum(Calendar.HOUR_OF_DAY));
        endDate.set(Calendar.MINUTE, endDate.getMaximum(Calendar.MINUTE));
        endDate.set(Calendar.SECOND, endDate.getMaximum(Calendar.SECOND));
        jdbcTemplate.setMaxRows(limit);
        return jdbcTemplate.query(sql, new Object[] {site, startDate.getTime(), endDate.getTime()}, types,
            new SiteUpdateMapper());
    }
    /**
     * Retrieve all abnormal site updates.
     * 
     * @param includeFdbWarning true if FDB warnings are enabled
     * @param fdbSessionDate current FDB session date
     * @param includeCustomWarning true if FDB custom warning are enabled
     * @param customSessionDate FDB custom session date
     * @return list of abnormal updates
     */
    @SuppressWarnings("unchecked")
    public List<SiteUpdateReportVo> retrieveAbnormalSiteUpdates(boolean includeFdbWarning, String fdbSessionDate,
                                                                boolean includeCustomWarning, String customSessionDate) {
        String sql = "SELECT site.site_id, site.site_name, "
            + "version.region_name, version.fdb_id, version.is_successful, version.message, version.last_update_time, "
            + "version.last_update_timezone, fdb.is_custom, fdb.version_number "
            + "FROM  datup.fdb_site_update version, datup.fdb_version FDB, datup.site site "
            + "WHERE FDB.fdb_id = version.fdb_id "
            + "AND site.site_id = version.site_id "
            + "AND ("
            + "  (FDB.is_custom = 'N' AND"
            + "   version.last_update_time ="
            + "   (select max(last_update_time) from datup.fdb_site_update x, datup.fdb_version FDB where x.site_id = version.site_id and FDB.fdb_id = x.fdb_id and FDB.is_custom = 'N')"
            + "   AND ((version.is_successful = 'N') OR ((? = 1) AND (FDB.version_number <> ?))))"
            + "OR"
            + "  (FDB.is_custom = 'Y' AND"
            + "   version.last_update_time = "
            + "   (select max(last_update_time) from datup.fdb_site_update x, datup.fdb_version FDB where x.site_id = version.site_id and FDB.fdb_id = x.fdb_id and FDB.is_custom = 'Y')"
            + "   AND ((version.is_successful = 'N') OR ((? = 1) AND (FDB.version_number <> ?))))"
            + ") ORDER BY 1 ASC, 7 DESC";
        Object[] params = new Object[] {includeFdbWarning, fdbSessionDate, includeCustomWarning, customSessionDate};
        int[] types = new int[] {Types.SMALLINT, Types.VARCHAR, Types.SMALLINT, Types.VARCHAR};
        jdbcTemplate.setMaxRows(Integer.MAX_VALUE);
        List<SiteUpdateReportVo> fdbUpdateResults = jdbcTemplate.query(sql, params, types, new SiteUpdateMapper());
        for (SiteUpdateReportVo site : fdbUpdateResults) {
            if (site.getFdbCustomId() != null) {
                // get non-custom data
                SiteUpdateReportVo nonCustom = retrieveLastSiteUpdate(site.getSiteId(), false);
                if (nonCustom != null) {
                    site.setFdbMessage(nonCustom.getFdbMessage());
                    site.setFdbSuccessful(nonCustom.isFdbSuccessful());
                    site.setFdbLastUpdateTime(nonCustom.getFdbLastUpdateTime());
                    site.setFdbLastUpdateTimeZone(nonCustom.getFdbLastUpdateTimeZone());
                    site.setFdbSessionDate(nonCustom.getFdbSessionDate());
                    site.setFdbId(nonCustom.getFdbId());
                }
            }
            else {
                // get custom data
                SiteUpdateReportVo custom = retrieveLastSiteUpdate(site.getSiteId(), true);
                if (custom != null) {
                    site.setFdbCustomMessage(custom.getFdbCustomMessage());
                    site.setFdbCustomSuccessful(custom.isFdbCustomSuccessful());
                    site.setFdbCustomLastUpdateTime(custom.getFdbCustomLastUpdateTime());
                    site.setFdbCustomSessionDate(custom.getFdbCustomSessionDate());
                    site.setFdbCustomLastUpdateTimeZone(custom.getFdbCustomLastUpdateTimeZone());
                    site.setFdbCustomId(custom.getFdbCustomId());
                }
            }
        }
        return fdbUpdateResults;
    }
    /**
     * Retrieve the last FDB version.
     * 
     * @param isCustom true if custom version
     * @return FDB version
     */
    @SuppressWarnings("unchecked")
    public FdbVersionVo retrieveLastFdbVersion(boolean isCustom) {
        String sql = "select fdb_id, is_custom, version_number, when_created from datup.fdb_version where is_custom = ? and version_number = "
            + "(select max(version_number) from datup.fdb_version where is_custom = ?)";
        Object[] params = new Object[] {isCustom ? "Y" : "N", isCustom ? "Y" : "N"};
        int[] types = new int[] {Types.VARCHAR, Types.VARCHAR};
        jdbcTemplate.setMaxRows(1);
        List<FdbVersionVo> list = jdbcTemplate.query(sql, params, types, new FdbVersionMapper());
        if (list.size() != 1) {
            LOG.warn("No FDB versions available, returning empty version");
            FdbVersionVo vo = new FdbVersionVo();
            vo.setFdbId(0);
            vo.setFdbSessionDate("00000000");
            vo.setIsCustom(isCustom);
            vo.setWhen(new Date());
            return vo;
        }
        return list.get(0);
    }
    /**
     * Retrieve a specific FDB version.
     * 
     * @param isCustom true is custom version
     * @param versionNumber session date
     * @return identifier
     */
    public long retrieveFdbVersion(boolean isCustom, String versionNumber) {
        String sql = "select fdb_id from datup.fdb_version where is_custom = ? and  version_number = ?";
        Object[] params = new Object[] {isCustom ? "Y" : "N", versionNumber};
        int[] types = new int[] {Types.VARCHAR, Types.VARCHAR};
        try {
            jdbcTemplate.setMaxRows(1);
            return jdbcTemplate.queryForLong(sql, params, types);
        }
        catch (DataAccessException e) {
            LOG.error("Unable to retrieve FDB Version: " + (isCustom ? "Custom" : "Incremental") + ", " + versionNumber);
            throw e;
        }
    }
    /**
     * Retrieve Site list.
     * 
     * @return site list
     */
    @SuppressWarnings("unchecked")
    public List<SiteVo> retrieveSiteList() {
        String sql = "SELECT site_id, site_name, visn from datup.site order by 1";
        jdbcTemplate.setMaxRows(Integer.MAX_VALUE);
        return jdbcTemplate.query(sql, new SiteMapper());
    }
    /**
     * Retrieve site update.
     * 
     * @param siteId site ID
     * @param isCustom true if custom update
     * @return
     */
    @SuppressWarnings("unchecked")
    public Calendar[] retrieveUpdateDateRange() {
        String sql = "SELECT min(when_created), max(when_created) from datup.fdb_version";
        jdbcTemplate.setMaxRows(1);
        List<Calendar[]> dates = jdbcTemplate.query(sql, new RowMapper() {
            /**
             * Map Date.
             * 
             * @param rs result set
             * @param line line #
             * @return site
             * @throws SQLException DB error
             * 
             * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
             */
            public Calendar[] mapRow(ResultSet rs, int line) throws SQLException {
                Calendar minimum = new GregorianCalendar();
                if (rs.getTimestamp(1) != null) {
                    minimum.setTime(rs.getTimestamp(1));
                }
                minimum.set(Calendar.DAY_OF_MONTH, minimum.getMinimum(Calendar.DAY_OF_MONTH));
                Calendar maximum = new GregorianCalendar();
                if (rs.getTimestamp(2) != null) {
                    maximum.setTime(rs.getTimestamp(2));
                }
                maximum.set(Calendar.DAY_OF_MONTH, maximum.getMaximum(Calendar.DAY_OF_MONTH));
                return new Calendar[] {minimum, maximum};
            }
        });
        return dates.get(0);
    }
    /**
     * Retrieve site update.
     * 
     * @param siteId site ID
     * @param isCustom true if custom update
     * @return
     */
    @SuppressWarnings("unchecked")
    private SiteUpdateReportVo retrieveLastSiteUpdate(long siteId, boolean isCustom) {
        String sql = "select b.version_number, a.site_id, a.last_update_time, a.message, a.is_successful, a.fdb_id, a.last_update_timezone"
            + " from datup.fdb_site_update a, datup.fdb_version b "
            + " where b.fdb_id = a.fdb_id "
            + " and b.is_custom = ?"
            + " and a.last_update_time = (select max(last_update_time) from datup.fdb_site_update x "
            + " where x.site_id = a.site_id "
            + " and exists( select * from datup.fdb_version c where c.fdb_id = x.fdb_id and c.is_custom = ?)) and a.site_id = ?";
        Object[] params = new Object[] {isCustom ? "Y" : "N", isCustom ? "Y" : "N", siteId};
        int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.NUMERIC};
        jdbcTemplate.setMaxRows(1);
        List<SiteUpdateReportVo> results = (isCustom) ? jdbcTemplate.query(sql, params, types, new FdbCustomUpdateMapper())
            : jdbcTemplate.query(sql, params, types, new FdbUpdateMapper());
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
    /**
     * Datasource.
     * 
     * @param dataSource property
     */
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
