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


package de.hf.dac.marketdata.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * StatusChange
 */
@javax.annotation.Generated(value = "class de.hf.dac.marketdata.codegen.MarketDataClient_JavaGenerator", date = "2017-03-03T12:58:46.482+01:00")
public class StatusChange  implements Serializable {
  @JsonProperty("changeTime")
  private String changeTime = null;

  /**
   * Gets or Sets oldStatus
   */
  public enum OldStatusEnum {
    UNKNOWN("UNKNOWN"),
    
    QUEUED("QUEUED"),
    
    PROCESSING("PROCESSING"),
    
    FINISHED("FINISHED"),
    
    FAILED("FAILED");

    private String value;

    OldStatusEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OldStatusEnum fromValue(String text) {
      for (OldStatusEnum b : OldStatusEnum.values()) {
          if (String.valueOf(b.value).equals(text)) {
              return b;
          }
      }
      return null;
    }
  }

  @JsonProperty("oldStatus")
  private OldStatusEnum oldStatus = null;

  /**
   * Gets or Sets newStatus
   */
  public enum NewStatusEnum {
    UNKNOWN("UNKNOWN"),
    
    QUEUED("QUEUED"),
    
    PROCESSING("PROCESSING"),
    
    FINISHED("FINISHED"),
    
    FAILED("FAILED");

    private String value;

    NewStatusEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static NewStatusEnum fromValue(String text) {
      for (NewStatusEnum b : NewStatusEnum.values()) {
          if (String.valueOf(b.value).equals(text)) {
              return b;
          }
      }
      return null;
    }
  }

  @JsonProperty("newStatus")
  private NewStatusEnum newStatus = null;

  @JsonProperty("changeInformation")
  private String changeInformation = null;

  public StatusChange changeTime(String changeTime) {
    this.changeTime = changeTime;
    return this;
  }

   /**
   * Get changeTime
   * @return changeTime
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getChangeTime() {
    return changeTime;
  }

  public void setChangeTime(String changeTime) {
    this.changeTime = changeTime;
  }

  public StatusChange oldStatus(OldStatusEnum oldStatus) {
    this.oldStatus = oldStatus;
    return this;
  }

   /**
   * Get oldStatus
   * @return oldStatus
  **/
  @ApiModelProperty(example = "null", value = "")
  public OldStatusEnum getOldStatus() {
    return oldStatus;
  }

  public void setOldStatus(OldStatusEnum oldStatus) {
    this.oldStatus = oldStatus;
  }

  public StatusChange newStatus(NewStatusEnum newStatus) {
    this.newStatus = newStatus;
    return this;
  }

   /**
   * Get newStatus
   * @return newStatus
  **/
  @ApiModelProperty(example = "null", value = "")
  public NewStatusEnum getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(NewStatusEnum newStatus) {
    this.newStatus = newStatus;
  }

  public StatusChange changeInformation(String changeInformation) {
    this.changeInformation = changeInformation;
    return this;
  }

   /**
   * Get changeInformation
   * @return changeInformation
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getChangeInformation() {
    return changeInformation;
  }

  public void setChangeInformation(String changeInformation) {
    this.changeInformation = changeInformation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusChange statusChange = (StatusChange) o;
    return Objects.equals(this.changeTime, statusChange.changeTime) &&
        Objects.equals(this.oldStatus, statusChange.oldStatus) &&
        Objects.equals(this.newStatus, statusChange.newStatus) &&
        Objects.equals(this.changeInformation, statusChange.changeInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(changeTime, oldStatus, newStatus, changeInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusChange {\n");
    
    sb.append("    changeTime: ").append(toIndentedString(changeTime)).append("\n");
    sb.append("    oldStatus: ").append(toIndentedString(oldStatus)).append("\n");
    sb.append("    newStatus: ").append(toIndentedString(newStatus)).append("\n");
    sb.append("    changeInformation: ").append(toIndentedString(changeInformation)).append("\n");
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

