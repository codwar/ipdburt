IPDB Sudamericana
==================

Maven Setup
------------

## Confire GAE Repositories

    <profile>
        <id>gae-repository</id>
        <activation>
        <activeByDefault>true</activeByDefault>
        </activation>
        <repositories>
        <repository>
            <id>maven-gae-repo</id>
            <url>http://maven-gae-plugin.googlecode.com/svn/repository</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
        </repositories>
        <pluginRepositories>
        <pluginRepository>
            <id>maven-gae-plugins-repo</id>
            <url>http://maven-gae-plugin.googlecode.com/svn/repository</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
        </pluginRepositories>
    </profile>

## Running for the first time

    cd jipdbs-core
    mvn gae:unpack

`Will fetch and store all the GAE SDK files in the local maven repo.`

    cd ..
    mvn clean install

`In order to generate Eclipse .project, .classpath and .settings files and directories, *run only once*`

    mvn eclipse:configure-workspace -Declipse.workspace=/path/to/workspace
    mvn eclipse:clean eclipse:eclipse

Running
--------

    cd jipdbs-web
    mvn gae:run

Debugging
----------

    mvn gae:debug

`This uses JPWD. A connection can be opened from Eclipse Debug as remote app in order to debug.`


Deploy a development version
-----------------------------

1. Go to the web project: `cd jipdbs-web`
2. Deploy: `mvn gae:deploy`


*NOTE: remember to provide a valid recaptcha key.*

Release Procedure
-----------------

1. Go to the project root
2. Execute in the command line: `mvn clean release:prepare`

This will start an interactive process to confirm the versions being released, the tag to create and the next version

3. Checkout the tagged version (e.g. for the version 0.5): `git checkout v0.5`
4. Goto the web project: `cd jipdbs-web`
5. Deploy to appengine: `mvn gae:deploy`


*NOTE: remember to provide a valid recaptcha key.*


