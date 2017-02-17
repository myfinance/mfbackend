echo off
IF [%1]==[] echo Version Missing && goto:eof

set version=%1

set specfile=%2

set generatedroot=%~dp0/generated-projects

set MAVEN_OPTS=-DsupportingFiles -Dapis=Hello,Marketdata -Dmodels

echo %MAVEN_OPTS%

set generatorGeneral=exec:java -pl marketdata-client-generation -Dexec.mainClass="io.swagger.codegen.SwaggerCodegen"  -Dexec.classpathScope=runtime -Dexec.cleanupDaemonThreads=false

call mvn %generatorGeneral%  -Dexec.args="generate  -t %~dp0marketdata-client-generator\templates\MarketDataClient_Java --library jersey1 --additional-properties serializableModel=true --artifact-version %version%  -l de.hf.dac.marketdata.codegen.MarketDataClient_JavaGenerator -o %generatedroot%/marketdata-client-generated-java/ -i %specfile%"
::call mvn %generatorGeneral%  -Dexec.args="generate  -l de.dzbank.poet.codegen.CCRClient_PythonGenerator -o %generatedroot%/ccr-client-generated-python/ -i %specfile%"
::call mvn %generatorGeneral%  -Dexec.args="generate  -l de.dzbank.poet.codegen.CCRClient_Python3Generator -o %generatedroot%/ccr-client-generated-python3/ -i %specfile%"
::call mvn %generatorGeneral%  -Dexec.args="generate -t %~dp0ccr-client-generation\templates\CCRClient_Scala --artifact-version %version%  -l de.dzbank.poet.codegen.CCRClient_ScalaGenerator -o %generatedroot%/ccr-client-generated-scala/ -i %specfile%"
::call mvn %generatorGeneral%  -Dexec.args="generate  -l de.dzbank.poet.codegen.CCRClient_JavascriptGenerator -o %generatedroot%/ccr-client-generated-javascript/ -i %specfile%"
::call mvn %generatorGeneral%  -Dexec.args="generate  -l html -o %generatedroot%/ccr-client-htmldoc/ -i %specfile%"
::call mvn %generatorGeneral%  -Dexec.args="generate  -l jmeter -o %generatedroot%/ccr-client-jmeter/ -i %specfile%"