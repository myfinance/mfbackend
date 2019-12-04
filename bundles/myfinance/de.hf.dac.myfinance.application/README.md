Provides the application context of marketdataprovider.

The application module is the only module were we add other implementations(persistence) to setup the app-context dependent on runtime variables like the environment
(just impl's which contains classes with runtime dependencies).
It is not feasible to to import an OSGI-Service from persistence with an parameter "environment" and let the persistence service build the dao-objects with guice,
because if one guice-module(from app) calls another(from persistence) it try to resolve all injects from persistence as well which causes errors(app can not resolve daos without knowing the impl)
all other solutions for these issue work without inject in the persistence module, so we loose control, and it is not bad anyway to have all context-configurations in one place.


OSGI-Services can not evaluate runtime parameters so we have to use guice.