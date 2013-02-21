package EXT.DOMAIN.pharmacy.peps.updater.common.utility;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import commonj.timers.Timer;
import commonj.timers.TimerListener;
import commonj.timers.TimerManager;
import EXT.DOMAIN.pharmacy.peps.updater.common.servlet.TimerServlet;
/**
 * Manages the creation of timers.
 */
public class TimerFactory {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TimerFactory.class);
    private static TimerFactory INSTANCE = new TimerFactory();
    private TimerManager manager = new DefaultTimerManager();
    /**
     * Singleton instance.
     * 
     * @return instance
     */
    public static TimerFactory instance() {
        return INSTANCE;
    }
    /**
     * Default constructor.
     */
    private TimerFactory() {
    }
    /**
     * Locate the timer manager. If the JNDI name is not set, a non-container timer manager is created.
     * 
     * @return timer manager
     */
    public TimerManager getManager() {
        if (manager instanceof DefaultTimerManager) {
            LOG.debug("Defaulting to non-container managed timer.");
        }
        else {
            LOG.debug("Using container managed timer.");
        }
        return manager;
    }
    /**
     * Set JNDI name.
     * 
     * @param jndiName jndi name
     */
    public void setManager(TimerManager manager) {
        this.manager = manager;
    }
    /**
     * http://download.oracle.com/docs/cd/E12839_01/apirefs.1111/e13941/commonj/timers/TimerManager.html
     * 
     * Applications use a TimerManager to schedule TimerListeners. Each of the TimerManager schedule methods returns a Timer
     * object. The returned Timer can then be queried and/or cancelled. Applications are required to implement the
     * TimerListener interface and may optionally implement one or both of the CancelTimerListener and StopTimerListener
     * interfaces. All Timers execute in the same JVM as the thread that created the Timer and are transient. If the JVM
     * fails then all Timers will be lost.
     * 
     * TimerManagers are bound to a J2EE component using a resource-reference, similar to a WorkManager or DataSource. Each
     * lookup returns a new logical TimerManager who's lifecycle can be managed independently of all other TimerManagers.
     * Components should cache the TimerManager rather than look it up every time.
     * 
     * All TimerManagers are bound to the lifecycle of the application that looks it up. When the application state changes,
     * all TimerManagers associated with that application will be stopped, suspended or resumed, depending on the
     * capabilities of the application server. TimerManagers are configured by the server administrator. The vendor specific
     * systems management console allows the administrator to create one or more TimerManagers and associate a JNDI name with
     * each one. The administrator may specify implementation specific information such as the maximum number of concurrent
     * timers or thread priorities for each TimerManager. An application that requires a TimerManager should declare a
     * resource-ref in the EJB or webapp that needs the TimerManager. The vendor descriptor editor or J2EE IDE can be used to
     * bind this resource-ref to a physical TimerManager at deploy or development time. An EJB or servlet can then get a
     * reference to a TimerManager by looking up the resource-ref name in JNDI and then casting it. For example, if the
     * resource-ref was called tm/TimerManager:
     * 
     * <resource-ref> <res-ref-name>tm/TimerManager</res-ref-name> <res-type>commonj.timers.TimerManager</res-type>
     * <res-auth>Container</res-auth> <res-sharing-scope>Unshareable</res-sharing-scope> </resource-ref>
     * 
     * 
     * The res-auth is ignored in this version of the specification. The sharing scope must be set to unshareable. If this is
     * specified as shareable then a vendor specific action will occur. The Java code to look this up would look like:
     * 
     * InitialContext ic = new InitialContext(); TimerManager tm = (TimerManager)ic.lookup("java:comp/env/tm/TimerManager");
     * 
     * 
     * The EJB or servlet can then use the TimerManager as it needs to.
     * 
     * Recurring timers will execute their TimerListener multiple times. Invocations of the TimerListener are executed
     * serially. That is, if a timer is scheduled to repeat every 5 seconds and a previous execution is still running, then
     * the subsequent execution is delayed until the currently running execution completes.
     * 
     * A TimerListener scheduled multiple times, resulting in the return of multiple Timer instances, may execute
     * concurrently depending on implementation. Proper thread safety techniques need to be employed.
     * 
     * When a TimerListener is scheduled, the declared context that is present on the thread (the J2EE context) will be saved
     * and propagated to the asynchronous methods that are executed. This J2EE context at minimum will contain the java:comp
     * namespace and ClassLoader of the scheduler unless specified otherwise. Other J2EE contexts such as security or a
     * transactional context may be optionally added by the application server vendor. Global transactions are always
     * available using the java:comp/UserTransaction JNDI name and are used in the same fashion as they are used in servlets
     * and bean-managed transaction Enterprise Java Beans.
     * 
     * Two types of repeating timers are available:
     * 
     * fixed-rate - TimerListeners are executed at a constant rate based on the initial scheduled execution time. Each
     * subsequent execution time is based off of the period and the first execution time. This is appropriate for recurring
     * activities that are sensitive to absolute time, such as ringing a chime every hour on the hour. It is also appropriate
     * for recurring activities where the total time to perform a fixed number of executions is important, such as a
     * countdown timer that ticks once every second for ten seconds.
     * 
     * If a fixed-rate execution is delayed for any reason (such as garbage collection, suspension, or other background
     * activity), the late TimerListener will execute one time immediately, and then again at the next scheduled fixed-rate
     * time.
     * 
     * For example, if a fixed-rate timer was to execute every hour on the hour and missed 5 executions because the
     * TimerManager was suspended for over 5 hours, when the TimerManager is resumed, the TimerListener will run once
     * immedately, and each execution thereafter will be on the hour.
     * 
     * fixed-delay - TimerListeners are executed at a constant delay based on the actual execution time. Each subsequent
     * execution time is based off of the period and the end time of the previous execution. The period is the delay in
     * between TimerListener executions.
     * 
     * If a fixed-delay execution is delayed for any reason (such as garbage collection, suspension, or other background
     * activity), subsequent executions will be delayed as well. In the long run, the frequency of execution will generally
     * be slightly lower than the reciprocal of the specified period (assuming the system clock underlying Object.wait(long)
     * is accurate). Fixed-delay execution is appropriate for recurring activities that require "smoothness". In other words,
     * it is appropriate for activities where it is more important to keep the frequency accurate in the short run than in
     * the long run. This includes most animation tasks, such as blinking a cursor at regular intervals. It also includes
     * tasks wherein regular activity is performed in response to human input, such as automatically repeating a character as
     * long as a key is held down.
     * 
     * For example, if a fixed-delay timer was to execute every hour on the hour and missed 5 executions because the
     * TimerManager was suspended for over 5 hours, when the TimerManager is resumed, the TimerListener would execute
     * immediately and the next execution would occur one hour after the completion of the TimerListener.
     */
    public class DefaultTimerManager implements TimerManager {
        private int timerCount;
        private Map<Integer, TimerTask> timers = new HashMap<Integer, TimerTask>();
        private java.util.Timer timer = new java.util.Timer();
        public synchronized boolean isStopped() {
            throw new UnsupportedOperationException();
        }
        public synchronized boolean isStopping() {
            throw new UnsupportedOperationException();
        }
        public synchronized boolean isSuspended() throws IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized boolean isSuspending() throws IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized void resume() throws IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized Timer schedule(TimerListener listener, Date when) throws IllegalArgumentException,
            IllegalStateException {
            DefaultTimerTask task = new DefaultTimerTask(timerCount++, listener);
            timers.put(timerCount, task);
            // schedule to fire
            timer.schedule(task, when);
            return task;
        }
        public synchronized Timer schedule(TimerListener arg0, long arg1) throws IllegalArgumentException,
            IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized Timer schedule(TimerListener arg0, Date arg1, long arg2) throws IllegalArgumentException,
            IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized Timer schedule(TimerListener arg0, long arg1, long arg2) throws IllegalArgumentException,
            IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized Timer scheduleAtFixedRate(TimerListener arg0, Date arg1, long arg2)
            throws IllegalArgumentException, IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized Timer scheduleAtFixedRate(TimerListener arg0, long arg1, long arg2)
            throws IllegalArgumentException, IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized void stop() throws IllegalStateException {
            throw new UnsupportedOperationException();
        }
        public synchronized void suspend() {
            throw new UnsupportedOperationException();
        }
        public synchronized boolean waitForStop(long arg0) throws InterruptedException, IllegalArgumentException {
            throw new UnsupportedOperationException();
        }
        public synchronized boolean waitForSuspend(long arg0) throws InterruptedException, IllegalStateException,
            IllegalArgumentException {
            throw new UnsupportedOperationException();
        }
        /**
         * Pretty timer list representation.
         * 
         * @return list of timers
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (Iterator<Map.Entry<Integer, TimerTask>> i = timers.entrySet().iterator(); i.hasNext();) {
                Map.Entry<Integer, TimerTask> timer = i.next();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date(timer.getValue().scheduledExecutionTime()));
                b.append("Timer #").append(timer.getKey()).append(": ").append(DateTimeUtility.toString(calendar));
            }
            return b.toString();
        }
        /**
         * Wrapper class for the timer.
         */
        public class DefaultTimerTask extends java.util.TimerTask implements commonj.timers.Timer {
            private int id;
            private commonj.timers.TimerListener listener;
            public DefaultTimerTask(int id, commonj.timers.TimerListener listener) {
                this.id = id;
                this.listener = listener;
            }
            public void run() {
                timers.remove(id);
                listener.timerExpired(this);
            }
            public commonj.timers.TimerListener getTimerListener() {
                return listener;
            }
            public long getScheduledExecutionTime() {
                return super.scheduledExecutionTime();
            }
            public long getPeriod() {
                return 0;
            }
        }
    }
