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
auth.ldap.active = true
auth.ldap.user = cn=XNMEE01,ou=FR,ou=Accounts,dc=dzag,dc=vrnet
auth.ldap.credentials = Ready2Work11#
role.ldap.user = uid=eigenentwicklungen_lesend,ou=Eigenentwicklungen,ou=Special Users,dc=dzbank,dc=vrnet
role.ldap.credentials = eigen310511
role.mappings = poetStatus=admin,poetView=viewer
extendedUserAttrs=sn,givenName,mail,dzdepartment,dzkst,cn,dzuid,dzanrede,dzarbeitsplatz,dzani,dzbereich,dzfaxNumber,dztelNumber,dzlokkurz,dzraum,dzetage,dzgebaeude,dzpnrleit,l,dzkstdn,dzksthierarchy,dzdepartmentlong,dzgeschlecht,initials,employeeNumber,uid
