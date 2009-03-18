/*
 * EhcacheAdapter.java
 * 
 * Created on Aug 21, 2007, 11:46:51 AM
 * 
 */

package uk.ac.open.powermagpie.cache;

import com.tek271.memoize.cache.ICache;
import com.tek271.memoize.cache.TimeUnitEnum;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 *
 * @author Laurian Gridinoc
 */
 class EhcacheAdapter implements ICache {
     
    private String cacheName;
    private int maxSize;
    private long timeToLive;
    private TimeUnitEnum timeUnit;

    private Cache cache;
    
    public EhcacheAdapter(String cacheName, int maxSize, long timeToLive, TimeUnitEnum timeUnit) {
        this(cacheName);
        this.maxSize = maxSize;
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    EhcacheAdapter (String cacheName) {
        this.cacheName = cacheName;
        if (! CacheManager.getInstance().cacheExists(cacheName) )
            CacheManager.getInstance().addCache(cacheName);
        cache = CacheManager.getInstance().getCache(cacheName);
    }

    public long getTimeToLive () {
        return timeToLive;
    }

    public TimeUnitEnum getTimeToLiveUnit () {
        return timeUnit;
    }

    public int getMaxSize () {
        return maxSize;
    }

    public Object put (Object arg0, Object arg1) {
        Element element = new Element(arg0, arg1);
        cache.put(element);
        return arg1;
    }

    public Object get (Object arg0) {
        return cache.get(arg0).getObjectValue();
    }

    public boolean containsKey (Object arg0) {
        return cache.isKeyInCache(arg0);
    }

    public Object remove (Object arg0) {
        Object o = get(arg0);
        if (cache.remove(arg0)) return o;
        return null;
    }

    public void clear () {
        cache.removeAll();
    }

    public void removeExpired () {
    }

    public int size () {
        return cache.getSize();
    }

}
