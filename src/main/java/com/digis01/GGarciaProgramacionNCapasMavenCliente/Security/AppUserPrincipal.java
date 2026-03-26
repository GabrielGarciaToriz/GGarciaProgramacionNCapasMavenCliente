package com.digis01.GGarciaProgramacionNCapasMavenCliente.Security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserPrincipal implements UserDetails {

    private final String username;
    private final String displayName;
    private final String apiAuthorizationHeader;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUserPrincipal(String username, String displayName, String apiAuthorizationHeader,
            Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.displayName = displayName;
        this.apiAuthorizationHeader = apiAuthorizationHeader;
        this.authorities = authorities;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiAuthorizationHeader() {
        return apiAuthorizationHeader;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

