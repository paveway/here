package info.paveway.here;

import info.paveway.here.CommonConstants.REQ_PARAM_KEY;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginHandlerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String continuePage = request.getParameter(REQ_PARAM_KEY.CONTINUE);
        String openidIdentifier = request.getParameter(REQ_PARAM_KEY.OPENID_IDENTIFIER);

        String authDomain = continuePage.substring(0,  continuePage.lastIndexOf("/"));
        Set<String> attributeRequest = new HashSet<String>();

        UserService userService = UserServiceFactory.getUserService();
        String createUrl = userService.createLoginURL(continuePage, authDomain, openidIdentifier, attributeRequest);

        response.sendRedirect(createUrl);
    }
}
