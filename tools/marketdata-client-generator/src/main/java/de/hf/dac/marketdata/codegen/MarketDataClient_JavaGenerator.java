/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : MarketDataClient_JavaGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdata.codegen;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.JavaClientCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MarketDataClient_JavaGenerator  extends JavaClientCodegen implements CodegenConfig {

    // source folder where to write the files
    protected String sourceFolder = "src/main/java";
    protected String apiVersion = "1.0.0";

    /**
     * Configures the type of generator.
     *
     * @return the CodegenType for this generator
     * @see io.swagger.codegen.CodegenType
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
        return "templates/MarketDataClient_Java";
    }

    /**
     * Returns human-friendly help for the generator.  Provide the consumer with help
     * tips, parameters here
     *
     * @return A string value for the help message
     */
    public String getHelp() {
        return "Generates a MarketDataClient client library.";
    }

    public MarketDataClient_JavaGenerator() {
        super();

        // set the output folder here
        outputFolder = "generated-code/MarketDataClient_Java";


        /**
         * Api Package.  Optional, if needed, this can be used in templates
         */
        apiPackage = "de.hf.dac.marketdata.client.api";

        /**
         * Model Package.  Optional, if needed, this can be used in templates
         */
        modelPackage = "de.hf.dac.marketdata.client.model";

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
         * Supporting Files.  You can write single files for the generator with the
         * entire object tree available.  If the input file has a suffix of `.mustache
         * it will be processed by the template engine.  Otherwise, it will be copied
         */
        //    supportingFiles.add(new SupportingFile("myFile.mustache",   // the input template or file
        //      "",                                                       // the destination folder, relative `outputFolder`
        //      "myFile.sample")                                          // the output file
        //    );
        supportingFiles.add(new SupportingFile("bnd.bnd","","bnd.bnd"));

        /**
         * Language Specific Primitives.  These types will not trigger imports by
         * the client generator
         */
        languageSpecificPrimitives.addAll(new HashSet<String>());

        groupId = "de.hf.dac.marketdata";
        artifactId = "marketdata-client-generated";
        //artifactVersion = getClass().getPackage().getImplementationVersion();


        additionalProperties.put("invokerPackage", invokerPackage);
        additionalProperties.put("groupId", groupId);
        additionalProperties.put("artifactId", artifactId);
        //additionalProperties.put("artifactVersion", artifactVersion);

        //templateDir = "templates/CCRClient_Java";

    }



    // Build Method Names
    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions) {
        return this.fromOperation(path, httpMethod, operation, definitions, null);
    }

    public static SortedMap<Integer,CodegenParameter> getOrderedParams(String path, List<CodegenParameter> params) {
        SortedMap<Integer,CodegenParameter> paramsOrdered = new TreeMap<>();
        for (CodegenParameter p : params) {
            String pattern = "{" + p.paramName + "}";
            Integer location = path.indexOf(pattern);
            Integer lastLocation = path.lastIndexOf(pattern);
            if(!lastLocation.equals(location)) {
                throw new RuntimeException("Ambiguous Key in Path: " + pattern + " " + path);
            }
            if(location < 0) {
                throw new RuntimeException("Key not found in Path : " + pattern + " " + path);
            }
            paramsOrdered.put(location,p);
        }
        return paramsOrdered;
    }

    @Override
    public CodegenOperation fromOperation(String path, String httpMethod, Operation operation, Map<String, Model> definitions, Swagger swagger) {
        CodegenOperation op = super.fromOperation(path, httpMethod, operation, definitions, swagger);
        if(!op.operationId.toUpperCase().startsWith("VERBATIM")) {

            SortedMap<Integer,CodegenParameter> paramsOrdered = getOrderedParams(path, op.pathParams);

            op.nickname = op.operationId;

            for(CodegenParameter p : paramsOrdered.values()) {
                op.operationId += "_" + p.paramName;
            }
            for(CodegenParameter p : op.queryParams) {
                op.operationId += "_" + p.paramName;
            }
        } else {
            op.operationId = op.operationId.substring("VERBATIM".length());
            op.nickname = op.operationId;
        }
        return op;
    }

}
