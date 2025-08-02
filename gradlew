#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx2048m -Xms512m"

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# Check if gradle-wrapper.jar exists and download if needed
check_and_download_wrapper() {
    WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
    if [ ! -f "$WRAPPER_JAR" ] || [ $(stat -c%s "$WRAPPER_JAR" 2>/dev/null || stat -f%z "$WRAPPER_JAR" 2>/dev/null || echo 0) -lt 50000 ]; then
        echo "Downloading Gradle wrapper..."
        mkdir -p "$APP_HOME/gradle/wrapper"
        
        # Download the actual Gradle wrapper JAR
        if command -v curl >/dev/null 2>&1; then
            # Download from a direct reliable source
            curl -L -o "$WRAPPER_JAR" "https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar" ||
            curl -L -o "$WRAPPER_JAR" "https://github.com/gradle/gradle/releases/download/v8.2.0/gradle-8.2-bin.zip" -o temp.zip && unzip -j temp.zip "gradle-8.2/gradle/wrapper/gradle-wrapper.jar" -d "$APP_HOME/gradle/wrapper/" && rm -f temp.zip ||
            {
                echo "Downloading Gradle distribution and extracting wrapper..."
                curl -L -o gradle-temp.zip "https://services.gradle.org/distributions/gradle-8.2-bin.zip"
                if [ -f gradle-temp.zip ]; then
                    unzip -j gradle-temp.zip "gradle-8.2/gradle/wrapper/gradle-wrapper.jar" -d "$APP_HOME/gradle/wrapper/" 2>/dev/null
                    rm -f gradle-temp.zip
                fi
            }
        elif command -v wget >/dev/null 2>&1; then
            wget -O "$WRAPPER_JAR" "https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar" ||
            {
                echo "Downloading Gradle distribution and extracting wrapper..."
                wget -O gradle-temp.zip "https://services.gradle.org/distributions/gradle-8.2-bin.zip"
                if [ -f gradle-temp.zip ]; then
                    unzip -j gradle-temp.zip "gradle-8.2/gradle/wrapper/gradle-wrapper.jar" -d "$APP_HOME/gradle/wrapper/" 2>/dev/null
                    rm -f gradle-temp.zip
                fi
            }
        else
            die "ERROR: curl or wget is required to download Gradle wrapper"
        fi
        
        if [ ! -f "$WRAPPER_JAR" ] || [ $(stat -c%s "$WRAPPER_JAR" 2>/dev/null || stat -f%z "$WRAPPER_JAR" 2>/dev/null || echo 0) -lt 50000 ]; then
            die "ERROR: Failed to download Gradle wrapper. Please manually download gradle-wrapper.jar from https://services.gradle.org/distributions/gradle-8.2-bin.zip and extract it to gradle/wrapper/"
        fi
        echo "Gradle wrapper downloaded successfully ($(stat -c%s "$WRAPPER_JAR" 2>/dev/null || stat -f%z "$WRAPPER_JAR" 2>/dev/null) bytes)"
    fi
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

# Check and download wrapper before setting classpath
check_and_download_wrapper

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$cygwin" = "false" -a "$darwin" = "false" -a "$nonstop" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS -Dorg.gradle.appname="$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
