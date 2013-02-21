/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceValidationException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckHeaderVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckVo;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Checks;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DoseInformation;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Drug;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DrugDoseCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Header;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.PEPSRequest;
/**
 * Converts an XML request into a VO
 */
public class RequestConverter {
    /**
     * Convert the given PEPSRequest into an OrderCheckVo.
     * 
     * @param request PEPSRequest to convert
     * @return OrderCheckVo created from XML String
     */
    public static OrderCheckVo toOrderCheckVo(PEPSRequest request) {
        OrderCheckVo orderCheck = new OrderCheckVo();
        orderCheck.setHeader(toHeader(request.getHeader()));
        // the Body element does not need to be set if this is a ping request
        if (request.isSetBody()) {
            Checks checks = request.getBody().getDrugCheck().getChecks();
            if (checks.isSetUseCustomTables()) {
                orderCheck.setCustomTables(checks.isUseCustomTables());
            }
            else { // default use of custom tables to true
                orderCheck.setCustomTables(true);
            }
            if (checks.isSetProspectiveOnly()) {
                orderCheck.setProspectiveOnly(checks.isProspectiveOnly());
            }
            else { // prospective only defaults to false
                orderCheck.setProspectiveOnly(false);
            }
            orderCheck.setDrugDrugCheck(checks.isSetDrugDrugCheck());
            orderCheck.setDrugTherapyCheck(checks.isSetDrugTherapyCheck());
            if (checks.isSetDrugTherapyCheck() && checks.getDrugTherapyCheck().isSetUseDuplicateAllowance()) {
                orderCheck.setDuplicateAllowance(checks.getDrugTherapyCheck().isUseDuplicateAllowance());
            }
            else { // default use of duplicate allowance to true
                orderCheck.setDuplicateAllowance(true);
            }
            orderCheck.setDrugDoseCheck(checks.isSetDrugDoseCheck());
            orderCheck.setDrugsToScreen(new ArrayList<DrugCheckVo>(0));
            Collection<DrugCheckVo> prospectives;
            if (request.getBody().getDrugCheck().isSetProspectiveDrugs()) {
                prospectives = toDrugsToScreen(request.getBody().getDrugCheck().getProspectiveDrugs().getDrug(), true);
            }
            else {
                prospectives = Collections.emptyList();
            }
            orderCheck.getDrugsToScreen().addAll(prospectives);
            // if the check is for prospective drugs only, there must be at least one prospective drug
            if (orderCheck.isProspectiveOnly() && prospectives.isEmpty()) {
                throw new InterfaceValidationException(InterfaceValidationException.PROSPECTIVE_DRUGS_REQUIRED);
            }
            if (request.getBody().getDrugCheck().isSetMedicationProfile()) {
                orderCheck.getDrugsToScreen().addAll(
                    toDrugsToScreen(request.getBody().getDrugCheck().getMedicationProfile().getDrug(), false));
            }
            if (orderCheck.isDrugDoseCheck()) {
                addDemographicsData(checks.getDrugDoseCheck(), orderCheck);
            }
        }
        return orderCheck;
    }
    /**
     * All dose checks are completed on prospective drugs. Add the demographics information to the ORderCheckVo for the drugs
     * in the DrugDoseCheck type.
     * 
     * @param drugDoseCheck DrugDoseCheckType XML object with dosing information
     * @param orderCheck OrderCheckVo to add demographics
     */
    private static void addDemographicsData(DrugDoseCheck drugDoseCheck, OrderCheckVo orderCheck) {
        orderCheck.setAgeInDays(drugDoseCheck.getDemographics().getAgeInDays());
        orderCheck.setWeightInKg(drugDoseCheck.getDemographics().getWeightInKG());
        orderCheck.setBodySurfaceAreaInSqM(drugDoseCheck.getDemographics().getBodySurfaceAreaInSqM());
    }
    /**
     * Convert a List of DrugType XML objects into a Collection of DrugCheckVo
     * 
     * @param xmlDrugs List of DrugType XML objects
     * @param prospective true if the drugs in this list are prospective
     * @return Collection of DrugCheckVo
     */
    private static Collection<DrugCheckVo> toDrugsToScreen(List<Drug> xmlDrugs, boolean prospective) {
        Collection<DrugCheckVo> drugsToScreen = new ArrayList<DrugCheckVo>(xmlDrugs.size());
        for (Drug drugType : xmlDrugs) {
            drugsToScreen.add(toDrugCheckVo(drugType, prospective));
        }
        return drugsToScreen;
    }
    /**
     * Convert the DrugType into a DrugCheckVo
     * 
     * @param drugType Drug XML object
     * @param prospective boolean true if this drug is prospective
     * @return DrugCheckVo
     */
    private static DrugCheckVo toDrugCheckVo(Drug drugType, boolean prospective) {
        DrugCheckVo drug = new DrugCheckVo();
        drug.setProspective(prospective);
        drug.setGcnSeqNo(String.valueOf(drugType.getGcnSeqNo()));
        if (drugType.isSetIen()) {
            drug.setIen(String.valueOf(drugType.getIen()));
        }
        if (drugType.isSetOrderNumber()) {
            drug.setOrderNumber(drugType.getOrderNumber());
        }
        if (drugType.isSetVuid()) {
            drug.setVuid(String.valueOf(drugType.getVuid()));
        }
        if (drugType.isSetDrugName()) {
            drug.setDrugName(drugType.getDrugName());
        }
        if (drugType.isSetDoseInformation()) {
            drug.setDoseCheck(true);
            DoseInformation doseInformation = drugType.getDoseInformation();
            drug.setDoseRate(doseInformation.getDoseRate().value());
            drug.setDoseRoute(doseInformation.getRoute().value());
            drug.setDoseType(doseInformation.getDoseType().value());
            drug.setDoseUnit(doseInformation.getDoseUnit().value());
            drug.setDuration(doseInformation.getDuration().longValue());
            drug.setDurationRate(doseInformation.getDurationRate().value());
            drug.setFrequency(doseInformation.getFrequency());
            drug.setSingleDoseAmount(doseInformation.getDoseAmount());
        }
        return drug;
    }
    /**
     * Convert the XML HeaderType into an OrderCheckHeaderVo
     * 
     * @param header XML Header
     * @return OrderCheckHeaderVo
     */
    private static OrderCheckHeaderVo toHeader(Header header) {
        OrderCheckHeaderVo orderCheckHeaderVo = new OrderCheckHeaderVo();
        orderCheckHeaderVo.setDuz(header.getMUser().getDuz());
        orderCheckHeaderVo.setIp(header.getMServer().getIp());
        orderCheckHeaderVo.setJobNumber(header.getMUser().getJobNumber());
        orderCheckHeaderVo.setNamespace(header.getMServer().getNamespace());
        orderCheckHeaderVo.setServerName(header.getMServer().getServerName());
        orderCheckHeaderVo.setStationNumber(header.getMServer().getStationNumber());
        orderCheckHeaderVo.setTime(header.getTime().getValue());
        orderCheckHeaderVo.setUci(header.getMServer().getUci());
        orderCheckHeaderVo.setUserName(header.getMUser().getUserName());
        if (header.isSetPingOnly()) {
            orderCheckHeaderVo.setPingOnly(header.isPingOnly());
        }
        else {
            orderCheckHeaderVo.setPingOnly(false);
        }
        return orderCheckHeaderVo;
    }
    /**
     * Cannot instantiate class
     */
    private RequestConverter() {
        super();
    }
