package com.digis01.GGarciaProgramacionNCapasMavenCliente.Controller;

import com.digis01.GGarciaProgramacionNCapasMavenCliente.Security.AppUserPrincipal;
import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class UserSessionModelAdvice {

    @ModelAttribute("loggedUserName")
    public String loggedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUserPrincipal userPrincipal) {
            return userPrincipal.getDisplayName();
        }

        return Objects.toString(authentication.getName(), null);
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(granted -> "ROLE_ADMIN".equalsIgnoreCase(granted.getAuthority()));
    }
}

