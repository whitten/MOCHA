/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.StringUtils;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckMessageVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDoseCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDrugCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugTherapyCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckHeaderVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.Body;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.ConsumerMonograph;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DoseStatus;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.Drug;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugDoseCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugDoseChecks;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugDrugCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugDrugChecks;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugNotChecked;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugTherapyCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugTherapyChecks;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugsNotChecked;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.Header;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.InteractedDrugList;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.MServer;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.MUser;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.Message;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.MessageSeverity;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.MessageTypeType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.NotCheckedStatus;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.ObjectFactory;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.PEPSResponse;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.PEPSVersion;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.ProfessionalMonograph;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.ReferencesType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.SourceType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.Time;
/**
 * Converts a VO into an XML response
 */
public class ResponseConverter {
    /**
     * Marshal the given OrderCheckResultsVo into an XML message.
     * 
     * @param orderCheckResults OrderCheckResultsVo to convert into XML
     * @return String XML message
     */
    public static PEPSResponse toPepsResponse(OrderCheckResultsVo orderCheckResults) {
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            PEPSResponse response = objectFactory.createPEPSResponse();
            response.setHeader(toHeader(orderCheckResults.getHeader(), orderCheckResults.getDrugDataVendorVersion(),
                objectFactory));
            response.setBody(toBody(orderCheckResults, objectFactory));
            return response;
        }
        catch (JAXBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Convert the OrderCheckResultsVo into the XML Body.
     * 
     * @param orderCheckResults OrderCheckresultsVo to convert
     * @param objectFactory ObjectFactory to use to instantiate XML objects
     * @return Body from OrderCheckResultsVo
     * @throws JAXBException if error instantiating XML objects
     */
    private static Body toBody(OrderCheckResultsVo orderCheckResults, ObjectFactory objectFactory) throws JAXBException {
        Body body = objectFactory.createBody();
        DrugCheck drugCheck = objectFactory.createDrugCheck();
        body.setDrugCheck(drugCheck);
        if (orderCheckResults.getDrugsNotChecked() != null && !orderCheckResults.getDrugsNotChecked().isEmpty()) {
            drugCheck.setDrugsNotChecked(toDrugsNotChecked(orderCheckResults.getDrugsNotChecked(), objectFactory));
        }
        if (orderCheckResults.getDrugDrugCheckResults() != null) {
            drugCheck.setDrugDrugChecks(toDrugDrugChecks(orderCheckResults.getDrugDrugCheckResults(), objectFactory));
        }
        if (orderCheckResults.getDrugTherapyCheckResults() != null) {
            drugCheck
                .setDrugTherapyChecks(toDrugTherapyChecks(orderCheckResults.getDrugTherapyCheckResults(), objectFactory));
        }
        if (orderCheckResults.getDrugDoseCheckResults() != null) {
            drugCheck.setDrugDoseChecks(toDrugDoseChecks(orderCheckResults.getDrugDoseCheckResults(), objectFactory));
        }
        return body;
    }
    /**
     * Convert the DrugCheckResultsVo into a DrugDoseChecks XML object.
     * 
     * @param drugDoseCheckResults DrugCheckResultsVo from the OrderCheckResultsVo
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return DrugDoseChecks from DrugCheckResultsVo
     * @throws JAXBException if cannot instantiate XML objects
     */
    private static DrugDoseChecks toDrugDoseChecks(DrugCheckResultsVo<DrugDoseCheckResultVo> drugDoseCheckResults,
                                                   ObjectFactory objectFactory) throws JAXBException {
        DrugDoseChecks drugChecks = objectFactory.createDrugDoseChecks();
        if (!drugDoseCheckResults.getChecks().isEmpty()) {
            for (DrugDoseCheckResultVo result : drugDoseCheckResults.getChecks()) {
                DrugDoseCheck drugDoseCheck = objectFactory.createDrugDoseCheck();
                if (result.isCustom()) {
                    drugDoseCheck.setSource(SourceType.CUSTOM);
                }
                else {
                    drugDoseCheck.setSource(SourceType.FDB);
                }
                drugDoseCheck.setDrug(toDrug(result.getDrug(), objectFactory));
                if (!drugDoseCheckResults.getMessages().isEmpty()) {
                    Iterator<DrugCheckMessageVo> iter2 = drugDoseCheckResults.getMessages().iterator();
                    // look for messages with same GCNSeqNo as doseCheckResult
                    while (iter2.hasNext()) {
                        DrugCheckMessageVo drugCheckMessage = iter2.next();
                        if (drugCheckMessage.getDrug().getGcnSeqNo().equals(result.getDrug().getGcnSeqNo())) {
                            drugDoseCheck.getMessage().add(toMessage(drugCheckMessage, objectFactory));
                            iter2.remove();
                        }
                    }
                }
                drugDoseCheck.setChemoInjectable(Boolean.valueOf(result.isChemoInjectable()));
                drugDoseCheck.setSingleDoseStatus(DoseStatus.fromValue(result.getSingleDoseStatus()));
                drugDoseCheck.setSingleDoseStatusCode(result.getSingleDoseStatusCode());
                drugDoseCheck.setSingleDoseMessage(result.getSingleDoseMessage());
                drugDoseCheck.setSingleDoseMax(result.getSingleDoseMax());
                drugDoseCheck.setRangeDoseStatus(DoseStatus.fromValue(result.getRangeDoseStatus()));
                drugDoseCheck.setRangeDoseStatusCode(result.getRangeDoseStatusCode());
                drugDoseCheck.setRangeDoseMessage(result.getRangeDoseMessage());
                drugDoseCheck.setRangeDoseLow(result.getRangeDoseLow());
                drugDoseCheck.setRangeDoseHigh(result.getRangeDoseHigh());
                drugDoseCheck.setDurationStatus(DoseStatus.fromValue(result.getDurationStatus()));
                drugDoseCheck.setDurationStatusCode(result.getDurationStatusCode());
                drugDoseCheck.setDurationMessage(result.getDurationMessage());
                drugDoseCheck.setFrequencyStatus(DoseStatus.fromValue(result.getFrequencyStatus()));
                drugDoseCheck.setFrequencyStatusCode(result.getFrequencyStatusCode());
                drugDoseCheck.setFrequencyMessage(result.getFrequencyMessage());
                drugDoseCheck.setDailyDoseStatus(DoseStatus.fromValue(result.getDailyDoseStatus()));
                drugDoseCheck.setDailyDoseStatusCode(result.getDailyDoseStatusCode());
                drugDoseCheck.setDailyDoseMessage(result.getDailyDoseMessage());
                drugDoseCheck.setMaxDailyDoseStatus(DoseStatus.fromValue(result.getMaxDailyDoseStatus()));
                drugDoseCheck.setMaxDailyDoseStatusCode(result.getMaxDailyDoseStatusCode());
                drugDoseCheck.setMaxDailyDoseMessage(result.getMaxDailyDoseMessage());
                drugDoseCheck.setMaxLifetimeDose(result.getMaxLifetimeDose());
                drugDoseCheck.setDoseHigh(result.getDoseHigh());
                drugDoseCheck.setDoseHighUnit(result.getDoseHighUnit());
                drugDoseCheck.setDoseLow(result.getDoseLow());
                drugDoseCheck.setDoseLowUnit(result.getDoseLowUnit());
                drugDoseCheck.setDoseFormHigh(result.getDoseFormHigh());
                drugDoseCheck.setDoseFormHighUnit(result.getDoseFormHighUnit());
                drugDoseCheck.setDoseFormLow(result.getDoseFormLow());
                drugDoseCheck.setDoseFormLowUnit(result.getDoseFormLowUnit());
                drugDoseCheck.setDoseRouteDescription(result.getDoseRouteDescription());
                drugChecks.getDrugDoseCheck().add(drugDoseCheck);
            }
        }
        if (!drugDoseCheckResults.getMessages().isEmpty()) {
            drugChecks.getMessage().addAll(toMessages(drugDoseCheckResults.getMessages(), objectFactory));
        }
        return drugChecks;
    }
    /**
     * Create a new {@link Drug} from a {@link DrugCheckVo}.
     * 
     * @param drugCheck {@link DrugCheckVo}
     * @param objectFactory {@link ObjectFactory} to create {@link Drug}
     * @return {@link Drug} with values set from given {@link DrugCheckVo}
     */
    private static Drug toDrug(DrugCheckVo drugCheck, ObjectFactory objectFactory) {
        Drug drug = objectFactory.createDrug();
        // required attributes
        drug.setGcnSeqNo(new BigInteger(drugCheck.getGcnSeqNo()));
        drug.setOrderNumber(drugCheck.getOrderNumber());
        // optional attributes
        if (StringUtils.isNotBlank(drugCheck.getIen())) {
            drug.setIen(new BigInteger(drugCheck.getIen()));
        }
        if (StringUtils.isNotBlank(drugCheck.getVuid())) {
            drug.setVuid(new BigInteger(drugCheck.getVuid()));
        }
        if (StringUtils.isNotBlank(drugCheck.getDrugName())) {
            drug.setDrugName(drugCheck.getDrugName());
        }
        return drug;
    }
    /**
     * Convert the DrugCheckResultsVo into a DrugTherapyChecks XML object.
     * 
     * @param drugTherapyCheckResults DrugCheckResultsVo from OrderCheckResultsVo
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return DrugTherapyChecks from DrugCheckResultsVo
     * @throws JAXBException if cannot instantiate XML objects
     */
    private static DrugTherapyChecks toDrugTherapyChecks(
                                                         DrugCheckResultsVo<DrugTherapyCheckResultVo> drugTherapyCheckResults,
                                                         ObjectFactory objectFactory) throws JAXBException {
        DrugTherapyChecks drugChecks = objectFactory.createDrugTherapyChecks();
        if (!drugTherapyCheckResults.getChecks().isEmpty()) {
            for (DrugTherapyCheckResultVo result : drugTherapyCheckResults.getChecks()) {
                DrugTherapyCheck drugTherapyCheck = objectFactory.createDrugTherapyCheck();
                drugTherapyCheck.setId(String.valueOf(result.getId()));
                if (result.isCustom()) {
                    drugTherapyCheck.setSource(SourceType.CUSTOM);
                }
                else {
                    drugTherapyCheck.setSource(SourceType.FDB);
                }
                drugTherapyCheck.setClassification(result.getDuplicateClass());
                drugTherapyCheck.setShortText(result.getShortText());
                drugTherapyCheck.setDuplicateAllowance(result.getAllowance());
                drugTherapyCheck.setInteractedDrugList(toInteractedDrugList(result.getDrugs(), objectFactory));
                drugChecks.getDrugTherapyCheck().add(drugTherapyCheck);
            }
        }
        if (!drugTherapyCheckResults.getMessages().isEmpty()) {
            drugChecks.getMessage().addAll(toMessages(drugTherapyCheckResults.getMessages(), objectFactory));
        }
        return drugChecks;
    }
    /**
     * Convert the DrugCheckResultsVo for the drug interaction check into the DrugDrugChecks XML object.
     * 
     * @param drugDrugCheckResults DrugCheckResultsVo from the OrderCheckResultsVo
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return DrugDrugChecks XML object from DrugCheckResultsVo
     * @throws JAXBException if cannot instantiate XML object
     */
    private static DrugDrugChecks toDrugDrugChecks(DrugCheckResultsVo<DrugDrugCheckResultVo> drugDrugCheckResults,
                                                   ObjectFactory objectFactory) throws JAXBException {
        DrugDrugChecks drugChecks = objectFactory.createDrugDrugChecks();
        if (!drugDrugCheckResults.getChecks().isEmpty()) {
            for (DrugDrugCheckResultVo result : drugDrugCheckResults.getChecks()) {
                DrugDrugCheck drugDrugCheck = objectFactory.createDrugDrugCheck();
                drugDrugCheck.setId(String.valueOf(result.getId()));
                if (result.isCustom()) {
                    drugDrugCheck.setSource(SourceType.CUSTOM);
                }
                else {
                    drugDrugCheck.setSource(SourceType.FDB);
                }
                
                drugDrugCheck.setSeverity(result.getSeverity());
                drugDrugCheck.setShortText(result.getShortText());
                drugDrugCheck.setInteraction(result.getInteractionDescription());
                // Copy the Professional Monograph, only if the data came in from FDB
                if (result.getProfessionalMonograph() != null) {
                    drugDrugCheck.setProfessionalMonograph(objectFactory.createProfessionalMonograph());
                    ProfessionalMonograph professionalMonograph = drugDrugCheck.getProfessionalMonograph();
                    if (result.getProfessionalMonograph().isFdbMonographSourceType()) {
                        professionalMonograph.setMonographSource(SourceType.FDB);
                    }
                    else {
                        professionalMonograph.setMonographSource(SourceType.CUSTOM);
                    }
                    professionalMonograph.setDisclaimer(result.getProfessionalMonograph().getDisclaimer());
                    professionalMonograph.setMonographTitle(result.getProfessionalMonograph().getMonographTitle());
                    professionalMonograph.setSeverityLevel(result.getProfessionalMonograph().getSeverityLevel());
                    professionalMonograph.setMechanismOfAction(result.getProfessionalMonograph().getMechanismOfAction());
                    professionalMonograph.setClinicalEffects(result.getProfessionalMonograph().getClinicalEffects());
                    professionalMonograph.setPredisposingFactors(result.getProfessionalMonograph()
                        .getPredisposingFactors());
                    professionalMonograph.setPatientManagement(result.getProfessionalMonograph().getPatientManagement());
                    professionalMonograph.setDiscussion(result.getProfessionalMonograph().getDiscussion());
                    ReferencesType referencesType = objectFactory.createReferencesType();
                    referencesType.getReference().addAll(result.getProfessionalMonograph().getReferences());
                    professionalMonograph.setReferences(referencesType);
                }
                // Copy the Consumer Monograph, only if the data came in from FDB
                if (result.getConsumerMonograph() != null) {
                    drugDrugCheck.setConsumerMonograph(objectFactory.createConsumerMonograph());
                    ConsumerMonograph consumerMonograph = drugDrugCheck.getConsumerMonograph();
                    if (result.getConsumerMonograph().isFdbMonographSourceType()) {
                        consumerMonograph.setMonographSource(SourceType.FDB);
                    }
                    else {
                        consumerMonograph.setMonographSource(SourceType.CUSTOM);
                    }
                    consumerMonograph.setDisclaimer(result.getConsumerMonograph().getDisclaimer());
                    consumerMonograph.setHowOccurs(result.getConsumerMonograph().getHowOccurs());
                    consumerMonograph.setMedicalWarning(result.getConsumerMonograph().getMedicalWarning());
                    consumerMonograph.setMonographTitle(result.getConsumerMonograph().getMonographTitle());
                    ReferencesType referencesType = objectFactory.createReferencesType();
                    referencesType.getReference().addAll(result.getConsumerMonograph().getReferences());
                    consumerMonograph.setReferences(referencesType);
                    consumerMonograph.setWhatMightHappen(result.getConsumerMonograph().getWhatMightHappen());
                    consumerMonograph.setWhatToDo(result.getConsumerMonograph().getWhatToDo());
                }
                drugDrugCheck.setInteractedDrugList(toInteractedDrugList(result.getDrugs(), objectFactory));
                drugChecks.getDrugDrugCheck().add(drugDrugCheck);
            }
        }
        if (!drugDrugCheckResults.getMessages().isEmpty()) {
            drugChecks.getMessage().addAll(toMessages(drugDrugCheckResults.getMessages(), objectFactory));
        }
        return drugChecks;
    }
    /**
     * Convert a Collection of DrugCheckVo into an InteractedDrugList XML object.
     * 
     * @param drugs Collection of DrugCheckVo
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return InteractedDrugList
     * @throws JAXBException if cannot instantiate XML objects
     */
    private static InteractedDrugList toInteractedDrugList(Collection<DrugCheckVo> drugs, ObjectFactory objectFactory)
        throws JAXBException {
        InteractedDrugList interactedDrugList = objectFactory.createInteractedDrugList();
        for (DrugCheckVo drugCheck : drugs) {
            Drug drug = toDrug(drugCheck, objectFactory);
            interactedDrugList.getDrug().add(drug);
        }
        return interactedDrugList;
    }
    /**
     * Convert a Collection of DrugCheckMessageVo into a Collection of MessageType XML objects.
     * 
     * @param drugMessages Collection of DrugCheckMessageVo
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return Collection of MessageType XML objects
     * @throws JAXBException if cannot instantiate XML objects
     */
    private static Collection<Message> toMessages(Collection<DrugCheckMessageVo> drugMessages, ObjectFactory objectFactory)
        throws JAXBException {
        Collection<Message> messages = new ArrayList<Message>(drugMessages.size());
        for (DrugCheckMessageVo drugCheckMessage : drugMessages) {
            messages.add(toMessage(drugCheckMessage, objectFactory));
        }
        return messages;
    }
    /**
     * Create a {@link Message} from the data in a {@link DrugCheckMessageVo}.
     * 
     * @param drugCheckMessage {@link DrugCheckMessageVo} to convert
     * @param objectFactory {@link ObjectFactory} to use to create {@link Message}
     * @return {@link Message} with data from given {@link DrugCheckMessageVo}
     */
    private static Message toMessage(DrugCheckMessageVo drugCheckMessage, ObjectFactory objectFactory) {
        Message message = objectFactory.createMessage();
        message.setDrug(toDrug(drugCheckMessage.getDrug(), objectFactory));
        message.setSeverity(MessageSeverity.fromValue(drugCheckMessage.getSeverity()));
        message.setType(MessageTypeType.fromValue(drugCheckMessage.getType()));
        message.setDrugName(drugCheckMessage.getDrugName());
        message.setText(drugCheckMessage.getText());
        return message;
    }
    /**
     * Convert a Collection of drugs not checked from the OrderCheckResultsVo into the DrugsNotChecked XML object.
     * 
     * @param drugsNotChecked Collection of drugs not checked from OrderCheckResultsVo
     * @param objectFactory ObjectFactory to create XML objects with
     * @return DrugsNotChecked
     * @throws JAXBException if cannot instantiate XML objects
     */
    private static DrugsNotChecked toDrugsNotChecked(Collection<DrugCheckVo> drugsNotChecked, ObjectFactory objectFactory)
        throws JAXBException {
        DrugsNotChecked drugs = objectFactory.createDrugsNotChecked();
        for (DrugCheckVo drugCheck : drugsNotChecked) {
            DrugNotChecked drugNotChecked = objectFactory.createDrugNotChecked();
            drugNotChecked.setStatus(NotCheckedStatus.UNABLE_TO_LOAD_DRUG_FOR_GCN_SEQ_NO);
            drugNotChecked.setDrug(toDrug(drugCheck, objectFactory));
            drugs.getDrugNotChecked().add(drugNotChecked);
        }
        return drugs;
    }
    /**
     * Convert the OrderCheckHeaderVo into the PEPSResponse Header.
     * 
     * @param headerVo OrderCheckHeaderVo
     * @param drugDataVendorVersion DrugDataVendorVersionVo
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return PEPSResponse Header
     * @throws JAXBException if error instantiating objects for Header
     */
    private static Header toHeader(OrderCheckHeaderVo headerVo, DrugDataVendorVersionVo drugDataVendorVersion,
                                   ObjectFactory objectFactory) throws JAXBException {
        Header header = objectFactory.createHeader();
        MServer server = objectFactory.createMServer();
        server.setIp(headerVo.getIp());
        server.setNamespace(headerVo.getNamespace());
        server.setServerName(headerVo.getServerName());
        server.setStationNumber(headerVo.getStationNumber());
        server.setUci(headerVo.getUci());
        header.setMServer(server);
        MUser user = objectFactory.createMUser();
        user.setDuz(headerVo.getDuz());
        user.setJobNumber(headerVo.getJobNumber());
        user.setUserName(headerVo.getUserName());
        header.setMUser(user);
        Time time = objectFactory.createTime();
        time.setValue(headerVo.getTime());
        header.setTime(time);
        PEPSVersion version = objectFactory.createPEPSVersion();
        version.setDifBuildVersion(drugDataVendorVersion.getBuildVersion());
        version.setDifDbVersion(drugDataVendorVersion.getDataVersion());
        version.setDifIssueDate(drugDataVendorVersion.getIssueDate());
        version.setCustomBuildVersion(drugDataVendorVersion.getCustomBuildVersion());
        version.setCustomDbVersion(drugDataVendorVersion.getCustomDataVersion());
        version.setCustomIssueDate(drugDataVendorVersion.getCustomIssueDate());
        header.setPEPSVersion(version);
        return header;
    }
    /**
     * Convert the given OrderCheckHeaderVo and DrugDataVendorVersionVo into a ping response.
     * 
     * @param header OrderCheckHeaderVo to use for header data
     * @param drugDataVendorVersion DrugDataVendorVersionVo to use for DDV version data
     * @return ping PEPSResposne
     */
    public static PEPSResponse createPingResponse(OrderCheckHeaderVo header, DrugDataVendorVersionVo drugDataVendorVersion) {
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            PEPSResponse response = objectFactory.createPEPSResponse();
            response.setHeader(toHeader(header, drugDataVendorVersion, objectFactory));
            return response;
        }
        catch (JAXBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * The private constructor to prevent instantiation of the class.
     */
    private ResponseConverter() {
    }
