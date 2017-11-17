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
	if [%arg%] == [-refurl] (
		set REFURL="jdbc:postgresql://"%~2
		shift
		goto next_arg
	)
	if [%arg%] == [-ru] (
		set REFUSER=%~2
		shift
		goto next_arg
	)
	if [%arg%] == [-rp] (
    	set REFPW=%~2
		shift
    	goto next_arg
    )
	if [%arg%] == [-m] (
	    set MODE=%~2
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


if [%MODE%] == [] (
 	call:cerr "Mode is a necessary parameter"
 	goto usage
)

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
rem Select Mode
rem ----------------------------------------------------------------------

IF NOT DEFINED JAVA_OPTS set JAVA_OPTS=

if [%MODE%] == [diffChangeLog] (
 	call:cout mode:diffChangeLog
 	goto diff
)

if [%MODE%] == [migrate] (
 	call:cout mode:migrate
 	goto migrate
)

rem ----------------------------------------------------------------------
rem call Liquibase diff
rem ----------------------------------------------------------------------

:diff
if [%REFURL%] == [] (
 	call:cerr "REFURL is a necessary parameter for mode diffChangeLog"
 	goto usage
)
if [%REFUSER%] == [] (
	call:cerr "REFUSER is a necessary parameter for mode diffChangeLog"
 	goto usage
)
if [%REFPW%] == [] (
 	call:cerr "REFPW is a necessary parameter for mode diffChangeLog"
 	goto usage
)
java -cp "liquibase-core-3.5.3.jar;postgresql-9.4-1203-jdbc42.jar" %JAVA_OPTS% liquibase.integration.commandline.Main --driver=org.postgresql.Driver --classpath=".\postgresql-9.4-1203-jdbc42.jar" --changeLogFile=db.changelogdiff1.xml --url=%URL% --username=%USER% --password=%PW% %MODE% --referenceUrl=%REFURL% --referenceUsername=%REFUSER% --referencePassword=%REFPW% --referenceDriver=org.postgresql.Driver

goto end

rem ----------------------------------------------------------------------
rem call Liquibase migrate
rem ----------------------------------------------------------------------

:migrate

java -cp "liquibase-core-3.5.3.jar;postgresql-9.4-1203-jdbc42.jar" %JAVA_OPTS% liquibase.integration.commandline.Main --driver=org.postgresql.Driver --classpath=".\postgresql-9.4-1203-jdbc42.jar" --changeLogFile=changelog/dac/dac-changelog-master.xml --url=%URL% --username=%USER% --password=%PW% migrate

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
	echo.    -refurl Reference Database url: "<host>:<port>/<db-name>"
	echo.    -ru Reference Database user
	echo.    -rp Reference Database Password
	echo.    -m Mode: diffChangeLog-generate diff-script between db and ref-db
	echo.
	exit /b 1

:failure
	echo %appname%: Abgebrochen wegen vorheriger Fehler.
	echo.
	exit /b 1

:end
endlocal