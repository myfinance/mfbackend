/*
 * Dac Services
 * Dac Service REST API
 *
 * OpenAPI spec version: 1.1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package de.hf.dac.myfinance.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.hf.dac.myfinance.client.model.Cashflow;
import de.hf.dac.myfinance.client.model.Trade;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Transaction
 */
@javax.annotation.Generated(value = "de.hf.dac.myfinance.codegen.MyFinanceClient_JavaGenerator", date = "2020-03-23T23:45:07.507+01:00")
public class Transaction {
  @JsonProperty("transactionid")
  private Integer transactionid = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("transactiondate")
  private LocalDate transactiondate = null;

  @JsonProperty("lastchanged")
  private LocalDateTime lastchanged = null;

  @JsonProperty("trades")
  private List<Trade> trades = null;

  @JsonProperty("cashflows")
  private List<Cashflow> cashflows = new ArrayList<>();

  /**
   * Gets or Sets transactionType
   */
  public enum TransactionTypeEnum {
    INCOMEEXPENSES("INCOMEEXPENSES"),
    
    TRANSFER("TRANSFER"),
    
    BUDGETTRANSFER("BUDGETTRANSFER"),
    
    SECURITYCASHFLOW("SECURITYCASHFLOW"),
    
    TRADE("TRADE");

    private String value;

    TransactionTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TransactionTypeEnum fromValue(String text) {
      for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("transactionType")
  private TransactionTypeEnum transactionType = null;

  public Transaction transactionid(Integer transactionid) {
    this.transactionid = transactionid;
    return this;
  }

   /**
   * Get transactionid
   * @return transactionid
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getTransactionid() {
    return transactionid;
  }

  public void setTransactionid(Integer transactionid) {
    this.transactionid = transactionid;
  }

  public Transaction description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(required = true, value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Transaction transactiondate(LocalDate transactiondate) {
    this.transactiondate = transactiondate;
    return this;
  }

   /**
   * Get transactiondate
   * @return transactiondate
  **/
  @ApiModelProperty(required = true, value = "")
  public LocalDate getTransactiondate() {
    return transactiondate;
  }

  public void setTransactiondate(LocalDate transactiondate) {
    this.transactiondate = transactiondate;
  }

  public Transaction lastchanged(LocalDateTime lastchanged) {
    this.lastchanged = lastchanged;
    return this;
  }

   /**
   * Get lastchanged
   * @return lastchanged
  **/
  @ApiModelProperty(required = true, value = "")
  public LocalDateTime getLastchanged() {
    return lastchanged;
  }

  public void setLastchanged(LocalDateTime lastchanged) {
    this.lastchanged = lastchanged;
  }

  public Transaction trades(List<Trade> trades) {
    this.trades = trades;
    return this;
  }

  public Transaction addTradesItem(Trade tradesItem) {
    if (this.trades == null) {
      this.trades = new ArrayList<>();
    }
    this.trades.add(tradesItem);
    return this;
  }

   /**
   * Get trades
   * @return trades
  **/
  @ApiModelProperty(value = "")
  public List<Trade> getTrades() {
    return trades;
  }

  public void setTrades(List<Trade> trades) {
    this.trades = trades;
  }

  public Transaction cashflows(List<Cashflow> cashflows) {
    this.cashflows = cashflows;
    return this;
  }

  public Transaction addCashflowsItem(Cashflow cashflowsItem) {
    this.cashflows.add(cashflowsItem);
    return this;
  }

   /**
   * Get cashflows
   * @return cashflows
  **/
  @ApiModelProperty(required = true, value = "")
  public List<Cashflow> getCashflows() {
    return cashflows;
  }

  public void setCashflows(List<Cashflow> cashflows) {
    this.cashflows = cashflows;
  }

  public Transaction transactionType(TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
    return this;
  }

   /**
   * Get transactionType
   * @return transactionType
  **/
  @ApiModelProperty(required = true, value = "")
  public TransactionTypeEnum getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionTypeEnum transactionType) {
    this.transactionType = transactionType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.transactionid, transaction.transactionid) &&
        Objects.equals(this.description, transaction.description) &&
        Objects.equals(this.transactiondate, transaction.transactiondate) &&
        Objects.equals(this.lastchanged, transaction.lastchanged) &&
        Objects.equals(this.trades, transaction.trades) &&
        Objects.equals(this.cashflows, transaction.cashflows) &&
        Objects.equals(this.transactionType, transaction.transactionType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionid, description, transactiondate, lastchanged, trades, cashflows, transactionType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    
    sb.append("    transactionid: ").append(toIndentedString(transactionid)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    transactiondate: ").append(toIndentedString(transactiondate)).append("\n");
    sb.append("    lastchanged: ").append(toIndentedString(lastchanged)).append("\n");
    sb.append("    trades: ").append(toIndentedString(trades)).append("\n");
    sb.append("    cashflows: ").append(toIndentedString(cashflows)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
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
