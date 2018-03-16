# MyFinanceApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addCurrency_envID_currencyCode_description**](MyFinanceApi.md#addCurrency_envID_currencyCode_description) | **POST** /myfinance/environments/{envID}/addCurrency | save Instrument
[**addEquity_envID_isin_description**](MyFinanceApi.md#addEquity_envID_isin_description) | **POST** /myfinance/environments/{envID}/addEquity | save Instrument
[**addPrice_envID_currencyCode_isin_dayofprice_value**](MyFinanceApi.md#addPrice_envID_currencyCode_isin_dayofprice_value) | **POST** /myfinance/environments/{envID}/addPrice | save Price
[**addSymbol_envID_isin_symbol_currencycode**](MyFinanceApi.md#addSymbol_envID_isin_symbol_currencycode) | **POST** /myfinance/environments/{envID}/addSymbol | save Instrument
[**fillPricesHistory_envID_sourceId_isin**](MyFinanceApi.md#fillPricesHistory_envID_sourceId_isin) | **POST** /myfinance/environments/{envID}/fillpricehistory | fillpricehistory
[**getInstruments_envID**](MyFinanceApi.md#getInstruments_envID) | **GET** /myfinance/environments/{envID}/instruments | get Instruments
[**getInstrumentshateos_envID**](MyFinanceApi.md#getInstrumentshateos_envID) | **GET** /myfinance/environments/{envID}/instrumentshateos | get Instruments
[**getSecurity_envID_isin**](MyFinanceApi.md#getSecurity_envID_isin) | **GET** /myfinance/environments/{envID}/getsecurity | get Security
[**getStringList**](MyFinanceApi.md#getStringList) | **GET** /myfinance/environments/list | List Data
[**getValueCurve_envID_instrumentId_startdate_enddate**](MyFinanceApi.md#getValueCurve_envID_instrumentId_startdate_enddate) | **GET** /myfinance/environments/{envID}/getvaluecurve | get Security
[**importPrices_envID**](MyFinanceApi.md#importPrices_envID) | **POST** /myfinance/environments/{envID}/importprices | importprices


<a name="addCurrency_envID_currencyCode_description"></a>
# **addCurrency_envID_currencyCode_description**
> String addCurrency_envID_currencyCode_description(envID, currencyCode, description)

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
    String result = apiInstance.addCurrency_envID_currencyCode_description(envID, currencyCode, description);
    System.out.println(result);
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

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addEquity_envID_isin_description"></a>
# **addEquity_envID_isin_description**
> String addEquity_envID_isin_description(envID, isin, description)

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
    String result = apiInstance.addEquity_envID_isin_description(envID, isin, description);
    System.out.println(result);
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

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addPrice_envID_currencyCode_isin_dayofprice_value"></a>
# **addPrice_envID_currencyCode_isin_dayofprice_value**
> String addPrice_envID_currencyCode_isin_dayofprice_value(envID, currencyCode, isin, dayofprice, value)

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
    String result = apiInstance.addPrice_envID_currencyCode_isin_dayofprice_value(envID, currencyCode, isin, dayofprice, value);
    System.out.println(result);
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

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="addSymbol_envID_isin_symbol_currencycode"></a>
# **addSymbol_envID_isin_symbol_currencycode**
> String addSymbol_envID_isin_symbol_currencycode(envID, isin, symbol, currencycode)

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
    String result = apiInstance.addSymbol_envID_isin_symbol_currencycode(envID, isin, symbol, currencycode);
    System.out.println(result);
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

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="fillPricesHistory_envID_sourceId_isin"></a>
# **fillPricesHistory_envID_sourceId_isin**
> String fillPricesHistory_envID_sourceId_isin(envID, sourceId, isin)

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
    String result = apiInstance.fillPricesHistory_envID_sourceId_isin(envID, sourceId, isin);
    System.out.println(result);
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

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getInstruments_envID"></a>
# **getInstruments_envID**
> List&lt;Object&gt; getInstruments_envID(envID)

get Instruments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
try {
    List<Object> result = apiInstance.getInstruments_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getInstruments_envID");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getInstrumentshateos_envID"></a>
# **getInstrumentshateos_envID**
> List&lt;Object&gt; getInstrumentshateos_envID(envID)

get Instruments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
try {
    List<Object> result = apiInstance.getInstrumentshateos_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getInstrumentshateos_envID");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getSecurity_envID_isin"></a>
# **getSecurity_envID_isin**
> String getSecurity_envID_isin(envID, isin)

get Security



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
String isin = "isin_example"; // String | the isin
try {
    String result = apiInstance.getSecurity_envID_isin(envID, isin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getSecurity_envID_isin");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **isin** | **String**| the isin | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getStringList"></a>
# **getStringList**
> StringListModel getStringList()

List Data



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
try {
    StringListModel result = apiInstance.getStringList();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getStringList");
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

<a name="getValueCurve_envID_instrumentId_startdate_enddate"></a>
# **getValueCurve_envID_instrumentId_startdate_enddate**
> String getValueCurve_envID_instrumentId_startdate_enddate(envID, instrumentId, startdate, enddate)

get Security



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
Integer instrumentId = 56; // Integer | the instrumentId
String startdate = "startdate_example"; // String | startdate in Format yyyy-mm-dd
String enddate = "enddate_example"; // String | enddate in Format yyyy-mm-dd
try {
    String result = apiInstance.getValueCurve_envID_instrumentId_startdate_enddate(envID, instrumentId, startdate, enddate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceApi#getValueCurve_envID_instrumentId_startdate_enddate");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **envID** | **String**| The Service Environment |
 **instrumentId** | **Integer**| the instrumentId | [optional]
 **startdate** | **String**| startdate in Format yyyy-mm-dd | [optional]
 **enddate** | **String**| enddate in Format yyyy-mm-dd | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="importPrices_envID"></a>
# **importPrices_envID**
> String importPrices_envID(envID)

importprices



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceApi;


MyFinanceApi apiInstance = new MyFinanceApi();
String envID = "envID_example"; // String | The Service Environment
try {
    String result = apiInstance.importPrices_envID(envID);
    System.out.println(result);
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

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

