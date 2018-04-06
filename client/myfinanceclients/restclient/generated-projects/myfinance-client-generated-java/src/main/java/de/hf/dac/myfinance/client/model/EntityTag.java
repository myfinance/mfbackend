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

import java.io.Serializable;
/**
 * EntityTag
 */
@javax.annotation.Generated(value = "class de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2018-03-23T16:39:56.372+01:00")
public class EntityTag  implements Serializable {
  @JsonProperty("value")
  private String value = null;

  @JsonProperty("weak")
  private Boolean weak = null;

  public EntityTag value(String value) {
    this.value = value;
    return this;
  }

   /**
   * Get value
   * @return value
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public EntityTag weak(Boolean weak) {
    this.weak = weak;
    return this;
  }

   /**
   * Get weak
   * @return weak
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getWeak() {
    return weak;
  }

  public void setWeak(Boolean weak) {
    this.weak = weak;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntityTag entityTag = (EntityTag) o;
    return Objects.equals(this.value, entityTag.value) &&
        Objects.equals(this.weak, entityTag.weak);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, weak);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntityTag {\n");
    
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    weak: ").append(toIndentedString(weak)).append("\n");
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
