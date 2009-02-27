package uk.ac.open.powermagpie;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
import uk.ac.open.kmi.watson.clientapi.EntityResult;
import uk.ac.open.kmi.watson.clientapi.SemanticContentResult;
import uk.ac.open.powermagpie.context.*;
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

    public void process(String text) {
        context.process(text);
    }

    public int add(String term) {
        return context.add(term).matches().size();
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
            if (t.matches().size() > 0) terms.add(t.lexical());
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
}
