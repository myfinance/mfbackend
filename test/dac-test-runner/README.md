#  DAC Testautomation
In order to further enhance current unit- and integration testing
this project aims to show a way for more descriptive and maintainable 
tests for the overall project. 

Currently most of the integration tests are run within the black-box tests.
Due to complex data requirements, tests are not simple to set up and describe. 

## Current Approach
Almost all current integration tests are done using a mixture of copied data from 
a production / integration environment. 
Based on these copied data (.csv Files) the tests prepare an in memory database
 

In order to test single features, most of the time these large data setups
are adjusted to include the needed data for the testcase. 
This way it's hard to reason about failures and find the responsible single test
within the dataset. 

## Envisioned Result
This project aims to provide more insight into running tests.
This will include but is not limited to the following objectives: 

* Overall human readable Testintention
* Definition of Preconditions (Date, Times,...)
* Testdata used (Default Sets used, adjustments to the defaults,...)
* Test steps actually to be executed (ledis, staging, aggregation, single units)
* Expected Outcome (Assertion of results in the system. by querying database tables, asserting rest responses from ccr service,...)

