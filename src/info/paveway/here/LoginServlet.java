package info.paveway.here;

import info.paveway.here.CommonConstants.REQ_ATTR_KEY;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

    private static final String GOOGLE_URL = "https://www.google.com/accounts/o8/id";
    private static final String YAHOO_URL  = "yahoo.co.jp";
    private static final String MIXI_URL   = "https://mixi.jp";

    private static final String FORWARD_URL = "/WEB-INF/jsp/login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String continuePage =
                createServerStringBuffer(
                        request.getScheme(), request.getServerName(), request.getServerPort()).append("/index").toString();

        request.setAttribute(REQ_ATTR_KEY.GOOGLE, createOpenIdUrl(continuePage, GOOGLE_URL));
        request.setAttribute(REQ_ATTR_KEY.YAHOO,  createOpenIdUrl(continuePage, YAHOO_URL));
        request.setAttribute(REQ_ATTR_KEY.MIXI,   createOpenIdUrl(continuePage, MIXI_URL));

        getServletContext().getRequestDispatcher(FORWARD_URL).forward(request, response);
    }

    private StringBuffer createServerStringBuffer(String scheme, String serverName, int port) {
        StringBuffer url = new StringBuffer();
        if (0 > port) {
            port = 80;
        }

        url.append(scheme);
        url.append("://");
        url.append(serverName);

        if (("http".equals(scheme) && (80 != port)) || ("https".equals(scheme) & (443 != port))) {
            url.append(':');
            url.append(port);
        }

        return url;
    }

    private String createOpenIdUrl(String continuePage, String openidIdentifier) throws UnsupportedEncodingException {
        return
                "/loginhandler?continue=" +
                URLEncoder.encode(continuePage, "UTF-8") +
                "&openid_identifier=" +
                URLEncoder.encode(openidIdentifier, "UTF-8");
    }
}
