/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.context;

import uk.ac.open.kmi.watson.clientapi.EntityResult;
import uk.ac.open.kmi.watson.clientapi.SemanticContentResult;

/**
 *
 * @author lg3388
 */
public class Match {

    public SemanticContentResult sc;
    public EntityResult ntt;
    
    public String context;

    public Match() {}

    public Match(SemanticContentResult sc, EntityResult ntt, String context) {
        this.sc         = sc;
        this.ntt        = ntt;
        this.context    = context;
    }

    public SemanticContentResult getSemanticContentResult() {
        return sc;
    }

    public void setSemanticContentResult(SemanticContentResult sc) {
        this.sc = sc;
    }

    public EntityResult getEntityResult() {
        return ntt;
    }

    public void setEntityResult(EntityResult ntt) {
        this.ntt = ntt;
    }
}
