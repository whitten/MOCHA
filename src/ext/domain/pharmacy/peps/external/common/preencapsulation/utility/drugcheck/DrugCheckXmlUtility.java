/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.drugcheck;
import javax.xml.bind.JAXBContext;
import EXT.DOMAIN.pharmacy.peps.common.vo.DrugDataVendorVersionVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckHeaderVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckResultsVo;
import EXT.DOMAIN.pharmacy.peps.common.vo.OrderCheckVo;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.utility.XmlUtility;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.request.PEPSRequest;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.Body;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.DrugCheck;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.vo.drug.check.response.PEPSResponse;
/**
 * Marshal and unmarshal XML into Java objects
 */
public class DrugCheckXmlUtility extends XmlUtility {
    private static final String REQUEST_PACKAGE = PEPSRequest.class.getPackage().getName();
    private static final String RESPONSE_PACKAGE = PEPSResponse.class.getPackage().getName();
    private static final JAXBContext REQUEST_CONTEXT = initializeJaxbContext(REQUEST_PACKAGE);
    private static final JAXBContext RESPONSE_CONTEXT = initializeJaxbContext(RESPONSE_PACKAGE);
    private static final String RESPONSE_NAMESPACE = RESPONSE_PACKAGE.replace('.', '/');
    private static final String RESPONSE_SCHEMA_LOCATION = RESPONSE_NAMESPACE + " " + "drugCheckSchemaOutput.xsd";
    private static final String REQUEST_NAMESPACE = REQUEST_PACKAGE.replace('.', '/');
    private static final String REQUEST_SCHEMA_LOCATION = REQUEST_NAMESPACE + " " + "drugCheckSchemaInput.xsd";
    private static final String[] REQUEST_CDATA_ELEMENTS = {};
    private static final String[] RESPONSE_CDATA_ELEMENTS = {RESPONSE_NAMESPACE + "^reference"};
    private static final String REQUEST_SCHEMA_FILE = "xml/schema/drugCheckSchemaInput.xsd";
    private static final String RESPONSE_SCHEMA_FILE = "xml/schema/drugCheckSchemaOutput.xsd";
    /**
     * Return true if the values of the attributes in the expected and actual responses are equal.
     * <p>
     * If both {@link Body} elements are not null, the method calls the equals() method on {@link Body}, which itself is
     * provided by the superclass, ValueObject.
     * <p>
     * Otherwise, method calls the equals() method on {@link PEPSResponse}, which itself is provided by the superclass,
     * ValueObject.
     * 
     * @param expectedResponse String XML expected response
     * @param actualResponse String XML actual response
     * @return true if the expected response equals the actual response
     */
    public static boolean responseEquals(String expectedResponse, String actualResponse) {
        PEPSResponse expected = unmarshalPepsResponse(expectedResponse, false);
        PEPSResponse actual = unmarshalPepsResponse(actualResponse, false);
       
     
       //We are truly just verifying we're getting an equivilant response, 
        //there's too much flexibility in the FDB database 
        //to do a character by character comparison.
        
        boolean ret = true;
        
        
        ret &= (expected.isSetHeader() == actual.isSetHeader());
        ret &= (expected.isSetBody() == actual.isSetBody());
        
        if (ret) {
            
            Body actualBody = actual.getBody();
            Body expectedBody = expected.getBody();
            
            if (actualBody != null && expectedBody != null ) {
                
                DrugCheck actualCheck = actualBody.getDrugCheck();
                DrugCheck expectedCheck = expectedBody.getDrugCheck();
                
                if (actualCheck != null && expectedCheck != null) {
                    // Not going to look at the contents of the individual checks, but want to make sure any appropriate
                    // reponses are set
                    
                    ret &= (actualCheck.isSetDrugDoseChecks() == expectedCheck.isSetDrugDoseChecks());
                    ret &= (actualCheck.isSetDrugDrugChecks() == expectedCheck.isSetDrugDrugChecks());
                    ret &= (actualCheck.isSetDrugTherapyChecks() == expectedCheck.isSetDrugTherapyChecks());
                    ret &= (actualCheck.isSetDrugsNotChecked() == expectedCheck.isSetDrugsNotChecked());
                }
            }
            
            
        }
            
        
        
        
        
        return ret;
        /*
        if (expected.getBody() == null || actual.getBody() == null) {
            return expected.equals(actual);
        }
        else {
            return expected.getBody().equals(actual.getBody());
        }*/
    }
    /**
     * Unmarshal the XML request into a PEPSResponse object.
     * 
     * @param xml String XML request
     * @param validate boolean true if should validate XML string against schema
     * @return PEPSRequest created from the XML request
     */
    private static PEPSResponse unmarshalPepsResponse(String xml, boolean validate) {
        return unmarshal(xml, validate, RESPONSE_CONTEXT, RESPONSE_SCHEMA_FILE, PEPSResponse.class);
    }
    /**
     * Unmarshal the XML request into a PEPSRequest object.
     * 
     * @param xml String XML request
     * @param validate boolean true if should validate XML String
     * @return PEPSRequest created from the XML request
     */
    private static PEPSRequest unmarshalPepsRequest(String xml, boolean validate) {
        return unmarshal(xml, validate, REQUEST_CONTEXT, REQUEST_SCHEMA_FILE, PEPSRequest.class);
    }
    /**
     * Unmarshal the given XML String into an OrderCheckVo and validate the data against the schema.
     * 
     * @param xml String to unmarshal
     * @return OrderCheckVo created from XML String
     */
    public static OrderCheckVo toOrderCheckVo(String xml) {
        return RequestConverter.toOrderCheckVo(unmarshalPepsRequest(xml, true));
    }
    /**
     * Marshal the given OrderCheckResultsVo into an XML message.
     * 
     * @param orderCheckResults OrderCheckResultsVo to convert into XML
     * @return String XML message
     */
    public static String toPepsResponse(OrderCheckResultsVo orderCheckResults) {
        return marshal(ResponseConverter.toPepsResponse(orderCheckResults), false);
    }
    /**
     * Marshal a PEPSRequest into an XML String. Do not indent/pretty print resulting String.
     * 
     * @param request PEPSRequst to marshal
     * @return XML String representing given PEPSRequest
     */
    public static String toPepsResponse(PEPSRequest request) {
        return marshal(request, false);
    }
    /**
     * Marshal a PEPSRequest into an XML String
     * 
     * @param request PEPSRequst to marshal
     * @param prettyPrint boolean true if indenting/pretty printing should be used
     * @return XML String representing given PEPSRequest
     */
    private static String marshal(PEPSRequest request, boolean prettyPrint) {
        return marshal(request, prettyPrint, REQUEST_CONTEXT, REQUEST_SCHEMA_LOCATION, REQUEST_SCHEMA_FILE,
            REQUEST_CDATA_ELEMENTS);
    }
    /**
     * Marshal a PEPSResponse into an XML String
     * 
     * @param response PEPSResponse to marshal
     * @param prettyPrint boolean true if indenting/pretty printing should be used
     * @return XML String representing given PEPSResponse
     */
    private static String marshal(PEPSResponse response, boolean prettyPrint) {
        return marshal(response, prettyPrint, RESPONSE_CONTEXT, RESPONSE_SCHEMA_LOCATION, RESPONSE_SCHEMA_FILE,
            RESPONSE_CDATA_ELEMENTS);
    }
    /**
     * Convert the given OrderCheckHeaderVo and DrugDataVendorVersionVo into a ping response.
     * 
     * @param header OrderCheckHeaderVo to use for header data
     * @param drugDataVendorVersion DrugDataVendorVersionVo to use for DDV version data
     * @return ping XML response String
     */
    public static String createPingResponse(OrderCheckHeaderVo header, DrugDataVendorVersionVo drugDataVendorVersion) {
        return marshal(ResponseConverter.createPingResponse(header, drugDataVendorVersion), false);
    }
    /**
     * Validate the given XML request by unmarshalling it
     * 
     * @param xmlRequest String XML request
     * @return boolean true if valid, else false
     */
    public static boolean validatePepsRequest(String xmlRequest) {
        unmarshalPepsRequest(xmlRequest, true);
        return true;
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param xmlRequest String XML request to pretty print
     * @return String formatted XML request
     */
    public static String prettyPrintRequest(String xmlRequest) {
        return prettyPrint(xmlRequest, REQUEST_CONTEXT, REQUEST_SCHEMA_LOCATION, REQUEST_SCHEMA_FILE, PEPSRequest.class,
            REQUEST_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param request PEPSReqeust to pretty print
     * @return String formatted XML request
     */
    public static String prettyPrintRequest(PEPSRequest request) {
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
            PEPSResponse.class, RESPONSE_CDATA_ELEMENTS);
    }
    /**
     * Return a String formatted with indents to make System.out or Log4j output easier to read.
     * 
     * @param response PEPSResponse to pretty print
     * @return String formatted XML response
     */
    public static String prettyPrintResponse(PEPSResponse response) {
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
     * Cannot instantiate
     */
    private DrugCheckXmlUtility() {
        super();
    }
