/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.open.powermagpie.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Laurian Gridinoc
 */
public class Mount extends HttpServlet {

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
        ServletOutputStream out = response.getOutputStream();
        try {

            String resource = request.getPathInfo().substring(1);
            URL url = this.getClass().getClassLoader().getResource(resource);

            if (url == null) {
                response.sendError(response.SC_NOT_FOUND);
            } else {
                String mime = this.getServletContext().getMimeType(url.toString());
                if (mime == null) {
                    mime = "application/octet-stream";
                }

                response.setHeader("URI", url.toString());
                response.setContentType(mime);

                DataInputStream in = new DataInputStream(this.getClass().getClassLoader().getResourceAsStream(resource));

                byte[] buffer = new byte[1024];
                int length = 0;
                while ((in != null) && ((length = in.read(buffer)) != -1)) {
                    out.write(buffer, 0, length);
                }

                out.flush();
            }
        } finally {
            out.close();
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
