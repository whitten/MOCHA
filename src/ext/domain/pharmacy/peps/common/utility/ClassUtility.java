/**
 * Copyright 2008, Southwest Research Institute
 */
package EXT.DOMAIN.pharmacy.peps.common.utility;
import java.lang.reflect.ParameterizedType;
import org.springframework.util.StringUtils;
/**
 * Methods performed on classes
 */
public class ClassUtility {
    /**
     * Get the generic type declared at the first index (0).
     * 
     * @param clazz Subclass of a generic typed class with declared types (e.g., MyClass<AnotherClass>)
     * @return Class type at the first index (0)
     * @see #getGenericType(Object, int)
     */
    public static Class<?> getGenericType(Class<?> clazz) {
        return getGenericType(clazz, 0);
    }
    /**
     * Get the generic type declared in a subclass, at the given index.
     * 
     * This assumes that the class instance is a subclass of a typed superclass. The subclass must declare a type at the
     * given index.
     * 
     * @param clazz Subclass of a generic typed superclass with declared types (e.g., MyClass<AnotherClass>)
     * @param index of the type to get (starts at zero -- a class can have multiple types e.g., MyClass<A, B, C>)
     * @return Class type at the given index
     */
    public static Class<?> getGenericType(Class<?> clazz, int index) {
        Class<?> c = clazz;
        while (!(c.getGenericSuperclass() instanceof ParameterizedType)) {
            c = c.getSuperclass();
        }
        return (Class<?>) ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()[index];
    }
    /**
     * Return just the class name without the package.
     * 
     * @param clazz Class to get the name of
     * @return String of class name
     */
    public static String getClassName(Class<?> clazz) {
        return StringUtils.unqualify(clazz.getName());
    }
    /**
     * Get the Spring bean ID based upon the name of the given Class. This first calls {@link #getClassName(Class)} and then
     * returns the ID as the class name in initialCamelCase.
     * 
     * @param clazz Class to get Spring bean ID
     * @return String Spring bean ID
     */
    public static String getSpringBeanId(Class<?> clazz) {
        return StringUtils.uncapitalize(getClassName(clazz));
    }
    /**
     * Cannot instantiate
     */
    private ClassUtility() {
        super();
    }
