<#if ejb3?if_exists>
<#if pojo.isComponent()>
@${pojo.importType("javax.persistence.Embeddable")}
<#else>
<#if pojo.getDeclarationName()?starts_with("Base")>
@${pojo.importType("javax.persistence.MappedSuperclass")}
<#else>
@${pojo.importType("javax.persistence.Entity")}
</#if>
@${pojo.importType("javax.persistence.Table")}(name="${clazz.table.name}"<#assign uniqueConstraint=pojo.generateAnnTableUniqueConstraint()><#if uniqueConstraint?has_content>
    , uniqueConstraints = ${uniqueConstraint} 
</#if>)
</#if>
</#if>