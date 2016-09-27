Bundle reads with the OSGI-Service EnvironmentConfiguration  properties from a Res-Files. 
The following Res-files were considered: 
     * 1. get path to a res.file with login informations from Environment variable DAC_LOGIN_INFO - only admin should have read access to this file
     * 2. get path to dac.res from Environment variable CAD_RES_PATH - resfile for an environment like UAT or Production - with all informations even 3rd level support are allowed to read
     * 3. get dac.res from target/.. directory for development use
     * 4. get dev.res from target/.. directory for local overrides from a single developer (never check this in)
If a property is found in more then one file the last file wins (in the order described above)

The EnvironmentService (an OSGI-Service) opens with help of the EnvironmentConfiguration-Service a Bootstrap-DB-Connection to the table dacenvironmentconfiguration.
There are different environments defined like dev, test, prod, analysis...
For each environment exists a set of targets. A target is a Database, a cache, a Service-Url or a different datasource (You can use only database yet). 

The service reads for each combination of environment and target an Identifiert and uses these to get the connectiondetails from the res-file.

Why are the details in the res-file again and not in the database? Because the res-file is a central resource in our case which is used by many components, so we don't want to define each connection twice.
But it is planed to implement the possibility to read the connection instead of the identifier from the table.

