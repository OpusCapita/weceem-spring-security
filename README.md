![](https://github.com/jCatalog/weceem-plugin/blob/master/web-app/_weceem/images/layout/weceem-logo.png)

# Weceem Spring Security Plugin for Grails

This is the code for a plugin that will bridge your application's Spring Security and domain classes
to the free Open Source [Weceem CMS](http://weceem.org) plugin for [Grails](http://grails.org).

Security in [the Weceem plugin](http://github.com/jCatalog/weceem-plugin) is completely decoupled so that you can plug
in whatever security mechanism you are using, and this plugin uses this mechanism to have your user information supplied
to Weceem from Spring Security Core. You can configure the property mappings from your user domain classes to the user
object properties available at runtime.

It is now compatible with **Grails 2.4.x**, current released version: **1.3** [release notes](http://jira.jcatalog.com/secure/IssueNavigator.jspa?reset=true&jqlQuery=project+%3D+WCM+AND+fixVersion+%3D+%221.2%22)

For the latest updates and release information visit [Weceem site](http://weceem.org) and follow [@weceem](https://twitter.com/weceem) on twitter.

Full documentation about Weceem plugin is available on the [GitHub](http://jcatalog.github.io/weceem-plugin)

Lead: [Stephan Albers](https://github.com/stephanalbers)
