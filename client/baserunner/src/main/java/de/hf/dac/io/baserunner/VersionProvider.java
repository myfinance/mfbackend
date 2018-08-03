/**
 * ----------------------------------------------------------------------------
 * ---          DZ Bank FfM - Application Development                       ---
 * Copyright (c) 2014, ... All Rights Reserved
 * Project     : dac
 * File        : VersionProvider.java
 * Author(s)   : xn01598
 * Created     : 03.08.2018
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.io.baserunner;

import org.slf4j.MDC;

import de.hf.dac.common.BuildMetadataUtil;
import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {
    BuildMetadataUtil buildMetadata = null;
    String[] versionInfo;

    public VersionProvider(){
        this.buildMetadata = BuildMetadataUtil.get(this.getClass());
        versionInfo=new String[]{
            "version: "+buildMetadata.getVersion(),
            "time of build: "+buildMetadata.getBuildTimestamp(),
            "builduser: "+ buildMetadata.getUserName()};

        MDC.put("DacVersion", buildMetadata.getVersion());
    }

    @Override
    public String[] getVersion() throws Exception {
        return versionInfo;
    }
}
