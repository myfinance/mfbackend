package de.hf.dac.myfinance.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;

import javax.ws.rs.core.GenericType;

import de.hf.dac.myfinance.client.model.DateDoubleModel;
import de.hf.dac.myfinance.client.model.InstrumentListModel;
import de.hf.dac.myfinance.client.model.InstrumentModel;
import de.hf.dac.myfinance.client.model.TransactionListModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2020-03-23T23:45:07.507+01:00")
public class UtilityResourcesApi {
  private ApiClient apiClient;

  public UtilityResourcesApi() {
    this(Configuration.getDefaultApiClient());
  }

  public UtilityResourcesApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * List Data
   * 
   * @param envID The Service Environment (required)
   * @param tenant tenant id (optional)
   * @return InstrumentListModel
   * @throws ApiException if fails to make API call
   */
  public InstrumentListModel getInstrumentForTenantList_envID_tenant(String envID, Integer tenant) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getInstrumentForTenantList_envID_tenant");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/instrumentsfortenant"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "tenant", tenant));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<InstrumentListModel> localVarReturnType = new GenericType<InstrumentListModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * List Data
   * 
   * @param envID The Service Environment (required)
   * @return InstrumentListModel
   * @throws ApiException if fails to make API call
   */
  public InstrumentListModel getInstrumentList_envID(String envID) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getInstrumentList_envID");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/instruments"
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

    GenericType<InstrumentListModel> localVarReturnType = new GenericType<InstrumentListModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * List Data
   * 
   * @param envID The Service Environment (required)
   * @param tenant tenant id (optional)
   * @param instrumenttype instrumenttype (optional)
   * @return InstrumentListModel
   * @throws ApiException if fails to make API call
   */
  public InstrumentListModel getInstrumentPerTypeList_envID_tenant_instrumenttype(String envID, Integer tenant, String instrumenttype) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getInstrumentPerTypeList_envID_tenant_instrumenttype");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/instrumentspertype"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "tenant", tenant));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "instrumenttype", instrumenttype));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<InstrumentListModel> localVarReturnType = new GenericType<InstrumentListModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * get Data
   * 
   * @param envID The Service Environment (required)
   * @param isin the isin (optional)
   * @return InstrumentModel
   * @throws ApiException if fails to make API call
   */
  public InstrumentModel getInstrument_envID_isin(String envID, String isin) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getInstrument_envID_isin");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/getequity"
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

    GenericType<InstrumentModel> localVarReturnType = new GenericType<InstrumentModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * List Data
   * 
   * @param envID The Service Environment (required)
   * @return InstrumentListModel
   * @throws ApiException if fails to make API call
   */
  public InstrumentListModel getTenantList_envID(String envID) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getTenantList_envID");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/listTenants"
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

    GenericType<InstrumentListModel> localVarReturnType = new GenericType<InstrumentListModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * List Data
   * 
   * @param envID The Service Environment (required)
   * @param startdate startdate in Format yyyy-mm-dd (optional)
   * @param enddate enddate in Format yyyy-mm-dd (optional)
   * @return TransactionListModel
   * @throws ApiException if fails to make API call
   */
  public TransactionListModel getTransactionList_envID_startdate_enddate(String envID, String startdate, String enddate) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getTransactionList_envID_startdate_enddate");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/listTransactions"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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

    GenericType<TransactionListModel> localVarReturnType = new GenericType<TransactionListModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Map Data
   * 
   * @param instrumentId the instrumentId (required)
   * @param envID The Service Environment (required)
   * @param startdate startdate in Format yyyy-mm-dd (optional)
   * @param enddate enddate in Format yyyy-mm-dd (optional)
   * @return DateDoubleModel
   * @throws ApiException if fails to make API call
   */
  public DateDoubleModel getValueMap_envID_instrumentId_startdate_enddate(Integer instrumentId, String envID, String startdate, String enddate) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'instrumentId' is set
    if (instrumentId == null) {
      throw new ApiException(400, "Missing the required parameter 'instrumentId' when calling getValueMap_envID_instrumentId_startdate_enddate");
    }
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling getValueMap_envID_instrumentId_startdate_enddate");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/getvaluecurve/{instrumentId}"
      .replaceAll("\\{" + "instrumentId" + "\\}", apiClient.escapeString(instrumentId.toString()))
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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

    GenericType<DateDoubleModel> localVarReturnType = new GenericType<DateDoubleModel>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
