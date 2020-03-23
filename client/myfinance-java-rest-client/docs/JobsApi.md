# JobsApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**start**](JobsApi.md#start) | **POST** /Runner/{jobtype}/{env}/start | execute myfinance launcher


<a name="start"></a>
# **start**
> JobInformation start(env, jobtype, params)

execute myfinance launcher

Execute myfinance Core Launcher

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.JobsApi;


JobsApi apiInstance = new JobsApi();
String env = "env_example"; // String | The env
String jobtype = "jobtype_example"; // String | launching md jobs z.b. de.hf.dac.myfinance.importer.Import
BaseMFRunnerParameter params = new BaseMFRunnerParameter(); // BaseMFRunnerParameter | Parameter
try {
    JobInformation result = apiInstance.start(env, jobtype, params);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling JobsApi#start");
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

