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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.Serializable;
/**
 * MediaType
 */
@javax.annotation.Generated(value = "class de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2018-03-16T16:41:55.220+01:00")
public class MediaType  implements Serializable {
  @JsonProperty("type")
  private String type = null;

  @JsonProperty("subtype")
  private String subtype = null;

  @JsonProperty("parameters")
  private Map<String, String> parameters = new HashMap<String, String>();

  @JsonProperty("wildcardType")
  private Boolean wildcardType = null;

  @JsonProperty("wildcardSubtype")
  private Boolean wildcardSubtype = null;

  public MediaType type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public MediaType subtype(String subtype) {
    this.subtype = subtype;
    return this;
  }

   /**
   * Get subtype
   * @return subtype
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getSubtype() {
    return subtype;
  }

  public void setSubtype(String subtype) {
    this.subtype = subtype;
  }

  public MediaType parameters(Map<String, String> parameters) {
    this.parameters = parameters;
    return this;
  }

  public MediaType putParametersItem(String key, String parametersItem) {
    this.parameters.put(key, parametersItem);
    return this;
  }

   /**
   * Get parameters
   * @return parameters
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public MediaType wildcardType(Boolean wildcardType) {
    this.wildcardType = wildcardType;
    return this;
  }

   /**
   * Get wildcardType
   * @return wildcardType
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getWildcardType() {
    return wildcardType;
  }

  public void setWildcardType(Boolean wildcardType) {
    this.wildcardType = wildcardType;
  }

  public MediaType wildcardSubtype(Boolean wildcardSubtype) {
    this.wildcardSubtype = wildcardSubtype;
    return this;
  }

   /**
   * Get wildcardSubtype
   * @return wildcardSubtype
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getWildcardSubtype() {
    return wildcardSubtype;
  }

  public void setWildcardSubtype(Boolean wildcardSubtype) {
    this.wildcardSubtype = wildcardSubtype;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MediaType mediaType = (MediaType) o;
    return Objects.equals(this.type, mediaType.type) &&
        Objects.equals(this.subtype, mediaType.subtype) &&
        Objects.equals(this.parameters, mediaType.parameters) &&
        Objects.equals(this.wildcardType, mediaType.wildcardType) &&
        Objects.equals(this.wildcardSubtype, mediaType.wildcardSubtype);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, subtype, parameters, wildcardType, wildcardSubtype);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MediaType {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    subtype: ").append(toIndentedString(subtype)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    wildcardType: ").append(toIndentedString(wildcardType)).append("\n");
    sb.append("    wildcardSubtype: ").append(toIndentedString(wildcardSubtype)).append("\n");
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

