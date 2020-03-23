/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MyFinanceClient_JavascriptGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.codegen;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.TypeScriptAngularClientCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyFinanceClient_JavascriptGenerator extends TypeScriptAngularClientCodegen implements CodegenConfig {

    protected String apiVersion = "1.0.0";

    /**
     * Configures the type of generator.
     *
     * @return  the CodegenType for this generator
     * @see     CodegenType
     */
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -l flag.
     *
     * @return the friendly name for the generator
     */
    public String getName() {
        return "MyFinanceClient_Javascript";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    public String getHelp() {
        return "Generates a CCRClient client library.";
    }

    public MyFinanceClient_JavascriptGenerator() {
        super();


        /**
         * Reserved words.  Override this with reserved words specific to your language
         */
        reservedWords.addAll(new HashSet<String>());

        /**
         * Additional Properties.  These values can be passed to the templates and
         * are available in models, apis, and supporting files
         */
        additionalProperties.put("apiVersion", apiVersion);


        /**
         * Language Specific Primitives.  These types will not trigger imports by
         * the client generator
         */
        languageSpecificPrimitives.addAll(new HashSet<String>());

    }

    // Build Method Names
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions) {
        return this.fromOperation(path, httpMethod, operation, definitions, null);
    }

    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, definitions, swagger);
        op.nickname = op.operationId;
        for (CodegenParameter p : op.allParams) {
            op.operationId += "_" + p.paramName;
            op.nickname += "_" + p.paramName;
        }
        return op;
    }

    @Override
    public void processOpts() {
        super.processOpts();
        List<SupportingFile> filteredFileList = new ArrayList<>();
        for(SupportingFile file : supportingFiles){
            if(!file.destinationFilename.equals("git_push.sh")
                && !file.destinationFilename.equals(".gitignore")
                && !file.destinationFilename.equals(".swagger-codegen-ignore")
                ){
                filteredFileList.add(file);
            }
        }
        supportingFiles.clear();
        supportingFiles.addAll(filteredFileList);
    }


}
