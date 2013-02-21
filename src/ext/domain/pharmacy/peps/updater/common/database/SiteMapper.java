package EXT.DOMAIN.pharmacy.peps.updater.common.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import EXT.DOMAIN.pharmacy.peps.common.vo.SiteVo;
/**
 * Maps Site VOs.
 */
public class SiteMapper implements RowMapper {
    /**
     * Map Site VO.
     * 
     * @param rs result set
     * @param line line #
     * @return site
     * @throws SQLException DB error
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    public SiteVo mapRow(ResultSet rs, int line) throws SQLException {
        SiteVo site = new SiteVo();
        site.setId(rs.getLong(1));
        site.setName(rs.getString(2));
        site.setShortName(rs.getString(2));
        site.setVisn(rs.getString(3));
        return site;
    }
