/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.context.transreptor;

import com.ten60.netkernel.urii.IURRepresentation;
import com.ten60.netkernel.urii.aspect.IAspectBinaryStream;
import org.ten60.netkernel.layer1.nkf.INKFConvenienceHelper;
import org.ten60.netkernel.layer1.nkf.INKFRequestReadOnly;
import org.ten60.netkernel.layer1.nkf.INKFResponse;
import org.ten60.netkernel.layer1.nkf.impl.NKFTransreptorImpl;
import org.ten60.netkernel.xml.representation.IAspectXDA;
import org.ten60.netkernel.xml.xda.IXDAReadOnly;
import uk.ac.open.powermagpie.context.representation.ContextAspect;

/**
 *
 * @author lg3388
 */
public class ContextFactory extends NKFTransreptorImpl {

    @Override
    protected void transrepresent(INKFConvenienceHelper context) throws Exception {
        IXDAReadOnly from = ((IAspectXDA) context.sourceAspect(INKFRequestReadOnly.URI_SYSTEM, IAspectXDA.class)).getXDA();

        String name;
        if (context.getThisRequest().argumentExists("name")) {
            name = context.getThisRequest().getArgument("name");
        } else {
            name = "default";
        }

        ContextAspect contextAspect = new ContextAspect(name);

        INKFResponse response = context.createResponseFrom(contextAspect);
        response.setCreationCost(1024);
        context.setResponse(response);
    }

    @Override
    public boolean supports(IURRepresentation aFrom, Class aTo) {
        return (aFrom.hasAspect(IAspectXDA.class) || aFrom.hasAspect(IAspectBinaryStream.class))
                && aTo.isAssignableFrom(ContextAspect.class);
    }

}
