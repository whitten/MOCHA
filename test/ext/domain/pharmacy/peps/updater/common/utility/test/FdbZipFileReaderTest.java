package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import java.io.File;
import java.io.IOException;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FdbZipFileReader;
import EXT.DOMAIN.pharmacy.peps.updater.common.utility.FdbZipFileReader.FdbHeader;
import junit.framework.TestCase;
/**
 * Test the FDB ZIP file reader.
 */
public class FdbZipFileReaderTest extends TestCase {
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        System.out.println("------------------------- " + getName() + " -------------------------\n");
    }
    /**
     * @throws Exception
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
    }
    /**
     * Test reading custom header.
     * 
     * @throws Exception
     */
    public void testReadCustomHeader() throws Exception {
        FdbZipFileReader reader = new FdbZipFileReader(new File("etc/datup/CstmUpdFile_3.2.5_2010021610042945206.zip"));
        FdbHeader header = reader.readHeader();
        System.out.println(header);
        assertFalse("Session dates should not be equal", header.getPreviousSessionDate().equals(header.getNewSessionDate()));
        assertEquals("Wrong previous session date", "20091002", header.getPreviousSessionDate());
        assertEquals("Wrong new session date", "20100216", header.getNewSessionDate());
        assertEquals("Wrong database version", "3.2", header.getDatabaseVersion());
        assertTrue("Empty message", !"".equals(header.getMessage()));
        assertEquals("Not custom", true, header.isCustom());
        assertEquals("Not incremental", true, header.isIncremental());
    }
    /**
     * Test reading custom header.
     * 
     * @throws Exception
     */
    public void testReadCustomFullHeader() throws Exception {
        FdbZipFileReader reader = new FdbZipFileReader(new File("etc/datup/Custom-Full.zip"));
        FdbHeader header = reader.readHeader();
        System.out.println(header);
        assertTrue("Session dates should be equal", header.getPreviousSessionDate().equals(header.getNewSessionDate()));
        assertEquals("Wrong previous session date", "20100429", header.getPreviousSessionDate());
        assertEquals("Wrong new session date", "20100429", header.getNewSessionDate());
        assertEquals("Wrong database version", "3.2", header.getDatabaseVersion());
        assertTrue("Empty message", !"".equals(header.getMessage()));
        assertEquals("Not custom", true, header.isCustom());
        assertEquals("Not full", false, header.isIncremental());
    }
    /**
     * Test reading incremental header.
     * 
     * @throws Exception
     */
    public void testReadIncrementalHeader() throws Exception {
        FdbZipFileReader reader = new FdbZipFileReader(new File("etc/datup/DIF32 UPD.ZIP"));
        FdbHeader header = reader.readHeader();
        System.out.println(header);
        assertFalse("Session dates should not be equal", header.getPreviousSessionDate().equals(header.getNewSessionDate()));
        assertEquals("Wrong previous session date", "20100122", header.getPreviousSessionDate());
        assertEquals("Wrong new session date", "20100129", header.getNewSessionDate());
        assertEquals("Wrong database version", "3.2", header.getDatabaseVersion());
        assertTrue("Empty message", !"".equals(header.getMessage()));
        assertEquals("Not FDB", false, header.isCustom());
        assertEquals("Not incremental", true, header.isIncremental());
    }
    /**
     * Test reading full header.
     * 
     * @throws Exception
     */
    public void testReadFullHeader() throws Exception {
        FdbZipFileReader reader = new FdbZipFileReader(new File("etc/datup/DIF32 DB.ZIP"));
        FdbHeader header = reader.readHeader();
        System.out.println(header);
        assertTrue("Session dates should be equal", header.getPreviousSessionDate().equals(header.getNewSessionDate()));
        assertEquals("Wrong previous session date", "20091113", header.getPreviousSessionDate());
        assertEquals("Wrong new session date", "20091113", header.getNewSessionDate());
        assertEquals("Wrong database version", "3.2", header.getDatabaseVersion());
        assertTrue("Empty message", !"".equals(header.getMessage()));
        assertEquals("Not FDB", false, header.isCustom());
        assertEquals("Not full", false, header.isIncremental());
    }
    /**
     * Test reading bad archive.
     * 
     * @throws Exception
     */
    public void testReadBadHeader() throws Exception {
        FdbZipFileReader reader = new FdbZipFileReader(new File("etc/datup/DIF32 DB_BAD.ZIP"));
        try {
            reader.readHeader();
            fail("Should throw header file exception!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
