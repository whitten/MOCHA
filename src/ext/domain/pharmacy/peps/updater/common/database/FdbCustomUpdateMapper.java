package EXT.DOMAIN.pharmacy.peps.updater.common.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import org.springframework.jdbc.core.RowMapper;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteUpdateReportVo;
public class FdbCustomUpdateMapper implements RowMapper {
    public SiteUpdateReportVo mapRow(ResultSet rs, int line) throws SQLException {
        SiteUpdateReportVo siteUpdate = new SiteUpdateReportVo();
        siteUpdate.setFdbCustomSessionDate(rs.getString(1));
        siteUpdate.setSiteId(rs.getLong(2));
        siteUpdate.setFdbCustomLastUpdateTime(rs.getTimestamp(3));
        siteUpdate.setFdbCustomMessage(rs.getString(4));
        siteUpdate.setFdbCustomSuccessful("Y".equalsIgnoreCase(rs.getString(5)));
        siteUpdate.setFdbCustomId(rs.getLong(6));
        siteUpdate.setFdbCustomLastUpdateTimeZone(TimeZone.getTimeZone(rs.getString(7)));
        return siteUpdate;
    }
