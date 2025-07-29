package integration.security.context;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.srd.ediary.application.security.OwnerDetails;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WithMockOwnerDetailsSecurityContextFactory
        implements WithSecurityContextFactory<WithMockOwnerDetails> {
    @Override
    public SecurityContext createSecurityContext(WithMockOwnerDetails ownerDetails) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        OwnerDetails principal = new OwnerDetails(
                ownerDetails.username(),
                ownerDetails.password(),
                Arrays.stream(ownerDetails.roles()).toList()
                        .stream()
                        .map(stringAuth -> (GrantedAuthority) () -> stringAuth)
                        .collect(Collectors.toList()),
                ownerDetails.id()
        );

        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
