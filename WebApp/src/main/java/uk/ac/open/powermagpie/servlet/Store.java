/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Store</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Store at " + request.getContextPath() + "</h1>");


            //test

            System.out.println("reading n-triples");
            Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(
                    "<http://example.com/#xpointer(//p[5])> <http://www.w3.org/2000/01/rdf-schema#seeAlso> <http://purl.org/net/powermagpie/store/c36ec281-5650-0001-19dd-c2001fc06d00> . \n".getBytes()), null, "N-TRIPLE");

            System.out.println(model.listSubjects().nextResource().getURI());


            System.out.println("writing rdf/xml");
            model.write(System.out);

            //test




            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    //com.hp.hpl.jena.xmloutput.impl.BaseXMLWriter.checkURI(BaseXMLWriter.java:768)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                    this.getInitParameter("talis.username"), this.getInitParameter("talis.password")
                    ));

            String input = request.getParameter("nt");
            System.out.println(input);

            Model model = ModelFactory.createDefaultModel();
            model.read(new ByteArrayInputStream(
                    //((StringAspect) context.sourceAspect("this:param:operand", StringAspect.class)).getString()
                    input //"<http://example.com/#wagga> <http://www.w3.org/2000/01/rdf-schema#seeAlso> <http://purl.org/net/powermagpie/store/c36ec281-5650-0001-19dd-c2001fc06d00> . \n"
                    .getBytes()), null, "N-TRIPLE");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            model.write(out);
            
            //"http://api.talis.com/stores/lgridinoc-dev1"
            PostMethod post = new PostMethod(this.getInitParameter("talis.store") + "/meta");
            RequestEntity entity = new InputStreamRequestEntity(new ByteArrayInputStream(out.toByteArray()), "application/rdf+xml");
            post.setRequestEntity(entity);
            httpClient.executeMethod(post);

            Header[] hh = post.getRequestHeaders();
            for (int i = 0; i < hh.length; i++) {
                System.out.println("> " + hh[i].getName() + ": " + hh[i].getValue());
            }

            hh = post.getResponseHeaders();
            for (int i = 0; i < hh.length; i++) {
                System.out.println("< " + hh[i].getName() + ": " + hh[i].getValue());
            }

            System.out.println(new String(out.toByteArray()));

            response.sendError(response.SC_CREATED);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
