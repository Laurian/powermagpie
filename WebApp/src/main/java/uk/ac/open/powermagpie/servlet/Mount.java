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

    private String defaultResource;
    private ClassLoader classLoader;

    @Override
    public void init() throws ServletException {
        defaultResource = this.getInitParameter("default.resource");
        classLoader = this.getClass().getClassLoader();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletOutputStream out = response.getOutputStream();
        try {

            String resource = request.getPathInfo().substring(1);

            if (resource.length() == 0) {
                resource = defaultResource;
            }

            URL url = classLoader.getResource(resource);

            if (url == null) {
                response.sendError(response.SC_NOT_FOUND);
            } else {
                String mime = this.getServletContext().getMimeType(url.toString());
                if (mime == null) {
                    mime = "application/octet-stream";
                }

                response.setHeader("URI", url.toString());
                response.setContentType(mime);

                DataInputStream in = new DataInputStream(classLoader.getResourceAsStream(resource));

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

    @Override
    public String getServletInfo() {
        return "Mounts classloader accessible resources";
    }
}
