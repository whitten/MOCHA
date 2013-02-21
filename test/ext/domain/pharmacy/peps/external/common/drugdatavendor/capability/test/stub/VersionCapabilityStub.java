package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.test.stub;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.VersionCapability;
/**
 * Stub.
 */
public class VersionCapabilityStub implements VersionCapability {
    public String fdbIssueDate = "20090510";
    public String customIssueDate = "20090510";
    /**
     * Retrieve drug data vendor version information.
     * 
     * @return DrugDataVendorVersionVo with version information
     */
    public DrugDataVendorVersionVo retrieveDrugDataVendorVersion() {
        DrugDataVendorVersionVo vo = new DrugDataVendorVersionVo();
        vo.setBuildVersion("6");
        vo.setCustomBuildVersion("6");
        vo.setCustomDataVersion("3.2");
        vo.setCustomIssueDate(customIssueDate);
        vo.setDataVersion("3.2");
        vo.setIssueDate(fdbIssueDate);
        return vo;
    }
