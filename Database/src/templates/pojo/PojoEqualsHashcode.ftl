<#if pojo.needsEqualsHashCode() && !clazz.superclass?exists>
    /** Checks the equality of two entities.
     * @param other Other entity
     * @return True if the objects are equal
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true; 
        }
		if (other == null) {
		    return false;
		}
		if (!(other instanceof ${pojo.getDeclarationName()})) {
		    return false;
		}
		${pojo.getDeclarationName()} castOther = (${pojo.getDeclarationName()}) other; 
         
		return ${pojo.generateEquals("this", "castOther", jdk5)};
    }
   
    /** Computes hashCode.
     * @return The hash code 
     */
    public int hashCode() {
        int result = 17;
         
<#foreach property in pojo.getAllPropertiesIterator()>         ${pojo.generateHashCode(property, "result", "this", jdk5)}
</#foreach>        return result;
    }  
</#if>