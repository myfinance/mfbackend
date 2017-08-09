<#-- // Property accessors -->
<#foreach property in pojo.getAllPropertiesIterator()>
<#if pojo.getMetaAttribAsBool(property, "gen-property", true)>
 <#if pojo.hasFieldJavaDoc(property)>    
    /**       
     * ${pojo.getFieldJavaDoc(property, 4)}
     */
</#if>
    <#include "GetPropertyAnnotation.ftl"/>
    ${pojo.getPropertyGetModifiers(property)} ${pojo.getJavaTypeName(property, jdk5)} ${pojo.getGetterSignature(property)}() {
        return this.${property.name};
    }
    
    ${pojo.getPropertySetModifiers(property)} void set${pojo.getPropertyName(property)}(${pojo.getJavaTypeName(property, jdk5)} ${property.name}) {
        this.${property.name} = ${property.name};
    }
    <#if pojo.getJavaTypeName(property, jdk5) = "char">
    <#-- Generate boolean accessors for char field -->    
	@${pojo.importType("javax.persistence.Transient")}
    ${pojo.getPropertyGetModifiers(property)} boolean is${pojo.getGetterSignature(property).substring(3)}Boolean() {
        return this.${property.name} == '1';
    }
    
	@${pojo.importType("javax.persistence.Transient")}
    ${pojo.getPropertySetModifiers(property)} void set${pojo.getPropertyName(property)}Boolean(boolean a${property.name}) {
        this.${property.name} = a${property.name} ? '1' : '0';
    }
    
    </#if>
    <#if pojo.getJavaTypeName(property, jdk5) = "Character">
    <#-- Generate boolean accessors for char field -->
	@${pojo.importType("javax.persistence.Transient")}
    ${pojo.getPropertyGetModifiers(property)} boolean is${pojo.getGetterSignature(property).substring(3)}Boolean() {
        return this.${property.name} != null && this.${property.name} == '1';
    }
    
	@${pojo.importType("javax.persistence.Transient")}
    ${pojo.getPropertySetModifiers(property)} void set${pojo.getPropertyName(property)}Boolean(boolean a${property.name}) {
        this.${property.name} = a${property.name} ? '1' : '0';
    }
    
    </#if>
</#if>
</#foreach>
