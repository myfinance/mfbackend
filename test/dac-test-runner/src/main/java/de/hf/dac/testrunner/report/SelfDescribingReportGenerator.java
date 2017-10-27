/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : SelfDescribingReportGenerator.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 27.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.report;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class SelfDescribingReportGenerator extends ReportGeneratorBase implements ReportGenerator {

    private transient Set<ReportGeneratorType> allTypes = null;

    @Override
    public Set<ReportGeneratorType> getReportTypes() {
        if (allTypes == null) {
            if (getClass().isAnnotationPresent(ReportGeneratorTypes.class)) {
                this.allTypes = new HashSet<>();
                this.allTypes.addAll(Arrays.asList(getClass().getAnnotation(ReportGeneratorTypes.class).value()));
            } else {
                this.allTypes = Collections.emptySet();
            }
        }
        return this.allTypes;
    }

}
