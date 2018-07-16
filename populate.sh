#! /bin/sh

JAVACMD=java

_CLASSPATH=./build/classes/
#_CLASSPATH=/usr/share/java/mysql.jar:${_CLASSPATH}
_CLASSPATH=${JBOSS_DIST}/common/lib/mysql.jar:${_CLASSPATH}

$JAVACMD -classpath ${_CLASSPATH} edu.nyu.pdsg.tpcw.populate.Populate
