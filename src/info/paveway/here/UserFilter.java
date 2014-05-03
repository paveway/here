package info.paveway.here;

import info.paveway.here.CommonConstants.REQ_ATTR_KEY;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();
        if ((null != userService) && userService.isUserLoggedIn()) {
            User user = userService.getCurrentUser();
            request.setAttribute(REQ_ATTR_KEY.USER, user);

            String nickname = user.getNickname();
            if ((null == nickname) || "".equals(nickname)) {
                String email = user.getEmail();
                if ((null != email) && !"".equals(email)) {
                    nickname = email;

                } else {
                    nickname = user.getUserId();
                }
            }

            request.setAttribute(REQ_ATTR_KEY.NICKNAME, nickname);
        }

        chain.doFilter(request, response);
    }
}
