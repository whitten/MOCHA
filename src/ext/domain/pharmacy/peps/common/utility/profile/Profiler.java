/**
 * Copyright 2007, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.utility.profile;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
/**
 * Profile method calls
 * 
 * This class is only used when profiling is turned on. The class is not intended to be included within a deployed production
 * system.
 */
public class Profiler {
    private static final Logger LOG = Logger.getLogger(Profiler.class);
    // Using an instance of ThreadLocal to be thread safe by keeping a StopWatch instances per thread.
    private static ThreadLocal<StopWatch> THREAD_LOCAL = new ThreadLocal<StopWatch>() {
        /**
         * Provide the initial value for the thread safe ThreadLocal instance
         * 
         * @return instance of HashMap
         * 
         * @see java.lang.ThreadLocal#initialValue()
         */
        protected synchronized StopWatch initialValue() {
            return new StopWatch();
        }
    };
    /**
     * Profile the given method call.
     * 
     * @param call ProceedingJoinPoint method call to profile
     * @return Object return object for method call
     * @throws Throwable if error
     */
    public Object profile(ProceedingJoinPoint call) throws Throwable {
        try {
            getStopWatch().start(call.getSignature().toLongString());
            return call.proceed();
        }
        finally {
            getStopWatch().stop(call.getSignature().toLongString());
        }
    }
    /**
     * Print the execution time profile via Log4j.
     * 
     * @param call ProceedingJoinPoint method call to profile
     * @return Object return object for method call
     * @throws Throwable if error
     */
    public Object logProfile(ProceedingJoinPoint call) throws Throwable {
        Object result = call.proceed();
        LOG.info(getStopWatch().prettyPrint(call.getSignature().toLongString()));
        return result;
    }
    /**
     * Get this thread's Map of TaskInfo instances.
     * 
     * @return Map containing TaskInfo instances
     */
    private StopWatch getStopWatch() {
        return THREAD_LOCAL.get();
    }
