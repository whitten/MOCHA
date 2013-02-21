/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.utility.profile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.external.common.preencapsulation.capability.ProcessOrderChecksCapability;
/**
 * The main() method allows the profile runs to be executed from the command line.
 * 
 * This class is only used when profiling is turned on. The class is not intended to be included within a deployed production
 * system.
 */
public class ProfileRunner {
    private static final Logger LOG = Logger.getLogger(ProfileRunner.class);
    /**
     * Execute the profile run from the command line. The first command line argument is the path to the XML file to send as
     * a request. This path is relative to the execution directory, or must be on the class path.
     * 
     * @param args command line arguments
     * @throws IOException if error reading specified XML file
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            LOG.error("Command line usage: ProfileRunner [xmlFilePath]");
            return;
        }
        LOG.info("Executing order check with XML file " + args[0]);
        ApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] {"xml/spring/peps/test/InterfaceContext.xml", "xml/spring/mocha/ProfileContextDynamic.xml"});
        ProcessOrderChecksCapability capability = (ProcessOrderChecksCapability) context
            .getBean("ProcessOrderChecksCapability");
        InputStream xmlInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(args[0]);
        BufferedReader reader = new BufferedReader(new InputStreamReader(xmlInputStream));
        StringBuffer xmlRequest = new StringBuffer();
        String line = reader.readLine();
        while (line != null) {
            xmlRequest.append(line);
            line = reader.readLine();
        }
        capability.handleRequest(xmlRequest.toString());
    }
