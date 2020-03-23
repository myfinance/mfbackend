# HelloApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getHello**](HelloApi.md#getHello) | **GET** /hello | 
[**getProducts**](HelloApi.md#getProducts) | **GET** /hello/list | 


<a name="getHello"></a>
# **getHello**
> String getHello()



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.HelloApi;


HelloApi apiInstance = new HelloApi();
try {
    String result = apiInstance.getHello();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HelloApi#getHello");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="getProducts"></a>
# **getProducts**
> String getProducts()



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.myfinance.client.api.HelloApi;


HelloApi apiInstance = new HelloApi();
try {
    String result = apiInstance.getProducts();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HelloApi#getProducts");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

