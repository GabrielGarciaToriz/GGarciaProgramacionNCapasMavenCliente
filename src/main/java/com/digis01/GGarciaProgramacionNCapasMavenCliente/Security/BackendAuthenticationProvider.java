package com.digis01.GGarciaProgramacionNCapasMavenCliente.Security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class BackendAuthenticationProvider implements AuthenticationProvider {

    private final BackendAuthService backendAuthService;

    public BackendAuthenticationProvider(BackendAuthService backendAuthService) {
        this.backendAuthService = backendAuthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        AppUserPrincipal principal = backendAuthService.authenticate(username, password);
        if (principal == null) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        return UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

