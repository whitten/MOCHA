package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * Reads FDB ZIP archives.
 */
public class FdbZipFileReader {
    private static final String VERSIONCOMMENT_KEY = "versioncomment";
    private static final String ISSUEDATE_KEY = "issuedate";
    private static final String CUSTOM_TABLE_NAME = "ct_version";
    private static final String NEW_VERSION_MARKER = "E";
    private static final String PREVIOUS_VERSION_MARKER = "V";
    private static final String INDEX_MARKER = "F";
    private static final String INC_HEADER_FILENAME = "FDBUPDCONTROL.DAT";
    private static final String FULL_DIF_HEADER_FILENAME = "FDBVERSION.TXT";
    private static final String FULL_CUSTOM_HEADER_FILENAME = "CTVERSION.TXT";
    private File file;
    /**
     * Constructor.
     * 
     * @param file ZIP file
     */
    public FdbZipFileReader(File file) {
        this.file = file;
    }
    /**
     * Read the header.
     * 
     * @return header
     * @throws IOException file I/O error occurs
     */
    public FdbHeader readHeader() throws IOException {
        FdbHeader header = new FdbHeader();
        ZipFile zf = new ZipFile(file);
        ZipEntry headerEntry = getArchiveHeader(header, zf);
        BufferedReader reader = new BufferedReader(new InputStreamReader(zf.getInputStream(headerEntry)));
        try {
            String issueDateIndex = null;
            String versionCommentIndex = null;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] values = line.split("\\|");
                if (values == null || values.length == 0) { // skip blank lines
                    continue;
                }
                if (header.isIncremental) { // incremental (FDB and custom)
                    if (PREVIOUS_VERSION_MARKER.equalsIgnoreCase(values[0])) {
                        header.isCustom = CUSTOM_TABLE_NAME.equalsIgnoreCase(values[1]);
                        if (values.length > 5) {
                            header.databaseVersion = values[5];
                        }
                        else {
                            header.databaseVersion = "";
                        }
                        if (values.length > 11) {
                            header.previousSessionDate = values[11];
                        }
                        else {
                            header.previousSessionDate = "";
                        }
                    }
                    else if (INDEX_MARKER.equalsIgnoreCase(values[0])) { // locate value indices
                        for (int i = 0; i < values.length; i++) {
                            if (ISSUEDATE_KEY.equalsIgnoreCase(values[i])) {
                                issueDateIndex = values[i - 1];
                            }
                            else if (VERSIONCOMMENT_KEY.equalsIgnoreCase(values[i])) {
                                versionCommentIndex = values[i - 1];
                            }
                        }
                    }
                    else if (NEW_VERSION_MARKER.equalsIgnoreCase(values[0])) { // read values
                        for (int i = 0; i < values.length; i++) {
                            if (values[i].equals(issueDateIndex)) {
                                header.newSessionDate = stripQuotes(values[i + 1]);
                            }
                            else if (values[i].equals(versionCommentIndex)) {
                                header.message = stripQuotes(values[i + 1]);
                            }
                        }
                    }
                }
                else { // full
                    header.databaseVersion = values[1];
                    header.previousSessionDate = header.newSessionDate = values[4];
                    header.message = values[5];
                }
            }
        }
        finally {
            reader.close();
        }
        return header;
    }
    /**
     * Locate the archive header.
     * 
     * @param header FDB header
     * @param zf archive file
     * @return zip file entry
     * @throws IOException file is not found
     */
    private ZipEntry getArchiveHeader(FdbHeader header, ZipFile zf) throws IOException {
        ZipEntry headerEntry = zf.getEntry(INC_HEADER_FILENAME);
        if (headerEntry != null) { // incremental
            header.isIncremental = true;
            return headerEntry;
        }
        headerEntry = zf.getEntry(FULL_DIF_HEADER_FILENAME);
        if (headerEntry != null) { // full DIF
            header.isIncremental = false;
            header.isCustom = false;
            return headerEntry;
        }
        headerEntry = zf.getEntry(FULL_CUSTOM_HEADER_FILENAME);
        if (headerEntry != null) { // full custom
            header.isIncremental = false;
            header.isCustom = true;
            return headerEntry;
        }
        // unknown archive header
        throw new IOException("Unable to locate FDB header control file '" + INC_HEADER_FILENAME + " | "
            + FULL_DIF_HEADER_FILENAME + "' in: " + file.getAbsolutePath());
    }
    /**
     * Strip quotes from string.
     * 
     * @param s string
     * @return unquoted string
     */
    private String stripQuotes(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("'", "");
    }
    /**
     * FDB Header.
     */
    public static class FdbHeader {
        private boolean isIncremental;
        private boolean isCustom;
        private String previousSessionDate;
        private String databaseVersion;
        private String newSessionDate;
        private String message;
        /**
         * Default constructor.
         */
        public FdbHeader() {
        }
        /**
         * Constructor.
         * 
         * @param isIncremental incremental
         * @param isCustom custom
         * @param databaseVersion FDB DB version
         * @param previousSessionDate dependent session date
         * @param newSessionDate new session date
         * @param message update message
         */
        public FdbHeader(boolean isIncremental, boolean isCustom, String databaseVersion, String previousSessionDate,
                         String newSessionDate, String message) {
            this.isIncremental = isIncremental;
            this.isCustom = isCustom;
            this.databaseVersion = databaseVersion;
            this.previousSessionDate = previousSessionDate;
            this.newSessionDate = newSessionDate;
            this.message = message;
        }
        /**
         * Is it an incremental or full DB?
         * 
         * @return
         */
        public boolean isIncremental() {
            return isIncremental;
        }
        /**
         * Is it a custom-table update?
         * 
         * @return true if custom
         */
        public boolean isCustom() {
            return isCustom;
        }
        /**
         * Previous FDB session date.
         * 
         * @return YYYYMMDD
         */
        public String getPreviousSessionDate() {
            return previousSessionDate;
        }
        /**
         * New FDB session date.
         * 
         * @return YYYYMMDD
         */
        public String getNewSessionDate() {
            return newSessionDate;
        }
        /**
         * FDB database version.
         * 
         * @return I.I
         */
        public String getDatabaseVersion() {
            return databaseVersion;
        }
        /**
         * Update message.
         * 
         * @return message
         */
        public String getMessage() {
            return message;
        }
        /**
         * String representation.
         * 
         * @return string
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }
