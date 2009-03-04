package com.yahoo.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.*;

// TODO: Document.
// TODO: Figure out what to do about XML that doesn't conform to the schema.

public class XmlParser extends DefaultHandler {
    private Stack stack;
    private Map root = null;
    private Stack typeStack;

    public static final Object get(Map map, String path) {
        StringTokenizer tok = new StringTokenizer(path.substring(1), "/");
        Object obj = map;

        while((obj != null) && tok.hasMoreTokens()) {
            if (obj != null) {
                obj = ((Map)obj).get(tok.nextToken());
            }
        }

        return obj;
    }

    public static String getString(Map map, String path) {
        return (String) get(map, path);
    }

    public static List getList(Map map, String path) {
        Object item = get(map, path);

        if((item != null) && !(item instanceof List)) {
            List container = new ArrayList();
            container.add(item);
            item = container;
        }

        return (List) item;
    }

    public static BigInteger getBigInteger(Map map, String path) {
        String asString = getString(map, path);
        return (asString != null) ? new BigInteger(asString, 10) : null;
    }

    public static BigDecimal getBigDecimal(Map map, String path) {
        String asString = getString(map, path);
        if(asString.equalsIgnoreCase("NaN")) {
            asString = null;
        }

        return (asString != null) ? new BigDecimal(asString) : null;
    }

    public static Boolean getBoolean(Map map, String path) {
        String asString = getString(map, path);
        return (asString != null) ? Boolean.valueOf(asString) : null;
    }

    public void startDocument() throws SAXException {
        stack = new Stack();
        stack.push(new HashMap());

        typeStack = new Stack();
    }

    public void endDocument() throws SAXException {
        if (stack.size() != 1) {
            throw new SAXException("Parse error, stack size greater than 1");
        }
        root = (Map) stack.pop();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String parentPath;
        if(typeStack.size() > 0) {
            parentPath = (String) typeStack.peek();
        }
        else {
            parentPath = "";
        }
        typeStack.push(parentPath + "/" + qName);

        Map newMap = new HashMap();
        for (int i = 0; i < attributes.getLength(); i++) {
            newMap.put(attributes.getQName(i), attributes.getValue(i));
        }

        Map top = (Map) stack.peek();
        Object obj = top.get(qName);
        if (obj == null) {
            top.put(qName, newMap);
        }
        else if (obj instanceof Map) {
            List newList = new LinkedList();
            newList.add(obj);
            newList.add(newMap);
            top.put(qName, newList);
        }
        else if (obj instanceof List) {
            ((List) obj).add(newMap);
        }

        stack.push(newMap);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        stack.pop();
        typeStack.pop();
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        String current = (String) ((Map) stack.peek()).get("value");
        if(current != null) {
            current += new String(ch, start, length);
        }
        else {
            current = new String(ch, start, length);
        }

        ((Map) stack.peek()).put("value", current);
    }

    public Map getRoot() {
        return root;
    }
}