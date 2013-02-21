/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.impl;
import java.util.ArrayList;
import java.util.Collection;
import EXT.DOMAIN.pharmacy.peps.common.vo.DoseRouteVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DoseTypeVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.DrugInfoCapability;
import dbank.database.FDBDataManager;
import dbank.database.FDBException;
import dbank.dif.DispensableDrug;
import dbank.dif.DoseRoutes;
import dbank.dif.DoseTypes;
import dbank.dif.FDBDispensableDrugLoadType;
import dbank.dif.FDBDrugType;
import dbank.dif.ScreenDrug;
/**
 * Lookup the dose routes and types for the given drugs.
 */
public class DrugInfoCapabilityImpl implements DrugInfoCapability {
    private FDBDataManager fdbDataManager;
    /**
     * Lookup the dose routes and types for the given drugs.
     * 
     * @param drugs Collection of DrugInfoVo
     * @return DrugInfoResultsVo containing drugs not found and drugs with does routes and types
     */
    public DrugInfoResultsVo processDrugInfoRequest(Collection<DrugInfoVo> drugs) {
        DrugInfoResultsVo results = new DrugInfoResultsVo();
        for (DrugInfoVo drugInfoVo : drugs) {
            try {
                synchronized (dbank.dif.GlobalObjectManager.class) { // FDB locking contention
                    ScreenDrug screenDrug = newScreenDrugInstance();
                    screenDrug.load(drugInfoVo.getGcnSeqNo(), FDBDrugType.fdbDTGCNSeqNo);
                    drugInfoVo.setDoseRoutes(convertDoseRoutes(screenDrug.getDoseRoutes()));
                    drugInfoVo.setDoseTypes(convertDoseTypes(screenDrug.getDoseTypes()));
                    DispensableDrug dispensableDrug = newDispensableDrugInstance();
                    dispensableDrug.load(Long.valueOf(drugInfoVo.getGcnSeqNo()), FDBDispensableDrugLoadType.fdbDDLTGCNSeqNo,
                        "", "", "");
                    // Strength Units are lower case, but data from database in dose checks are upper case
                    String strengthUnit = dispensableDrug.getStrengthUnit();
                    if (strengthUnit != null) {
                        strengthUnit = strengthUnit.toUpperCase();
                    }
                    drugInfoVo.setStrengthUnit(strengthUnit);
                }
                results.getDrugs().add(drugInfoVo);
            }
            catch (FDBException e) {
                results.getDrugsNotFound().add(drugInfoVo);
            }
        }
        return results;
    }
    /**
     * Convert the FDB DoseTypes type to a collection of strings, representing the description of types.
     * 
     * @param doseTypes DoseTypes FDB object
     * @return Collection of DoseTypeVo each the description of a FDB DoseRoute
     */
    private Collection<DoseTypeVo> convertDoseTypes(DoseTypes doseTypes) {
        Collection<DoseTypeVo> types = new ArrayList<DoseTypeVo>(doseTypes.count());
        for (int i = 0; i < doseTypes.count(); i++) {
            DoseTypeVo doseType = new DoseTypeVo();
            doseType.setId(doseTypes.item(i).getID());
            doseType.setName(doseTypes.item(i).getDescription());
            types.add(doseType);
        }
        return types;
    }
    /**
     * Convert the FDB DoseRoutes type to a collection of strings, representing the description of routes.
     * 
     * @param doseRoutes DoseRoutes FDB object
     * @return Collection of DoseRouteVo
     */
    private Collection<DoseRouteVo> convertDoseRoutes(DoseRoutes doseRoutes) {
        Collection<DoseRouteVo> routes = new ArrayList<DoseRouteVo>(doseRoutes.count());
        for (int i = 0; i < doseRoutes.count(); i++) {
            DoseRouteVo doseRoute = new DoseRouteVo();
            doseRoute.setId(doseRoutes.item(i).getID());
            doseRoute.setName(doseRoutes.item(i).getDescription());
            routes.add(doseRoute);
        }
        return routes;
    }
    /**
     * Provide a new instance of ScreenDrug.
     * 
     * @return new instance of ScreenDrug
     */
    protected ScreenDrug newScreenDrugInstance() {
        return new ScreenDrug(fdbDataManager);
    }
    /**
     * Provide a new instance of DispensableDrug.
     * 
     * @return new instance of DispensableDrug
     */
    protected DispensableDrug newDispensableDrugInstance() {
        return new DispensableDrug(fdbDataManager);
    }
    /**
     * 
     * @param fdbDataManager fdbDataManager property
     */
    public void setFdbDataManager(FDBDataManager fdbDataManager) {
        this.fdbDataManager = fdbDataManager;
    }
