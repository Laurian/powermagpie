package uk.ac.open.powermagpie.context;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.open.kmi.watson.clientapi.EntityResult;
import uk.ac.open.kmi.watson.clientapi.SemanticContentResult;
import uk.ac.open.powermagpie.search.Factory;
import uk.ac.open.powermagpie.util.LabelSplitter;
import uk.ac.open.powermagpie.util.NameSpace;
/**
 *
 * @author Laurian Gridinoc
 */
public class Term {

    public static final String DELIM = " \t\n\r\f!@£$%^&*()_+±§-=[]{};'\\:\"|`~,./<>?#";
    public static final String STOP  = "|a|a's|able|about|about|above|above|according|accordingly|across|across|actually|after|after|afterwards|afterwards|again|again|against|against|ain't|all|all|allow|allows|almost|almost|alone|alone|along|along|already|already|also|also|although|although|always|always|am|am|among|among|amongst|amongst|amoungst|amount|an|an|and|and|another|another|any|any|anybody|anyhow|anyhow|anyone|anyone|anything|anything|anyway|anyway|anyways|anywhere|anywhere|apart|appear|appreciate|appropriate|are|are|aren't|around|around|as|as|aside|ask|asking|associated|at|at|available|away|awfully|b|back|be|be|became|became|because|because|become|become|becomes|becomes|becoming|becoming|been|been|before|before|beforehand|beforehand|behind|behind|being|being|believe|below|below|beside|beside|besides|besides|best|better|between|between|beyond|beyond|bill|both|both|bottom|brief|but|but|by|by|c|c'mon|c's|call|came|can|can|can't|cannot|cannot|cant|cant|cause|causes|certain|certainly|changes|clearly|co|co|com|come|comes|computer|con|concerning|consequently|consider|considering|contain|containing|contains|corresponding|could|could|couldn't|couldnt|course|cry|currently|d|de|definitely|describe|described|despite|detail|did|didn't|different|do|do|does|doesn't|doing|don't|done|done|down|down|downwards|due|during|during|e|each|each|edu|eg|eg|eight|eight|either|either|eleven|else|else|elsewhere|elsewhere|empty|enough|enough|entirely|especially|et|etc|etc|even|even|ever|ever|every|every|everybody|everyone|everyone|everything|everything|everywhere|everywhere|ex|exactly|example|except|except|f|far|few|few|fifteen|fifth|fify|fill|find|fire|first|first|five|five|followed|following|follows|for|for|former|former|formerly|formerly|forth|forty|found|four|four|from|from|front|full|further|further|furthermore|g|get|get|gets|getting|give|given|gives|go|go|goes|going|gone|got|gotten|greetings|h|had|had|hadn't|happens|hardly|has|has|hasn't|hasnt|have|have|haven't|having|he|he|he's|hello|help|hence|hence|her|her|here|here|here's|hereafter|hereafter|hereby|hereby|herein|herein|hereupon|hereupon|hers|hers|herself|herself|hi|him|him|himself|himself|his|his|hither|hopefully|how|how|howbeit|however|however|hundred|i|i|i'd|i'll|i'm|i've|ie|ie|if|if|ignored|immediate|in|in|inasmuch|inc|inc|indeed|indeed|indicate|indicated|indicates|inner|insofar|instead|interest|into|into|inward|is|is|isn't|it|it|it'd|it'll|it's|its|its|itself|itself|j|just|k|keep|keep|keeps|kept|know|known|knows|l|last|last|lately|later|latter|latter|latterly|latterly|least|least|less|less|lest|let|let's|like|liked|likely|little|look|looking|looks|ltd|ltd|m|made|mainly|many|many|may|may|maybe|me|me|mean|meanwhile|meanwhile|merely|might|might|mill|mine|more|more|moreover|moreover|most|most|mostly|mostly|move|much|much|must|must|my|my|myself|myself|n|name|name|namely|namely|nd|near|nearly|necessary|need|needs|neither|neither|never|never|nevertheless|nevertheless|new|next|next|nine|nine|no|no|nobody|nobody|non|none|none|noone|noone|nor|nor|normally|not|not|nothing|nothing|novel|now|now|nowhere|nowhere|o|obviously|of|of|off|off|often|often|oh|ok|okay|old|on|on|once|once|one|one|ones|only|only|onto|onto|or|or|other|other|others|others|otherwise|otherwise|ought|our|our|ours|ours|ourselves|ourselves|out|out|outside|over|over|overall|own|own|p|part|particular|particularly|per|per|perhaps|perhaps|placed|please|please|plus|possible|presumably|probably|provides|put|q|que|quite|qv|r|rather|rather|rd|re|re|really|reasonably|regarding|regardless|regards|relatively|respectively|right|s|said|same|same|saw|say|saying|says|second|secondly|see|see|seeing|seem|seem|seemed|seemed|seeming|seeming|seems|seems|seen|self|selves|sensible|sent|serious|serious|seriously|seven|several|several|shall|she|she|should|should|shouldn't|show|side|since|since|sincere|six|six|sixty|so|so|some|some|somebody|somehow|somehow|someone|someone|something|something|sometime|sometime|sometimes|sometimes|somewhat|somewhere|somewhere|soon|sorry|specified|specify|specifying|still|still|sub|such|such|sup|sure|system|t|t's|take|take|taken|tell|ten|tends|th|than|than|thank|thanks|thanx|that|that|that's|thats|the|the|their|their|theirs|them|them|themselves|themselves|then|then|thence|thence|there|there|there's|thereafter|thereafter|thereby|thereby|therefore|therefore|therein|therein|theres|thereupon|thereupon|these|these|they|they|they'd|they'll|they're|they've|thick|thin|think|third|third|this|this|thorough|thoroughly|those|those|though|though|three|three|through|through|throughout|throughout|thru|thru|thus|thus|to|to|together|together|too|too|took|top|toward|toward|towards|towards|tried|tries|truly|try|trying|twelve|twenty|twice|two|two|u|un|un|under|under|unfortunately|unless|unlikely|until|until|unto|up|up|upon|upon|us|us|use|used|useful|uses|using|usually|uucp|v|value|various|very|very|via|via|viz|vs|w|want|wants|was|was|wasn't|way|we|we|we'd|we'll|we're|we've|welcome|well|well|went|were|were|weren't|what|what|what's|whatever|whatever|when|when|whence|whence|whenever|whenever|where|where|where's|whereafter|whereafter|whereas|whereas|whereby|whereby|wherein|wherein|whereupon|whereupon|wherever|wherever|whether|whether|which|which|while|while|whither|whither|who|who|who's|whoever|whoever|whole|whole|whom|whom|whose|whose|why|why|will|will|willing|wish|with|with|within|within|without|without|won't|wonder|would|would|wouldn't|x|y|yes|yet|yet|you|you|you'd|you'll|you're|you've|your|your|yours|yours|yourself|yourself|yourselves|yourselves|z|zero|";

    private String lexical;

    private Vector<Match> matches;

    public Term(String lexical) {
        this.lexical = lexical;
        matches = new Vector<Match>();
        process();
    }

    private void process() {
        System.out.println("process()");
        try {
            SemanticContentResult[] sr = Factory.instance().getWatson().searchWatsonWithKeywordsWithLimit(lexical, 50);
            processResult(sr);
        } catch (RemoteException ex) {
            Logger.getLogger(Term.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void processResult(SemanticContentResult[] sr) {
        System.out.println("processResult()");
        for (SemanticContentResult r : sr) {
            EntityResult[] er = r.getEntityResultList();

            for (EntityResult e : er) {
                validateEntity(r, e);
            }
        }
    }

    private void processMatch(String normal, EntityResult ntt, SemanticContentResult sc, StringBuffer context) {

        System.out.println(">>>" + ntt.getType() + "::" + ntt.getURI() + "(" + ntt.getLabel() + ")");

        LabelSplitter ls = new LabelSplitter();

        String[][] relFrom = ntt.getRelationFrom();
        if (relFrom != null) {
            for (String[] rf : relFrom) {
                context.append(' ').append(ls.splitLabel(NameSpace.splitNamespace(rf[1])[1]));
                System.out.println("\t\t" + rf[1] + " -f-> " + rf[2]);
            }
        }
        String[][] relTo = ntt.getRelationTo();
        if (relTo != null) {
            for (String[] rt : relTo) {
                context.append(' ').append(ls.splitLabel(NameSpace.splitNamespace(rt[2])[1]));
                System.out.println("\t\t" + rt[2] + " -t-> " + rt[1]);
            }
        }

        System.out.println("**************************");
        System.out.println(uniq(context.toString()));
        System.out.println("**************************");

        String c = uniq(context.toString());
        if (c.length() > 2) matches.add(new Match(sc, ntt, c));
    }

    private void validateEntity(SemanticContentResult sc, EntityResult ntt) {

        StringBuffer context = new StringBuffer();

        String[][] literals = ntt.getLiterals();
        if (literals != null) {
            for (String[] l : literals) {
                context.append(' ').append(l[2]);
                if (lexical.indexOf(normal(l[2])) != -1) {
                    System.out.println("Match: (literal) " + l[2]);
                    processMatch(normal(l[2]), ntt, sc, context);
                    return;
                }
            }
        }

        if (lexical.indexOf(normal(ntt.getLabel())) != -1) {
            context.append(' ').append(ntt.getLabel());
            System.out.println("Match: (label) " + ntt.getLabel());
            processMatch(normal(ntt.getLabel()), ntt, sc, context);
            return;
        }

        LabelSplitter ls = new LabelSplitter();
        String name = ls.splitLabel(NameSpace.splitNamespace(ntt.getURI())[1]);
        context.append(' ').append(name);
        if (lexical.indexOf(normal(name)) != -1) {
            System.out.println("Match: (name) " + name);
            processMatch(normal(name), ntt, sc, context);
        }

    }

    private String normal(String text) {
        if (text == null) {
            text = "NULL";
        }
        return text.trim().toLowerCase();
    }

    public static boolean isStopWord(String lexical) {
        if (lexical.length() < 3) return true;
        return (STOP.indexOf("|"+lexical+"|") != -1);
    }

    private String uniq(String in) {
        HashMap<String, String> tokens = new HashMap<String, String>();

        StringTokenizer st = new StringTokenizer(in, DELIM);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isStopWord(token.toLowerCase())) continue;
            if (!tokens.containsKey(token.toLowerCase())) {
                tokens.put(token.toLowerCase(), token);
            }
        }

        StringBuffer out = new StringBuffer();
        Iterator<String> it = tokens.keySet().iterator();
        while (it.hasNext()) {
            out.append(tokens.get(it.next())).append(' ');
        }

        return out.toString().trim();
    }

    public String lexical() {
        return lexical;
    }

    public Vector<Match> matches() {
        return matches;
    }

}
