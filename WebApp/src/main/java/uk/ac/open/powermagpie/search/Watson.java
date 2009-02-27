package uk.ac.open.powermagpie.search;

import com.tek271.memoize.Remember;
import com.tek271.memoize.cache.TimeUnitEnum;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;
import uk.ac.open.kmi.watson.clientapi.*;
import uk.ac.open.powermagpie.util.NameSpace;
import org.apache.commons.lang.StringEscapeUtils;


/**
 *
 * @author Laurian Gridinoc
 */
public class Watson {

    // code for OWL Species
    public static final int NOT_OWL = 300;
    public static final int FULL = 301;
    public static final int DL = 302;
    public static final int LITE = 4;
    // Entity modifiers for restrictions
    public static final int CLASS = 1;
    public static final int PROPERTY = 2;
    public static final int INDIVIDUAL = 4;
    // Scope modifiers for restrictions
    public static final int NS = 1;
    public static final int LOCAL_NAME = 2;
    public static final int LABEL = 4;
    public static final int COMMENT = 8;
    public static final int LITERAL = 16;
    // Matchers code (note that this has no effect for the moment...
    public static final int EXACT_MATCH = 1;
    public static final int PARTIAL_MATCH = 2;
    /** Information to be retrived in the SemanticContentResult **/
    public static final int SC_LANGUAGES_INFO = 1;
    public static final int SC_SIZE_INFO = 2;
    public static final int SC_DLEXPR_INFO = 8;
    public static final int SC_LOCATION_INFO = 16;
    public static final int SC_NBCLASSES_INFO = 32;
    public static final int SC_NBPROPS_INFO = 64;
    public static final int SC_NBINDIS_INFO = 128;
    /** Information to be retrived in the SemanticContentResult **/
    public static final int ENT_TYPE_INFO = 1;
    public static final int ENT_LABEL_INFO = 2;
    public static final int ENT_COMMENT_INFO = 4;
    public static final int ENT_ANYRELATIONFROM_INFO = 8;
    public static final int ENT_ANYRELATIONTO_INFO = 16;
    public static final int ENT_ANYLITERAL_INFO = 32;
    private OntologySearch ontologySearch;
    private EntitySearch entitySearch;
    private WatsonSearch ws;

    public Watson() throws ServiceException {
        OntologySearchServiceLocator locator = new OntologySearchServiceLocator();
        ontologySearch = locator.getUrnOntologySearch();
        EntitySearchServiceLocator locator2 = new EntitySearchServiceLocator();
        entitySearch = locator2.getUrnEntitySearch();
        WatsonSearchServiceLocator locator3 = new WatsonSearchServiceLocator();
        ws = locator3.getUrnWatsonSearch();
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getSuperClasses(String onto, String uri) {
        try {
            return entitySearch.getAllSuperClasses(onto, uri);
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
            return new String[]{};
        }
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getSubClasses(String onto, String uri) {
        try {
            return entitySearch.getAllSubClasses(onto, uri);
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
            return new String[]{};
        }
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getClasses(String uri) {
        try {
            return ontologySearch.listClasses(uri);
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String[]{};
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getProperties(String uri) {
        try {
            return ontologySearch.listProperties(uri);
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String[]{};
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getIndividuals(String uri) {
        try {
            return ontologySearch.listIndividuals(uri);
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String[]{};
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getLabels(String onto, String uri) throws RemoteException {
        String[] res = entitySearch.getLabels(onto, uri);
        return res;
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[][] getLiterals(String onto, String uri) throws RemoteException {
        String[][] res = entitySearch.getLiteralsFor(onto, uri);
        return res;
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getComments(String onto, String uri) throws RemoteException {
        return entitySearch.getComments(onto, uri);
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String getType(String onto, String uri) throws RemoteException {
        String res = entitySearch.getType(onto, uri);
        return res;
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getEntitiesByKeyword(String onto, String term) throws RemoteException {
        String[] res = entitySearch.getEntitiesByKeywordWithRestriction(onto, term, LOCAL_NAME + LABEL, CLASS + INDIVIDUAL + PROPERTY, PARTIAL_MATCH);
        //String[] res = entitySearch.getEntitiesByKeyword(onto, term);
        System.out.println(term + " > " + res.length);
        return res;
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getSemanticContentByKeywords(String keyword) throws RemoteException {
        return getSemanticContentByKeywords(new String[]{keyword});
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getSemanticContentByKeywords(String[] keywords) throws RemoteException {
        String[] results = ontologySearch.getSemanticContentByKeywordsWithRestrictions(keywords, LOCAL_NAME + LABEL, CLASS + INDIVIDUAL + PROPERTY, PARTIAL_MATCH);
        System.out.println(results.length);
        return results;
    }

    /*@Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String getOWLSpecy(String uri) throws RemoteException {
        String res = ontologySearch.getOWLSpecy(uri);
        if (Integer.parseInt(res) == FULL) {
            return new String("OWL Full");
        }
        if (Integer.parseInt(res) == DL) {
            return new String("OWL DL");
        }
        if (Integer.parseInt(res) == LITE) {
            return new String("OWL Lite");
        }
        if (Integer.parseInt(res) == NOT_OWL || Integer.parseInt(res) == -1) {
            return new String("N/A");
        }
        return new String("N/A");
    }*/
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String getDLExpressivness(String uri) throws RemoteException {
        return ontologySearch.getDLExpressivness(uri);
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public long getSizeInBytes(String uri) throws RemoteException {
        return ontologySearch.getSizeInBytes(uri);
    }

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getSemanticContentLanguages(String uri) throws RemoteException {
        return ontologySearch.getSemanticContentLanguages(uri);
    }

    //
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public SemanticContentResult[] complexSearch(String text) throws RemoteException {
        String[] params = {text};
        int scopeModifier = LOCAL_NAME + LABEL + COMMENT + LITERAL;
        int entityTypeModifier = CLASS + PROPERTY + INDIVIDUAL;
        // the one that use the DB:
        // SC_NBCLASSES_INFO+SC_NBPROPS_INFO+SC_NBINDIS_INFO+SC_LANGUAGES_INFO+SC_LOCATION_INFO
        int scInfo = SC_DLEXPR_INFO + SC_SIZE_INFO + SC_LANGUAGES_INFO;
        int entInfo = ENT_TYPE_INFO + ENT_LABEL_INFO + ENT_ANYRELATIONTO_INFO + ENT_ANYLITERAL_INFO;
        SemanticContentResult[] sr = ws.searchWatsonWithKeywords(params, scopeModifier, entityTypeModifier, 1, scInfo, entInfo);
        displayResult(sr);
        return sr;
    }
    
    //// 1.2 additions

    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String[] getBestCoverageWithRestrictions(String[] keywords) throws RemoteException {
        String[] results = ontologySearch.getBestCoverageWithRestrictions(keywords, LOCAL_NAME + LABEL, CLASS + INDIVIDUAL + PROPERTY, PARTIAL_MATCH);
        System.out.println(results.length);
        return results;
    }   
    
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public SemanticContentResult[] searchWatsonWithKeywordsWithLimit(String text, int limit) throws RemoteException {
        String[] params = {text};
        int scopeModifier = LOCAL_NAME + LABEL + COMMENT + LITERAL;
        int entityTypeModifier = CLASS + PROPERTY + INDIVIDUAL;
        // the one that use the DB:
        // SC_NBCLASSES_INFO+SC_NBPROPS_INFO+SC_NBINDIS_INFO+SC_LANGUAGES_INFO+SC_LOCATION_INFO
        int scInfo = SC_DLEXPR_INFO + SC_SIZE_INFO + SC_LANGUAGES_INFO;
        int entInfo = ENT_TYPE_INFO + ENT_LABEL_INFO + ENT_ANYRELATIONTO_INFO + ENT_ANYLITERAL_INFO;
        SemanticContentResult[] sr = ws.searchWatsonWithKeywordsWithLimit(params, scopeModifier, entityTypeModifier, 1, scInfo, entInfo, limit);
        //displayResult(sr);
        return sr;
    }
    
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public SemanticContentResult[] searchBestCoverageWithRestriction(String[] params) throws RemoteException {
        //String[] params = {text};
        int scopeModifier = LOCAL_NAME + LABEL + COMMENT + LITERAL;
        int entityTypeModifier = CLASS + PROPERTY + INDIVIDUAL;
        // the one that use the DB:
        // SC_NBCLASSES_INFO+SC_NBPROPS_INFO+SC_NBINDIS_INFO+SC_LANGUAGES_INFO+SC_LOCATION_INFO
        int scInfo = SC_DLEXPR_INFO + SC_SIZE_INFO + SC_LANGUAGES_INFO;
        int entInfo = ENT_TYPE_INFO + ENT_LABEL_INFO + ENT_ANYRELATIONTO_INFO + ENT_ANYLITERAL_INFO;
        SemanticContentResult[] sr = ws.searchBestCoverageWithRestriction(params, scopeModifier, entityTypeModifier, 1, scInfo, entInfo);
        //displayResult(sr);
        return sr;
    }
    
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String SubSuperclassesXML(String URI, String OntURI){
        StringBuffer buf = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?><relations>");
        try {
            String[] rel = entitySearch.getSuperClasses(OntURI, URI);
            for(int i = 0; i < rel.length; i++) {
                    buf.append("\t<super uri='"+rel[i]+"'>"+NameSpace.splitNamespace(rel[i])[1]+"</super>\n");
            }
            rel = entitySearch.getSubClasses(OntURI, URI);
            for(int i = 0; i < rel.length; i++) {
                    buf.append("\t<sub uri='"+rel[i]+"'>"+NameSpace.splitNamespace(rel[i])[1]+"</sub>\n");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return buf.append("</relations>").toString();
    }
    
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String relationsXML(String URI, String OntURI){
        StringBuffer buf = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?><relations>");
        try {
            String[][] rel = entitySearch.getRelationsFrom(OntURI, URI);
            for(int i = 0; i < rel.length; i++) {
                    buf.append("\t<from predicate='"+rel[i][0]+"' relation='"+rel[i][1]+"' uri='"+rel[i][2]+"'>"+NameSpace.splitNamespace(rel[i][2])[1]+"</from>\n");
            }
            rel = entitySearch.getRelationsTo(OntURI, URI);
            for(int i = 0; i < rel.length; i++) {
                    buf.append("\t<to predicate='"+rel[i][0]+"' relation='"+rel[i][1]+"' uri='"+rel[i][2]+"'>"+NameSpace.splitNamespace(rel[i][2])[1]+"</to>\n");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return buf.append("</relations>").toString();
    } 
    
    @Remember(maxSize = 16384, timeToLive = 672, timeUnit = TimeUnitEnum.HOUR)
    public String literalsXML(String URI, String OntURI){
        StringBuffer buf = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?><relations>");
        try {
            String[][] rel = entitySearch.getLiteralsFor(OntURI, URI);
            for(int i = 0; i < rel.length; i++) {
                    buf.append("\t<literal predicate='"+rel[i][0]+"' relation='"+rel[i][1]+"'>"
                            +StringEscapeUtils.escapeXml(rel[i][2])
                            +"</literal>\n");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Watson.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return buf.append("</relations>").toString();
    }
    ////

    private void displayResult(SemanticContentResult[] sr) {
        System.out.println("Number of results: " + sr.length);
        for (SemanticContentResult r : sr) {
            System.out.println("SC:: " + r.getURI() + " (" + r.getDLExpressivness() + ", C:" + r.getNBClasses() + ", P:" + r.getNBProperties() + ", I:" + r.getNBIndividuals() + r.getSize() + "B)");
            String[] languages = r.getLanguages();
            if (languages != null) {
                System.out.print("  Languages: ");
                for (String l : languages) {
                    System.out.print(l + " ");
                }
                System.out.println();
            }
            String[] locations = r.getLocations();
            if (locations != null) {
                System.out.print("  Locations: ");
                for (String l : locations) {
                    System.out.print(l + " ");
                }
                System.out.println();
            }
            EntityResult[] er = r.getEntityResultList();
            for (EntityResult e : er) {
                System.out.println("\t" + e.getType() + "::" + e.getURI() + "(" + e.getLabel() + ")");
                String[][] relFrom = e.getRelationFrom();
                if (relFrom != null) {
                    for (String[] rf : relFrom) {
                        System.out.println("\t\t" + rf[1] + " -- " + rf[2]);
                    }
                }
                String[][] relTo = e.getRelationTo();
                if (relTo != null) {
                    for (String[] rt : relTo) {
                        System.out.println("\t\t" + rt[2] + " -- " + rt[1]);
                    }
                }
                String[][] literals = e.getLiterals();
                if (literals != null) {
                    for (String[] l : literals) {
                        System.out.println("\t\t" + l[1] + " = " + l[2]);
                    }
                }
            }
        }
    }
}