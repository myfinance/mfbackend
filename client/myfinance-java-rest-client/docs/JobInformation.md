
# JobInformation

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**uuid** | **String** |  |  [optional]
**startTime** | **String** |  |  [optional]
**status** | [**StatusEnum**](#StatusEnum) |  |  [optional]
**statusChangeHistory** | [**List&lt;StatusChange&gt;**](StatusChange.md) |  |  [optional]
**payload** | **Object** |  |  [optional]
**result** | **Object** |  |  [optional]
**statusMessageCollector** | [**List&lt;StatusChange&gt;**](StatusChange.md) |  |  [optional]
**endTime** | **String** |  |  [optional]


<a name="StatusEnum"></a>
## Enum: StatusEnum
Name | Value
---- | -----
UNKNOWN | &quot;UNKNOWN&quot;
QUEUED | &quot;QUEUED&quot;
PROCESSING | &quot;PROCESSING&quot;
FINISHED | &quot;FINISHED&quot;
FAILED | &quot;FAILED&quot;



