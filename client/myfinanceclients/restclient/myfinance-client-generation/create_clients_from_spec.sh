#!/bin/bash
version=$1
specfile=$2

DIRNAME=`dirname "$0"`

echo "create rest client with version=$version and specfile=$specfile" 
generatedroot=$DIRNAME/../generated-projects
echo $generatedroot
MAVEN_OPTS="-DsupportingFiles -Dapis=Hello,MyFinance,MyFinanceRunner -Dmodels"

echo $MAVEN_OPTS

generatorGeneral="exec:java -Dexec.mainClass='io.swagger.codegen.SwaggerCodegen' -Dexec.classpathScope=runtime -Dexec.cleanupDaemonThreads=false"

exec mvn $generatorGeneral  -Dexec.args="generate -t $DIRNAME/../myfinance-client-generator/templates/MyFinanceClient_Java --library jersey1 --additional-properties serializableModel=true --artifact-version $version -l de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator -o $generatedroot/myfinance-client-generated-java/ -i $specfile"
