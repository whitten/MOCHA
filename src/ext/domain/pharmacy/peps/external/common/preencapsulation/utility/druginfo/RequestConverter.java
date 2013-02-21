/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.druginfo;
import java.util.ArrayList;
import java.util.Collection;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.request.Drug;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.request.DrugInfoRequest;
/**
 * Convert DrugInfoRequest JAXB objects into value objects
 */
public class RequestConverter {
    /**
     * Convert DrugInfoRequest JAXB objects into value objects
     * 
     * @param request DrugInfoRequest to convert
     * @return Collection of DrugInfoVo
     */
    public static Collection<DrugInfoVo> toDrugInfoVo(DrugInfoRequest request) {
        Collection<DrugInfoVo> drugs = new ArrayList<DrugInfoVo>(request.getDrug().size());
        for (Drug drug : request.getDrug()) {
            DrugInfoVo drugInfo = new DrugInfoVo();
            drugInfo.setGcnSeqNo(drug.getGcnSeqNo().toString());
            drugs.add(drugInfo);
        }
        return drugs;
    }
    /**
     * Cannot instantiate
     */
    private RequestConverter() {
        super();
    }
