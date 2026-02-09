package org.tutorial.venusbackend.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.tutorial.venusbackend.model.MyUser;

import java.util.Collection;
import java.util.Arrays;

public class MyUserDetails implements UserDetails {

    private final MyUser user;

    public MyUserDetails(MyUser user) {
        this.user = user;
    }

    public MyUser getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MyUser.Role role = user.getRole() != null ? user.getRole() : MyUser.Role.USER;
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();

    }
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}