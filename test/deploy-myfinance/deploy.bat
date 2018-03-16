echo "start deploy"
@echo off
setlocal ENABLEDELAYEDEXPANSION

rem ----------------------------------------------------------------------
rem Setup & Initialisierung
rem ----------------------------------------------------------------------
set appname=%~n0
set verbose=0
set mydir=%~dp0
set targetdir=c:\temp
set filename=MyFinance.zip

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
	if [%arg%] == [-t] (
		set targetdir=%~2
		goto next_arg
	)
	if [%arg%] == [-f] (
    	set filename=%~2
    	goto next_arg
    )
	goto usage
:next_arg
	shift
	shift
	goto parse_args
:done_args
	set arg=

rem ----------------------------------------------------------------------
rem deploy
rem ----------------------------------------------------------------------
call:cout %targetdir%
if exist %targetdir%\karaf (
    rmdir %targetdir%\karaf /s /q
)
call:cout %mydir%
call:cout %filename%
mkdir %targetdir%\karaf
copy %mydir%\..\myfinance-full-packaging\target\%filename%-bin.zip %targetdir%\karaf
copy /y %mydir%\..\karaf-full\integrationtest.res %targetdir%\dac.res
cd %targetdir%\karaf
unzip %targetdir%\karaf\%filename%-bin.zip
Start "" "%targetdir%\karaf\%filename%\karaf\bin\karaf_local.bat"

:end_with_msg
	call:cout Done.
	REM /*FALLTHROUGH*/

goto end

rem ----------------------------------------------------------------------
rem Funktionen & Sprungziele
rem ----------------------------------------------------------------------

:cout
	echo %appname%: %*
	goto:EOF

:cerr
	echo %appname%: ERROR: %*
	goto:EOF

:dbgmsg
	if %verbose% neq 0 (
		echo %appname%: %*
	)
	goto:EOF
:usage
	echo.
	echo Deploy myfinance for integrationtests
	echo.
	echo call: %appname%.cmd [OPTIONEN] [
	echo.
	echo Optionen:
	echo.    -v           print additional infos
	echo.    -t [target] change the default target directory
	echo.
	exit /b 1

:failure
	echo %appname%: error see above
	echo.
	exit /b 1

:end
endlocal

