/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.context.accessor;

import com.ten60.netkernel.container.Container;
import com.ten60.netkernel.module.ModuleDefinition;
import com.ten60.netkernel.urii.IURAspect;
import com.ten60.netkernel.urii.IURRepresentation;
import com.ten60.netkernel.urii.aspect.BooleanAspect;
import com.ten60.netkernel.urii.aspect.StringAspect;
import com.ten60.netkernel.util.NetKernelException;
import java.util.Hashtable;
import java.util.Map;
import org.ten60.netkernel.layer1.nkf.INKFConvenienceHelper;
import org.ten60.netkernel.layer1.nkf.INKFRequestReadOnly;
import org.ten60.netkernel.layer1.nkf.INKFResponse;
import org.ten60.netkernel.layer1.nkf.NKFException;
import org.ten60.netkernel.layer1.nkf.impl.NKFAccessorImpl;
import org.ten60.netkernel.xml.representation.IAspectNodeList;
import org.ten60.netkernel.xml.representation.IXAspect;
import uk.ac.open.powermagpie.context.representation.ContextAspect;

/**
 *
 * @author lg3388
 */
public class ContextAccessor extends NKFAccessorImpl {

    private final static Map<String, ContextAspect> context = new Hashtable<String, ContextAspect>();

    public ContextAccessor() {
        super(SAFE_FOR_CONCURRENT_USE,
                INKFRequestReadOnly.RQT_SOURCE |
                INKFRequestReadOnly.RQT_SINK |
                INKFRequestReadOnly.RQT_DELETE |
                INKFRequestReadOnly.RQT_EXISTS);
    }

    @Override
    public void initialise(Container container, ModuleDefinition moduleDef) throws NetKernelException {
        super.initialise(container, moduleDef);
    }

    private ContextAspect context(INKFConvenienceHelper context) throws NKFException {
        String configURI;
        if (context.getThisRequest().argumentExists("configuration")) {
            configURI = "this:param:configuration";
        } else {
            configURI = "ffcpl:/etc/context.xml";
        }

        String name;
        if (context.getThisRequest().argumentExists("name")) {
            name = context.getThisRequest().getArgument("name");
        } else {
            name = "default";
        }
        
        if (! this.context.containsKey(name)) {
            this.context.put(name,
                    (ContextAspect) context.sourceAspect(configURI, ContextAspect.class));
        }

        return this.context.get(name);
    }

    @Override
    public void processRequest(INKFConvenienceHelper context) throws Exception {
        switch (context.getThisRequest().getRequestType()) {
            case INKFRequestReadOnly.RQT_SINK:
                sink(context);
                break;
            case INKFRequestReadOnly.RQT_SOURCE:
                source(context);
                break;
            case INKFRequestReadOnly.RQT_DELETE:
                delete(context);
                break;
            case INKFRequestReadOnly.RQT_EXISTS:
                exists(context);
                break;
            default:
                throw new Exception("Unsupported Request Type");
        }
    }

    protected void sink(INKFConvenienceHelper context) throws Exception {

        System.out.println("SINK");

        IURRepresentation representation = context.source(INKFRequestReadOnly.URI_SYSTEM);

        //IXAspect aspect = (IXAspect) context.transrept(
        //        (IAspectNodeList) representation.getAspects().iterator().next(), IXAspect.class);

        IXAspect aspect = (IXAspect) context.transrept((IURAspect) representation.getAspects().iterator().next(), IXAspect.class);

        String path = context.getThisRequest().getURIWithoutFragment();
        if (context.getThisRequest().argumentExists("path")) {
            path = context.getThisRequest().getArgument("path");
        }

        String term;
        if (context.getThisRequest().argumentExists("term")) {
            System.out.println("SINK term");
            term = context.getThisRequest().getArgument("term");
            context(context).add(context.getThisRequest().getArgument("term"));
        } else {
            System.out.println("SINK generic");
            context(context).set(
                ((StringAspect) context.transrept(aspect, StringAspect.class)).getString(),
                path);
        }
    }

    protected void source(INKFConvenienceHelper context) throws Exception {
        System.out.println("SOURCE");

        String path = context.getThisRequest().getURIWithoutFragment();

        IXAspect aspect = (IXAspect) context.transrept(
                new StringAspect(context(context).serialise(path)),
                IXAspect.class);

        INKFResponse resp = context.createResponseFrom(aspect);
        context.setResponse(resp);
    }

    // TODO
    protected void delete(INKFConvenienceHelper context) throws Exception {
        String path = context.getThisRequest().getURIWithoutFragment();
        IURAspect aspect = new BooleanAspect(false);
        INKFResponse resp = context.createResponseFrom(aspect);
        context.setResponse(resp);
    }

    //TODO
    protected void exists(INKFConvenienceHelper context) throws Exception {
        String path = context.getThisRequest().getURIWithoutFragment();

        String result = context(context).serialise(path);

        BooleanAspect ba = new BooleanAspect(result != null && !"<null/>".equals(result));
        INKFResponse resp = context.createResponseFrom(ba);
        context.setResponse(resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
