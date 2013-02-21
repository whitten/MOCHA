package EXT.DOMAIN.pharmacy.peps.updater.common.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import org.springframework.jdbc.core.RowMapper;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateReportVo;
public class SiteUpdateMapper implements RowMapper {
    public SiteUpdateReportVo mapRow(ResultSet rs, int line) throws SQLException {
        SiteUpdateReportVo siteUpdate = new SiteUpdateReportVo();
        siteUpdate.setSiteId(rs.getLong(1));
        siteUpdate.setSiteName(rs.getString(2));
        siteUpdate.setRegionName(rs.getString(3));
        if (rs.getLong(4) > 0) {
            if ("Y".equalsIgnoreCase(rs.getString(9))) {
                siteUpdate.setFdbCustomId(rs.getLong(4));
                siteUpdate.setFdbCustomSuccessful("Y".equalsIgnoreCase(rs.getString(5)));
                siteUpdate.setFdbCustomMessage(rs.getString(6));
                siteUpdate.setFdbCustomLastUpdateTime(rs.getTimestamp(7));
                siteUpdate.setFdbCustomLastUpdateTimeZone(TimeZone.getTimeZone(rs.getString(8)));
                siteUpdate.setFdbCustomSessionDate(rs.getString(10));
            }
            else {
                siteUpdate.setFdbId(rs.getLong(4));
                siteUpdate.setFdbSuccessful("Y".equalsIgnoreCase(rs.getString(5)));
                siteUpdate.setFdbMessage(rs.getString(6));
                siteUpdate.setFdbLastUpdateTime(rs.getTimestamp(7));
                siteUpdate.setFdbLastUpdateTimeZone(TimeZone.getTimeZone(rs.getString(8)));
                siteUpdate.setFdbSessionDate(rs.getString(10));
            }
        }
        return siteUpdate;
    }
