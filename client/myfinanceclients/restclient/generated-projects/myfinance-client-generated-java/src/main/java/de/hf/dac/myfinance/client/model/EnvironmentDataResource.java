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
import de.hf.dac.myfinance.client.model.Instrument;
import de.hf.dac.myfinance.client.model.Response;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
/**
 * EnvironmentDataResource
 */
@javax.annotation.Generated(value = "class de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2018-04-24T10:07:05.461+02:00")
public class EnvironmentDataResource  implements Serializable {
  @JsonProperty("instruments")
  private List<Instrument> instruments = new ArrayList<Instrument>();

  @JsonProperty("instrumentshateos")
  private Response instrumentshateos = null;

  public EnvironmentDataResource instruments(List<Instrument> instruments) {
    this.instruments = instruments;
    return this;
  }

  public EnvironmentDataResource addInstrumentsItem(Instrument instrumentsItem) {
    this.instruments.add(instrumentsItem);
    return this;
  }

   /**
   * Get instruments
   * @return instruments
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<Instrument> getInstruments() {
    return instruments;
  }

  public void setInstruments(List<Instrument> instruments) {
    this.instruments = instruments;
  }

  public EnvironmentDataResource instrumentshateos(Response instrumentshateos) {
    this.instrumentshateos = instrumentshateos;
    return this;
  }

   /**
   * Get instrumentshateos
   * @return instrumentshateos
  **/
  @ApiModelProperty(example = "null", value = "")
  public Response getInstrumentshateos() {
    return instrumentshateos;
  }

  public void setInstrumentshateos(Response instrumentshateos) {
    this.instrumentshateos = instrumentshateos;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnvironmentDataResource environmentDataResource = (EnvironmentDataResource) o;
    return Objects.equals(this.instruments, environmentDataResource.instruments) &&
        Objects.equals(this.instrumentshateos, environmentDataResource.instrumentshateos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instruments, instrumentshateos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnvironmentDataResource {\n");
    
    sb.append("    instruments: ").append(toIndentedString(instruments)).append("\n");
    sb.append("    instrumentshateos: ").append(toIndentedString(instrumentshateos)).append("\n");
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

