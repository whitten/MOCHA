/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.utility.profile;
import org.apache.log4j.Logger;
/**
 * Store execution time information for a task. This TaskInfo class was inspired from the Spring StopWatch.TaskInfo class.
 * 
 * This class is only used when profiling is turned on. The class is not intended to be included within a deployed production
 * system.
 * 
 * @see org.springframework.util.StopWatch.TaskInfo
 */
public class TaskInfo {
    private static final Logger LOG = Logger.getLogger(TaskInfo.class);
    private String name = null;
    private long start = 0;
    private long total = 0;
    private long count = 0;
    private boolean running = false;
    private boolean paused = false;
    /**
     * Create a new task with the given name. The name is used as a key for all tasks, therefore it must be unique.
     * 
     * @param taskName String name for this task
     */
    public TaskInfo(String taskName) {
        this.name = taskName;
    }
    /**
     * Set the start time to the given milliseconds.
     * 
     * @param milliseconds long time of start
     */
    public void start(long milliseconds) {
        if (running || paused) {
            LOG.trace("Can't start task " + name + ": it's already running. Try resuming the task!");
        }
        else {
            this.start = milliseconds;
            this.count++;
            this.running = true;
            this.paused = false;
        }
    }
    /**
     * Resume this task from a paused state.
     * 
     * @param milliseconds long time of resume
     */
    public void resume(long milliseconds) {
        if (running) {
            LOG.trace("Can't resume task " + name + ": it's already running. Try pausing or stopping the task!");
        }
        else {
            this.start = milliseconds;
            this.running = true;
            this.paused = false;
        }
    }
    /**
     * Add to the total time the elapsed time since starting.
     * 
     * @param milliseconds long time of stop
     */
    public void stop(long milliseconds) {
        if (running) {
            this.total += milliseconds - start;
            this.running = false;
            this.paused = false;
        }
        else {
            LOG.trace("Can't stop task " + name + ": it's not running. Try starting the task!");
        }
    }
    /**
     * Pause this task from a running state.
     * 
     * @param milliseconds long time of pause
     */
    public void pause(long milliseconds) {
        if (running) {
            this.total += milliseconds - start;
            this.running = false;
            this.paused = true;
        }
        else {
            LOG.trace("Can't pause task " + name + ": it's not running. Try starting the task!");
        }
    }
    /**
     * Return the name of this task.
     * 
     * @return String name for this task
     */
    public String getName() {
        return name;
    }
    /**
     * Return the time in seconds this task took.
     * 
     * @return double total time in seconds
     */
    public double getTotalTimeSeconds() {
        return total / 1000.0;
    }
    /**
     * Time in milliseconds executions of this task took.
     * 
     * @return total property
     */
    public long getTotal() {
        return total;
    }
    /**
     * The number of times this task was timed.
     * 
     * @return iterations property
     */
    public long getCount() {
        return count;
    }
    /**
     * Print the total time elapsed for this task.
     * 
     * @return String total time elapsed for this task
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name + " executed in " + total + " milliseconds";
    }
    /**
     * 
     * @return running property
     */
    public boolean isRunning() {
        return running;
    }
    /**
     * 
     * @return paused property
     */
    public boolean isPaused() {
        return paused;
    }
