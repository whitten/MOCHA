package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * Date/time utilities.
 */
public class DateTimeUtility {
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("EEE, MM/dd/yyyy, hh:mm:ss a, z");
    public static final Month[] MONTHS_IN_YEAR = getMonths();
    /**
     * Lookup timezone by ID.
     * 
     * @param zone zone ID
     * @return timezone
     */
    public static TimeZone toTimeZone(String zone) {
        if (zone == null) {
            return null;
        }
        return TimeZone.getTimeZone(zone);
    }
    /**
     * Default calendar to string conversion.
     * 
     * @param calendar calendar
     * @return string representation
     */
    public static String toString(Calendar calendar) {
        return toString(calendar, DEFAULT_DATE_FORMAT);
    }
    /**
     * Convert date and time zone to a default string representation.
     * 
     * @param date date
     * @param zone time zone
     * @return string representation
     */
    public static String toString(Date date, TimeZone zone) {
        return toString(date, zone, DEFAULT_DATE_FORMAT);
    }
    /**
     * Convert date and time zone to a specified string representation.
     * 
     * @param date date
     * @param zone time zone
     * @param formatter specified format
     * @return string representation
     */
    public static String toString(Date date, TimeZone zone, DateFormat formatter) {
        if (date == null) {
            return null;
        }
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        if (zone != null) {
            c.setTimeZone(zone);
        }
        return toString(c, formatter);
    }
    /**
     * Convert calendar to a string representation.
     * 
     * @param calendar calendar
     * @param formatter formatter
     * @return string representation
     */
    public static synchronized String toString(Calendar calendar, DateFormat formatter) {
        formatter.setCalendar(calendar);
        return formatter.format(calendar.getTime());
    }
    /**
     * Convert from military time.
     * 
     * @param militaryTime military time "xxxx"
     * @return calendar
     */
    public static Calendar fromMilitaryTime(int militaryTime) {
        int hour = (int) Math.floor(militaryTime / 100d);
        int minute = militaryTime - (hour * 100);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    /**
     * Reformat the FDB session date is human readable form.
     * 
     * @param sessionDate FDB session date
     * @return human readable form
     */
    public static String reformatFdbSessionDate(String sessionDate) {
        if ((sessionDate == null) || !sessionDate.matches("\\d{8}")) {
            return sessionDate;
        }
        StringBuilder b = new StringBuilder();
        b.append(sessionDate.substring(4, 6)).append("-").append(sessionDate.substring(6, 8)).append("-").append(
            sessionDate.substring(0, 4));
        return b.toString();
    }
    /**
     * Convert FDB session date to java date.
     * 
     * @param sessionDate FDB session date
     * @return date
     */
    public static Date fromFdbSessionDate(String sessionDate) {
        if ((sessionDate == null) || !sessionDate.matches("\\d{8}")) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, Integer.parseInt(sessionDate.substring(4, 6)));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(sessionDate.substring(6, 8)));
        calendar.set(Calendar.YEAR, Integer.parseInt(sessionDate.substring(0, 4)));
        return calendar.getTime();
    }
    /**
     * Retrieve list of months.
     * 
     * @return months
     */
    private static Month[] getMonths() {
        Calendar c = new GregorianCalendar();
        Map<String, Integer> monthsInYear = c.getDisplayNames(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        Month[] months = new Month[monthsInYear.size()];
        for (Map.Entry<String, Integer> month : monthsInYear.entrySet()) {
            months[month.getValue()] = new Month(month.getKey(), month.getValue());
        }
        return months;
    }
    /**
     * Represents a month of the year.
     */
    public static class Month {
        private String name;
        private int key;
        /**
         * Constructor.
         * 
         * @param name month name
         * @param key month ID
         */
        public Month(String name, int key) {
            this.name = name;
            this.key = key;
        }
        /**
         * Name.
         * 
         * @return name
         */
        public String getName() {
            return name;
        }
        /**
         * Key.
         * 
         * @return key
         */
        public int getKey() {
            return key;
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
