# UtilityResourcesApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getInstrumentForTenantList_envID_tenant**](UtilityResourcesApi.md#getInstrumentForTenantList_envID_tenant) | **GET** /myfinance/environments/{envID}/instrumentsfortenant | List Data
[**getInstrumentList_envID**](UtilityResourcesApi.md#getInstrumentList_envID) | **GET** /myfinance/environments/{envID}/instruments | List Data
[**getInstrumentPerTypeList_envID_tenant_instrumenttype**](UtilityResourcesApi.md#getInstrumentPerTypeList_envID_tenant_instrumenttype) | **GET** /myfinance/environments/{envID}/instrumentspertype | List Data
[**getInstrument_envID_isin**](UtilityResourcesApi.md#getInstrument_envID_isin) | **GET** /myfinance/environments/{envID}/getequity | get Data
[**getTenantList_envID**](UtilityResourcesApi.md#getTenantList_envID) | **GET** /myfinance/environments/{envID}/listTenants | List Data
[**getTransactionList_envID_startdate_enddate**](UtilityResourcesApi.md#getTransactionList_envID_startdate_enddate) | **GET** /myfinance/environments/{envID}/listTransactions | List Data
[**getValueMap_envID_instrumentId_startdate_enddate**](UtilityResourcesApi.md#getValueMap_envID_instrumentId_startdate_enddate) | **GET** /myfinance/environments/{envID}/getvaluecurve/{instrumentId} | Map Data


<a name="getInstrumentForTenantList_envID_tenant"></a>
# **getInstrumentForTenantList_envID_tenant**
> InstrumentListModel getInstrumentForTenantList_envID_tenant(envID, tenant)

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
String envID = "envID_example"; // String | The Service Environment
Integer tenant = 56; // Integer | tenant id
try {
    InstrumentListModel result = apiInstance.getInstrumentForTenantList_envID_tenant(envID, tenant);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getInstrumentForTenantList_envID_tenant");
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
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
String envID = "envID_example"; // String | The Service Environment
try {
    InstrumentListModel result = apiInstance.getInstrumentList_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getInstrumentList_envID");
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
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
String envID = "envID_example"; // String | The Service Environment
Integer tenant = 56; // Integer | tenant id
String instrumenttype = "instrumenttype_example"; // String | instrumenttype
try {
    InstrumentListModel result = apiInstance.getInstrumentPerTypeList_envID_tenant_instrumenttype(envID, tenant, instrumenttype);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getInstrumentPerTypeList_envID_tenant_instrumenttype");
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
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
String envID = "envID_example"; // String | The Service Environment
String isin = "isin_example"; // String | the isin
try {
    InstrumentModel result = apiInstance.getInstrument_envID_isin(envID, isin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getInstrument_envID_isin");
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
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
String envID = "envID_example"; // String | The Service Environment
try {
    InstrumentListModel result = apiInstance.getTenantList_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getTenantList_envID");
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
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
String envID = "envID_example"; // String | The Service Environment
String startdate = "startdate_example"; // String | startdate in Format yyyy-mm-dd
String enddate = "enddate_example"; // String | enddate in Format yyyy-mm-dd
try {
    TransactionListModel result = apiInstance.getTransactionList_envID_startdate_enddate(envID, startdate, enddate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getTransactionList_envID_startdate_enddate");
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
//import de.hf.dac.myfinance.client.api.UtilityResourcesApi;


UtilityResourcesApi apiInstance = new UtilityResourcesApi();
Integer instrumentId = 56; // Integer | the instrumentId
String envID = "envID_example"; // String | The Service Environment
String startdate = "startdate_example"; // String | startdate in Format yyyy-mm-dd
String enddate = "enddate_example"; // String | enddate in Format yyyy-mm-dd
try {
    DateDoubleModel result = apiInstance.getValueMap_envID_instrumentId_startdate_enddate(instrumentId, envID, startdate, enddate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UtilityResourcesApi#getValueMap_envID_instrumentId_startdate_enddate");
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

