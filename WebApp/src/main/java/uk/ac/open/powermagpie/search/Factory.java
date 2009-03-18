/*
 * Factory.java
 * 
 * Created on Aug 21, 2007, 10:55:57 AM
 *
 */

package uk.ac.open.powermagpie.search;

import com.tek271.memoize.RememberFactory;
import com.tek271.memoize.cache.ICacheFactory;
import net.sf.ehcache.CacheManager;
import uk.ac.open.powermagpie.cache.CacheFactory;

/**
 *
 * @author Laurian Gridinoc
 */
public class Factory {

    public Yahoo yahoo;
    public Watson watson;
    
    private static Factory instance = null;
    private ICacheFactory cacheFactory;
    
    private Factory() {
        System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
        CacheManager.create();
        cacheFactory = new CacheFactory(CacheFactory.EHCACHE);
    }
    
    public static synchronized Factory instance() {
        if (instance == null) instance = new Factory();
        return instance;
    }
    
    public Yahoo getYahoo() {
        if (yahoo == null) yahoo = (Yahoo) RememberFactory.createProxy(Yahoo.class, cacheFactory);
        return yahoo;
    }
    
    public Watson getWatson() {
        if (watson == null) watson = (Watson) RememberFactory.createProxy(Watson.class, cacheFactory);
        return watson;
    }
    
}
