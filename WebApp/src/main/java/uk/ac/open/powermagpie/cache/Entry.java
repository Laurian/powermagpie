/*
 * Entry.java
 * 
 * Created on Aug 27, 2007, 1:33:06 PM
 *
 */

package uk.ac.open.powermagpie.cache;

import java.io.Serializable;

/**
 *
 * @author Laurian Gridinoc
 */
public class Entry implements Serializable {

    public Object key;
    public Object value;
    
    public Entry () {}
    
    public Entry(Object key, Object value) {
        this();
        this.key = key;
        this.value = value;
    }

}
