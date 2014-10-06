package org.weceem.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import grails.plugin.springsecurity.userdetails.GrailsUser

class WeceemUserDetails extends GrailsUser {

    final def extraProperties
    
    WeceemUserDetails(String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                      Collection<GrantedAuthority> authorities,
                      long id, HashMap otherProps) {
        super(username, password, enabled, accountNonExpired,
              credentialsNonExpired, accountNonLocked, authorities, id)
        this.extraProperties = otherProps
    }
    
    def propertyMissing(String name) {
        return extraProperties[name]
    }
}