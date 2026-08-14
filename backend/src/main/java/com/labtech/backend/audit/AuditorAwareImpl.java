package com.labtech.backend.audit;

import com.labtech.backend.util.ApplicationUtility;
import io.jsonwebtoken.security.Jwks;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component ("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor () {return Optional.of(ApplicationUtility.getLoggedInUser());}
}
