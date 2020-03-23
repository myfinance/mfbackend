package de.hf.dac.myfinance.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;

import javax.ws.rs.core.GenericType;

import de.hf.dac.myfinance.client.model.BaseMFRunnerParameter;
import de.hf.dac.myfinance.client.model.JobInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2020-03-23T23:45:07.507+01:00")
public class MyFinanceRunnerApi {
  private ApiClient apiClient;

  public MyFinanceRunnerApi() {
    this(Configuration.getDefaultApiClient());
  }

  public MyFinanceRunnerApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * list known cops jobs
   * List all JobInformation
   * @return List&lt;Object&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Object> list() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/Runner/list";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<Object>> localVarReturnType = new GenericType<List<Object>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * execute myfinance launcher
   * Execute myfinance Core Launcher
   * @param env The env (required)
   * @param jobtype launching md jobs z.b. de.hf.dac.myfinance.importer.Import (required)
   * @param params Parameter (optional)
   * @return JobInformation
   * @throws ApiException if fails to make API call
   */
  public JobInformation start(String env, String jobtype, BaseMFRunnerParameter params) throws ApiException {
    Object localVarPostBody = params;
    
    // verify the required parameter 'env' is set
    if (env == null) {
      throw new ApiException(400, "Missing the required parameter 'env' when calling start");
    }
    
    // verify the required parameter 'jobtype' is set
    if (jobtype == null) {
      throw new ApiException(400, "Missing the required parameter 'jobtype' when calling start");
    }
    
    // create path and map variables
    String localVarPath = "/Runner/{jobtype}/{env}/start"
      .replaceAll("\\{" + "env" + "\\}", apiClient.escapeString(env.toString()))
      .replaceAll("\\{" + "jobtype" + "\\}", apiClient.escapeString(jobtype.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<JobInformation> localVarReturnType = new GenericType<JobInformation>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * get status of cops jobs
   * JobInformation of actual Job
   * @param uuid uuid of job (required)
   * @return JobInformation
   * @throws ApiException if fails to make API call
   */
  public JobInformation status(String uuid) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'uuid' is set
    if (uuid == null) {
      throw new ApiException(400, "Missing the required parameter 'uuid' when calling status");
    }
    
    // create path and map variables
    String localVarPath = "/Runner/status/{uuid}"
      .replaceAll("\\{" + "uuid" + "\\}", apiClient.escapeString(uuid.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<JobInformation> localVarReturnType = new GenericType<JobInformation>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
