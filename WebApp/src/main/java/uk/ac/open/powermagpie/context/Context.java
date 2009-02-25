package uk.ac.open.powermagpie.context;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.OutputStream;
import java.util.HashMap;
import uk.ac.open.powermagpie.search.Factory;

/**
 *
 * @author Laurian Gridinoc
 */
public class Context {

    private String uri;
    private StringBuffer text;

    private Model model;

    private HashMap<String, Term> terms;

    public Context() {
        text = new StringBuffer();
        terms = new HashMap<String, Term>();
        model = ModelFactory.createDefaultModel();
    }

    public Context(String uri) {
        this();
        this.uri = uri;
    }

    public void process(String text) {
        this.text.append(' ').append(text);
        Factory.instance().getYahoo().terms(this, text);
    }

    public Term add(String term) {
        Term t = new Term(term);
        terms.put(term, t);
        return t;
    }

    public void rdf(OutputStream out) {
        model.write(out, "RDF/XML");
    }

    public HashMap<String, Term> terms() {
        return terms;
    }

}
