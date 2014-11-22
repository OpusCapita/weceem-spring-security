class WeceemSpringSecurityGrailsPlugin {
    // the plugin version
    def version = "1.3-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.3.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [springSecurityCore:'2.0-RC4 > *']

    def loadAfter = ['springSecurityCore'] // So that our user details service overrides

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/domain/**/*.groovy"
    ]

    def author = "jCatalog AG"
    def authorEmail = "info@weceem.org"
    def title = "Bridges Weceem authentication to Spring Security"
    def description = '''\\
Provides the glue needed to make Weceem plugin use Spring Security for authorisation and authentication.
Your application still needs to configure Spring-Security however. The domain class is expected to include "email" property.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/weceem-spring-security"
    def license = "APACHE"
    def organization = [ name: "jCatalog AG", url: "http://weceem.org/" ]
    def developers = [
            [ name: "Marc Palmer", email: "marc@grailsrocks.com" ]
    ]
    def issueManagement = [ system: "JIRA", url: "http://jira.jcatalog.com/browse/WCM" ]
    def scm = [ url: "https://github.com/jCatalog/weceem-plugin" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        userDetailsService(org.weceem.auth.WeceemUserDetailsService) {
            grailsApplication = ref('grailsApplication')
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        def authenticateService = applicationContext.springSecurityService
        applicationContext.wcmSecurityService.securityDelegate = [
            getUserName : { ->
                def princ = authenticateService.principal
                if (log.debugEnabled) {
                    log.debug "Weceem security getUserName callback - user principal is: ${princ} (an instance of ${princ?.class})"
                }
                if (princ instanceof String) {
                    return null
                } else {
                    return princ?.username
                }
            },
            getUserEmail : { ->
                def princ = authenticateService.principal
                if (log.debugEnabled) {
                    log.debug "Weceem security getUserEmail callback - user principal is: ${princ} (an instance of ${princ?.class})"
                }
                return (princ instanceof String) ? null : princ?.email
            },
            getUserRoles : { ->
                def princ = authenticateService.principal
                if (log.debugEnabled) {
                    log.debug "Weceem security getUserRoles callback - user principal is: ${princ} (an instance of ${princ?.class})"
                }
                if (princ instanceof String) {
                    return ['ROLE_GUEST']
                }
                def auths = []
                def authorities = princ?.authorities
                if (authorities) {
                    auths.addAll(authorities?.authority)
                }
                return auths ?: ['ROLE_GUEST']
            },
            getUserPrincipal : { ->
                authenticateService.principal
            }
        ]
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
