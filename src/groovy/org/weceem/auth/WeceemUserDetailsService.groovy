package org.weceem.auth

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService

import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.beans.factory.InitializingBean

import org.apache.commons.logging.LogFactory

class WeceemUserDetailsService implements GrailsUserDetailsService, InitializingBean {

    def log = LogFactory.getLog("grails.app.service."+WeceemUserDetailsService.name)

    /**
    * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least one role, so
    * we give a user with no granted roles this one which gets past that restriction but
    * doesn't grant anything.
    */
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

    static final String[] REQUIRED_MAPPED_FIELDS = ['username', 'password', 'enabled', 'authorities']

    def grailsApplication

    Class domainClass

    Closure detailsMapper

    void afterPropertiesSet() {
        def conf = grailsApplication.config
        def clsname = conf.grails.plugins.springsecurity.userLookup.userDomainClassName
        domainClass = grailsApplication.getDomainClass(clsname).clazz
        
        def mapper = conf.weceem.springsecurity.details.mapper
        if (!(mapper instanceof Closure)) {
            throw new IllegalArgumentException(
                "Your Config must specify a closure in weceem.springsecurity.details.mapper "+
                "that maps the domain model to a non-domain object, providing at least: ${REQUIRED_MAPPED_FIELDS}")
        }
        detailsMapper = mapper
    }

    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        println "Getting WUDS user for $username"
        domainClass.withTransaction { status ->

            def user = domainClass.findByUsername(username)
            if (!user) throw new UsernameNotFoundException('User not found', username)

            def mapper = detailsMapper.clone()
            mapper.delegate = user
            mapper.resolveStrategy = Closure.DELEGATE_FIRST
            def details = mapper()
            
            def requiredDetails = REQUIRED_MAPPED_FIELDS.collect { details[it] }
            if ( requiredDetails.find { v -> v == null } ) {
                throw new IllegalArgumentException("User details mapper must supply a value for each of the following: ${REQUIRED_MAPPED_FIELDS}")
            }
            
            def authorities = details.authorities.collect {new GrantedAuthorityImpl(it.authority)}

            if (log.debugEnabled) {
                log.debug "Returning user details objecting with values: ${requiredDetails.dump()}"
            }
            return new WeceemUserDetails(details.username, details.password, details.enabled, authorities ?: NO_ROLES, details)
        }
    }
}
