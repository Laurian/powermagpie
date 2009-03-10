package uk.ac.open.powermagpie;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import uk.ac.open.kmi.watson.clientapi.EntityResult;
import uk.ac.open.kmi.watson.clientapi.SemanticContentResult;
import uk.ac.open.powermagpie.context.*;
import uk.ac.open.powermagpie.search.Factory;
import uk.ac.open.powermagpie.util.NameSpace;

/**
 *
 * @author Laurian Gridinoc
 */
public class Connector {

    Context context;

    public Connector() {
        context = new Context("tag:powermagpie.open.ac.uk,2009:test");
    }

    public boolean ping(String session) {
        return true;
    }

    public void process(String text) {
        log("processing input text ...");
        System.out.println("processing " + text);
        context.process(text);
        send("*", "term:");
    }

    public int add(String term) {
        int i = context.add(term).matches().size();
        if (i > 0) send("*", "term:" + term.trim());
        return i;
    }

    public Vector<String> terms() {
        Vector<String> v = new Vector<String>();
        v.addAll(context.terms().keySet());
        return v;
    }

    public Vector<String> matches(String term) {
        Vector<String> v = new Vector<String>();

        Iterator<Match> i = context.terms().get(term).matches().iterator();
        while(i.hasNext()) {
            Match m = i.next();
            v.add(m.getEntityResult().getURI());
        }

        return v;
    }

    public String[] getEntities() {
        //actually covered terms
        //Collections.sort(Terms);
        Vector<String> terms = new Vector<String>();

        Iterator<Entry<String, Term>> it  = context.terms().entrySet().iterator();
        while(it.hasNext()) {
            Term t = it.next().getValue();
            //if (t.matches().size() > 0)
            terms.add(t.lexical());
        }
        return (String[]) terms.toArray(new String[]{});
    }

    public HashMap expandTerm(String name) {
        Term term = context.terms().get(name);
        Vector<Match> v = term.matches();

        HashMap<String, HashMap<String, String>> h = new HashMap<String, HashMap<String, String>>(v.size());

        //Collections.sort(v);

        Iterator<Match> it = v.iterator();
        while (it.hasNext()) {
            Match e = it.next();
            HashMap<String, String> attr = new HashMap<String, String>();
            attr.put("type", e.getEntityResult().getType());
            attr.put("localName", NameSpace.splitNamespace(e.getEntityResult().getURI())[1]);
            attr.put("URI", e.getEntityResult().getURI());
            h.put(e.getEntityResult().getURI(), attr);
        }

        return h;
    }

    private Vector<SemanticContentResult> getOntologiesForEntity(String uri) {
        Vector<SemanticContentResult> v = new Vector<SemanticContentResult>();
        Iterator<Entry<String, Term>> it  = context.terms().entrySet().iterator();

        while(it.hasNext()) {
            Term t = it.next().getValue();
            Iterator<Match> mi = t.matches().iterator();
            while(mi.hasNext()) {
                Match m = mi.next();
                if (m.getEntityResult().getURI().equals(uri)) {
                    v.add(m.getSemanticContentResult());
                }
            }
        }

        return v;
    }

    public HashMap expandEntity(String uri) {
        Vector<SemanticContentResult> v = getOntologiesForEntity(uri);

        HashMap<String, HashMap<String, String>> h = new HashMap<String, HashMap<String, String>>(v.size());

        //Collections.sort(v);

        Iterator<SemanticContentResult> it = v.iterator();
        while (it.hasNext()) {
            SemanticContentResult o = it.next();
            HashMap<String, String> attr = new HashMap<String, String>();
            attr.put("URI", o.getURI());
            h.put(o.getURI(), attr);
        }

        return h;
    }

    public String[] getOntologies() {
        Vector<Match> v = new Vector<Match>();
        Iterator<Entry<String, Term>> it = context.terms().entrySet().iterator();
        while(it.hasNext()) {
            v.addAll(it.next().getValue().matches());
        }

        //Collections.sort(v);

        String[] uris = new String[v.size()];

        Iterator<Match> it2 = v.iterator();
        int i = 0;
        while(it2.hasNext()) uris[i++] = it2.next().getSemanticContentResult().getURI();

        return uris;
    }

    private SemanticContentResult getOntology(String uri) {
        Vector<Match> v = new Vector<Match>();
        Iterator<Entry<String, Term>> it = context.terms().entrySet().iterator();
        while(it.hasNext()) {
            v.addAll(it.next().getValue().matches());
        }

        //Collections.sort(v);


        Iterator<Match> it2 = v.iterator();
        int i = 0;
        while(it2.hasNext()) {
            SemanticContentResult o = it2.next().getSemanticContentResult();
            if (o.getURI().equals(uri))
                return o;
        }

        return null;
    }

    private Vector<EntityResult> getEntities(String uri) {
        Vector<EntityResult> v = new Vector<EntityResult>();
        Iterator<Entry<String, Term>> it  = context.terms().entrySet().iterator();

        while(it.hasNext()) {
            Term t = it.next().getValue();
            Iterator<Match> mi = t.matches().iterator();
            while(mi.hasNext()) {
                Match m = mi.next();
                if (m.getSemanticContentResult().getURI().equals(uri)) {
                    v.add(m.getEntityResult());
                }
            }
        }

        return v;
    }

    public HashMap expandOnt(String uri) {
        
        Vector<EntityResult> v = getEntities(uri);

        HashMap<String, HashMap<String, String>> h = new HashMap<String, HashMap<String, String>>(v.size());

        //Collections.sort(v);

        Iterator<EntityResult> it = v.iterator();
        while (it.hasNext()) {
            EntityResult e = it.next();
            HashMap<String, String> attr = new HashMap<String, String>();
            attr.put("type", e.getType());
            attr.put("localName", NameSpace.splitNamespace(e.getURI())[1]);
            attr.put("URI", e.getURI());
            h.put(e.getURI(), attr);
        }

        return h;
    }

    public String[] expandOntEntity(String uri) {
        Vector<String> v = new Vector<String>();

        Iterator<Entry<String, Term>> it  = context.terms().entrySet().iterator();
        while(it.hasNext()) {
            Term t = it.next().getValue();
            Iterator<Match> mi = t.matches().iterator();
            while(mi.hasNext()) {
                Match m = mi.next();
                if (m.getEntityResult().getURI().equals(uri)) {
                    v.add(t.lexical());
                }
            }
        }

        return v.toArray(new String[]{});
    }


    /// ontology tree, TODO move to its own class, etc.
    /// get all concepts, keep only top ones.
    public HashMap _expandOntTop(String uri) {
        HashMap<String, HashMap<String, String>> h = new HashMap<String, HashMap<String, String>>();

        String[] classes = Factory.instance().getWatson().getClasses(uri);
        for(int i = 0; i < classes.length; i++) {
            String[] superClasses = Factory.instance().getWatson().getSuperClasses(uri, classes[i]);
            if (superClasses.length == 0) {
                HashMap<String, String> attr = new HashMap<String, String>();
                attr.put("type", "Class");
                attr.put("localName", NameSpace.splitNamespace(classes[i])[1]);
                attr.put("URI", classes[i]);
                attr.put("ont", uri);
                h.put(classes[i], attr);
            }
        }

        return h;
    }

    public HashMap expandOntTop(String uri) {
        HashMap<String, HashMap<String, String>> h = new HashMap<String, HashMap<String, String>>();

        String[] classes = Factory.instance().getWatson().getClasses(uri);
        Vector<String> c = new Vector<String>(classes.length);
        for(int i = 0; i < classes.length; i++) {
            c.add(classes[i]);
        }

        for(int i = 0; i < classes.length; i++) {
            String[] subClasses = Factory.instance().getWatson().getSubClasses(uri, classes[i]);
            System.out.println(classes[i] + " subclasses: " + subClasses.length);
            for(int j = 0; j < subClasses.length; j++) {
                //c.remove(subClasses[j]);
            }
        }

        for (int i = 0; i < c.size(); i++) {
                HashMap<String, String> attr = new HashMap<String, String>();
                attr.put("type", "Class");
                attr.put("localName", NameSpace.splitNamespace(c.elementAt(i))[1]);
                attr.put("URI", c.elementAt(i));
                attr.put("ont", uri);
                h.put(c.elementAt(i), attr);
        }

        return h;
    }

    public HashMap expandOntClass(String ont, String uri) {
        System.out.println("Expand " + ont + " >>> " + uri);
        HashMap<String, HashMap<String, String>> h = new HashMap<String, HashMap<String, String>>();

            String[] classes = Factory.instance().getWatson().getSubClasses(ont, uri);
            for (int i = 0; i < classes.length; i++) {
                HashMap<String, String> attr = new HashMap<String, String>();
                attr.put("type", "Class");
                attr.put("localName", NameSpace.splitNamespace(classes[i])[1]);
                attr.put("URI", classes[i]);
                attr.put("ont", ont);
                h.put(classes[i], attr);
            }

        return h;
    }

    public static void send(String to, String message) {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("message('" + to + "', '" + message + "');");
        WebContext ctx = WebContextFactory.get();
        Collection scriptSessions = ctx.getAllScriptSessions();//getScriptSessionsByPage(ctx.getCurrentPage());
        Util all = new Util(scriptSessions);
        all.addScript(script);
    }

    public void match(String to, String term, String match, String id) {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("_match('" + to + "', '" + term + "', '" + match + "', '" + id + "');");
        WebContext ctx = WebContextFactory.get();
        Collection scriptSessions = ctx.getAllScriptSessions();//getScriptSessionsByPage(ctx.getCurrentPage());
        Util all = new Util(scriptSessions);
        all.addScript(script);
    }

    public void select(String to, String term) {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("_select('" + to + "', '" + term + "');");
        WebContext ctx = WebContextFactory.get();
        Collection scriptSessions = ctx.getAllScriptSessions();//getScriptSessionsByPage(ctx.getCurrentPage());
        Util all = new Util(scriptSessions);
        all.addScript(script);
    }

    public void selectNode(String to, String term) {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("_selectNode('" + to + "', '" + term + "');");
        WebContext ctx = WebContextFactory.get();
        Collection scriptSessions = ctx.getAllScriptSessions();//getScriptSessionsByPage(ctx.getCurrentPage());
        Util all = new Util(scriptSessions);
        all.addScript(script);
    }

    public void tagged(String nid, String term, String uri, String node) {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("_tagged('" + nid + "', '" + term + "', '" + uri + "', '" + node + "');");
        WebContext ctx = WebContextFactory.get();
        Collection scriptSessions = ctx.getAllScriptSessions();//getScriptSessionsByPage(ctx.getCurrentPage());
        Util all = new Util(scriptSessions);
        all.addScript(script);
    }

    public HashMap infoEntity(String uri) {
        HashMap<String, String> h = new HashMap<String, String>();
        EntityResult e = getEntities(uri).firstElement();
        h.put("URI", uri);
        h.put("localName", NameSpace.splitNamespace(uri)[1]);
        h.put("namespace", NameSpace.splitNamespace(uri)[0]);
        h.put("type", e.getType());

        h.put("label", e.getLabel());
        h.put("comment", e.getComment());

        return h;
    }

    public HashMap infoOntology(String uri) {
        SemanticContentResult o =  getOntology(uri);

        HashMap<String, String> h = new HashMap<String, String>();

        String lang = "";
        String[] langs = o.getLanguages();
        for (int i = 0; i < langs.length; i++) {
            lang += " " + langs[i];
        }

        h.put("URI", uri);

        h.put("size", Long.toString(o.getSize()));
        h.put("DL", o.getDLExpressivness());
        h.put("lang", lang);
        h.put("C", Integer.toString(o.getNBClasses()));
        h.put("P", Integer.toString(o.getNBProperties()));
        h.put("I", Integer.toString(o.getNBIndividuals()));


        return h;
    }

    public void log(String line) {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("_log('" + line + "');");
        WebContext ctx = WebContextFactory.get();
        Collection scriptSessions = ctx.getAllScriptSessions();//getScriptSessionsByPage(ctx.getCurrentPage());
        Util all = new Util(scriptSessions);
        all.addScript(script);
    }
}
