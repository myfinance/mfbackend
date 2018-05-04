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
import org.joda.time.DateTime;

import java.io.Serializable;
/**
 * NewCookie
 */
@javax.annotation.Generated(value = "class de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2018-05-03T09:44:46.536+02:00")
public class NewCookie  implements Serializable {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("value")
  private String value = null;

  @JsonProperty("version")
  private Integer version = null;

  @JsonProperty("path")
  private String path = null;

  @JsonProperty("domain")
  private String domain = null;

  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("maxAge")
  private Integer maxAge = null;

  @JsonProperty("expiry")
  private DateTime expiry = null;

  @JsonProperty("secure")
  private Boolean secure = null;

  @JsonProperty("httpOnly")
  private Boolean httpOnly = null;

  public NewCookie name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public NewCookie value(String value) {
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

  public NewCookie version(Integer version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public NewCookie path(String path) {
    this.path = path;
    return this;
  }

   /**
   * Get path
   * @return path
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public NewCookie domain(String domain) {
    this.domain = domain;
    return this;
  }

   /**
   * Get domain
   * @return domain
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public NewCookie comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public NewCookie maxAge(Integer maxAge) {
    this.maxAge = maxAge;
    return this;
  }

   /**
   * Get maxAge
   * @return maxAge
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(Integer maxAge) {
    this.maxAge = maxAge;
  }

  public NewCookie expiry(DateTime expiry) {
    this.expiry = expiry;
    return this;
  }

   /**
   * Get expiry
   * @return expiry
  **/
  @ApiModelProperty(example = "null", value = "")
  public DateTime getExpiry() {
    return expiry;
  }

  public void setExpiry(DateTime expiry) {
    this.expiry = expiry;
  }

  public NewCookie secure(Boolean secure) {
    this.secure = secure;
    return this;
  }

   /**
   * Get secure
   * @return secure
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getSecure() {
    return secure;
  }

  public void setSecure(Boolean secure) {
    this.secure = secure;
  }

  public NewCookie httpOnly(Boolean httpOnly) {
    this.httpOnly = httpOnly;
    return this;
  }

   /**
   * Get httpOnly
   * @return httpOnly
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getHttpOnly() {
    return httpOnly;
  }

  public void setHttpOnly(Boolean httpOnly) {
    this.httpOnly = httpOnly;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewCookie newCookie = (NewCookie) o;
    return Objects.equals(this.name, newCookie.name) &&
        Objects.equals(this.value, newCookie.value) &&
        Objects.equals(this.version, newCookie.version) &&
        Objects.equals(this.path, newCookie.path) &&
        Objects.equals(this.domain, newCookie.domain) &&
        Objects.equals(this.comment, newCookie.comment) &&
        Objects.equals(this.maxAge, newCookie.maxAge) &&
        Objects.equals(this.expiry, newCookie.expiry) &&
        Objects.equals(this.secure, newCookie.secure) &&
        Objects.equals(this.httpOnly, newCookie.httpOnly);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value, version, path, domain, comment, maxAge, expiry, secure, httpOnly);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewCookie {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
    sb.append("    expiry: ").append(toIndentedString(expiry)).append("\n");
    sb.append("    secure: ").append(toIndentedString(secure)).append("\n");
    sb.append("    httpOnly: ").append(toIndentedString(httpOnly)).append("\n");
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

