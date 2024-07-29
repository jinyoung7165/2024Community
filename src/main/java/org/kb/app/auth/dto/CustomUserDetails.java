package org.kb.app.auth.dto;


import lombok.Getter;
import lombok.ToString;
import org.kb.app.auth.enums.Role;
import org.kb.app.member.entity.Member;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class CustomUserDetails implements UserDetails, AuthenticatedPrincipal { //권한을 담은 UserDetails

    private String socialId;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String socialId, Collection<? extends GrantedAuthority> authorities) {
        this.socialId = socialId;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(Member member) {
        String role;
        if (member.getRole().equals(Role.ADMIN)) {role = Role.ADMIN.getKey();}
        else { role = Role.USER.getKey(); }
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(role));

        return new CustomUserDetails(
                member.getSocialId(),
                authorities
        );
    }


    // UserDetail Override
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return socialId;
    }

    @Override
    public String getPassword() {
        return null;
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

    @Override
    public String getName() {
        return socialId;
    }

}
