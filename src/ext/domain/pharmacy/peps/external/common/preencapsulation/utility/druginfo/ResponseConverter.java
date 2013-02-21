/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.druginfo;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import EXT.DOMAIN.pharmacy.peps.common.vo.DoseRouteVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DoseTypeVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.DoseType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.DoseTypes;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.Drug;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.DrugInfoResponse;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.DrugNotFound;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.DrugsNotFound;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.NotFoundStatus;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.ObjectFactory;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.Route;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.Routes;
/**
 * Convert a Collection of DrugInfoVo into a DrugInfoResponse
 */
public class ResponseConverter {
    /**
     * Convert a Collection of DrugInfoVo into a DrugInfoResponse
     * 
     * @param drugInfoResultsVo result of looking up dose routes and types
     * @return DrugInfoResponse
     */
    public static DrugInfoResponse toDrugInfoResponse(DrugInfoResultsVo drugInfoResultsVo) {
        ObjectFactory objectFactory = new ObjectFactory();
        DrugInfoResponse response = objectFactory.createDrugInfoResponse();
        response.getDrug().addAll(convertDrugs(drugInfoResultsVo.getDrugs(), objectFactory));
        response.setDrugsNotFound(convertDrugsNotFound(drugInfoResultsVo.getDrugsNotFound(), objectFactory));
        return response;
    }
    /**
     * Convert the drugs found in FDB to JAXB Drug objects.
     * 
     * @param drugs Collection of DrugInfoVo
     * @param objectFactory JAXB ObjectFactory to create new instances of Drug
     * @return Collection of JAXB Drug XML objects
     */
    private static Collection<Drug> convertDrugs(Collection<DrugInfoVo> drugs, ObjectFactory objectFactory) {
        Collection<Drug> results = new ArrayList<Drug>(drugs.size());
        for (DrugInfoVo drugInfo : drugs) {
            Drug drug = objectFactory.createDrug();
            drug.setGcnSeqNo(new BigInteger(drugInfo.getGcnSeqNo()));
            Routes routes = objectFactory.createRoutes();
            drug.setRoutes(routes);
            for (DoseRouteVo doseRoute : drugInfo.getDoseRoutes()) {
                Route route = objectFactory.createRoute();
                route.setId(doseRoute.getId());
                route.setName(doseRoute.getName());
                drug.getRoutes().getRoute().add(route);
            }
            DoseTypes doseTypes = objectFactory.createDoseTypes();
            drug.setDoseTypes(doseTypes);
            for (DoseTypeVo doseType : drugInfo.getDoseTypes()) {
                DoseType type = objectFactory.createDoseType();
                type.setId(doseType.getId());
                type.setName(doseType.getName());
                drug.getDoseTypes().getDoseType().add(type);
            }
            drug.setStrengthUnit(drugInfo.getStrengthUnit());
            
            results.add(drug);
        }
        return results;
    }
    /**
     * Convert a Collection of DrugInfoVo representing drugs that could not be loaded by FDB into a DrugsNotFound JAXB
     * object.
     * 
     * @param drugsNotFound Collection of DrugInfoVo
     * @param objectFactory ObjectFactory used to create JAXB object instances
     * @return DrugsNotFound
     */
    private static DrugsNotFound convertDrugsNotFound(Collection<DrugInfoVo> drugsNotFound, ObjectFactory objectFactory) {
        DrugsNotFound results = null;
        if (!drugsNotFound.isEmpty()) {
            results = objectFactory.createDrugsNotFound();
            for (DrugInfoVo drugInfoVo : drugsNotFound) {
                DrugNotFound drugNotFound = objectFactory.createDrugNotFound();
                Drug drug = objectFactory.createDrug();
                drug.setGcnSeqNo(new BigInteger(drugInfoVo.getGcnSeqNo()));
                drugNotFound.setDrug(drug);
                drugNotFound.setStatus(NotFoundStatus.UNABLE_TO_LOAD_DRUG_FOR_GCN_SEQ_NO);
                results.getDrugNotFound().add(drugNotFound);
            }
        }
        return results;
    }
    /**
     * Cannot instantiate
     */
    private ResponseConverter() {
        super();
    }
