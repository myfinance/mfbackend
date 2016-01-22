@echo off
setlocal ENABLEDELAYEDEXPANSION

rem ----------------------------------------------------------------------
rem Setup & Initialisierung
rem ----------------------------------------------------------------------
set version=0
set verbose=0


rem ----------------------------------------------------------------------
rem Argument Parsen
rem ----------------------------------------------------------------------

:parse_args
	if [%~1] == [] (
		goto done_args
	)
	set arg=%~1
	if not [%arg:~0,1%] == [-] (
		set version=%arg%
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
rem start build
rem ----------------------------------------------------------------------	

if [%version%] == [0] (
    call:dbgmsg "java -cp groovy-2.4.0.jar groovy.lang.GroovyShell build.groovy hg win"
	java -cp groovy-2.4.0.jar groovy.lang.GroovyShell build.groovy hg win
)else (
	call:dbgmsg "java -cp groovy-2.4.0.jar groovy.lang.GroovyShell build.groovy hg win %version%"
	java -cp groovy-2.4.0.jar groovy.lang.GroovyShell build.groovy hg win %version%
)
hg push

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
	echo.    -o            Erstellen einer offiziellen Version. Es wird ein Tag erzeugt
	echo.
	exit /b 1
	
:failure
	echo %appname%: Abgebrochen wegen vorheriger Fehler.
	echo.
	exit /b 1
	
:end
endlocal