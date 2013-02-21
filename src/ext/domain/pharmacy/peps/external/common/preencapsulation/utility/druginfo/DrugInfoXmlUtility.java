/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.druginfo;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugInfoVo;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.XmlUtility;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.request.DrugInfoRequest;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.info.response.DrugInfoResponse;
/**
 * Convert a drug information request to and from XML
 */
public class DrugInfoXmlUtility extends XmlUtility {
    private static final String REQUEST_PACKAGE = DrugInfoRequest.class.getPackage().getName();
    private static final String RESPONSE_PACKAGE = DrugInfoResponse.class.getPackage().getName();
    private static final JAXBContext REQUEST_CONTEXT = initializeJaxbContext(REQUEST_PACKAGE);
    private static final JAXBContext RESPONSE_CONTEXT = initializeJaxbContext(RESPONSE_PACKAGE);
    private static final String RESPONSE_NAMESPACE = RESPONSE_PACKAGE.replace('.', '/');
    private static final String RESPONSE_SCHEMA_LOCATION = RESPONSE_NAMESPACE + " " + "drugInfoSchemaOutput.xsd";
    private static final String REQUEST_NAMESPACE = REQUEST_PACKAGE.replace('.', '/');
    private static final String REQUEST_SCHEMA_LOCATION = REQUEST_NAMESPACE + " " + "drugInfoSchemaInput.xsd";
    private static final String[] REQUEST_CDATA_ELEMENTS = {};
    private static final String[] RESPONSE_CDATA_ELEMENTS = {RESPONSE_NAMESPACE + "^references"};
    private static final String REQUEST_SCHEMA_FILE = "xml/schema/drugInfoSchemaInput.xsd";
    private static final String RESPONSE_SCHEMA_FILE = "xml/schema/drugInfoSchemaOutput.xsd";
    /**
     * Convert a XML request for drug information into a Collection of DrugInfoVo
     * 
     * @param xmlRequest String XML request from Vista
     * @return Collection of DrugInfoVo
     */
    public static Collection<DrugInfoVo> toDrugInfoVo(String xmlRequest) {
        return RequestConverter.toDrugInfoVo(unmarshal(xmlRequest, true, REQUEST_CONTEXT, REQUEST_SCHEMA_FILE,
            DrugInfoRequest.class));
    }
    /**
     * Convert a Collection of DrugInfoVo into a XML response for Vista
     * 
     * @param drugInfoResults results from looking up dose routes and dose types
     * @return String XML response
     */
    public static String toDrugInfoResponse(DrugInfoResultsVo drugInfoResults) {
        return marshal(ResponseConverter.toDrugInfoResponse(drugInfoResults), false, RESPONSE_CONTEXT,
            RESPONSE_SCHEMA_LOCATION, RESPONSE_SCHEMA_FILE, RESPONSE_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param xmlRequest String XML request to pretty print
     * @return String formatted XML request
     */
    public static String prettyPrintRequest(String xmlRequest) {
        return prettyPrint(xmlRequest, REQUEST_CONTEXT, REQUEST_SCHEMA_LOCATION, REQUEST_SCHEMA_FILE, DrugInfoRequest.class,
            REQUEST_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param request DrugInfoRequest to pretty print
     * @return String formatted XML request
     */
    public static String prettyPrintRequest(DrugInfoRequest request) {
        return prettyPrint(request, REQUEST_CONTEXT, REQUEST_SCHEMA_LOCATION, REQUEST_SCHEMA_FILE, REQUEST_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param xmlResponse String XML response to pretty print
     * @return String formatted XML response
     */
    public static String prettyPrintResponse(String xmlResponse) {
        return prettyPrint(xmlResponse, RESPONSE_CONTEXT, RESPONSE_SCHEMA_LOCATION, RESPONSE_SCHEMA_FILE,
            DrugInfoResponse.class, RESPONSE_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param response DrugInfoResponse to pretty print
     * @return String formatted XML response
     */
    public static String prettyPrintResponse(DrugInfoResponse response) {
        return prettyPrint(response, RESPONSE_CONTEXT, RESPONSE_SCHEMA_LOCATION, RESPONSE_SCHEMA_FILE,
            RESPONSE_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read. Tests if the given XML
     * contains {@value #REQUEST_SCHEMA_LOCATION}, {@value #RESPONSE_SCHEMA_LOCATION}, or
     * {@value #EXCEPTION_SCHEMA_LOCATION} to decide which schema to use. If the given XML doesn't match any of those, the
     * method returns the given XML unformatted.
     * 
     * @param xml String XML request, response, or exception to pretty print
     * @return String formatted XML
     */
    public static String prettyPrint(String xml) {
        if (xml.contains(REQUEST_SCHEMA_LOCATION)) {
            return prettyPrintRequest(xml);
        }
        else if (xml.contains(RESPONSE_SCHEMA_LOCATION)) {
            return prettyPrintResponse(xml);
        }
        else if (xml.contains(EXCEPTION_SCHEMA_LOCATION)) {
            return prettyPrintException(xml);
        }
        else {
            return xml;
        }
    }
    /**
     * Return true if the values of the attributes in the expected and actual responses are equal. The method calls the
     * equals() method on DrugInfoResponse, which itself is provided by the superclass, ValueObject.
     * 
     * @param expectedResponse String XML expected response
     * @param actualResponse String XML actual response
     * @return true if the expected response equals the actual response
     */
    public static boolean responseEquals(String expectedResponse, String actualResponse) {
        DrugInfoResponse expected = unmarshal(expectedResponse, false, RESPONSE_CONTEXT, RESPONSE_SCHEMA_FILE,
            DrugInfoResponse.class);
        DrugInfoResponse actual = unmarshal(actualResponse, false, RESPONSE_CONTEXT, RESPONSE_SCHEMA_FILE,
            DrugInfoResponse.class);
        return expected.equals(actual);
    }
    /**
     * Cannot instantiate
     */
    private DrugInfoXmlUtility() {
        super();
    }
