<#if ejb3?if_exists>
    <#if pojo.isComponent()>
@${pojo.importType("javax.persistence.Embeddable")}
    <#else>
@${pojo.importType("javax.persistence.Entity")}
@${pojo.importType("javax.persistence.Table")}(
    name="${clazz.table.name}"<#assign uniqueConstraint=pojo.generateAnnTableUniqueConstraint()><#if uniqueConstraint?has_content>,
    uniqueConstraints = ${uniqueConstraint}
    </#if>)
    </#if>
</#if>