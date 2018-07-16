@echo off
setlocal

set ANT_CLASSPATH=%JAVA_HOME%/lib/tools.jar
set ANT_CLASSPATH=%ANT_HOME%/lib/ant.jar;%ANT_CLASSPATH%
set ANT_CLASSPATH=%JBOSS_DIST%/server/default/lib/jboss-j2ee.jar;%ANT_CLASSPATH%
set ANT_CLASSPATH=%JBOSS_DIST%/server/default/lib/javax.servlet.jar;%ANT_CLASSPATH%

%JAVA_HOME%\bin\java -classpath "%ANT_CLASSPATH%" -Djboss.dist=%JBOSS_DIST% -Dant.home=%ANT_HOME% org.apache.tools.ant.Main %1 %2 %3 %4
