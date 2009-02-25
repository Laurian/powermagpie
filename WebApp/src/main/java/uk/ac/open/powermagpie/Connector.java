package uk.ac.open.powermagpie;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
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
}
