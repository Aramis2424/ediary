package integration.context;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOwnerDetailsSecurityContextFactory.class)
public @interface WithMockOwnerDetails {
    String username() default "ivan00";
    String password() default "abc123";
    String[] roles() default {};
    long id() default 1;
}
