rem ----------------------------------------------------------------------
rem zum generieren der initialskripte aus einer referenzdatenbank
rem ----------------------------------------------------------------------
@echo off
setlocal ENABLEDELAYEDEXPANSION

rem ----------------------------------------------------------------------
rem Setup & Initialisierung
rem ----------------------------------------------------------------------

set verbose=0

set liquibaseHome=D:\liquibase-3.4.1-bin
set changeLogFile=db.changelog.xml

set classpath=D:\repository\postgresql\postgresql\9.1-901.jdbc4\postgresql-9.1-901.jdbc4.jar
set driver=org.postgresql.Driver
set url=jdbc:postgresql://localhost:5432/marketdata
set user=postgres
set pw=vulkan

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

call %liquibaseHome%\liquibase --driver=%driver% --classpath=%classpath% --changeLogFile=%changeLogFile% --url=%url% --username=%user% --password=%pw% generateChangeLog

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