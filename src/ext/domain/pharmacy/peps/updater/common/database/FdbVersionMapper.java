package EXT.DOMAIN.pharmacy.peps.updater.common.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import EXT.DOMAIN.pharmacy.peps.common.vo.FdbVersionVo;
/**
 * Maps FDB versions.
 */
public class FdbVersionMapper implements RowMapper {
    /**
     * Map version row.
     * 
     * @param rs resultset
     * @param line current row
     * @return version
     * @throws SQLException error occurs
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public FdbVersionVo mapRow(ResultSet rs, int line) throws SQLException {
        FdbVersionVo siteUpdate = new FdbVersionVo();
        siteUpdate.setFdbId(rs.getLong(1));
        siteUpdate.setIsCustom("Y".equalsIgnoreCase(rs.getString(2)));
        siteUpdate.setFdbSessionDate(rs.getString(3));
        siteUpdate.setWhen(rs.getTimestamp(4));
        return siteUpdate;
    }
