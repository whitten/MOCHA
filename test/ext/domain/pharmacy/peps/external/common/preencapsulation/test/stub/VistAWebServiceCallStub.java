/**
 * Copyright 2006, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.test.stub;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
/**
 * Class used to send POST requests to a web servlet
 */
public class VistAWebServiceCallStub {
    private static final String ENCODING = "UTF-8";
    private static final String XML_REQUEST = "xmlRequest";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private URL destination;
    /**
     * The constructor to use to setup the destination url and the parameters hash map
     * 
     * @param destinationUrl the destination to send the following requests
     * @throws MalformedURLException if the url passed is not correctly formed
     */
    public VistAWebServiceCallStub(String destinationUrl) throws MalformedURLException {
        this.destination = new URL(destinationUrl);
    }
    /**
     * Creates and sends the request to a servlet and returns the response
     * 
     * @param xmlRequest the xml payload of the request
     * @return The response received from the servlet
     * @throws IOException if an error occurred while sending the request
     */
    public String sendRequest(String xmlRequest) throws IOException {
        URLConnection connection = destination.openConnection();
        connection.setReadTimeout(60000);
        connection.setDoOutput(true);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        writer.write(encodeUrlParameter(XML_REQUEST, xmlRequest));
        writer.flush();
        writer.close();
        StringBuffer response = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = reader.readLine();
        while (line != null) {
            response.append(line);
            response.append(LINE_SEPARATOR);
            line = reader.readLine();
        }
        reader.close();
        return response.toString();
    }
    /**
     * 
     * @return the URL this call will send a request to
     */
    public String getUrl() {
        return destination.toExternalForm();
    }
    
    /**
     * Encode a URL parameter.
     * 
     * @param name String name of the parameter
     * @param value String value of the parameter
     * @return String encoded name/value URL parameter
     * @throws UnsupportedEncodingException if unable to encode name/value URL Parameter
     */
    private String encodeUrlParameter(String name, String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(name, ENCODING) + "=" + URLEncoder.encode(value, ENCODING);
    }
