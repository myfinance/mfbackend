
# Instrument

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**instrumentid** | **Integer** |  | 
**description** | **String** |  | 
**isactive** | **Boolean** |  | 
**maturitydate** | [**LocalDate**](LocalDate.md) |  |  [optional]
**closingdate** | [**LocalDate**](LocalDate.md) |  |  [optional]
**treelastchanged** | [**LocalDateTime**](LocalDateTime.md) |  | 
**instrumentType** | [**InstrumentTypeEnum**](#InstrumentTypeEnum) |  | 
**businesskey** | **String** |  |  [optional]


<a name="InstrumentTypeEnum"></a>
## Enum: InstrumentTypeEnum
Name | Value
---- | -----
GIRO | &quot;Giro&quot;
MONEYATCALL | &quot;MoneyAtCall&quot;
TIMEDEPOSIT | &quot;TimeDeposit&quot;
BUILDINGSAVINGACCOUNT | &quot;BuildingsavingAccount&quot;
BUDGET | &quot;Budget&quot;
TENANT | &quot;Tenant&quot;
ACCOUNTPORTFOLIO | &quot;AccountPortfolio&quot;
ARTIFICIALPORTFOLIO | &quot;ArtificialPortfolio&quot;
BUDGETGROUP | &quot;BudgetGroup&quot;
DEPOT | &quot;Depot&quot;
BUILDINGSAVING | &quot;Buildingsaving&quot;
CURRENCY | &quot;Currency&quot;
EQUITY | &quot;Equity&quot;
FONDS | &quot;Fonds&quot;
ETF | &quot;ETF&quot;
INDEX | &quot;Index&quot;
BOND | &quot;Bond&quot;
LIFEINSURANCE | &quot;LifeInsurance&quot;
DEPRECIATIONOBJECT | &quot;DepreciationObject&quot;
REALESTATE | &quot;RealEstate&quot;
LOAN | &quot;Loan&quot;
UNKNOWN | &quot;UNKNOWN&quot;



