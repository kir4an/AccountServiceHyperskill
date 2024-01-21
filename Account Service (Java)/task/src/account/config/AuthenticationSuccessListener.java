package account.config;

import account.service.LoginAttemptService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
@Configuration
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private LoginAttemptService loginAttemptService;

    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        if(username!=null){
            loginAttemptService.loginSuccess(username);
        }
    }
}
