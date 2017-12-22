package com.pluralsight.springbatch.patientbatchloader.config.audit;

import com.pluralsight.springbatch.patientbatchloader.config.Constants;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware with default system account
 */
@Component
public class SystemAccountAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return Constants.SYSTEM_ACCOUNT;
    }
}
