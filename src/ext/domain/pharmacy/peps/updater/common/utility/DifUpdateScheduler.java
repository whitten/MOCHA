/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import commonj.timers.Timer;
import commonj.timers.TimerListener;
import commonj.timers.TimerManager;
import EXT.DOMAIN.pharmacy.peps.updater.common.capability.DifUpdateCapability;
/**
 * Manages the schedule for the FDB-DIF update task.
 */
public class DifUpdateScheduler {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DifUpdateScheduler.class);
    private String jndi;
    private TimerManager timerManager;
    private ScheduledTimer difTimer;
    private Configuration config;
    private DifUpdateCapability updater;
    /**
     * Default constructor.
     */
    private DifUpdateScheduler() {
        timerManager = TimerFactory.instance().getManager();
    }
    /**
     * Start the timer. Re-read the current timer data/time from the configuration file each pass. If the times have changed,
     * cancel the associated timers and schedule new timers.
     */
    public synchronized void start() {
        if (difTimer != null) {
            throw new IllegalStateException();
        }
        scheduleNextTimer();
    }
    /**
     * Stop the timer.
     * 
     * @return true if successful, false if the timer has already fired
     */
    public synchronized boolean stop() {
        if (difTimer == null) {
            throw new IllegalStateException();
        }
        try {
            return difTimer.getTimer().cancel();
        }
        finally {
            this.difTimer = null;
        }
    }
    /**
     * Get the next scheduled time interval.
     * 
     * @return calendar
     */
    public synchronized Calendar getNextScheduledTime() {
        if (difTimer == null) {
            return null;
        }
        return difTimer.getScheduledTime();
    }
    /**
     * Set DIF updater.
     * 
     * @param capability updater
     */
    public void setDifUpdateCapability(DifUpdateCapability capability) {
        this.updater = capability;
    }
    /**
     * JNDI name.
     * 
     * @param jndi timer JNDI name
     */
    public void setJndi(String jndi) {
        this.jndi = jndi;
    }
    /**
     * Set configuration.
     * 
     * @param config configuration
     */
    public void setConfiguration(Configuration config) {
        this.config = config;
    }
    /**
     * Read the time interval from the schedule and schedule the next timer event.
     */
    private void scheduleNextTimer() {
        Calendar calendar = DateTimeUtility.fromMilitaryTime(config.load().getInt(Configuration.SCHEDULED_TIME));
        // advance 24 hours if time is in the past
        if (calendar.before(new GregorianCalendar())) {
            calendar.add(Calendar.HOUR_OF_DAY, 24);
        }
        LOG.debug("Next scheduled DIF update time: " + DateTimeUtility.toString(calendar));
        this.difTimer = new ScheduledTimer("FDB-DIF Timer", calendar.getTime());
    }
    /**
     * Encapsulates a timer to provide a name and other behavior.
     */
    public class ScheduledTimer implements TimerListener {
        private String name;
        private Timer timer;
        /**
         * Create a new timer.
         * 
         * @param name timer name
         * @param when timer start time
         */
        public ScheduledTimer(String name, Date when) {
            this.name = name;
            this.timer = timerManager.schedule(this, when);
        }
        /**
         * Invoked when the time has elapsed.
         * 
         * @param timer timer reference
         * 
         * @see commonj.timers.TimerListener#timerExpired(commonj.timers.Timer)
         */
        public void timerExpired(Timer timer) {
            LOG.debug(name + " fired on " + DateTimeUtility.toString(getScheduledTime()));
            try {
                updater.execute();
            }
            catch (Throwable t) {
                LOG.error("Error encountered during scheduled FDB-DIF update process", t);
            }
            scheduleNextTimer(); // schedule next timer event
        }
        public Calendar getScheduledTime() {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date(timer.getScheduledExecutionTime()));
            return calendar;
        }
        /**
         * Get the timer.
         * 
         * @return Timer
         */
        public Timer getTimer() {
            return timer;
        }
    }
