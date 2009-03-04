/*
 * NameSpace.java
 *
 * Created on Nov 5, 2007, 10:04:34 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.util;

import com.sun.org.apache.xerces.internal.util.XMLChar;

/**
 *
 * @author lg3388
 */
public class NameSpace {

    public static String[] splitNamespace(String uri) {
        String ns;
        String localName;
        ns = uri;
        int i = uri.lastIndexOf('#');
        if (i == -1) {
            i = uri.lastIndexOf('/');
        }
        if (i != -1) {
            ns = uri.substring(0, i + 1);
        } else {
            ns = uri.substring(0, canonicalSplitNamespace(uri));
        }
        localName = uri.substring(ns.length());
        return new String[]{ns, localName};
    }

    /**
     * From: com.hp.hpl.jena.rdf.model.impl.Util
     * Given an absolute URI, determine the split point between the namespace part
     * and the localname part.
     * If there is no valid localname part then the length of the
     * string is returned.
     * The algorithm tries to find the longest NCName at the end
     * of the uri, not immediately preceeded by the first colon
     * in the string.
     * @param uri
     * @return the index of the first character of the localname
     */
    public static int canonicalSplitNamespace(String uri) {
        char ch;
        int lg = uri.length();
        if (lg == 0) {
            return 0;
        }
        int j;
        int i;
        for (i = lg - 1; i >= 1; i--) {
            ch = uri.charAt(i);
            if (notNameChar(ch)) {
                break;
            }
        }
        for (j = i + 1; j < lg; j++) {
            ch = uri.charAt(j);
            if (XMLChar.isNCNameStart(ch)) {
                if (uri.charAt(j - 1) == ':' && uri.lastIndexOf(':', j - 2) == -1) {
                    continue; // split "mailto:me" as "mailto:m" and "e" !
                } else {
                    break;
                }
            }
        }
        return j;
    }

    /**
     * From: com.hp.hpl.jena.rdf.model.impl.Util
     * answer true iff this is not a legal NCName character, ie, is
     * a possible split-point start.
     */
    public static boolean notNameChar(char ch) {
        return !XMLChar.isNCName(ch);
    }
}