
# Transaction

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**transactionid** | **Integer** |  | 
**description** | **String** |  | 
**transactiondate** | [**LocalDate**](LocalDate.md) |  | 
**lastchanged** | [**LocalDateTime**](LocalDateTime.md) |  | 
**trades** | [**List&lt;Trade&gt;**](Trade.md) |  |  [optional]
**cashflows** | [**List&lt;Cashflow&gt;**](Cashflow.md) |  | 
**transactionType** | [**TransactionTypeEnum**](#TransactionTypeEnum) |  | 


<a name="TransactionTypeEnum"></a>
## Enum: TransactionTypeEnum
Name | Value
---- | -----
INCOMEEXPENSES | &quot;INCOMEEXPENSES&quot;
TRANSFER | &quot;TRANSFER&quot;
BUDGETTRANSFER | &quot;BUDGETTRANSFER&quot;
SECURITYCASHFLOW | &quot;SECURITYCASHFLOW&quot;
TRADE | &quot;TRADE&quot;



