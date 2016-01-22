def targetRepository="file://repository/"
def major=""
def minor=""
def micro=""
def repotype=args[0]
def env=args[1]
if(args.length>2) {
	println args[2]
	def matcher = args[2] =~ /[0-9]+/
	major = matcher[0]
	minor = matcher[1]
	micro = matcher[2]
} else {
	def lastTag = ""
	if(repotype.equals("hg")) {
		lastTag = "hg parents --template {latesttag}".execute().text
	} else {
		lastTag = "git describe --tags".execute().text
	}
	println "lastTag:"+lastTag
	def matcher = lastTag =~ /[0-9]+/
	major = matcher[0]
	minor = matcher[1]
	def oldMicro = matcher[2]
	micro=1+oldMicro.toInteger()
}
println "major:"+major
println "minor:"+minor
println "minor:"+micro
def releaseVersion=major+"."+minor+"."+micro
println "releaseVersion:"+releaseVersion
newDevMicro=1+micro.toInteger()
def developmentVersion=major+"."+minor+"."+newDevMicro+"-SNAPSHOT"
println "developmentVersion:"+developmentVersion

//Aufräumen
File f = new File("../release.properties");
if(f.exists() && !f.isDirectory()) { 
    f.delete();
}
def repoPath = ""
if(env.equals("win")) {
	repoPath = "cmd /c mvn help:evaluate -Dexpression=settings.localRepository | grep -v '[INFO]'".execute().text
} else {
	repoPath = "mvn help:evaluate -Dexpression=settings.localRepository | grep -v '[INFO]'".execute().text
}

println "repopath:"+repoPath
def cleanCmd=""
if(env.equals("win")) {
	cleanCmd = "cmd /c mvn -f ../pom.xml clean"
	repoPath = "D:/repository"
} else {
	cleanCmd = "mvn -f ../pom.xml clean"
	repoPath = "/home/surak/.m2/repository"
	targetRepository="file:///home/surak/.m2/repository/"
}
println "repopath:"+repoPath
println "cleanCmd:"+cleanCmd
def proc = cleanCmd.execute();
proc.waitForProcessOutput(System.out, System.err);


def relaeseCmd=""
if(env.equals("win")) {
	relaeseCmd = "cmd /c mvn --batch-mode -f ../pom.xml -s ../settings.xml release:prepare release:perform -Dusername=surak -Dpassword=1a:2b;3c -DreleaseVersion="+releaseVersion+" -DdevelopmentVersion="+developmentVersion+" -DtargetRepository="+targetRepository+" -DscmConn=scm:hg:file:///Q://marketDataProvider -DtoPush=true"
} else {
	relaeseCmd = "mvn --batch-mode -f ../pom.xml -s ../settingsUbuntu.xml release:prepare release:perform -Dusername=surak -Dpassword=1a:2b;3c -DreleaseVersion="+releaseVersion+" -DdevelopmentVersion="+developmentVersion+" -DtargetRepository="+targetRepository+" -DscmConn=scm:git:ssh://192.168.178.20:122/volume1/Data/workspace/repos/MarketDataProvider -DtoPush=false"
}

println "relaeseCmd:"+relaeseCmd
proc = relaeseCmd.execute();
proc.waitForProcessOutput(System.out, System.err);

proc = cleanCmd.execute();
proc.waitForProcessOutput(System.out, System.err);




def folder = new File( "../../marketdata-bin" )
//def jarfile = new File("D:/repository/de/hf/marketDataProvider/bootstrapper/"+releaseVersion+"/bootstrapper-"+releaseVersion+".jar")
def jarfile = new File(repoPath+"/de/hf/marketDataProvider/bootstrapper/"+releaseVersion+"/bootstrapper-"+releaseVersion+".jar")
if( jarfile.exists() && folder.exists() ) {
  folder.deleteDir();
}
folder.mkdirs()




new File("../../marketdata-bin/bootstrapper-"+releaseVersion+".jar") << jarfile.bytes

new File( folder, 'run.cmd' ).withWriterAppend { w ->
  w << "java -jar bootstrapper-"+releaseVersion+".jar --spring.config.location=application.properties \n"
}


new File( folder, 'run.sh' ).withWriterAppend { w ->
  w << "java -jar bootstrapper-"+releaseVersion+".jar --spring.config.location=application.properties \n"
}
