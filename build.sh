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

#lei export JBOSS_DIST="C:/McMaster/2013/Project/jboss/jboss-6.1.0.Final"
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

#export CLASSPATH="$JBOSS_DIST/server/default/lib/jboss-j2ee.jar:$JBOSS_DIST/server/default/deploy/jbossweb.sar/javax.servlet.jar"
export CLASSPATH="$JBOSS_DIST/common/lib/jboss-ejb-api_3.1_spec.jar:$JBOSS_DIST/common/lib/jboss-servlet-api_3.0_spec.jar"

#$JAVACMD -classpath $ANT_CLASSPATH -Dant.home=$ANT_HOME -Djboss.dist=$JBOSS_DIST org.apache.tools.ant.Main "$@"
ant -Djboss.dist=$JBOSS_DIST "$@"
