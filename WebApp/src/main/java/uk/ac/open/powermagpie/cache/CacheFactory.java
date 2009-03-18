/*
 * CacheFactory.java
 * 
 * Created on Aug 21, 2007, 11:21:49 AM
 * 
 */

package uk.ac.open.powermagpie.cache;

import com.tek271.memoize.cache.ICache;
import com.tek271.memoize.cache.ICacheFactory;
import com.tek271.memoize.cache.TimeUnitEnum;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 *
 * @author Laurian Gridinoc
 */
public class CacheFactory implements ICacheFactory {
    
    public static final int EHCACHE = 0;
    public static final int JDBM = 1;
    
    private int type = 0;
    
    public CacheFactory(int type) {
        this.type = type;
    }

    public ICache getCache (String name, int maxSize, long timeToLive, TimeUnitEnum timeUnit) {
        if (type == EHCACHE) return new EhcacheAdapter(name, maxSize, timeToLive, timeUnit);
        //if (type == JDBM) return new JdbmAdapter(name, maxSize, timeToLive, timeUnit);
        return null;
    }

    public ICache getCache (String name) {
        if (type == EHCACHE) return new EhcacheAdapter(name);
        //if (type == JDBM) return new JdbmAdapter(name);
        return null;
    }

    public void clear () {
        CacheManager.getInstance().removalAll();
    }

    public void remove (String name) {
        CacheManager.getInstance().removeCache(name);
    }

    public void removeExpired () {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
