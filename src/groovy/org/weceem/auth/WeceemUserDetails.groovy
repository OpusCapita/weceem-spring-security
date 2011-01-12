package org.weceem.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class WeceemUserDetails implements UserDetails {
    final Collection<GrantedAuthority> authorities
    final String password
    final String username
    final boolean enabled

    final boolean accountNonExpired = true
    final boolean accountNonLocked = true
    final boolean credentialsNonExpired = true
    
    final def extraProperties
    
    WeceemUserDetails(username, password, enabled, authorities, otherProps) {
        this.username = username
        this.password = password
        this.authorities = authorities
        this.enabled = enabled
        this.extraProperties = otherProps
    }
    
    def propertyMissing(String name) {
        return extraProperties[name]
    }
}