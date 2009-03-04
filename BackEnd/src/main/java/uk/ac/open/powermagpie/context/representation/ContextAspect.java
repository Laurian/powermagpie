/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.open.powermagpie.context.representation;

import com.ten60.netkernel.urii.IURAspect;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.jxpath.JXPathContext;
import uk.ac.open.powermagpie.context.Context;

/**
 *
 * @author lg3388
 */
public class ContextAspect extends Context implements IURAspect {

    private JXPathContext context;
    private XStream xstream;

    public ContextAspect(String name) {
        super(name);

        context = JXPathContext.newContext(this);
        context.setLenient(true);
        xstream = new XStream();
    }

    public String add(String lexical) {
        return xstream.toXML(super.addTerm(lexical));
    }

    public void set(String xml, String path) {
        context.setValue(path.substring(12), xstream.fromXML(xml));
    }

    public String serialise(String path) {
        return xstream.toXML(context.getValue(path.substring(12)));
    }
}
