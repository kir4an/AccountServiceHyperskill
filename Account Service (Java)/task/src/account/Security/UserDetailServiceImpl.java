package account.Security;

import account.Exception.UserNotFoundException;
import account.model.SecurityUser;
import account.model.User;
import account.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private AccountRepository accountRepository;

    public UserDetailServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(email==null){
            throw new UserNotFoundException("User not found!");
        }
        User user = accountRepository.findByEmailIgnoreCase(email);
        if(user != null){
            return new SecurityUser(user);
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
