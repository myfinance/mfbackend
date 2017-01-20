[CONFIGDB_BOOTSTRAP_SECTION]
BOOTSTRAP_USER=postgres
BOOTSTRAP_PWD=vulkan
BOOTSTRAP_URL=jdbc:postgresql://localhost:5432/marketdata
BOOTSTRAP_DRIVER=org.postgresql.Driver

[LOGIN_INFO]
DEVPOSTGRES=jdbc:postgresql://localhost:5432/marketdata,postgres,vulkan,org.postgresql.Driver
H2DEV=jdbc:h2:file:./h2dev;AUTO_SERVER=TRUE,sa,sa,org.h2.Driver

[EMFB]
POOL_SIZE=20

[LDAP]
auth.ldap.active = false
auth.ldap.url = ldap://ldap.dzag.vrnet
auth.ldap.baseCtxDN = ou=Accounts,dc=dzag,dc=vrnet
auth.ldap.rolesCtxDN = ou=Accounts,dc=dzag,dc=vrnet
auth.ldap.user = cn=XNMEE01,ou=FR,ou=Accounts,dc=dzag,dc=vrnet
auth.ldap.credentials = Ready2Work11#
role.ldap.user = uid=eigenentwicklungen_lesend,ou=Eigenentwicklungen,ou=Special Users,dc=dzbank,dc=vrnet
role.ldap.credentials = eigen310511
role.ldap.url = ldap://dfvvpldps1.dzbank.vrnet:389
role.ldap.baseCtxDN = ou=People,dc=dzbank,dc=vrnet
role.ldap.rolesCtxDN = ou=Rollen,ou=Test,ou=PoET,ou=Eigenentwicklungen,ou=Anwendungen,dc=dzbank,dc=vrnet
#to map the persions from the ldap to internal roles. for karaf admin and view are necessary
role.mappings = poetStatus=admin,poetView=viewer
role.userAttributes = sn,givenName,mail,dzdepartment,dzkst,cn,dzuid,dzanrede,dzarbeitsplatz,dzani,dzbereich,dzfaxNumber,dztelNumber,dzlokkurz,dzraum,dzetage,dzgebaeude,dzpnrleit,l,dzkstdn,dzksthierarchy,dzdepartmentlong,dzgeschlecht,initials,employeeNumber,uid

[CM_CONFIG]
pids=org.ops4j.pax.web

[CMPID_org.ops4j.pax.web]
org.ops4j.pax.web.ssl.keystore=D:/devenv/bitbucket/dac/bootstrapper/karaf-stable/devkeystore.jks
org.ops4j.pax.web.ssl.password = password
org.ops4j.pax.web.ssl.keypassword = password
org.osgi.service.http.port.secure = 8443
org.osgi.service.http.secure.enabled = true
