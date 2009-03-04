/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.context;

import java.util.Vector;
import uk.ac.open.kmi.watson.clientapi.EntityResult;
import uk.ac.open.kmi.watson.clientapi.SemanticContentResult;

/**
 *
 * @author lg3388
 */
public class Term {

    private String lexical;
    private boolean OpenK;

    public Vector<Match> matches;

    public Term(String lexical, boolean OpenK) {
        this.lexical = lexical;
        this.OpenK = OpenK;
        //test
        matches = new Vector<Match>();
        matches.add(new Match(new SemanticContentResult(), new EntityResult(), "foo"));
    }

    public String getLexical() {
        return lexical;
    }

    public Vector<Match> getMatches() {
        return matches;
    }

    public void setMatches(Vector<Match> matches) {
        this.matches = matches;
    }
}
