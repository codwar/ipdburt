IPDB Sudamericana
==================

Maven Setup
------------

    Run mvn clean install in your project root directory

`In order to generate Eclipse .project, .classpath and .settings files and directories, *run only once*`

    mvn eclipse:configure-workspace -Declipse.workspace=/path/to/workspace
    mvn eclipse:clean eclipse:eclipse

Release Procedure
-----------------

1. Go to the project root
2. Create a file named jipdbs.properties in iddb-web/src/main/resources containing recaptcha.public.key and recaptcha.private.key
3. Configure the necessary properties files in iddb-web/profile/prod/resources. Use the ones in src/main/resources as example
4. Execute in the command line: `mvn clean release:prepare`

This will start an interactive process to confirm the versions being released, the tag to create and the next version

5. Checkout the tagged version (e.g. for the version 0.5): `git checkout v0.5`
6. Goto the web project: `cd jipdbs-web`
7. Deploy to your application server: `mvn tomcat:deploy`
