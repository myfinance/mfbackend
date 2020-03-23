# MyFinanceApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addBudget_envID_description_budgetGroupId**](MyFinanceApi.md#addBudget_envID_description_budgetGroupId) | **POST** /myfinance/environments/{envID}/addBudget | add Budget
[**addCurrency_envID_currencyCode_description**](MyFinanceApi.md#addCurrency_envID_currencyCode_description) | **POST** /myfinance/environments/{envID}/addCurrency | save Instrument
[**addEquity_envID_isin_description**](MyFinanceApi.md#addEquity_envID_isin_description) | **POST** /myfinance/environments/{envID}/addEquity | save Instrument
[**addGiro_envID_description_tenantId**](MyFinanceApi.md#addGiro_envID_description_tenantId) | **POST** /myfinance/environments/{envID}/addGiro | save Giro
[**addIncomeExpense_envID_description_accId_budgetId_value_transactiondate**](MyFinanceApi.md#addIncomeExpense_envID_description_accId_budgetId_value_transactiondate) | **POST** /myfinance/environments/{envID}/addIncomeExpense | save Income or Expense
[**addPrice_envID_currencyCode_isin_dayofprice_value**](MyFinanceApi.md#addPrice_envID_currencyCode_isin_dayofprice_value) | **POST** /myfinance/environments/{envID}/addPrice | save Price
[**addSymbol_envID_isin_symbol_currencycode**](MyFinanceApi.md#addSymbol_envID_isin_symbol_currencycode) | **POST** /myfinance/environments/{envID}/addSymbol | save Instrument
[**addTenant_envID_description**](MyFinanceApi.md#addTenant_envID_description) | **POST** /myfinance/environments/{envID}/addTenant | save Tenant
[**addTransfer_envID_description_srcId_trgId_value_transactiondate**](MyFinanceApi.md#addTransfer_envID_description_srcId_trgId_value_transactiondate) | **POST** /myfinance/environments/{envID}/addTransfer | save Transfer
[**delTransfer_envID_transactionId**](MyFinanceApi.md#delTransfer_envID_transactionId) | **POST** /myfinance/environments/{envID}/delTransfer | delete Transfer
[**fillPricesHistory_envID_sourceId_isin**](MyFinanceApi.md#fillPricesHistory_envID_sourceId_isin) | **POST** /myfinance/environments/{envID}/fillpricehistory | fillpricehistory
[**getEnvironmentList**](MyFinanceApi.md#getEnvironmentList) | **GET** /myfinance/environments/list | List Environments
[**getInstrumentForTenantList_envID_tenant**](MyFinanceApi.md#getInstrumentForTenantList_envID_tenant) | **GET** /myfinance/environments/{envID}/instrumentsfortenant | List Data
[**getInstrumentList_envID**](MyFinanceApi.md#getInstrumentList_envID) | **GET** /myfinance/environments/{envID}/instruments | List Data
[**getInstrumentPerTypeList_envID_tenant_instrumenttype**](MyFinanceApi.md#getInstrumentPerTypeList_envID_tenant_instrumenttype) | **GET** /myfinance/environments/{envID}/instrumentspertype | List Data
[**getInstrument_envID_isin**](MyFinanceApi.md#getInstrument_envID_isin) | **GET** /myfinance/environments/{envID}/getequity | get Data
[**getTenantList_envID**](MyFinanceApi.md#getTenantList_envID) | **GET** /myfinance/environments/{envID}/listTenants | List Data
[**getTransactionList_envID_startdate_enddate**](MyFinanceApi.md#getTransactionList_envID_startdate_enddate) | **GET** /myfinance/environments/{envID}/listTransactions | List Data
[**getValueMap_envID_instrumentId_startdate_enddate**](MyFinanceApi.md#getValueMap_envID_instrumentId_startdate_enddate) | **GET** /myfinance/environments/{envID}/getvaluecurve/{instrumentId} | Map Data
[**importPrices_envID**](MyFinanceApi.md#importPrices_envID) | **POST** /myfinance/environments/{envID}/importprices | importprices
[**updateInstrument_envID_id_description_isactive**](MyFinanceApi.md#updateInstrument_envID_id_description_isactive) | **POST** /myfinance/environments/{envID}/updateInstrument | update Instrument


<a name="addBudget_envID_description_budgetGroupId"></a>
# **addBudget_envID_description_budgetGroupId**
> addBudget_envID_description_budgetGroupId(envID, description, budgetGroupId)

add Budget



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String description = "description_example"; // String | description
Integer budgetGroupId = 56; // Integer | the Id of the budgetGroup which the budget is attached to
try {
    apiInstance.addBudget_envID_description_budgetGroupId(envID, description, budgetGroupId);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addBudget_envID_description_budgetGroupId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **description** | **String**| description | [optional]
 **budgetGroupId** | **Integer**| the Id of the budgetGroup which the budget is attached to | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addCurrency_envID_currencyCode_description"></a>
# **addCurrency_envID_currencyCode_description**
> addCurrency_envID_currencyCode_description(envID, currencyCode, description)

save Instrument



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String currencyCode = "currencyCode_example"; // String | the currencyCode
String description = "description_example"; // String | description
try {
    apiInstance.addCurrency_envID_currencyCode_description(envID, currencyCode, description);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addCurrency_envID_currencyCode_description");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **currencyCode** | **String**| the currencyCode | [optional]
 **description** | **String**| description | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addEquity_envID_isin_description"></a>
# **addEquity_envID_isin_description**
> addEquity_envID_isin_description(envID, isin, description)

save Instrument



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String isin = "isin_example"; // String | the isin
String description = "description_example"; // String | description
try {
    apiInstance.addEquity_envID_isin_description(envID, isin, description);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addEquity_envID_isin_description");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **isin** | **String**| the isin | [optional]
 **description** | **String**| description | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addGiro_envID_description_tenantId"></a>
# **addGiro_envID_description_tenantId**
> addGiro_envID_description_tenantId(envID, description, tenantId)

save Giro



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String description = "description_example"; // String | description
Integer tenantId = 56; // Integer | the Id of the tenant which the giro is attached to
try {
    apiInstance.addGiro_envID_description_tenantId(envID, description, tenantId);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addGiro_envID_description_tenantId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **description** | **String**| description | [optional]
 **tenantId** | **Integer**| the Id of the tenant which the giro is attached to | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addIncomeExpense_envID_description_accId_budgetId_value_transactiondate"></a>
# **addIncomeExpense_envID_description_accId_budgetId_value_transactiondate**
> addIncomeExpense_envID_description_accId_budgetId_value_transactiondate(envID, description, accId, budgetId, value, transactiondate)

save Income or Expense



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String description = "description_example"; // String | description
Integer accId = 56; // Integer | the accountId of the income or expense
Integer budgetId = 56; // Integer | the budgetId of the income or expense
Double value = 3.4D; // Double | the value of the income or expense
String transactiondate = "transactiondate_example"; // String | the transactiondate(yyyy-mm-dd
try {
    apiInstance.addIncomeExpense_envID_description_accId_budgetId_value_transactiondate(envID, description, accId, budgetId, value, transactiondate);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addIncomeExpense_envID_description_accId_budgetId_value_transactiondate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **description** | **String**| description | [optional]
 **accId** | **Integer**| the accountId of the income or expense | [optional]
 **budgetId** | **Integer**| the budgetId of the income or expense | [optional]
 **value** | **Double**| the value of the income or expense | [optional]
 **transactiondate** | **String**| the transactiondate(yyyy-mm-dd | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addPrice_envID_currencyCode_isin_dayofprice_value"></a>
# **addPrice_envID_currencyCode_isin_dayofprice_value**
> addPrice_envID_currencyCode_isin_dayofprice_value(envID, currencyCode, isin, dayofprice, value)

save Price



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String currencyCode = "currencyCode_example"; // String | the currencyCode
String isin = "isin_example"; // String | the isin
String dayofprice = "dayofprice_example"; // String | the dayofprice(yyyy-mm-dd
Double value = 3.4D; // Double | value
try {
    apiInstance.addPrice_envID_currencyCode_isin_dayofprice_value(envID, currencyCode, isin, dayofprice, value);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addPrice_envID_currencyCode_isin_dayofprice_value");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **currencyCode** | **String**| the currencyCode | [optional]
 **isin** | **String**| the isin | [optional]
 **dayofprice** | **String**| the dayofprice(yyyy-mm-dd | [optional]
 **value** | **Double**| value | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addSymbol_envID_isin_symbol_currencycode"></a>
# **addSymbol_envID_isin_symbol_currencycode**
> addSymbol_envID_isin_symbol_currencycode(envID, isin, symbol, currencycode)

save Instrument



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String isin = "isin_example"; // String | the isin
String symbol = "symbol_example"; // String | symbol
String currencycode = "currencycode_example"; // String | the code of the currency in which the security is traded in the exchange referenced by the symbol
try {
    apiInstance.addSymbol_envID_isin_symbol_currencycode(envID, isin, symbol, currencycode);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addSymbol_envID_isin_symbol_currencycode");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **isin** | **String**| the isin | [optional]
 **symbol** | **String**| symbol | [optional]
 **currencycode** | **String**| the code of the currency in which the security is traded in the exchange referenced by the symbol | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addTenant_envID_description"></a>
# **addTenant_envID_description**
> addTenant_envID_description(envID, description)

save Tenant



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String description = "description_example"; // String | description
try {
    apiInstance.addTenant_envID_description(envID, description);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addTenant_envID_description");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **description** | **String**| description | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addTransfer_envID_description_srcId_trgId_value_transactiondate"></a>
# **addTransfer_envID_description_srcId_trgId_value_transactiondate**
> addTransfer_envID_description_srcId_trgId_value_transactiondate(envID, description, srcId, trgId, value, transactiondate)

save Transfer



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String description = "description_example"; // String | description
Integer srcId = 56; // Integer | the instrumentId of the source
Integer trgId = 56; // Integer | the instrumentId of the target
Double value = 3.4D; // Double | the value of the income or expense
String transactiondate = "transactiondate_example"; // String | the transactiondate(yyyy-mm-dd
try {
    apiInstance.addTransfer_envID_description_srcId_trgId_value_transactiondate(envID, description, srcId, trgId, value, transactiondate);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#addTransfer_envID_description_srcId_trgId_value_transactiondate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **description** | **String**| description | [optional]
 **srcId** | **Integer**| the instrumentId of the source | [optional]
 **trgId** | **Integer**| the instrumentId of the target | [optional]
 **value** | **Double**| the value of the income or expense | [optional]
 **transactiondate** | **String**| the transactiondate(yyyy-mm-dd | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="delTransfer_envID_transactionId"></a>
# **delTransfer_envID_transactionId**
> delTransfer_envID_transactionId(envID, transactionId)

delete Transfer



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
Integer transactionId = 56; // Integer | transactionId
try {
    apiInstance.delTransfer_envID_transactionId(envID, transactionId);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#delTransfer_envID_transactionId");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **transactionId** | **Integer**| transactionId | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="fillPricesHistory_envID_sourceId_isin"></a>
# **fillPricesHistory_envID_sourceId_isin**
> fillPricesHistory_envID_sourceId_isin(envID, sourceId, isin)

fillpricehistory



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
Integer sourceId = 56; // Integer | the sourceId
String isin = "isin_example"; // String | the isin
try {
    apiInstance.fillPricesHistory_envID_sourceId_isin(envID, sourceId, isin);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#fillPricesHistory_envID_sourceId_isin");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **sourceId** | **Integer**| the sourceId | [optional]
 **isin** | **String**| the isin | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getEnvironmentList"></a>
# **getEnvironmentList**
> StringListModel getEnvironmentList()

List Environments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
try {
    StringListModel result = apiInstance.getEnvironmentList();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getEnvironmentList");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**StringListModel**](StringListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getInstrumentForTenantList_envID_tenant"></a>
# **getInstrumentForTenantList_envID_tenant**
> InstrumentListModel getInstrumentForTenantList_envID_tenant(envID, tenant)

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
Integer tenant = 56; // Integer | tenant id
try {
    InstrumentListModel result = apiInstance.getInstrumentForTenantList_envID_tenant(envID, tenant);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getInstrumentForTenantList_envID_tenant");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **tenant** | **Integer**| tenant id | [optional]

### Return type

[**InstrumentListModel**](InstrumentListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getInstrumentList_envID"></a>
# **getInstrumentList_envID**
> InstrumentListModel getInstrumentList_envID(envID)

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
try {
    InstrumentListModel result = apiInstance.getInstrumentList_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getInstrumentList_envID");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |

### Return type

[**InstrumentListModel**](InstrumentListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getInstrumentPerTypeList_envID_tenant_instrumenttype"></a>
# **getInstrumentPerTypeList_envID_tenant_instrumenttype**
> InstrumentListModel getInstrumentPerTypeList_envID_tenant_instrumenttype(envID, tenant, instrumenttype)

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
Integer tenant = 56; // Integer | tenant id
String instrumenttype = "instrumenttype_example"; // String | instrumenttype
try {
    InstrumentListModel result = apiInstance.getInstrumentPerTypeList_envID_tenant_instrumenttype(envID, tenant, instrumenttype);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getInstrumentPerTypeList_envID_tenant_instrumenttype");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **tenant** | **Integer**| tenant id | [optional]
 **instrumenttype** | **String**| instrumenttype | [optional] [enum: Giro, MoneyAtCall, TimeDeposit, BuildingsavingAccount, Budget, Tenant, AccountPortfolio, ArtificialPortfolio, BudgetGroup, Depot, Buildingsaving, Currency, Equity, Fonds, ETF, Index, Bond, LifeInsurance, DepreciationObject, RealEstate, Loan, UNKNOWN]

### Return type

[**InstrumentListModel**](InstrumentListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getInstrument_envID_isin"></a>
# **getInstrument_envID_isin**
> InstrumentModel getInstrument_envID_isin(envID, isin)

get Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String isin = "isin_example"; // String | the isin
try {
    InstrumentModel result = apiInstance.getInstrument_envID_isin(envID, isin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getInstrument_envID_isin");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **isin** | **String**| the isin | [optional]

### Return type

[**InstrumentModel**](InstrumentModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTenantList_envID"></a>
# **getTenantList_envID**
> InstrumentListModel getTenantList_envID(envID)

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
try {
    InstrumentListModel result = apiInstance.getTenantList_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getTenantList_envID");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |

### Return type

[**InstrumentListModel**](InstrumentListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getTransactionList_envID_startdate_enddate"></a>
# **getTransactionList_envID_startdate_enddate**
> TransactionListModel getTransactionList_envID_startdate_enddate(envID, startdate, enddate)

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String startdate = "startdate_example"; // String | startdate in Format yyyy-mm-dd
String enddate = "enddate_example"; // String | enddate in Format yyyy-mm-dd
try {
    TransactionListModel result = apiInstance.getTransactionList_envID_startdate_enddate(envID, startdate, enddate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getTransactionList_envID_startdate_enddate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **startdate** | **String**| startdate in Format yyyy-mm-dd | [optional]
 **enddate** | **String**| enddate in Format yyyy-mm-dd | [optional]

### Return type

[**TransactionListModel**](TransactionListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getValueMap_envID_instrumentId_startdate_enddate"></a>
# **getValueMap_envID_instrumentId_startdate_enddate**
> DateDoubleModel getValueMap_envID_instrumentId_startdate_enddate(instrumentId, envID, startdate, enddate)

Map Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
Integer instrumentId = 56; // Integer | the instrumentId
String envID = "envID_example"; // String | The Service Environment
String startdate = "startdate_example"; // String | startdate in Format yyyy-mm-dd
String enddate = "enddate_example"; // String | enddate in Format yyyy-mm-dd
try {
    DateDoubleModel result = apiInstance.getValueMap_envID_instrumentId_startdate_enddate(instrumentId, envID, startdate, enddate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getValueMap_envID_instrumentId_startdate_enddate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **instrumentId** | **Integer**| the instrumentId |
 **envID** | **String**| The Service Environment |
 **startdate** | **String**| startdate in Format yyyy-mm-dd | [optional]
 **enddate** | **String**| enddate in Format yyyy-mm-dd | [optional]

### Return type

[**DateDoubleModel**](DateDoubleModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="importPrices_envID"></a>
# **importPrices_envID**
> importPrices_envID(envID)

importprices



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
try {
    apiInstance.importPrices_envID(envID);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#importPrices_envID");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="updateInstrument_envID_id_description_isactive"></a>
# **updateInstrument_envID_id_description_isactive**
> updateInstrument_envID_id_description_isactive(envID, id, description, isactive)

update Instrument



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
Integer id = 56; // Integer | id
String description = "description_example"; // String | description
Boolean isactive = true; // Boolean | isactive
try {
    apiInstance.updateInstrument_envID_id_description_isactive(envID, id, description, isactive);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#updateInstrument_envID_id_description_isactive");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **id** | **Integer**| id | [optional]
 **description** | **String**| description | [optional]
 **isactive** | **Boolean**| isactive | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

