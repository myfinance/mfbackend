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
import de.hf.dac.marketdata.client.api.MDRunnerApi;
import de.hf.dac.marketdataprovider.api.runner.BaseMDRunnerParameter;
import de.hf.dac.marketdataprovider.importer.ImportRunnerParameter;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import org.apache.commons.codec.binary.Base64;
import de.hf.dac.marketdata.client.model.JobInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class BaseMDRunner extends BaseRestCommandLineRunner {

    private MDRunnerApi runnerClient;
    private String credentialsHeader;

    public static final String ENV_OPTION = "env";

    public BaseMDRunner(){
        super(new OptionsParser());
    }


    @Override
    protected void addCustomCommandLineOptions() {

    }

    @Override
    protected void passParamsToExternal(RunnerParameter runnerParameter) {


        if (runnerParameter instanceof ImportRunnerParameter) {
            ImportRunnerParameter p = (ImportRunnerParameter)runnerParameter;
            MDRunnerApi client = createRestClient();
            try {
                JobInformation start = client.start(p.getEnvironment(), ImportRunnerParameter.JOBTYPE, convertParam(p));
                int maxTimeWait = Configuration.getInt("MARKETDATA", "MD_LAUNCH_TIMEOUT", 60*60*1000);

                String uid = start.getUuid();
                int count = 1;
                while (start.getStatus().compareTo(JobInformation.StatusEnum.FINISHED) != 0
                    && start.getStatus().compareTo(JobInformation.StatusEnum.FAILED) != 0 && maxTimeWait > 0) {
                    try {
                        long timeout = getNextTimeOut(maxTimeWait, ++count);
                        Thread.sleep(timeout);
                        maxTimeWait -= timeout;
                        if (count % 30 == 0) {
                            log.info("Still waiting for Job {}", start.getStatus().toString());
                        }
                        start = client.status(uid);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }

                if (start.getStatus().compareTo(JobInformation.StatusEnum.FINISHED) != 0) {
                    throw new RuntimeException("Job Execution Failed " + start );
                }

            } catch (ApiException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to call Externa Rest Resource");
            }
        }
    }

    private MDRunnerApi createRestClient() {
        String apiUser = Configuration.getString("MARKETDATA", "MARKETDATA_LAUNCH_USER");
        String password = Configuration.getString("MARKETDATA", "MARKETDATA_LAUNCH_PASSWORD");
        String basePath = Configuration.getString("MARKETDATA", "MARKETDATA_LAUNCH_URL", "http://localhost:8181/dac/rest");
        if (this.runnerClient == null) {
            if (password.startsWith("Basic ")) {
                this.credentialsHeader = password;
            } else {
                this.credentialsHeader = String.format("Basic %s", new String(Base64.encodeBase64((apiUser + ":" + password).getBytes())));
            }

            ApiClient client = new ApiClient();
            if (basePath != null) {client.setBasePath(basePath);}

            client.addDefaultHeader("Authorization", this.credentialsHeader);

            this.runnerClient = new MDRunnerApi(client);
        }
        return this.runnerClient;
    }

    protected long getNextTimeOut(int maxTimeWait, int count) {
        // some fancier logic possible
        return maxTimeWait > 1000 ? 1000*(count%10) : maxTimeWait;
    }

    private de.hf.dac.marketdata.client.model.BaseMDRunnerParameter convertParam(BaseMDRunnerParameter rp) {
        de.hf.dac.marketdata.client.model.BaseMDRunnerParameter p = new de.hf.dac.marketdata.client.model.BaseMDRunnerParameter();
        p.setEnvironment(rp.getEnvironment());
        p.setParams(new HashMap());
        p.getParams().putAll(rp.getParams());
        return p;
    }
}
