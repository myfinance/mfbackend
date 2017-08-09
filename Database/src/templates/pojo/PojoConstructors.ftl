
    /** Default constructor. */ 
    public ${pojo.getDeclarationName()}() {
    }

<#if pojo.needsMinimalConstructor() && pojo.getPropertiesForMinimalConstructor().size() lte 10>	
    /** Minimal constructor. 
<#foreach field in pojo.getPropertiesForMinimalConstructor()>
      * @param ${field.name} Field
</#foreach>    
    */ 
    public ${pojo.getDeclarationName()}(${c2j.asParameterList(pojo.getPropertyClosureForMinimalConstructor(), jdk5, pojo)}) {
<#if pojo.isSubclass() && !pojo.getPropertyClosureForSuperclassMinimalConstructor().isEmpty()>
        super(${c2j.asArgumentList(pojo.getPropertyClosureForSuperclassMinimalConstructor())});        
</#if>
<#foreach field in pojo.getPropertiesForMinimalConstructor()>
        this.${field.name} = ${field.name};
</#foreach>
    }
</#if>    
<#if pojo.needsFullConstructor() && pojo.getPropertiesForFullConstructor().size() lte 20>
    /** Full constructor. 
<#foreach field in pojo.getPropertiesForFullConstructor()>
      * @param ${field.name} Field
</#foreach>    
    */ 
    public ${pojo.getDeclarationName()}(${c2j.asParameterList(pojo.getPropertyClosureForFullConstructor(), jdk5, pojo)}) {
<#if pojo.isSubclass() && !pojo.getPropertyClosureForSuperclassFullConstructor().isEmpty()>
        super(${c2j.asArgumentList(pojo.getPropertyClosureForSuperclassFullConstructor())});        
</#if>
<#foreach field in pojo.getPropertiesForFullConstructor()> 
        this.${field.name} = ${field.name};
</#foreach>
    }
</#if>    
