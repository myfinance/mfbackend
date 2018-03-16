# MyFinanceRunnerApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**list**](MyFinanceRunnerApi.md#list) | **GET** /Runner/list | list known cops jobs
[**start**](MyFinanceRunnerApi.md#start) | **POST** /Runner/{jobtype}/{env}/start | execute myfinance launcher
[**status**](MyFinanceRunnerApi.md#status) | **GET** /Runner/status/{uuid} | get status of cops jobs


<a name="list"></a>
# **list**
> List&lt;Object&gt; list()

list known cops jobs

List all JobInformation

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceRunnerApi;


MyFinanceRunnerApi apiInstance = new MyFinanceRunnerApi();
try {
    List<Object> result = apiInstance.list();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceRunnerApi#list");
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

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="start"></a>
# **start**
> JobInformation start(env, jobtype, params)

execute myfinance launcher

Execute myfinance Core Launcher

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceRunnerApi;


MyFinanceRunnerApi apiInstance = new MyFinanceRunnerApi();
String env = "env_example"; // String | The env
String jobtype = "jobtype_example"; // String | launching md jobs z.b. de.hf.dac.myfinance.importer.Import
BaseMFRunnerParameter params = new BaseMFRunnerParameter(); // BaseMFRunnerParameter | Parameter
try {
    JobInformation result = apiInstance.start(env, jobtype, params);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceRunnerApi#start");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **env** | **String**| The env |
 **jobtype** | **String**| launching md jobs z.b. de.hf.dac.myfinance.importer.Import |
 **params** | [**BaseMFRunnerParameter**](BaseMFRunnerParameter.md)| Parameter | [optional]

### Return type

[**JobInformation**](JobInformation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="status"></a>
# **status**
> JobInformation status(uuid)

get status of cops jobs

JobInformation of actual Job

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.MyFinanceRunnerApi;


MyFinanceRunnerApi apiInstance = new MyFinanceRunnerApi();
String uuid = "uuid_example"; // String | uuid of job
try {
    JobInformation result = apiInstance.status(uuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MyFinanceRunnerApi#status");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uuid** | **String**| uuid of job |

### Return type

[**JobInformation**](JobInformation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

