[CONFIGDB_BOOTSTRAP_SECTION]
BOOTSTRAP_USER=postgres
BOOTSTRAP_PWD=vulkan
BOOTSTRAP_URL=jdbc:postgresql://postgres_mf:5432/marketdata
BOOTSTRAP_DRIVER=org.postgresql.Driver

[LOGIN_INFO]
DEVPOSTGRES=jdbc:postgresql://postgres_mf:5432/marketdata,postgres,vulkan,org.postgresql.Driver
H2DEV=jdbc:h2:file:./h2dev;AUTO_SERVER=TRUE,sa,sa,org.h2.Driver

[EMFB]
POOL_SIZE=20

[CM_CONFIG]
pids=org.ops4j.pax.web,DAC.DacJaasRealmService

[CMPID_org.ops4j.pax.web]
#replace in production with keystore containing trusted certificate
org.ops4j.pax.web.ssl.keystore=/MyFinance/envconfig/devkeystore.jks
org.ops4j.pax.web.ssl.password = password
org.ops4j.pax.web.ssl.keypassword = password
#should be true in Production
org.osgi.service.http.secure.enabled = false
#should be false in Production
org.osgi.service.http.enabled = true

[CMPID_DAC.DacJaasRealmService]
#ldap,off or some thing else for karaf default
auth.mode=karaf

[MARKETDATA]
MARKETDATA_LAUNCH_USER=karaf
MARKETDATA_LAUNCH_PASSWORD=karaf
MARKETDATA_LAUNCH_URL=https://localhost:8443/dac/rest