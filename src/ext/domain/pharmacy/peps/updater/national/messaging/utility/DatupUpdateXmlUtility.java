/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.national.messaging.utility;
import javax.xml.bind.JAXBContext;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.XmlUtility;
import EXT.DOMAIN.pharmacy.peps.updater.common.vo.external.update.DatupUpdate;
/**
 * Marshal XML into Java objects
 */
public class DatupUpdateXmlUtility extends XmlUtility {
    private static final String RESPONSE_PACKAGE = DatupUpdate.class.getPackage().getName();
    private static final JAXBContext RESPONSE_CONTEXT = initializeJaxbContext(RESPONSE_PACKAGE);
    private static final String RESPONSE_NAMESPACE = RESPONSE_PACKAGE.replace('.', '/');
    private static final String RESPONSE_SCHEMA_LOCATION = RESPONSE_NAMESPACE + " " + "datupUpdateSchema.xsd";
    private static final String[] RESPONSE_CDATA_ELEMENTS = {RESPONSE_NAMESPACE + "^message"};
    private static final String RESPONSE_SCHEMA_FILE = "xml/schema/datupUpdateSchema.xsd";
    /**
     * Marshal a DATUP update into an XML string.
     * 
     * @param response DATUP update to marshal
     * @param prettyPrint boolean true if indenting/pretty printing should be used
     * @return XML String representing given DATUP update
     */
    public static String marshal(DatupUpdate update, boolean prettyPrint) {
        return marshal(update, prettyPrint, RESPONSE_CONTEXT, RESPONSE_SCHEMA_LOCATION, RESPONSE_SCHEMA_FILE,
            RESPONSE_CDATA_ELEMENTS);
    }
    /**
     * Cannot instantiate
     */
    private DatupUpdateXmlUtility() {
        super();
    }
