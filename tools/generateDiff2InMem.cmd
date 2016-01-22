@echo off
setlocal ENABLEDELAYEDEXPANSION

rem ----------------------------------------------------------------------
rem zum generieren der diff-Skripte zwischen prod und dev
rem ----------------------------------------------------------------------

rem ----------------------------------------------------------------------
rem Setup & Initialisierung
rem ----------------------------------------------------------------------

set verbose=1

set liquibaseHome=D:\liquibase-3.4.1-bin
set changeLogFile=db.changelog.diff.xml

set postgresclasspath=/repository/postgresql/postgresql/9.1-901.jdbc4/postgresql-9.1-901.jdbc4.jar
set postgresdriver=org.postgresql.Driver
set postgresurl=jdbc:postgresql://localhost:5432/marketdata
set postgresuser=postgres
set postgrespw=vulkan

set h2devclasspath=/repository/com/h2database/h2/1.4.188/h2-1.4.188.jar
set h2devdriver=org.h2.Driver
set h2devurl=jdbc:h2:file:d:/evalSpringBoot/MarketDataProvider/Bootstrapper/h2dev;AUTO_SERVER=true
set h2devuser=sa
set h2devpw=

set classpath=!postgresclasspath!;!h2devclasspath!
set driver=%postgresdriver%
set url=%postgresurl%
set user=%postgresuser%
set pw=%postgrespw%

set referenceUser=%h2devuser%
set referenceDriver=%h2devdriver%
set referenceUrl=%h2devurl%
set referencepw=%h2devpw%

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
		goto next_arg
	)
	goto usage
:next_arg
	shift
	goto parse_args
:done_args
	set arg=
	
rem ----------------------------------------------------------------------
rem Changelog erzeugen
rem ----------------------------------------------------------------------	

set stmd=%liquibaseHome%\liquibase --driver=%driver% --classpath="!classpath!" --changeLogFile=%changeLogFile% --url="!url!" --username=%user% --password=%pw% diffChangeLog --referenceUsername=%referenceUser% --referenceDriver=%referenceDriver% --referenceUrl="!referenceUrl!" --referencePassword="%referencepw%"
call:dbgmsg statement:!stmd!

call !stmd!

rem liquibase --driver=org.postgresql.Driver --classpath=".\postgresql-9.1-901.jdbc4.jar;h2-1.4.188.jar" --changeLogFile=db.changelogdiff1.xml --url="jdbc:postgresql://localhost:5432/marketdata" --username=postgres --password=vulkan diffChangeLog --referenceUsername=sa --referenceDriver=org.h2.Driver --referenceUrl="jdbc:h2:file:d:/evalSpringBoot/MarketDataProvider/Bootstrapper/h2dev;AUTO_SERVER=true"

:end_with_msg
	call:cout Fertig.
	REM /*FALLTHROUGH*/

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
	echo Berechnung der aktuellen nächsten Version anhand von Mercurial-Tags
	echo.
	echo Aufruf: %appname%.cmd [OPTIONEN] [
	echo.
	echo Optionen:
	echo.    -v            Zusaetzliche Meldungen ausgeben.
	echo.
	exit /b 1
	
:failure
	echo %appname%: Abgebrochen wegen vorheriger Fehler.
	echo.
	exit /b 1
	
:end
endlocal
