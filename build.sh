#! /bin/sh

if [ -z "$JAVA_HOME" ]
then
JAVACMD=`which java`
if [ -z "$JAVACMD" ]
then
echo "Cannot find JAVA. Please set your PATH."
exit 1
fi
JAVA_BINDIR=`dirname $JAVACMD`
JAVA_HOME=$JAVA_BINDIR/..
fi

if [ -z "$JBOSS_DIST" ]
then
echo "Please set JBOSS_DIST."
exit 1
fi

#JAVACMD=$JAVA_HOME/bin/java

#ANT_CLASSPATH=$JAVA_HOME/lib/tools.jar
#ANT_CLASSPATH=$ANT_HOME/lib/ant.jar:$ANT_CLASSPATH
#ANT_CLASSPATH=$J2EE_HOME/server/default/lib/jboss-j2ee.jar:$ANT_CLASSPATH
#ANT_CLASSPATH=$J2EE_HOME/server/default/lib/javax.servlet.jar:$ANT_CLASSPATH

export CLASSPATH="$JBOSS_DIST/server/default/lib/jboss-j2ee.jar:$JBOSS_DIST/server/default/deploy/jbossweb-tomcat50.sar/servlet-api.jar"

#$JAVACMD -classpath $ANT_CLASSPATH -Dant.home=$ANT_HOME -Djboss.dist=$JBOSS_DIST org.apache.tools.ant.Main "$@"
ant -Djboss.dist=$JBOSS_DIST "$@"
