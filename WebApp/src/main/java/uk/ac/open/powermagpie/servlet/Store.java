package uk.ac.open.powermagpie.servlet;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 *
 * @author Laurian Gridinoc
 */
public class Store extends HttpServlet {

    private String redirect;
    //private boolean escape;

    @Override
    public void init() throws ServletException {
        redirect    = this.getInitParameter("redirect.prefix");
        //escape      = Boolean.parseBoolean(this.getInitParameter("redirect.escape")); //TODO
    }

    @Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.addHeader("Location", redirect + request.getPathInfo());
        response.sendError(response.SC_SEE_OTHER);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            doHead(request, response);
    }

    @Override
    //TODO see com.hp.hpl.jena.xmloutput.impl.BaseXMLWriter.checkURI(BaseXMLWriter.java:768)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpClient httpClient = new HttpClient();
            httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                    this.getInitParameter("talis.username"), this.getInitParameter("talis.password")
                    ));

            String input = request.getParameter("nt");
            System.out.println(input);

            Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(input.getBytes()), null, "N-TRIPLE");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            model.write(out);
            
            PostMethod post = new PostMethod(this.getInitParameter("talis.store") + "/meta");
            RequestEntity entity = new InputStreamRequestEntity(new ByteArrayInputStream(out.toByteArray()), "application/rdf+xml");
            post.setRequestEntity(entity);
            httpClient.executeMethod(post);

            //TODO remove, use logging
            Header[] hh = post.getRequestHeaders();
            for (int i = 0; i < hh.length; i++) {
                System.out.println("> " + hh[i].getName() + ": " + hh[i].getValue());
            }

            hh = post.getResponseHeaders();
            for (int i = 0; i < hh.length; i++) {
                System.out.println("< " + hh[i].getName() + ": " + hh[i].getValue());
            }

            System.out.println(new String(out.toByteArray()));

            //TODO response.setHeader("Location", "?");
            response.sendError(response.SC_CREATED);
    }

    @Override
    public String getServletInfo() {
        return "Short description"; //TODO 
    }
}
