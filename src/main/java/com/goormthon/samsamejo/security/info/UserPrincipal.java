package com.goormthon.samsamejo.security.info;

import com.goormthon.samsamejo.domain.Users;
import com.goormthon.samsamejo.domain.type.ERole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserPrincipal implements OAuth2User, UserDetails {

    private final Long id;
    private final String password;
    private final ERole eRole;
    private final boolean isRegistered;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(Users user, Map<String, Object> attributes) {
        return UserPrincipal.builder()
                .id(user.getId())
                .password(user.getPassword())
                .eRole(user.getRole())
                .isRegistered(user.isRegistered())
                .attributes(attributes)
                .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRole())))
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.id.toString();
    }

    @Override
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    public String getPassword() {
        return this.password;
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
