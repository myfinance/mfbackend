@echo off
setlocal ENABLEDELAYEDEXPANSION

rem ----------------------------------------------------------------------
rem Setup & Initialisierung
rem ----------------------------------------------------------------------
set appname=%~n0
set verbose=0
set beta=1
set skipTest=true
set mydir=%~dp0
set targetdir=!mydir!..\modules\deploy\target
set modulesdir=!mydir!..\modules
set mavenversion=0

rem ----------------------------------------------------------------------
rem Argument Parsen
rem ----------------------------------------------------------------------

:parse_args
	if [%~1] == [] (
		goto done_args
	)
	set arg=%~1
	if not [%arg:~0,1%] == [-] (
		goto done_args
	)
	if [%arg%] == [-v] (
		set verbose=1
		call:cout Set Verbose on
		goto next_arg
	)
	if [%arg%] == [-url] (
		set URL="jdbc:postgresql://"%~2
		shift
		goto next_arg
	)
	if [%arg%] == [-u] (
		set USER=%~2
		shift
		goto next_arg
	)
	if [%arg%] == [-p] (
    	set PW=%~2
		shift
    	goto next_arg
    )
	goto usage
:next_arg
	shift
	goto parse_args
:done_args
	set arg=


rem ----------------------------------------------------------------------
rem parameter check
rem ----------------------------------------------------------------------


if [%URL%] == [] (
 	call:cerr "URL is a necessary parameter"
 	goto usage
)
if [%USER%] == [] (
 	call:cerr "USER is a necessary parameter"
	goto usage
)
if [%PW%] == [] (
 	call:cerr "PW is a necessary parameter"
 	goto usage
)


rem ----------------------------------------------------------------------
rem call Liquibase migrate
rem ----------------------------------------------------------------------


java -cp "liquibase-core-3.5.3.jar;postgresql-9.4-1203-jdbc42.jar" %JAVA_OPTS% liquibase.integration.commandline.Main --driver=org.postgresql.Driver --classpath=".\postgresql-9.4-1203-jdbc42.jar" --changeLogFile=changelog/changelog-master.xml --url=%URL% --username=%USER% --password=%PW% update

goto end

rem ----------------------------------------------------------------------
rem Funktionen & Sprungziele
rem ----------------------------------------------------------------------

:cout
	echo %appname%: %*
	goto:EOF

:cerr
	echo %appname%: FEHLER: %*
	goto:EOF

:dbgmsg
	if %verbose% neq 0 (
		echo %appname%: %*
	)
	goto:EOF
:usage
	echo.
	echo Database Update via Liquibase
	echo.
	echo Usage: %appname%.cmd [OPTIONEN] [
	echo.
	echo Optionen:
	echo.    -v print debug info
	echo.    -url Database url: "<host>:<port>/<db-name>"
	echo.    -u Database user
	echo.    -p Database Password
	echo.
	exit /b 1

:failure
	echo %appname%: Abgebrochen wegen vorheriger Fehler.
	echo.
	exit /b 1

:end
endlocal