package info.paveway.here;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

    private static final String FORWARD_URL = "/WEB-INF/jsp/top.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        getServletContext().getRequestDispatcher(FORWARD_URL).forward(request, response);
    }
}
