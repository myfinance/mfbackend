# myfinance-client-generated

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>de.hf.dac.myfinance.client</groupId>
    <artifactId>myfinance-client-generated</artifactId>
    <version>0.10.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "de.hf.dac.myfinance.client:myfinance-client-generated:0.10.0-SNAPSHOT"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/myfinance-client-generated-0.10.0-SNAPSHOT.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import de.hf.dac.myfinance.client.api.HelloApi;

import java.io.File;
import java.util.*;

public class HelloApiExample {

    public static void main(String[] args) {
        
        HelloApi apiInstance = new HelloApi();
        try {
            String result = apiInstance.getHello();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling HelloApi#getHello");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://localhost/dac/rest*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*HelloApi* | [**getHello**](docs/HelloApi.md#getHello) | **GET** /hello | 
*HelloApi* | [**getProducts**](docs/HelloApi.md#getProducts) | **GET** /hello/list | 
*MyFinanceApi* | [**addCurrency_envID_currencyCode_description**](docs/MyFinanceApi.md#addCurrency_envID_currencyCode_description) | **POST** /myfinance/environments/{envID}/addCurrency | save Instrument
*MyFinanceApi* | [**addEquity_envID_isin_description**](docs/MyFinanceApi.md#addEquity_envID_isin_description) | **POST** /myfinance/environments/{envID}/addEquity | save Instrument
*MyFinanceApi* | [**addPrice_envID_currencyCode_isin_dayofprice_value**](docs/MyFinanceApi.md#addPrice_envID_currencyCode_isin_dayofprice_value) | **POST** /myfinance/environments/{envID}/addPrice | save Price
*MyFinanceApi* | [**addSymbol_envID_isin_symbol_currencycode**](docs/MyFinanceApi.md#addSymbol_envID_isin_symbol_currencycode) | **POST** /myfinance/environments/{envID}/addSymbol | save Instrument
*MyFinanceApi* | [**fillPricesHistory_envID_sourceId_isin**](docs/MyFinanceApi.md#fillPricesHistory_envID_sourceId_isin) | **POST** /myfinance/environments/{envID}/fillpricehistory | fillpricehistory
*MyFinanceApi* | [**getInstruments_envID**](docs/MyFinanceApi.md#getInstruments_envID) | **GET** /myfinance/environments/{envID}/instruments | get Instruments
*MyFinanceApi* | [**getInstrumentshateos_envID**](docs/MyFinanceApi.md#getInstrumentshateos_envID) | **GET** /myfinance/environments/{envID}/instrumentshateos | get Instruments
*MyFinanceApi* | [**getSecurity_envID_isin**](docs/MyFinanceApi.md#getSecurity_envID_isin) | **GET** /myfinance/environments/{envID}/getsecurity | get Security
*MyFinanceApi* | [**getStringList**](docs/MyFinanceApi.md#getStringList) | **GET** /myfinance/environments/list | List Data
*MyFinanceApi* | [**getValueCurve_envID_instrumentId_startdate_enddate**](docs/MyFinanceApi.md#getValueCurve_envID_instrumentId_startdate_enddate) | **GET** /myfinance/environments/{envID}/getvaluecurve | get Security
*MyFinanceApi* | [**importPrices_envID**](docs/MyFinanceApi.md#importPrices_envID) | **POST** /myfinance/environments/{envID}/importprices | importprices
*MyFinanceRunnerApi* | [**list**](docs/MyFinanceRunnerApi.md#list) | **GET** /Runner/list | list known cops jobs
*MyFinanceRunnerApi* | [**start**](docs/MyFinanceRunnerApi.md#start) | **POST** /Runner/{jobtype}/{env}/start | execute myfinance launcher
*MyFinanceRunnerApi* | [**status**](docs/MyFinanceRunnerApi.md#status) | **GET** /Runner/status/{uuid} | get status of cops jobs


## Documentation for Models

 - [BaseMFRunnerParameter](docs/BaseMFRunnerParameter.md)
 - [EntityTag](docs/EntityTag.md)
 - [EnvironmentDataResource](docs/EnvironmentDataResource.md)
 - [Instrument](docs/Instrument.md)
 - [JobInformation](docs/JobInformation.md)
 - [Link](docs/Link.md)
 - [Locale](docs/Locale.md)
 - [MediaType](docs/MediaType.md)
 - [NewCookie](docs/NewCookie.md)
 - [Response](docs/Response.md)
 - [StatusChange](docs/StatusChange.md)
 - [StatusType](docs/StatusType.md)
 - [StringListModel](docs/StringListModel.md)
 - [UriBuilder](docs/UriBuilder.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issue.

## Author



