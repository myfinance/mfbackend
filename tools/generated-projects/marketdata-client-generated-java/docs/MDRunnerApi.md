# MDRunnerApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getEnvironments1**](MDRunnerApi.md#getEnvironments1) | **GET** /runner/getEnvironments | List Environments
[**list**](MDRunnerApi.md#list) | **GET** /runner/list | list known cops jobs
[**start**](MDRunnerApi.md#start) | **POST** /runner/{jobtype}/{env}/start | execute marketdata launcher
[**status**](MDRunnerApi.md#status) | **GET** /runner/status/{uuid} | get status of cops jobs


<a name="getEnvironments1"></a>
# **getEnvironments1**
> List&lt;Object&gt; getEnvironments1()

List Environments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.MDRunnerApi;


MDRunnerApi apiInstance = new MDRunnerApi();
try {
    List<Object> result = apiInstance.getEnvironments1();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MDRunnerApi#getEnvironments1");
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

<a name="list"></a>
# **list**
> List&lt;Object&gt; list()

list known cops jobs

List all JobInformation

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.MDRunnerApi;


MDRunnerApi apiInstance = new MDRunnerApi();
try {
    List<Object> result = apiInstance.list();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MDRunnerApi#list");
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

execute marketdata launcher

Execute marketdata Core Launcher

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.MDRunnerApi;


MDRunnerApi apiInstance = new MDRunnerApi();
String env = "env_example"; // String | The env
String jobtype = "jobtype_example"; // String | launching md jobs
BaseMDRunnerParameter params = new BaseMDRunnerParameter(); // BaseMDRunnerParameter | Parameter
try {
    JobInformation result = apiInstance.start(env, jobtype, params);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MDRunnerApi#start");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **env** | **String**| The env |
 **jobtype** | **String**| launching md jobs |
 **params** | [**BaseMDRunnerParameter**](BaseMDRunnerParameter.md)| Parameter | [optional]

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
//import de.hf.dac.marketdata.client.api.MDRunnerApi;


MDRunnerApi apiInstance = new MDRunnerApi();
String uuid = "uuid_example"; // String | uuid of job
try {
    JobInformation result = apiInstance.status(uuid);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MDRunnerApi#status");
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

