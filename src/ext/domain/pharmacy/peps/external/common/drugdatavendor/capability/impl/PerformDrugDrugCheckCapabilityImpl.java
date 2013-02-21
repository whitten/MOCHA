/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.impl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.math.NumberUtils;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.common.utility.PropertyUtility;
import EXT.DOMAIN.pharmacy.peps.common.vo.ConsumerMonographVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckMessageVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugCheckVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDrugCheckResultVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.ProfessionalMonographVo;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.capability.PerformDrugDrugCheckCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility.MessageConversionUtility;
import EXT.DOMAIN.pharmacy.peps.external.common.drugdatavendor.utility.StringUtility;
import dbank.database.FDBDataManager;
import dbank.database.FDBException;
import dbank.database.FDBSQLException;
import dbank.dif.DDIMCustomStrings;
import dbank.dif.DDIMScreenResult;
import dbank.dif.DDIMScreenResults;
import dbank.dif.FDBCode;
import dbank.dif.FDBDDIMSeverityFilter;
import dbank.dif.FDBMonographSource;
import dbank.dif.FDBMonographSourceType;
import dbank.dif.FDBMonographType;
import dbank.dif.FDBUnknownIDException;
import dbank.dif.Monograph;
import dbank.dif.MonographLine;
import dbank.dif.MonographSection;
import dbank.dif.MonographSections;
import dbank.dif.ScreenDrugs;
import dbank.dif.Screening;
/**
 * Perform drug-drug interaction check
 */
public class PerformDrugDrugCheckCapabilityImpl implements PerformDrugDrugCheckCapability {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(PerformDrugDrugCheckCapabilityImpl.class);
    private static final String CONTINUATION_FORMAT_CODE = "0";
    private static final int SEVERITY_CODE_TYPE = 14;
    private static final String FDB_ID_CATEGORY = PropertyUtility.getProperty(PerformDrugDrugCheckCapabilityImpl.class,
        "fdb.id.category");
    private Screening screening;
    private FDBDataManager fdbDataManager;
    /**
     * Empty constructor
     */
    public PerformDrugDrugCheckCapabilityImpl() {
        super();
    }
    /**
     * Perform drug-drug interaction check
     * 
     * @param screenDrugs FDB ScreenDrugs used as input to dose check call
     * @param drugMap Map of the combined String of IEN and order number to DrugCheckVo. The combined String of IEN and order
     *            number is stored in the FDB ScreenDrug's custom ID attribute.
     * @param prospectiveOnly boolean true if check should only be done on prospective drugs
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugDrugCheckResultsVo contains response from FDB
     */
    public DrugCheckResultsVo<DrugDrugCheckResultVo> performDrugDrugCheck(ScreenDrugs screenDrugs,
                                                                          Map<String, DrugCheckVo> drugMap,
                                                                          boolean prospectiveOnly, boolean customTables) {
        try {
            DDIMScreenResults fdbResults = screening.DDIMScreen(screenDrugs, prospectiveOnly,
                FDBDDIMSeverityFilter.fdbDDIMSFUnknownModerate, customTables, customTables, customTables, FDB_ID_CATEGORY,
                false, "");
            return convertResults(fdbResults, screenDrugs, drugMap, customTables);
        }
        catch (FDBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.DRUG_DATA_VENDOR);
        }
        catch (SQLException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.DRUG_DATA_VENDOR);
        }
    }
    /**
     * Convert from the results received into Collection of value objects
     * 
     * @param screenResults results from FDB
     * @param screenDrugs FDB ScreenDrugs used as input to dose check call
     * @param drugMap Map of the combined String of IEN and order number to DrugCheckVo. The combined String of IEN and order
     *            number is stored in the FDB ScreenDrug's custom ID attribute.
     * @param customTables boolean true if customized tables to be used in the check
     * @return DrugCheckResultsVO results converted from Drug Data Vendor return containing DrugDrugCheckResultVO for checks
     * @throws FDBException if error within FDB API
     * @throws SQLException if error within FDB API
     */
    private DrugCheckResultsVo<DrugDrugCheckResultVo> convertResults(DDIMScreenResults screenResults,
                                                                     ScreenDrugs screenDrugs,
                                                                     Map<String, DrugCheckVo> drugMap, boolean customTables)
        throws FDBException, SQLException {
        DrugCheckResultsVo<DrugDrugCheckResultVo> vo = new DrugCheckResultsVo<DrugDrugCheckResultVo>();
        Collection<DrugCheckMessageVo> messages = MessageConversionUtility.toDrugCheckMessages(screenResults.getMessages(),
            drugMap, screenDrugs);
        vo.setMessages(messages);
        Collection<DDIMScreenResult> filtered;
        if (customTables) {
            filtered = filterResults(screenResults);
        }
        else {
            filtered = new ArrayList<DDIMScreenResult>(screenResults.count());
            for (int i = 0; i < screenResults.count(); i++) {
                filtered.add(screenResults.item(i));
            }
        }
        for (DDIMScreenResult result : filtered) {
            if (result.getUserInteraction()) {
                LOG.debug("Converting Custom DDIMScreenResult with ID " + result.getInteractionID());
            }
            else {
                LOG.debug("Converting FDB DDIMScreenResult with ID " + result.getInteractionID());
            }
            DrugDrugCheckResultVo check = new DrugDrugCheckResultVo();
            check.setId(result.getInteractionID());
            check.setCustom(result.getUserInteraction());
            check.setDrugs(new ArrayList<DrugCheckVo>(2));
            DrugCheckVo drug1 = drugMap.get(screenDrugs.get(result.getDrug1Index()).getCustomID());
            check.getDrugs().add(drug1);
            DrugCheckVo drug2 = drugMap.get(screenDrugs.get(result.getDrug2Index()).getCustomID());
            check.getDrugs().add(drug2);
            check.setInteractionDescription(StringUtility.nullToEmptyString(result.getInteractionDescription()));
            check.setShortText(StringUtility.nullToEmptyString(result.getScreenMessage()));
            FDBCode severity = newFdbCodeInstance();
            severity.load(SEVERITY_CODE_TYPE, result.getSeverityLevelCode());
            check.setSeverity(StringUtility.nullToEmptyString(severity.getDescription()));
            try {
                check.setProfessionalMonograph(loadProfessionalMonograph(result.getMonographID(), customTables));
            }
            catch (FDBUnknownIDException e) {
                LOG.warn("DDIM Professional Monograph not found with ID " + result.getMonographID());
            }
            try {
                check.setConsumerMonograph(loadConsumerMonograph(result.getMonographID(), customTables));
            }
            catch (FDBUnknownIDException e) {
                LOG.warn("DDIM Consumer Monograph not found with ID " + result.getMonographID());
            }
            vo.getChecks().add(check);
        }
        return vo;
    }
    /**
     * Filter out the FDB interactions replaced by custom interactions.
     * <p>
     * First accept all interactions. Then for each FDB interaction, find if there are mapped custom interactions. If there
     * are, find the FDB interactions with the same two drugs as the custom interaction and filter it out.
     * 
     * @param screenResults DDIMScreenResults from FDB with results to filter
     * @return filtered results
     */
    private Collection<DDIMScreenResult> filterResults(DDIMScreenResults screenResults) {
        Map<ScreenResultKey, DDIMScreenResult> filtered = new HashMap<ScreenResultKey, DDIMScreenResult>(screenResults
            .count());
        for (int i = 0; i < screenResults.count(); i++) {
            DDIMScreenResult screenResult = screenResults.item(i);
            ScreenResultKey key = new ScreenResultKey(screenResult);
            if (screenResult.getUserInteraction()) {
                DDIMScreenResult fdb = filtered.put(key, screenResult);
                if (fdb != null) {
                    LOG.debug("Filtered out FDB interaction with ID " + fdb.getInteractionID());
                }
            }
            else {
                if (!filtered.containsKey(key)) {
                    filtered.put(key, screenResult);
                }
                else {
                    LOG.debug("Filtered out FDB interaction with ID " + screenResult.getInteractionID());
                }
            }
        }
        return filtered.values();
    }
    /**
     * Find the custom interaction IDs which map to the given FDB interaction.
     * <p>
     * Note that the FDB DIF API loads custom strings only for FDB interactions so the mapping is actually from the FDB
     * interaction to the custom interaction, not the other way around!
     * 
     * @param screenResult FDB DDIMScreenResult
     * @return Set of Long ID of mapped custom interactions
     */
    private Set<Long> getMappedIds(DDIMScreenResult screenResult) {
        Set<Long> interactionIds = new HashSet<Long>();
        DDIMCustomStrings customStrings = screenResult.getCustomStrings();
        for (int i = 0; i < customStrings.count(); i++) {
            String customInteractionId = customStrings.item(i).getCustomString();
            if (NumberUtils.isNumber(customInteractionId)) {
                interactionIds.add(Long.valueOf(customInteractionId));
            }
            else {
                LOG.error("FDB ID Custom String '" + customInteractionId + "' is not a number."
                    + "The mapping will not be used!");
            }
        }
        return interactionIds;
    }
    /**
     * Load the professional monograph
     * 
     * @param monographId ID for the monograph to load
     * @param customTables boolean true if customized tables to be used in the check
     * @return ProfessionalMonographVo
     * @throws FDBUnknownIDException if FDB API cannot find the given monograph ID
     * @throws FDBSQLException if the FDB API cannot load the monograph
     */
    private ProfessionalMonographVo loadProfessionalMonograph(long monographId, boolean customTables)
        throws FDBSQLException, FDBUnknownIDException {
        FDBMonographSource source = FDBMonographSource.fdbMSFDBOnly;
        if (customTables) {
            source = FDBMonographSource.fdbMSCustomWithFDBDefault;
        }
        Monograph monograph = newMonographInstance();
        monograph.load(FDBMonographType.fdbMTDDIM, ProfessionalMonographVo.MONOGRAPH_TYPE, monographId, source);
        MonographSections sections = monograph.getSections();
        ProfessionalMonographVo professional = new ProfessionalMonographVo();
        if (FDBMonographSourceType.fdbMSTFDB.equals(monograph.getMonographSourceType())) {
            professional.setFdbMonographSourceType(true);
        }
        else {
            professional.setFdbMonographSourceType(false);
        }
        professional.setDisclaimer(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.DISCLAIMER_SECTION)));
        professional.setMonographTitle(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.TITLE_SECTION)));
        professional.setSeverityLevel(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.SEVERITY_SECTION)));
        professional.setMechanismOfAction(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.MECHANISM_SECTION)));
        professional.setClinicalEffects(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.CLINICAL_SECTION)));
        professional.setPredisposingFactors(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.PREDISPOSING_SECTION)));
        professional.setPatientManagement(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.PATIENT_SECTION)));
        professional.setDiscussion(getMonographSectionText(sections
            .getItemBySectionID(ProfessionalMonographVo.DISCUSSION_SECTION)));
        professional.setReferences(getReferences(sections.getItemBySectionID(ProfessionalMonographVo.REFERENCES_SECTION)));
        return professional;
    }
    /**
     * Load the consumer monograph
     * 
     * @param monographId ID for the monograph to load
     * @param customTables boolean true if customized tables to be used in the check
     * @return ConsumerMonographVo
     * @throws FDBUnknownIDException if FDB API cannot find the given monograph ID
     * @throws FDBSQLException if the FDB API cannot load the monograph
     */
    private ConsumerMonographVo loadConsumerMonograph(long monographId, boolean customTables) throws FDBSQLException,
        FDBUnknownIDException {
        Monograph monograph = newMonographInstance();
        boolean found = false;
        // The FDB DIF API has a bug where if you use FDBMonographSource.fdbMSCustomWithFDBDefault and no custom
        // monograph exists, it will always return the professional FDB monograph, even if a consumer was requested.
        // The workaround is to try and load the custom monograph and manually default to the FDB monograph.
        if (customTables) {
            try {
                monograph.load(FDBMonographType.fdbMTDDIM, ConsumerMonographVo.MONOGRAPH_TYPE, monographId,
                    FDBMonographSource.fdbMSCustomOnly);
                found = true;
            }
            catch (FDBUnknownIDException e) {
                LOG.warn("DDIM Custom Consumer Monograph not found with ID " + monographId);
            }
        }
        if (!found) {
            monograph.load(FDBMonographType.fdbMTDDIM, ConsumerMonographVo.MONOGRAPH_TYPE, monographId,
                FDBMonographSource.fdbMSFDBOnly);
            found = true;
        }
        MonographSections sections = monograph.getSections();
        ConsumerMonographVo consumer = new ConsumerMonographVo();
        if (FDBMonographSourceType.fdbMSTFDB.equals(monograph.getMonographSourceType())) {
            consumer.setFdbMonographSourceType(true);
        }
        else {
            consumer.setFdbMonographSourceType(false);
        }
        consumer.setDisclaimer(getMonographSectionText(sections.getItemBySectionID(ConsumerMonographVo.DISCLAIMER_SECTION)));
        consumer.setHowOccurs(getMonographSectionText(sections.getItemBySectionID(ConsumerMonographVo.HOW_OCCURS_SECTION)));
        consumer.setMedicalWarning(getMonographSectionText(sections
            .getItemBySectionID(ConsumerMonographVo.MEDICAL_WARNING_SECTION)));
        consumer.setMonographTitle(getMonographSectionText(sections.getItemBySectionID(ConsumerMonographVo.TITLE_SECTION)));
        consumer.setReferences(getReferences(sections.getItemBySectionID(ConsumerMonographVo.REFERENCE_SECTION)));
        consumer.setWhatMightHappen(getMonographSectionText(sections
            .getItemBySectionID(ConsumerMonographVo.WHAT_MIGHT_HAPPEN_SECTION)));
        consumer.setWhatToDo(getMonographSectionText(sections.getItemBySectionID(ConsumerMonographVo.WHAT_TO_DO_SECTION)));
        return consumer;
    }
    /**
     * Provide a new instance of FDBCode.
     * 
     * @return new instance of FDBCode
     */
    protected FDBCode newFdbCodeInstance() {
        return new FDBCode(fdbDataManager);
    }
    /**
     * Provide a new instance of Monograph.
     * 
     * @return new instance of Monograph
     */
    protected Monograph newMonographInstance() {
        return new Monograph(fdbDataManager);
    }
    /**
     * Returns the text for a monograph section
     * 
     * @param section monograph section
     * @return String section text
     */
    private String getMonographSectionText(MonographSection section) {
        StringBuffer text = new StringBuffer();
        if (section != null && section.getLines() != null) {
            for (int i = 0; i < section.getLines().count(); i++) {
                MonographLine line = section.getLines().item(i);
                if (line != null) {
                    text.append(line.getLineText());
                }
            }
        }
        return text.toString();
    }
    /**
     * Convert the given references MonographSection into a List of String, with each reference as a single String.
     * 
     * @param section references monograph section
     * @return List of references as Strings
     */
    private List<String> getReferences(MonographSection section) {
        List<String> references = new ArrayList<String>();
        if (section != null && section.getLines() != null) {
            StringBuffer text = null;
            for (int i = 0; i < section.getLines().count(); i++) {
                MonographLine line = section.getLines().item(i);
                if (line != null) {
                    if (!CONTINUATION_FORMAT_CODE.equals(line.getFormatCode())) {
                        if (text != null) {
                            references.add(text.toString());
                        }
                        text = new StringBuffer();
                    }
                    text.append(line.getLineText());
                }
            }
            if (text != null) {
                references.add(text.toString());
            }
        }
        return references;
    }
    /**
     * 
     * @param screening screening property
     */
    public void setScreening(Screening screening) {
        this.screening = screening;
    }
    /**
     * 
     * @param fdbDataManager fdbDataManager property
     */
    public void setFdbDataManager(FDBDataManager fdbDataManager) {
        this.fdbDataManager = fdbDataManager;
    }
    /**
     * Key class used in a Map to filter out duplicate FDB interactions replaced by custom user interactions.
     */
    private class ScreenResultKey {
        private Set<String> drugCustomIds = new HashSet<String>(2);
        private Long interactionId = null;
        private Set<Long> mappedInteractionIds;
        private boolean fdbInteraction;
        /**
         * Set the ID values using the given {@link DDIMScreenResult}.
         * 
         * @param screenResult {@link DDIMScreenResult}
         */
        public ScreenResultKey(DDIMScreenResult screenResult) {
            drugCustomIds.add(screenResult.getDrug1CustomID());
            drugCustomIds.add(screenResult.getDrug2CustomID());
            fdbInteraction = !screenResult.getUserInteraction();
            this.interactionId = Long.valueOf(screenResult.getInteractionID());
            
            if(fdbInteraction) {
                this.mappedInteractionIds = getMappedIds(screenResult);
            }
            
        }
        /**
         * 
         * @return boolean true if this is a FDB interaction 
         */
        private boolean isFdbInteraction() {
            return fdbInteraction;
        }
        /**
         * hashCode is simply a method on the drugCustomIds, 
         * due to the complicated nature of the equals method other properties are ignored
         * 
         * @return int hash code
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return drugCustomIds.hashCode();
        }
        /**
         * Two {@link ScreenResultKey} are equal if the drug custom IDs are equal (in any order) and if this set of FDB
         * interaction IDs are "equal".
         * <p>
         * FDB interaction IDs are equal when:
         * <ul>
         *
         * <li>If this is a user interaction and the given key is not, then check that the {@link #mappedInteractionIds}
         * contains the given {@link #interactionId}.</li>
         * <li>If this is a not a user interaction and the given key is, then check that the {@link #interactionId} is
         * contained in the given {@link #mappedInteractionIds}</li>
         * <li>Else the two are both either user interactions or not user interactions and test if the {@link #interactionId}
         * are equal</li>
         * </ul>
         * 
         * @param obj object to compare
         * @return boolean true if equals
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            boolean equal = false;
            if (obj instanceof ScreenResultKey) {
                ScreenResultKey other = (ScreenResultKey) obj;
                if (drugCustomIds.equals(other.drugCustomIds)) {
                    if (isFdbInteraction() && !other.isFdbInteraction()) {
                        equal = mappedInteractionIds.contains(other.interactionId);
                    }
                    else if (!isFdbInteraction() && other.isFdbInteraction()) {
                        equal = other.mappedInteractionIds.contains(interactionId);
                    }
                    else {
                        equal = interactionId.equals(other.interactionId);
                    }
                }
            }
            return equal;
        }
    }
