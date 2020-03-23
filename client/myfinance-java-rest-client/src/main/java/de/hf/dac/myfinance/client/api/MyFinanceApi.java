package de.hf.dac.myfinance.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;

import javax.ws.rs.core.GenericType;

import de.hf.dac.myfinance.client.model.DateDoubleModel;
import de.hf.dac.myfinance.client.model.InstrumentListModel;
import de.hf.dac.myfinance.client.model.InstrumentModel;
import de.hf.dac.myfinance.client.model.StringListModel;
import de.hf.dac.myfinance.client.model.TransactionListModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2020-03-23T23:45:07.507+01:00")
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
   * add Budget
   * 
   * @param envID The Service Environment (required)
   * @param description description (optional)
   * @param budgetGroupId the Id of the budgetGroup which the budget is attached to (optional)
   * @throws ApiException if fails to make API call
   */
  public void addBudget_envID_description_budgetGroupId(String envID, String description, Integer budgetGroupId) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addBudget_envID_description_budgetGroupId");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addBudget"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "budgetGroupId", budgetGroupId));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Instrument
   * 
   * @param envID The Service Environment (required)
   * @param currencyCode the currencyCode (optional)
   * @param description description (optional)
   * @throws ApiException if fails to make API call
   */
  public void addCurrency_envID_currencyCode_description(String envID, String currencyCode, String description) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addCurrency_envID_currencyCode_description");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addCurrency"
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


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Instrument
   * 
   * @param envID The Service Environment (required)
   * @param isin the isin (optional)
   * @param description description (optional)
   * @throws ApiException if fails to make API call
   */
  public void addEquity_envID_isin_description(String envID, String isin, String description) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addEquity_envID_isin_description");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addEquity"
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


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Giro
   * 
   * @param envID The Service Environment (required)
   * @param description description (optional)
   * @param tenantId the Id of the tenant which the giro is attached to (optional)
   * @throws ApiException if fails to make API call
   */
  public void addGiro_envID_description_tenantId(String envID, String description, Integer tenantId) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addGiro_envID_description_tenantId");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addGiro"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "tenantId", tenantId));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Income or Expense
   * 
   * @param envID The Service Environment (required)
   * @param description description (optional)
   * @param accId the accountId of the income or expense (optional)
   * @param budgetId the budgetId of the income or expense (optional)
   * @param value the value of the income or expense (optional)
   * @param transactiondate the transactiondate(yyyy-mm-dd (optional)
   * @throws ApiException if fails to make API call
   */
  public void addIncomeExpense_envID_description_accId_budgetId_value_transactiondate(String envID, String description, Integer accId, Integer budgetId, Double value, String transactiondate) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addIncomeExpense_envID_description_accId_budgetId_value_transactiondate");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addIncomeExpense"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "accId", accId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "budgetId", budgetId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "value", value));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "transactiondate", transactiondate));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Price
   * 
   * @param envID The Service Environment (required)
   * @param currencyCode the currencyCode (optional)
   * @param isin the isin (optional)
   * @param dayofprice the dayofprice(yyyy-mm-dd (optional)
   * @param value value (optional)
   * @throws ApiException if fails to make API call
   */
  public void addPrice_envID_currencyCode_isin_dayofprice_value(String envID, String currencyCode, String isin, String dayofprice, Double value) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addPrice_envID_currencyCode_isin_dayofprice_value");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addPrice"
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


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Instrument
   * 
   * @param envID The Service Environment (required)
   * @param isin the isin (optional)
   * @param symbol symbol (optional)
   * @param currencycode the code of the currency in which the security is traded in the exchange referenced by the symbol (optional)
   * @throws ApiException if fails to make API call
   */
  public void addSymbol_envID_isin_symbol_currencycode(String envID, String isin, String symbol, String currencycode) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addSymbol_envID_isin_symbol_currencycode");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addSymbol"
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


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Tenant
   * 
   * @param envID The Service Environment (required)
   * @param description description (optional)
   * @throws ApiException if fails to make API call
   */
  public void addTenant_envID_description(String envID, String description) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addTenant_envID_description");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addTenant"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * save Transfer
   * 
   * @param envID The Service Environment (required)
   * @param description description (optional)
   * @param srcId the instrumentId of the source (optional)
   * @param trgId the instrumentId of the target (optional)
   * @param value the value of the income or expense (optional)
   * @param transactiondate the transactiondate(yyyy-mm-dd (optional)
   * @throws ApiException if fails to make API call
   */
  public void addTransfer_envID_description_srcId_trgId_value_transactiondate(String envID, String description, Integer srcId, Integer trgId, Double value, String transactiondate) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling addTransfer_envID_description_srcId_trgId_value_transactiondate");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/addTransfer"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "srcId", srcId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "trgId", trgId));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "value", value));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "transactiondate", transactiondate));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * delete Transfer
   * 
   * @param envID The Service Environment (required)
   * @param transactionId transactionId (optional)
   * @throws ApiException if fails to make API call
   */
  public void delTransfer_envID_transactionId(String envID, Integer transactionId) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling delTransfer_envID_transactionId");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/delTransfer"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "transactionId", transactionId));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * fillpricehistory
   * 
   * @param envID The Service Environment (required)
   * @param sourceId the sourceId (optional)
   * @param isin the isin (optional)
   * @throws ApiException if fails to make API call
   */
  public void fillPricesHistory_envID_sourceId_isin(String envID, Integer sourceId, String isin) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling fillPricesHistory_envID_sourceId_isin");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/fillpricehistory"
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


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * List Environments
   * 
   * @return StringListModel
   * @throws ApiException if fails to make API call
   */
  public StringListModel getEnvironmentList() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/list";

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
  /**
   * importprices
   * 
   * @param envID The Service Environment (required)
   * @throws ApiException if fails to make API call
   */
  public void importPrices_envID(String envID) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling importPrices_envID");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/importprices"
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


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * update Instrument
   * 
   * @param envID The Service Environment (required)
   * @param id id (optional)
   * @param description description (optional)
   * @param isactive isactive (optional)
   * @throws ApiException if fails to make API call
   */
  public void updateInstrument_envID_id_description_isactive(String envID, Integer id, String description, Boolean isactive) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'envID' is set
    if (envID == null) {
      throw new ApiException(400, "Missing the required parameter 'envID' when calling updateInstrument_envID_id_description_isactive");
    }
    
    // create path and map variables
    String localVarPath = "/myfinance/environments/{envID}/updateInstrument"
      .replaceAll("\\{" + "envID" + "\\}", apiClient.escapeString(envID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "id", id));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "description", description));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "isactive", isactive));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };


    apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
}
