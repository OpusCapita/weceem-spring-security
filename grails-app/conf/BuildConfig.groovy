grails.servlet.version = "3.0"
grails.project.work.dir="target/work"

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    inherits "global" // inherit Grails default dependencies

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {

    }

    plugins {
        // plugins for the build system only
        build ':tomcat:7.0.54'
        // plugins needed at runtime but not for compilation
        runtime(':hibernate4:4.3.5.5') {
            export = false
        }
        compile ":spring-security-core:2.0-RC4"
        build ":release:3.0.1"
    }
}

