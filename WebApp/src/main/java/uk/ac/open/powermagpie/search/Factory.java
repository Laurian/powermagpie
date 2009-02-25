/*
 * Factory.java
 * 
 * Created on Aug 21, 2007, 10:55:57 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.search;

import com.tek271.memoize.RememberFactory;
import com.tek271.memoize.cache.ICacheFactory;

/**
 *
 * @author lg3388
 */
public class Factory {

    public Yahoo yahoo;
    public Watson watson;
    
    private static Factory instance = null;
    //private ICacheFactory cacheFactory;
    
    private Factory() {
    }
    
    public static synchronized Factory instance() {
        if (instance == null) instance = new Factory();
        return instance;
    }
    
    public Yahoo getYahoo() {
        if (yahoo == null) yahoo = (Yahoo) RememberFactory.createProxy(Yahoo.class);//, cacheFactory);
        return yahoo;
    }
    
    public Watson getWatson() {
        if (watson == null) watson = (Watson) RememberFactory.createProxy(Watson.class);//, cacheFactory);
        return watson;
    }
    
}
