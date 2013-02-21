/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.utility.profile;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import org.apache.log4j.Logger;
/**
 * Profile the execution time of various named tasks. This StopWatch class was inspired from the Spring StopWatch class.
 * 
 * This class is only used when profiling is turned on. The class is not intended to be included within a deployed production
 * system.
 * 
 * @see org.springframework.util.StopWatch
 */
public class StopWatch {
    private static final Logger LOG = Logger.getLogger(StopWatch.class);
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private long start = 0;
    private long total = 0;
    private Map<String, TaskInfo> tasks = new LinkedHashMap<String, TaskInfo>();
    // top most task name is the running task
    private Stack<String> taskNames = new Stack<String>();
    /**
     * Start the timing of a task associated with the given name. If a task with the given name already exists, start a new
     * iteration of that task.
     * 
     * @param taskName String name for the task
     */
    public void start(String taskName) {
        long currentTime = System.currentTimeMillis();
        if (!tasks.containsKey(taskName)) {
            tasks.put(taskName, new TaskInfo(taskName));
        }
        // if this is the first time starting the StopWatch, set the start time for all tasks
        if (start == 0) {
            this.start = currentTime;
        }
        // pause the current running task
        if (!taskNames.isEmpty()) {
            tasks.get(taskNames.peek()).pause(currentTime);
        }
        tasks.get(taskName).start(currentTime);
        // push the current running task
        taskNames.push(taskName);
    }
    /**
     * Stop the timing of a task associated with the given name. If the task already existed prior to this execution, add the
     * time this iteration took to the total time.
     * 
     * @param taskName String name for the task
     */
    public void stop(String taskName) {
        long currentTime = System.currentTimeMillis();
        // reset the total time for all tasks
        this.total = currentTime - start;
        if (tasks.containsKey(taskName)) {
            if (!taskNames.isEmpty()) {
                taskNames.pop(); // top one should be this task, second in line is the previous task
                if (!taskNames.isEmpty()) { // this could have been the top most method call
                    tasks.get(taskNames.peek()).resume(currentTime);
                }
            }
            tasks.get(taskName).stop(currentTime);
        }
        else {
            LOG.warn("Unable to find task " + taskName + " to stop profile stop watch");
        }
    }
    /**
     * Return the total time in milliseconds for all tasks.
     * 
     * @return long total time in milliseconds
     */
    public long getTotal() {
        return total;
    }
    /**
     * Set the start and totals values to zero (0) and set the Map of TaskInfo instances to a new LinkedHashMap.
     */
    public void reset() {
        this.start = 0;
        this.total = 0;
        this.tasks = new LinkedHashMap<String, TaskInfo>();
        this.taskNames = new Stack<String>();
    }
    /**
     * Return a string with a table describing all tasks performed. For custom reporting, call getTaskInfo() and use the task
     * info directly.
     * 
     * After printing, the information will be reset.
     * 
     * @param totalName String name of the method being profiled for its total execution time
     * @return String formatted profile of execution time
     */
    public String prettyPrint(String totalName) {
        StringBuffer sb = new StringBuffer();
        sb.append(LINE_SEPARATOR);
        sb.append(getTotal());
        sb.append(" milliseconds total execution time for ");
        sb.append(totalName);
        sb.append(LINE_SEPARATOR);
        sb.append("-------------------------------------------------");
        sb.append(LINE_SEPARATOR);
        sb.append("ms     %     Count  Task name");
        sb.append(LINE_SEPARATOR);
        sb.append("-------------------------------------------------");
        sb.append(LINE_SEPARATOR);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumIntegerDigits(5);
        nf.setGroupingUsed(false);
        NumberFormat pf = NumberFormat.getPercentInstance();
        pf.setMinimumIntegerDigits(3);
        pf.setGroupingUsed(false);
        for (String taskKey : tasks.keySet()) {
            TaskInfo task = tasks.get(taskKey);
            sb.append(nf.format(task.getTotal()));
            sb.append("  ");
            if (getTotal() == 0) {
                sb.append(pf.format(1.0));
            }
            else {
                sb.append(pf.format(1.0 * task.getTotal() / getTotal()));
            }
            sb.append("  ");
            sb.append(nf.format(task.getCount()));
            sb.append("  ");
            sb.append(task.getName());
            sb.append(LINE_SEPARATOR);
        }
        reset();
        return sb.toString();
    }
