#! /bin/sh

JAVACMD=java

_CLASSPATH=./build/classes/
_CLASSPATH=/usr/local/jboss/server/default/lib/mysql.jar:${_CLASSPATH}

$JAVACMD -classpath ${_CLASSPATH} edu.nyu.pdsg.tpcw.populate.Populate
