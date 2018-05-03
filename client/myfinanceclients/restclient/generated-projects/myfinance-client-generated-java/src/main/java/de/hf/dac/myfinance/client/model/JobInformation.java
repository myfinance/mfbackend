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


package de.hf.dac.myfinance.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.hf.dac.myfinance.client.model.StatusChange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
/**
 * JobInformation
 */
@javax.annotation.Generated(value = "class de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2018-04-24T10:07:05.461+02:00")
public class JobInformation  implements Serializable {
  @JsonProperty("uuid")
  private String uuid = null;

  @JsonProperty("startTime")
  private String startTime = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    UNKNOWN("UNKNOWN"),
    
    QUEUED("QUEUED"),
    
    PROCESSING("PROCESSING"),
    
    FINISHED("FINISHED"),
    
    FAILED("FAILED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
          if (String.valueOf(b.value).equals(text)) {
              return b;
          }
      }
      return null;
    }
  }

  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("statusChangeHistory")
  private List<StatusChange> statusChangeHistory = new ArrayList<StatusChange>();

  @JsonProperty("payload")
  private Object payload = null;

  @JsonProperty("result")
  private Object result = null;

  @JsonProperty("statusMessageCollector")
  private List<StatusChange> statusMessageCollector = new ArrayList<StatusChange>();

  @JsonProperty("endTime")
  private String endTime = null;

  public JobInformation uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * Get uuid
   * @return uuid
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public JobInformation startTime(String startTime) {
    this.startTime = startTime;
    return this;
  }

   /**
   * Get startTime
   * @return startTime
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public JobInformation status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(example = "null", value = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public JobInformation statusChangeHistory(List<StatusChange> statusChangeHistory) {
    this.statusChangeHistory = statusChangeHistory;
    return this;
  }

  public JobInformation addStatusChangeHistoryItem(StatusChange statusChangeHistoryItem) {
    this.statusChangeHistory.add(statusChangeHistoryItem);
    return this;
  }

   /**
   * Get statusChangeHistory
   * @return statusChangeHistory
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<StatusChange> getStatusChangeHistory() {
    return statusChangeHistory;
  }

  public void setStatusChangeHistory(List<StatusChange> statusChangeHistory) {
    this.statusChangeHistory = statusChangeHistory;
  }

  public JobInformation payload(Object payload) {
    this.payload = payload;
    return this;
  }

   /**
   * Get payload
   * @return payload
  **/
  @ApiModelProperty(example = "null", value = "")
  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public JobInformation result(Object result) {
    this.result = result;
    return this;
  }

   /**
   * Get result
   * @return result
  **/
  @ApiModelProperty(example = "null", value = "")
  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public JobInformation statusMessageCollector(List<StatusChange> statusMessageCollector) {
    this.statusMessageCollector = statusMessageCollector;
    return this;
  }

  public JobInformation addStatusMessageCollectorItem(StatusChange statusMessageCollectorItem) {
    this.statusMessageCollector.add(statusMessageCollectorItem);
    return this;
  }

   /**
   * Get statusMessageCollector
   * @return statusMessageCollector
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<StatusChange> getStatusMessageCollector() {
    return statusMessageCollector;
  }

  public void setStatusMessageCollector(List<StatusChange> statusMessageCollector) {
    this.statusMessageCollector = statusMessageCollector;
  }

  public JobInformation endTime(String endTime) {
    this.endTime = endTime;
    return this;
  }

   /**
   * Get endTime
   * @return endTime
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JobInformation jobInformation = (JobInformation) o;
    return Objects.equals(this.uuid, jobInformation.uuid) &&
        Objects.equals(this.startTime, jobInformation.startTime) &&
        Objects.equals(this.status, jobInformation.status) &&
        Objects.equals(this.statusChangeHistory, jobInformation.statusChangeHistory) &&
        Objects.equals(this.payload, jobInformation.payload) &&
        Objects.equals(this.result, jobInformation.result) &&
        Objects.equals(this.statusMessageCollector, jobInformation.statusMessageCollector) &&
        Objects.equals(this.endTime, jobInformation.endTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, startTime, status, statusChangeHistory, payload, result, statusMessageCollector, endTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JobInformation {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusChangeHistory: ").append(toIndentedString(statusChangeHistory)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    statusMessageCollector: ").append(toIndentedString(statusMessageCollector)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

