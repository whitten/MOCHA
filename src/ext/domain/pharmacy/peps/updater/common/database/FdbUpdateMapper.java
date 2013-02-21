package EXT.DOMAIN.pharmacy.peps.updater.common.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import org.springframework.jdbc.core.RowMapper;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateReportVo;
public class FdbUpdateMapper implements RowMapper {
    public SiteUpdateReportVo mapRow(ResultSet rs, int line) throws SQLException {
        SiteUpdateReportVo siteUpdate = new SiteUpdateReportVo();
        siteUpdate.setFdbSessionDate(rs.getString(1));
        siteUpdate.setSiteId(rs.getLong(2));
        siteUpdate.setFdbLastUpdateTime(rs.getTimestamp(3));
        siteUpdate.setFdbMessage(rs.getString(4));
        siteUpdate.setFdbSuccessful("Y".equalsIgnoreCase(rs.getString(5)));
        siteUpdate.setFdbId(rs.getLong(6));
        siteUpdate.setFdbLastUpdateTimeZone(TimeZone.getTimeZone(rs.getString(7)));
        return siteUpdate;
    }
