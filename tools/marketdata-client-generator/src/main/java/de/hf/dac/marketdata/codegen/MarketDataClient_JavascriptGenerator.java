/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataClient_JavascriptGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdata.codegen;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.languages.JavascriptClientCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

import java.util.HashSet;
import java.util.Map;

public class MarketDataClient_JavascriptGenerator  extends JavascriptClientCodegen implements CodegenConfig {

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
        return "MarketDataClient_Javascript";
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

    public MarketDataClient_JavascriptGenerator() {
        super();

        // set the output folder here
        outputFolder = "generated-code/MarketDataClient_Javascript";


        /**
         * Api Package.  Optional, if needed, this can be used in templates
         */
        apiPackage = "MarketData.client.api";

        /**
         * Model Package.  Optional, if needed, this can be used in templates
         */
        modelPackage = "MarketData.client.model";

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


}
