# HelloApi

All URIs are relative to *https://localhost/dac/rest*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addProduct_productId_description**](HelloApi.md#addProduct_productId_description) | **GET** /hello/addproduct | save Product
[**getEnvironments2**](HelloApi.md#getEnvironments2) | **GET** /hello/getEnvironments | List Environments
[**getHello**](HelloApi.md#getHello) | **GET** /hello | 
[**getProductObjects**](HelloApi.md#getProductObjects) | **GET** /hello/productobjects | get ProductObjects
[**getProducts**](HelloApi.md#getProducts) | **GET** /hello/products | get Products


<a name="addProduct_productId_description"></a>
# **addProduct_productId_description**
> String addProduct_productId_description(productId, description)

save Product



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.HelloApi;


HelloApi apiInstance = new HelloApi();
String productId = "productId_example"; // String | the isin
String description = "description_example"; // String | description
try {
    String result = apiInstance.addProduct_productId_description(productId, description);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HelloApi#addProduct_productId_description");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **productId** | **String**| the isin | [optional]
 **description** | **String**| description | [optional]

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getEnvironments2"></a>
# **getEnvironments2**
> List&lt;Object&gt; getEnvironments2()

List Environments



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.HelloApi;


HelloApi apiInstance = new HelloApi();
try {
    List<Object> result = apiInstance.getEnvironments2();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HelloApi#getEnvironments2");
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

<a name="getHello"></a>
# **getHello**
> String getHello()



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.HelloApi;


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

<a name="getProductObjects"></a>
# **getProductObjects**
> List&lt;Object&gt; getProductObjects()

get ProductObjects



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.HelloApi;


HelloApi apiInstance = new HelloApi();
try {
    List<Object> result = apiInstance.getProductObjects();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HelloApi#getProductObjects");
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

<a name="getProducts"></a>
# **getProducts**
> String getProducts()

get Products



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import de.hf.dac.marketdata.client.api.HelloApi;


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
 - **Accept**: application/json

