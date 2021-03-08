package example.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException {
      // run custom logics upon successful login

      handle(request, response, authentication);
      clearAuthenticationAttributes(request);
  }

  private void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
      final String targetUrl = determineTargetUrl(authentication);

      if (request == null) {
        return ;
      } else if (response == null) {
        return ;
      }

      RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

      if (response.isCommitted()) {
          System.out.println("Response has already been committed. Unable to redirect to " + targetUrl);
          return;
      }

      redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  private String determineTargetUrl(final Authentication authentication) {
    if (authentication == null) {
      return "/";
    }
      Map<String, String> roleTargetUrlMap = new HashMap<>();
      roleTargetUrlMap.put("ROLE_USER", "/user");
      roleTargetUrlMap.put("ROLE_ADMIN", "/admin");
      roleTargetUrlMap.put("ROLE_SUPER", "/home");

      final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      for (final GrantedAuthority grantedAuthority : authorities) {

          String authorityName = grantedAuthority.getAuthority();
          if(roleTargetUrlMap.containsKey(authorityName)) {
              return roleTargetUrlMap.get(authorityName);
          }
      }

      return "/";
  }

  /**
   * Removes temporary authentication-related data which may have been stored in the session
   * during the authentication process.
   */
  private final void clearAuthenticationAttributes(final HttpServletRequest request) {

      final HttpSession session = request.getSession(false);

      if (session == null) {
          return;
      }

      session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}
