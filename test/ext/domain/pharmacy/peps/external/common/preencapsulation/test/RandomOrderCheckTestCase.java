/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import EXT.DOMAIN.pharmacy.peps.common.exception.InterfaceException;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.RandomOrderCheckCapability;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck.DrugCheckXmlUtility;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Body;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Checks;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Demographics;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DoseInformation;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DoseRateType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DoseTypeType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DoseUnitType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Drug;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DrugCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DrugDoseCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.DurationRateType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Header;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.MServer;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.MUser;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.MedicationProfile;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.ObjectFactory;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.PEPSRequest;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.ProspectiveDrugs;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.RouteType;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.Time;
/**
 * Generate random order checks for use by JUnit test case subclasses.
 */
public class RandomOrderCheckTestCase extends OrderCheckTestCase { // NOPMD
    private static final int MIN_PROSPECTIVE_DRUGS = 2;
    private static final int MAX_PROSPECTIVE_DRUGS = 10;
    private static final int MIN_PROFILE_DRUGS = 0;
    private static final int MAX_PROFILE_DRUGS = 10;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final List<BigInteger> AVAILABLE_GCN = Collections.synchronizedList(new ArrayList<BigInteger>(23815));
    private static final List<BigInteger> AVAILABLE_DOSING_GCN = Collections.synchronizedList(new ArrayList<BigInteger>(
        12496));
    private static final Set<String> USED_ORDER_NUMBERS = Collections.synchronizedSet(new HashSet<String>());
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private RandomOrderCheckCapability randomOrderCheckCapability;
    public RandomOrderCheckCapability getRandomOrderCheckCapability() {
        return randomOrderCheckCapability;
    }
    public void setRandomOrderCheckCapability(RandomOrderCheckCapability randomOrderCheckCapability) {
        this.randomOrderCheckCapability = randomOrderCheckCapability;
    }
    /**
     * Generate 200 dosing checks, 200 drug-drug checks, 200 duplicate therapy checks, and 200 "all" checks.
     * 
     * @param args command line arguments (not used)
     * @throws Exception if file I/O error
     */
    public static void main(String[] args) throws Exception {
        RandomOrderCheckTestCase random = new RandomOrderCheckTestCase("Generator");
        System.out.println("Writing random dosing checks...");
        random.writeRandomOrderChecks("dosing.txt", DrugCheckType.DRUG_DOSE);
        System.out.println("Writing random interaction checks...");
        random.writeRandomOrderChecks("interaction.txt", DrugCheckType.DRUG_DRUG);
        System.out.println("Writing random therapy checks...");
        random.writeRandomOrderChecks("therapy.txt", DrugCheckType.DRUG_THERAPY);
        System.out.println("Writing random all checks...");
        random.writeRandomOrderChecks("all.txt", DrugCheckType.ALL);
    }
    /**
     * Create random order checks
     */
    public RandomOrderCheckTestCase() {
        super();
    }
    /**
     * Create random order checks
     * 
     * @param name TestCase name
     */
    public RandomOrderCheckTestCase(String name) {
        super(name);
    }
    /**
     * Write 200 random order checks of type {@link DrugCheckType} to the given file.
     * 
     * @param file String file path
     * @param drugCheckType {@link DrugCheckType}
     * @throws IOException if file I/O error
     */
    private void writeRandomOrderChecks(String file, DrugCheckType drugCheckType) throws IOException {
        FileWriter fstream = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fstream);
        for (int i = 0; i < 200; i++) {
            out.write(getRandomOrderCheck(drugCheckType));
            out.write(LINE_SEPARATOR);
            System.out.println(StringUtils.leftPad(String.valueOf(i + 1), 4));
        }
        System.out.println();
        out.close();
    }
    /**
     * Setup the static Lists of data
     */
    @Override
    protected void initialize() {
        super.initialize();
        if (AVAILABLE_GCN.isEmpty()) {
            AVAILABLE_GCN.addAll(getAvailableGcnSequenceNumbers());
        }
        if (AVAILABLE_DOSING_GCN.isEmpty()) {
            AVAILABLE_DOSING_GCN.addAll(getAvailableDosingGcnSequenceNumbers());
        }
    }
    /**
     * Create and return a random order check
     * 
     * @param drugCheckType {@link DrugCheckType} specifying which types of checks to generate
     * @return String XML order check request
     */
    protected String getRandomOrderCheck(DrugCheckType drugCheckType) {
        try {
            ObjectFactory objectFactory = new ObjectFactory();
            PEPSRequest request = objectFactory.createPEPSRequest();
            request.setHeader(randomHeader(objectFactory));
            request.setBody(randomBody(objectFactory, drugCheckType));
            String response = DrugCheckXmlUtility.toPepsResponse(request);
            response = response.replaceAll("[\\r\\n]", "");
            return response;
        }
        catch (JAXBException e) {
            throw new InterfaceException(e, InterfaceException.INTERFACE_ERROR, InterfaceException.PRE_ENCAPSULATION);
        }
    }
    /**
     * Populate the Header section of the order check request XML
     * 
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return Header
     * @throws JAXBException if cannot instantiate XML objects
     */
    private Header randomHeader(ObjectFactory objectFactory) throws JAXBException {
        Header header = objectFactory.createHeader();
        MServer mServer = objectFactory.createMServer();
        mServer.setIp(randomPositiveInt(256) + "." + randomPositiveInt(256) + "." + randomPositiveInt(256) + "."
            + randomPositiveInt(256));
        mServer.setNamespace(randomAlphabetic(10));
        mServer.setServerName(randomAlphabetic(10));
        mServer.setStationNumber(new BigInteger(String.valueOf(randomPositiveInt())));
        mServer.setUci(randomAlphabetic(10));
        header.setMServer(mServer);
        MUser mUser = objectFactory.createMUser();
        mUser.setDuz(new BigInteger(String.valueOf(randomPositiveInt())));
        mUser.setJobNumber(new BigInteger(String.valueOf(randomPositiveInt())));
        mUser.setUserName(randomAlphabetic(10));
        header.setMUser(mUser);
        Time time = objectFactory.createTime();
        time.setValue(new Date().toString());
        header.setTime(time);
        return header;
    }
    /**
     * Populate a random Body section of the order check request XML
     * 
     * @param objectFactory ObjectFactory to use to create XML objects
     * @param drugCheckType {@link DrugCheckType} specifying which types of checks to generate
     * @return Body
     * @throws JAXBException if cannot instantiate XML objects
     */
    private Body randomBody(ObjectFactory objectFactory, DrugCheckType drugCheckType) throws JAXBException {
        Body body = objectFactory.createBody();
        DrugCheck drugCheck = objectFactory.createDrugCheck();
        body.setDrugCheck(drugCheck);
        Checks checks = objectFactory.createChecks();
        drugCheck.setChecks(checks);
        checks.setProspectiveOnly(RANDOM.nextBoolean());
        boolean drugDrugCheck = DrugCheckType.ALL.equals(drugCheckType) || DrugCheckType.DRUG_DRUG.equals(drugCheckType);
        boolean drugTherapyCheck = DrugCheckType.ALL.equals(drugCheckType)
            || DrugCheckType.DRUG_THERAPY.equals(drugCheckType);
        boolean drugDoseCheck = DrugCheckType.ALL.equals(drugCheckType) || DrugCheckType.DRUG_DOSE.equals(drugCheckType);
        // At least one boolean value must be true
        while (!(drugDrugCheck || drugTherapyCheck || drugDoseCheck)) {
            drugDrugCheck = RANDOM.nextBoolean();
            drugTherapyCheck = RANDOM.nextBoolean();
            drugDoseCheck = RANDOM.nextBoolean();
        }
        if (drugDrugCheck) {
            checks.setDrugDrugCheck(objectFactory.createDrugDrugCheck());
        }
        if (drugTherapyCheck) {
            checks.setDrugTherapyCheck(objectFactory.createDrugTherapyCheck());
        }
        if (drugDoseCheck) {
            checks.setDrugDoseCheck(randomDrugDoseCheck(objectFactory));
        }
        drugCheck.setProspectiveDrugs(randomProspectiveDrugs(objectFactory, drugDoseCheck));
        drugCheck.setMedicationProfile(randomMedicationProfile(objectFactory));
        return body;
    }
    /**
     * Populate a random drug dose check demographics.
     * 
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return DrugDoseCheck random DrugDoseCheck
     */
    private DrugDoseCheck randomDrugDoseCheck(ObjectFactory objectFactory) {
        DrugDoseCheck doseCheck = objectFactory.createDrugDoseCheck();
        Demographics demographics = objectFactory.createDemographics();
        demographics.setAgeInDays(randomPositiveInt(18 * 365, 65 * 365));
        demographics.setBodySurfaceAreaInSqM(randomPositiveDouble(1.5, 3.5));
        demographics.setWeightInKG(randomPositiveDouble(50, 225));
        doseCheck.setDemographics(demographics);
        return doseCheck;
    }
    /**
     * Populate random dose information.
     * 
     * @param gcn GCN sequence number to populate data for (used to retrieve valid dose route, type, and unit data)
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return {@link DoseInformation} random {@link DoseInformation}
     */
    private DoseInformation randomDoseInformation(BigInteger gcn, ObjectFactory objectFactory) {
        DoseInformation doseInformation = objectFactory.createDoseInformation();
        // set dose route, type, and units
        populateDoseInformation(gcn, doseInformation);
        // dose amount must be greater than 0
        doseInformation.setDoseAmount(randomPositiveDouble(3) + 1);
        // Dose rate int values are from 0 to 5
        int doseRate = randomPositiveInt(6);
        doseInformation.setDoseRate(DoseRateType.values()[doseRate]);
        doseInformation.setDuration(BigInteger.valueOf(randomPositiveLong()));
        // Duration rate will equal dose rate (to eliminate errors by FDB saying it cannot convert)
        doseInformation.setDurationRate(DurationRateType.fromValue(doseInformation.getDoseRate().value()));
        doseInformation.setFrequency(String.valueOf(randomPositiveInt()));
        return doseInformation;
    }
    /**
     * Populate the given instance of MedicationProfile. If no profile drugs will be used, return null.
     * 
     * @param objectFactory ObjectFactory to use to create XML objects
     * @return MedicationProfile random medication profile, or null if no profile drugs
     * @throws JAXBException if cannot instantiate XML objects
     */
    private MedicationProfile randomMedicationProfile(ObjectFactory objectFactory) throws JAXBException {
        MedicationProfile medicationProfile = null;
        int size;
        if (MIN_PROFILE_DRUGS == 0) {
            size = RANDOM.nextInt(MAX_PROFILE_DRUGS - MIN_PROFILE_DRUGS);
        }
        else {
            size = RANDOM.nextInt(MAX_PROFILE_DRUGS - MIN_PROFILE_DRUGS) + (MAX_PROFILE_DRUGS - MIN_PROFILE_DRUGS);
        }
        if (size > 0) {
            medicationProfile = objectFactory.createMedicationProfile();
            for (int i = 0; i < size; i++) {
                medicationProfile.getDrug().add(randomDrug(objectFactory, false));
            }
        }
        return medicationProfile;
    }
    /**
     * Create a random list of prospective drugs from the List of available GCN sequence numbers in FDB DIF
     * 
     * @param objectFactory ObjectFactory to use to create XML objects
     * @param drugDoseCheck boolean true if some prospective drugs require dose information
     * @return ProspectiveDrugs random list of prospective drugs
     * @throws JAXBException if cannot instantiate XML objects
     */
    private ProspectiveDrugs randomProspectiveDrugs(ObjectFactory objectFactory, boolean drugDoseCheck) throws JAXBException {
        ProspectiveDrugs drugs = objectFactory.createProspectiveDrugs();
        int size;
        if (MIN_PROSPECTIVE_DRUGS == 0) {
            size = RANDOM.nextInt(MAX_PROSPECTIVE_DRUGS - MIN_PROSPECTIVE_DRUGS);
        }
        else {
            size = RANDOM.nextInt(MAX_PROSPECTIVE_DRUGS - MIN_PROSPECTIVE_DRUGS)
                + (MAX_PROSPECTIVE_DRUGS - MIN_PROSPECTIVE_DRUGS);
        }
        for (int i = 0; i < size; i++) {
            boolean doseCheck = drugDoseCheck && randomBoolean();
            drugs.getDrug().add(randomDrug(objectFactory, doseCheck));
        }
        return drugs;
    }
    /**
     * Create a random drug with a unique IEN and order number combination.
     * 
     * @param objectFactory ObjectFactory to use to create XML objects
     * @param doseCheck boolean true if this drug should have dosing information
     * @return Drug
     */
    private Drug randomDrug(ObjectFactory objectFactory, boolean doseCheck) {
        Drug drug = objectFactory.createDrug();
        BigInteger gcn;
        if (doseCheck) {
            gcn = getRandomDosingGcnSequenceNumber();
            drug.setDoseInformation(randomDoseInformation(gcn, objectFactory));
        }
        else {
            gcn = getRandomGcnSequenceNumber();
        }
        drug.setGcnSeqNo(gcn);
        drug.setIen(BigInteger.valueOf(randomPositiveLong()));
        String orderNumber;
        do {
            orderNumber = String.valueOf(randomPositiveInt());
        }
        while (USED_ORDER_NUMBERS.contains(orderNumber));
        USED_ORDER_NUMBERS.add(orderNumber);
        drug.setOrderNumber(orderNumber);
        return drug;
    }
    /**
     * Use Sring's JdbcTemplate to retrieve a List of Numbers containing all GCN sequence numbers available for an order
     * check.
     * 
     * @return List of BigIntegers representing GCN sequence numbers
     */
    @SuppressWarnings("unchecked")
    private List<BigInteger> getAvailableGcnSequenceNumbers() {
        String sql = "SELECT GCNSEQNO FROM FDB_GENERIC_DISPENSABLE WHERE GCNSEQNO > 0";
        return getFdbDifJdbcTemplate().queryForList(sql, BigInteger.class);
    }
    /**
     * Use Sring's getFdbDifJdbcTemplate() to retrieve a List of Numbers containing all GCN sequence numbers available for a
     * dosing order check.
     * 
     * @return List of BigIntegers representing GCN sequence numbers
     */
    @SuppressWarnings("unchecked")
    private List<BigInteger> getAvailableDosingGcnSequenceNumbers() {
        String sql = "SELECT DISTINCT GCNSEQNO FROM FDB_DOSERANGECHECK WHERE DOSELOWUNITS IS NOT NULL";
        return getFdbDifJdbcTemplate().queryForList(sql, BigInteger.class);
    }
    /**
     * Use Sring's getFdbDifJdbcTemplate() to retrieve a List of Numbers containing all GCN sequence numbers available for a
     * dosing order check.
     * <p>
     * Populates the dose route, dose type, and dose units from a row in FDB_DOSERANGECHECK for the given GCN sequence
     * number.
     * 
     * @param gcn GCN sequence number of drug with dosing information in FDB_DOSERANGECHECK
     * @param doseInformation {@link DoseInformation} to populate
     */
    private void populateDoseInformation(BigInteger gcn, DoseInformation doseInformation) {
        String countSql = "SELECT COUNT(1) FROM FDB_DOSERANGECHECK WHERE GCNSEQNO = ? AND DOSELOWUNITS IS NOT NULL";
        int count = getFdbDifJdbcTemplate().queryForInt(countSql, new Object[] {gcn.intValue()});
        String sql = "SELECT DOSEROUTEID, DOSETYPEID, DOSELOWUNITS FROM FDB_DOSERANGECHECK WHERE GCNSEQNO = ? AND DOSELOWUNITS IS NOT NULL";
        SqlRowSet sqlRowSet = getFdbDifJdbcTemplate().queryForRowSet(sql, new Object[] {gcn.intValue()});
        sqlRowSet.absolute(randomPositiveInt(count) + 1);
        String doseRouteId = sqlRowSet.getString(1);
        String doseTypeId = sqlRowSet.getString(2);
        String doseLowUnits = sqlRowSet.getString(3);
        doseInformation.setRoute(getDoseRoute(doseRouteId));
        doseInformation.setDoseType(getDoseType(doseTypeId));
        doseInformation.setDoseUnit(getDoseUnit(doseLowUnits));
    }
    /**
     * Retrieve the {@link RouteType} by looking up the ID in FDB_DOSEROUTE.
     * 
     * @param doseRouteId ID
     * @return {@link RouteType}
     */
    private RouteType getDoseRoute(String doseRouteId) {
        String sql = "SELECT DESCRIPTION1 FROM FDB_DOSEROUTE WHERE DOSEROUTEID = ?";
        String routeType = (String) getFdbDifJdbcTemplate().queryForObject(sql, new Object[] {doseRouteId}, String.class);
        return RouteType.fromValue(routeType);
    }
    /**
     * Retrieve the {@link DoseTypeType} by looking up the ID in FDB_DOSETYPE.
     * 
     * @param doseTypeId ID
     * @return {@link DoseTypeType}
     */
    private DoseTypeType getDoseType(String doseTypeId) {
        String sql = "SELECT DESCRIPTION1 FROM FDB_DOSETYPE WHERE DOSETYPEID = ?";
        String doseType = (String) getFdbDifJdbcTemplate().queryForObject(sql, new Object[] {doseTypeId}, String.class);
        return DoseTypeType.fromValue(doseType);
    }
    /**
     * Retrieve the {@link DoseUnitType} by looking parsing the dose low units, stripping off everything after the first '/'.
     * 
     * @param doseLowUnits dose low units
     * @return {@link DoseUnitType}
     */
    private DoseUnitType getDoseUnit(String doseLowUnits) {
        int index = doseLowUnits.indexOf('/');
        String doseUnit;
        if (index < 0) {
            doseUnit = doseLowUnits;
        }
        else {
            doseUnit = doseLowUnits.substring(0, index);
        }
        return DoseUnitType.fromValue(doseUnit);
    }
    /**
     * Get a random GCN sequence number from the List of available GCN sequence numbers in FDB DIF
     * 
     * @return BigInteger GCN sequence number
     */
    private BigInteger getRandomGcnSequenceNumber() {
        return AVAILABLE_GCN.get(randomPositiveInt(AVAILABLE_GCN.size()));
    }
    /**
     * Get a random GCN sequence number from the List of available dosing GCN sequence numbers in FDB DIF
     * 
     * @return BigInteger GCN sequence number
     */
    private BigInteger getRandomDosingGcnSequenceNumber() {
        return AVAILABLE_DOSING_GCN.get(randomPositiveInt(AVAILABLE_DOSING_GCN.size()));
    }
    /**
     * Generate a random boolean true/false.
     * 
     * @return boolean
     */
    private boolean randomBoolean() {
        return randomPositiveInt(2) == 0;
    }
    /**
     * Get a random positive integer between zero (inclusive) and n (exclusive)
     * 
     * @param n max number to return (exclusive)
     * @return random number
     */
    private int randomPositiveInt(int n) {
        return Math.abs(RANDOM.nextInt(n));
    }
    /**
     * Get a random positive integer between the low and high values, inclusive.
     * 
     * @param low int low value
     * @param high int high value
     * @return random number
     */
    private int randomPositiveInt(int low, int high) {
        return (int) randomPositiveDouble(low, high);
    }
    /**
     * Get a random positive integer
     * 
     * @return random number
     */
    private int randomPositiveInt() {
        return randomPositiveInt(Integer.MAX_VALUE);
    }
    /**
     * Get a random positive long
     * 
     * @return random number
     */
    private long randomPositiveLong() {
        return Math.abs(RANDOM.nextLong());
    }
    /**
     * Get a random positive double, multiplying by 10 ^ decimals
     * 
     * @param decimals number of decimals to move the generated random 0-1 number by
     * @return random number
     */
    private double randomPositiveDouble(int decimals) {
        long multiplier = 10 ^ decimals;
        return Math.abs(RANDOM.nextDouble() * multiplier);
    }
    /**
     * Get a random positive double between the low and high values, inclusive.
     * 
     * @param low double low value
     * @param high double high value
     * @return random number
     */
    private double randomPositiveDouble(double low, double high) {
        double random = RANDOM.nextDouble();
        return (random * (high - low)) + low;
    }
    /**
     * Generate a random alphabetic String of a given length.
     * 
     * @param length int length of generated String
     * @return String
     */
    private String randomAlphabetic(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }
    /**
     * Generate only one type of drug check, a random set of drug checks, or all drug checks.
     */
    protected enum DrugCheckType {
            DRUG_DOSE,
            DRUG_DRUG,
            DRUG_THERAPY,
            RANDOM,
            ALL;
    }
