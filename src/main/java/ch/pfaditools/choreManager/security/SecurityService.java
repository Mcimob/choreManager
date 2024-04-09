package ch.pfaditools.choreManager.security;

import ch.pfaditools.choreManager.backend.repository.UserRepository;
import ch.pfaditools.choreManager.model.UserEntity;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityService {

    // https://vaadin.com/blog/jwt-authentication-with-vaadin-flow-for-better-developer-and-user-experience
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.authenticationContext = authenticationContext;
        this.userRepository = userRepository;
    }

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<UserEntity> getAuthenticatedUser() {
        return getAuthentication().map(authentication -> userRepository.findByUsername(authentication.getName()));
    }

    public void logout() {
        authenticationContext.logout();
        clearCookies();
    }

    private static final String JWT_HEADER_AND_PAYLOAD_COOKIE_NAME = "jwt.headerAndPayload";
    private static final String JWT_SIGNATURE_COOKIE_NAME = "jwt.signature";

    private void clearCookies() {
        clearCookie(JWT_HEADER_AND_PAYLOAD_COOKIE_NAME);
        clearCookie(JWT_SIGNATURE_COOKIE_NAME);
    }

    private void clearCookie(String cookieName) {
        HttpServletRequest request = VaadinServletRequest.getCurrent()
                .getHttpServletRequest();
        HttpServletResponse response = VaadinServletResponse.getCurrent()
                .getHttpServletResponse();

        Cookie k = new Cookie(
                cookieName, null);
        k.setPath(getRequestContextPath(request));
        k.setMaxAge(0);
        k.setSecure(request.isSecure());
        k.setHttpOnly(false);
        response.addCookie(k);
    }

    private String getRequestContextPath(HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        return "".equals(contextPath) ? "/" : contextPath;
    }
}
