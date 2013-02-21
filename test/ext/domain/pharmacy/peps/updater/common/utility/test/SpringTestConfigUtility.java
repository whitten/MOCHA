/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.updater.common.utility.test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import EXT.DOMAIN.pharmacy.peps.common.utility.ClassUtility;
/**
 * Provide test cases and stubs the ability to load a Spring ApplicationContext without having to know the location of the
 * Spring configuration files and having to know how to initialize Spring.
 */
public class SpringTestConfigUtility {
    private static final String[] LOCAL_ONE_SPRING_CONFIG = {"classpath*:xml/spring/datup/test/*Context.xml"};
    private static final String[] LOCAL_TWO_SPRING_CONFIG = {"classpath*:xml/spring/datup/test/*Context.xml"};
    private static final String[] NATIONAL_SPRING_CONFIG = {"xml/spring/datup/national/test/*Context.xml"};
    private static ApplicationContext LOCAL_ONE_APPLICATION_CONTEXT;
    private static ApplicationContext LOCAL_TWO_APPLICATION_CONTEXT;
    private static ApplicationContext NATIONAL_APPLICATION_CONTEXT;
    /**
     * Get an instance of a Spring managed class for the Local-1 test configuration.
     * 
     * @param <T> return type
     * @param clazz Class of the bean to get, the name of the bean must be the initialCamelCase of the Class provided
     * @return Spring managed bean pointing to LocalEPL-1 database
     */
    public static <T> T getLocalOneSpringBean(Class<T> clazz) {
        return (T) getLocalOneSpringBean(ClassUtility.getSpringBeanId(clazz));
    }
    /**
     * Get an instance of a Spring managed class for the Local-1 test configuration.
     * 
     * @param <T> return type
     * @param beanId String ID of the Spring bean to get
     * @return Spring managed bean pointing to LocalEPL-1 database
     */
    public static <T> T getLocalOneSpringBean(String beanId) {
        return (T) getLocalOneApplicationContext().getBean(beanId);
    }
    /**
     * Get an instance of a Spring managed class for the Local-2 test configuration.
     * 
     * @param <T> return type
     * @param clazz Class of the bean to get, the name of the bean must be the initialCamelCase of the Class provided
     * @return Spring managed bean pointing to LocalEPL-2 database
     */
    public static <T> T getLocalTwoSpringBean(Class<T> clazz) {
        return (T) getLocalTwoSpringBean(ClassUtility.getSpringBeanId(clazz));
    }
    /**
     * Get an instance of a Spring managed class for the Local-2 test configuration.
     * 
     * @param <T> return type
     * @param beanId String ID of the Spring bean to get
     * @return Spring managed bean pointing to LocalEPL-2 database
     */
    public static <T> T getLocalTwoSpringBean(String beanId) {
        return (T) getLocalTwoApplicationContext().getBean(beanId);
    }
    /**
     * Get an instance of a Spring managed class for the National test configuration.
     * 
     * @param <T> return type
     * @param clazz Class of the bean to get, the name of the bean must be the initialCamelCase of the Class provided
     * @return Spring managed bean
     */
    public static <T> T getNationalSpringBean(Class<T> clazz) {
        return (T) getNationalSpringBean(ClassUtility.getSpringBeanId(clazz));
    }
    /**
     * Get an instance of a Spring managed class for the National test configuration.
     * 
     * @param <T> return type
     * @param beanId String ID of the Spring bean to get
     * @return Spring managed bean
     */
    public static <T> T getNationalSpringBean(String beanId) {
        return (T) getNationalApplicationContext().getBean(beanId);
    }
    /**
     * Get the Local-1 Spring ApplicationContext. This context uses the LocalEPL-1 database.
     * 
     * @return Local's Spring ApplicationContext
     */
    public static synchronized ApplicationContext getLocalOneApplicationContext() {
        if (LOCAL_ONE_APPLICATION_CONTEXT == null) {
            LOCAL_ONE_APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(LOCAL_ONE_SPRING_CONFIG);
        }
        return LOCAL_ONE_APPLICATION_CONTEXT;
    }
    /**
     * Get the Local-2 Spring ApplicationContext. This context uses the LocalEPL-2 database.
     * 
     * @return Local's Spring ApplicationContext
     */
    public static synchronized ApplicationContext getLocalTwoApplicationContext() {
        if (LOCAL_TWO_APPLICATION_CONTEXT == null) {
            LOCAL_TWO_APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(LOCAL_TWO_SPRING_CONFIG);
        }
        return LOCAL_TWO_APPLICATION_CONTEXT;
    }
    /**
     * Get that National Spring ApplicationContext. This context uses the NationalEPL databsae.
     * 
     * @return National's Spring ApplicationContext
     */
    public static synchronized ApplicationContext getNationalApplicationContext() {
        if (NATIONAL_APPLICATION_CONTEXT == null) {
            NATIONAL_APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(NATIONAL_SPRING_CONFIG);
        }
        return NATIONAL_APPLICATION_CONTEXT;
    }
    /**
     * Cannot instantiate
     */
    private SpringTestConfigUtility() {
        super();
    }
