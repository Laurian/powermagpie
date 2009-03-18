/*
 * AppListener.java
 * 
 * Created on Aug 25, 2007, 12:43:30 AM
 *
 */

package uk.ac.open.powermagpie;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.sf.ehcache.CacheManager;
import uk.ac.open.powermagpie.search.Factory;

/**
 * Web application lifecycle listener.
 * @author Laurian Gridinoc
 */

public class AppListener implements ServletContextListener {

    public void contextInitialized (ServletContextEvent sce) {
        Factory.instance();
    }

    public void contextDestroyed (ServletContextEvent sce) {
        CacheManager.getInstance().shutdown();
    }
}