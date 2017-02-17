# MarketdataApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEnvironments**](MarketdataApi.md#getEnvironments) | **GET** /marketdata/getEnvironments | List Environments
[**getInstruments_envID**](MarketdataApi.md#getInstruments_envID) | **GET** /marketdata/environments/{envID}/instruments | get Instruments
[**importData_envID_jobtype**](MarketdataApi.md#importData_envID_jobtype) | **POST** /marketdata/environments/{envID}/{jobtype}/instruments | get Instruments


<a name="getEnvironments"></a>
# **getEnvironments**
> List&lt;Object&gt; getEnvironments()

List Environments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.MarketdataApi;


MarketdataApi apiInstance = new MarketdataApi();
try {
    List<Object> result = apiInstance.getEnvironments();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MarketdataApi#getEnvironments");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**List&lt;Object&gt;**

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
//import de.hf.dac.marketdata.client.api.MarketdataApi;


MarketdataApi apiInstance = new MarketdataApi();
String envID = "envID_example"; // String | The Service Environment
try {
    List<Object> result = apiInstance.getInstruments_envID(envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MarketdataApi#getInstruments_envID");
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

<a name="importData_envID_jobtype"></a>
# **importData_envID_jobtype**
> List&lt;Object&gt; importData_envID_jobtype(jobtype, envID)

get Instruments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.MarketdataApi;


MarketdataApi apiInstance = new MarketdataApi();
String jobtype = "jobtype_example"; // String | launching cops jobs
String envID = "envID_example"; // String | The Service Environment
try {
    List<Object> result = apiInstance.importData_envID_jobtype(jobtype, envID);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MarketdataApi#importData_envID_jobtype");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **jobtype** | **String**| launching cops jobs |
 **envID** | **String**| The Service Environment |

### Return type

**List&lt;Object&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

