/**
 * Dac Services
 * Dac Service REST API
 *
 * OpenAPI spec version: 1.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hf.dac.myfinance.client.api;

import com.sun.jersey.api.client.GenericType;

import io.swagger.client.ApiException;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import de.hf.dac.myfinance.client.model.*;
import io.swagger.client.Pair;

import de.hf.dac.myfinance.client.model.StringListModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "class de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2018-04-19T16:38:35.219+02:00")
public class MyFinanceApi {
  private ApiClient apiClient;

  public MyFinanceApi() {
    this(Configuration.getDefaultApiClient());
  }

  public MyFinanceApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * save Instrument
   * 
   * @param envID The Service Environment (required)
   * @param currencyCode the currencyCode (optional)
   * @param description description (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String addCurrency_envID_currencyCode_description(String envID, String currencyCode, String description) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addCurrency_envID_currencyCode_description");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addCurrency".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "currencyCode", currencyCode));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * save Instrument
   * 
   * @param envID The Service Environment (required)
   * @param isin the isin (optional)
   * @param description description (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String addEquity_envID_isin_description(String envID, String isin, String description) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addEquity_envID_isin_description");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addEquity".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "isin", isin));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * save Price
   * 
   * @param envID The Service Environment (required)
   * @param currencyCode the currencyCode (optional)
   * @param isin the isin (optional)
   * @param dayofprice the dayofprice(yyyy-mm-dd (optional)
   * @param value value (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String addPrice_envID_currencyCode_isin_dayofprice_value(String envID, String currencyCode, String isin, String dayofprice, Double value) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addPrice_envID_currencyCode_isin_dayofprice_value");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addPrice".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "currencyCode", currencyCode));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "isin", isin));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "dayofprice", dayofprice));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "value", value));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * save Instrument
   * 
   * @param envID The Service Environment (required)
   * @param isin the isin (optional)
   * @param symbol symbol (optional)
   * @param currencycode the code of the currency in which the security is traded in the exchange referenced by the symbol (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String addSymbol_envID_isin_symbol_currencycode(String envID, String isin, String symbol, String currencycode) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addSymbol_envID_isin_symbol_currencycode");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addSymbol".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "isin", isin));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "symbol", symbol));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "currencycode", currencycode));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * fillpricehistory
   * 
   * @param envID The Service Environment (required)
   * @param sourceId the sourceId (optional)
   * @param isin the isin (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String fillPricesHistory_envID_sourceId_isin(String envID, Integer sourceId, String isin) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling fillPricesHistory_envID_sourceId_isin");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/fillpricehistory".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "sourceId", sourceId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "isin", isin));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * get Instruments
   * 
   * @param envID The Service Environment (required)
   * @return List<Object>
   * @throws ApiException if fails to make API call
   */
  public List<Object> getInstruments_envID(String envID) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getInstruments_envID");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/instruments".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<Object>> localVarReturnType = new GenericType<List<Object>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * get Instruments
   * 
   * @param envID The Service Environment (required)
   * @return List<Object>
   * @throws ApiException if fails to make API call
   */
  public List<Object> getInstrumentshateos_envID(String envID) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getInstrumentshateos_envID");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/instrumentshateos".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<Object>> localVarReturnType = new GenericType<List<Object>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * get Security
   * 
   * @param envID The Service Environment (required)
   * @param isin the isin (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String getSecurity_envID_isin(String envID, String isin) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getSecurity_envID_isin");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/getsecurity".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "isin", isin));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * List Data
   * 
   * @return StringListModel
   * @throws ApiException if fails to make API call
   */
  public StringListModel getStringList() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/list".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<StringListModel> localVarReturnType = new GenericType<StringListModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * get Security
   * 
   * @param envID The Service Environment (required)
   * @param instrumentId the instrumentId (optional)
   * @param startdate startdate in Format yyyy-mm-dd (optional)
   * @param enddate enddate in Format yyyy-mm-dd (optional)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String getValueCurve_envID_instrumentId_startdate_enddate(String envID, Integer instrumentId, String startdate, String enddate) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getValueCurve_envID_instrumentId_startdate_enddate");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/getvaluecurve".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "instrumentId", instrumentId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "startdate", startdate));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "enddate", enddate));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * importprices
   * 
   * @param envID The Service Environment (required)
   * @return String
   * @throws ApiException if fails to make API call
   */
  public String importPrices_envID(String envID) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling importPrices_envID");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/importprices".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<String> localVarReturnType = new GenericType<String>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
