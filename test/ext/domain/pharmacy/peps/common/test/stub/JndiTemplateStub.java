package EXT.DOMAIN.pharmacy.peps.common.test.stub;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jndi.JndiTemplate;
public class JndiTemplateStub extends JndiTemplate {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(JndiTemplateStub.class);
    
    private Map<String, Object> values = new HashMap<String, Object>();
    
    @Override
    public void bind(String name, Object object) {
        LOG.debug("binding " + name + ": " + object);
        if(values.containsKey(name)) {
            throw new RuntimeException("context already contains key: " + name);
        }
        
        values.put(name, object);
    }
    
    @Override
    public void rebind(String name, Object object) {
        LOG.debug("rebinding " + name + ": " + object);
        
        if(!values.containsKey(name)) {
            throw new RuntimeException("context does not contain key: " + name);
        }
        
        values.put(name, object);
    }
    
    @Override
    public void unbind(String name) {
        LOG.debug("unbinding " + name);
        
        if(!values.containsKey(name)) {
            throw new RuntimeException("context does not contain key: " + name);
        }
        
        values.remove(name);
    }
    
    @Override
    public Object lookup(String name) {
        LOG.debug("looking up " + name);
        
        if(!values.containsKey(name)) {
            throw new RuntimeException("key not found: " + name);
        }
        
        return values.get(name);
    }
