/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : BaseMFRunnerClient.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 10.02.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.myfinance.runner;

import de.hf.dac.api.io.routes.job.RunnerParameter;
import de.hf.dac.io.baserunner.BaseRunner;
import de.hf.dac.io.config.resfile.Configuration;
import de.hf.dac.myfinance.api.runner.BaseMFRunnerParameter;
import de.hf.dac.myfinance.client.api.MyFinanceRunnerApi;
import de.hf.dac.myfinance.client.model.JobInformation;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;

public abstract class BaseMFRunnerClient extends BaseRunner {

    private MyFinanceRunnerApi runnerClient;
    private String credentialsHeader;
    private static final String CONFIG_SECTION = "MF_SERVER_CON";

    public abstract String getJobType();

    protected abstract RunnerParameter extractParameters();

    @Override
    protected void run() {
        RunnerParameter runnerParameter = extractParameters();
        if (runnerParameter instanceof BaseMFRunnerParameter) {
            BaseMFRunnerParameter p = (BaseMFRunnerParameter)runnerParameter;
            MyFinanceRunnerApi client = createRestClient();
            try {
                JobInformation start = client.start(p.getEnvironment(), getJobType(), convertParam(p));
                int maxTimeWait = Configuration.getInt(CONFIG_SECTION, "MF_LAUNCH_TIMEOUT", 60*60*1000);

                String uid = start.getUuid();
                int count = 1;
                while (start.getStatus().compareTo(JobInformation.StatusEnum.FINISHED) != 0
                    && start.getStatus().compareTo(JobInformation.StatusEnum.FAILED) != 0 && maxTimeWait > 0) {
                    try {
                        long timeout = getNextTimeOut(maxTimeWait, ++count);
                        Thread.sleep(timeout);
                        maxTimeWait -= timeout;
                        if (count % 30 == 0) {
                            log.info("Still waiting for Import {}", start.getStatus().toString());
                        }
                        start = client.status(uid);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }

                if (start.getStatus().compareTo(JobInformation.StatusEnum.FINISHED) != 0) {
                    throw new RuntimeException("Import Execution Failed " + start );
                }

            } catch (ApiException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to call External Rest Resource");
            }
        }
    }

    private MyFinanceRunnerApi createRestClient() {
        String apiUser = Configuration.getString(CONFIG_SECTION, "MF_LAUNCH_USER");
        String password = Configuration.getString(CONFIG_SECTION, "MF_LAUNCH_PASSWORD");
        String basePath = Configuration.getString(CONFIG_SECTION, "MF_LAUNCH_URL", "http://localhost:8181/dac/rest");
        if (this.runnerClient == null) {
            if (password.startsWith("Basic ")) {
                this.credentialsHeader = password;
            } else {
                this.credentialsHeader = String.format("Basic %s", new String(Base64.encodeBase64((apiUser + ":" + password).getBytes())));
            }
            ApiClient client = client = new ApiClient();

            if (basePath != null) {client.setBasePath(basePath);}

            client.addDefaultHeader("Authorization", this.credentialsHeader);

            this.runnerClient = new MyFinanceRunnerApi(client);
        }
        return this.runnerClient;
    }

    protected long getNextTimeOut(int maxTimeWait, int count) {
        // some fancier logic possible
        return maxTimeWait > 1000 ? 1000*(count%10) : maxTimeWait;
    }

    private de.hf.dac.myfinance.client.model.BaseMFRunnerParameter convertParam(BaseMFRunnerParameter rp) {
        de.hf.dac.myfinance.client.model.BaseMFRunnerParameter p = new de.hf.dac.myfinance.client.model.BaseMFRunnerParameter();
        p.setEnvironment(rp.getEnvironment());
        p.setParams(new HashMap());
        p.getParams().putAll(rp.getParams());
        return p;
    }
}
