/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseMDRunner.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.marketdataprovider.runner;

import de.hf.dac.io.baserunner.BaseRestCommandLineRunner;
import de.hf.dac.io.baserunner.OptionsParser;
import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.io.config.resfile.Configuration;
import de.hf.dac.marketdata.client.api.MarketdataApi;
import de.hf.dac.marketdataprovider.api.runner.BaseMDRunnerParameter;
import de.hf.dac.marketdataprovider.api.runner.ImportRunnerParameter;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import org.apache.commons.codec.binary.Base64;

public abstract class BaseMDRunner extends BaseRestCommandLineRunner {


    private MarketdataApi runnerClient;
    private String credentialsHeader;
    private String apiUser = Configuration.getString("MARKETDATA", "MARKETDATA_LAUNCH_USER");
    private String password = Configuration.getString("MARKETDATA", "MARKETDATA_LAUNCH_PASSWORD");
    private String basePath = Configuration.getString("MARKETDATA", "MARKETDATA_LAUNCH_URL", "http://http://localhost:8181/dac/rest");

    public static final String ENV_OPTION = "env";

    public BaseMDRunner(){
        super(new OptionsParser());
    }


    @Override
    protected void addCustomCommandLineOptions() {

    }

    @Override
    protected RunnerParameter extractParameters() {
        String env = "dev";
        if (optionsParser.hasOption(ENV_OPTION)) {
            env = optionsParser.getOptionArg(ENV_OPTION);
        }
        return new BaseMDRunnerParameter(env);
    }

    @Override
    protected void passParamsToExternal(RunnerParameter runnerParameter) {


        if (runnerParameter instanceof ImportRunnerParameter) {
            ImportRunnerParameter p = (ImportRunnerParameter)runnerParameter;
            MarketdataApi client = createRestClient();
            try {
                client.importData_envID_jobtype("myimportjob", p.getEnvironment());
                /*JobInformation start = client.importData_envID_jobtype(p));
                int maxTimeWait = Configuration.getInt("CCR", "CCR_LAUNCH_TIMEOUT", 60*60*1000);

                String uid = start.getUuid();
                int count = 1;
                while (start.getStatus().compareTo(JobInformation.StatusEnum.FINISHED) != 0
                    && start.getStatus().compareTo(JobInformation.StatusEnum.FAILED) != 0 && maxTimeWait > 0) {
                    try {
                        long timeout = getNextTimeOut(maxTimeWait, ++count);
                        Thread.sleep(timeout);
                        maxTimeWait -= timeout;
                        if (count % 30 == 0) {
                            LOG.info("Still waiting for Job {}", start.getStatus().toString());
                        }
                        start = client.status(uid);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }

                if (start.getStatus().compareTo(JobInformation.StatusEnum.FINISHED) != 0) {
                    throw new RuntimeException("Job Execution Failed " + start );
                }*/

            } catch (ApiException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to call Externa Rest Resource");
            }
        }
    }

    private MarketdataApi createRestClient() {

        if (this.runnerClient == null) {
            if (password.startsWith("Basic ")) {
                this.credentialsHeader = password;
            } else {
                this.credentialsHeader = String.format("Basic %s", new String(Base64.encodeBase64((apiUser + ":" + password).getBytes())));
            }

            ApiClient client = new ApiClient();
            if (basePath != null) {client.setBasePath(this.basePath);}

            client.addDefaultHeader("Authorization", this.credentialsHeader);

            this.runnerClient = new MarketdataApi(client);
        }
        return this.runnerClient;

    }
}
