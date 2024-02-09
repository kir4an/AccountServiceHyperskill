package account.service;

import account.Exception.UserLockedException;
import account.model.SecurityAction;
import account.model.SecurityUser;
import account.model.User;
import account.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class LoginAttemptService {
    public LogService logService;
    private AccountRepository accountRepository;

    public LoginAttemptService(LogService logService, AccountRepository accountRepository) {
        this.logService = logService;
        this.accountRepository = accountRepository;
    }

    public static final int MAX_ATTEMPT = 4;

    public void loginSuccess(String username) {
        User user = accountRepository.findByEmailIgnoreCase(username);
        if (user == null) {
            User user1 = new User();
            accountRepository.save(user1);
        } else {
            checkedTimeLocked(user);
        }
    }

    public void loginFailed(String username, String uri) {
        User user = accountRepository.findByEmailIgnoreCase(username);
        if (user != null) {
            logService.addEvent(SecurityAction.LOGIN_FAILED, username, uri, uri);
        } else {
            logService.addEvent(SecurityAction.LOGIN_FAILED, username, uri, uri);
            return;
        }

        user.setFailedAttempts(user.getFailedAttempts() + 1);
        if (user.getFailedAttempts() > MAX_ATTEMPT && !user.isAccountLocked()) {
            logService.addEvent(SecurityAction.BRUTE_FORCE, username, uri, uri);
            logService.addEvent(SecurityAction.LOCK_USER, username, "Lock user " + username, uri);
            lockingUser(user);
        }
        accountRepository.save(user);
    }

    public void lockingUser(User user) {
        user.setAccountLocked(true);
        LocalDateTime currentTime = LocalDateTime.now();
        user.setUnlockingTime(currentTime.plusMinutes(1));
    }
    public void checkedTimeLocked(User user){
        if(user.isAccountLocked()){
            LocalDateTime userLockedTime = user.getUnlockingTime();
            if(userLockedTime.isBefore(LocalDateTime.now())){
                user.setAccountLocked(false);
                user.setUnlockingTime(null);
                user.setFailedAttempts(0);
                accountRepository.save(user);
            }
        }
    }
}
