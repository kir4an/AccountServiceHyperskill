package account.service;

import account.model.HrManager;
import account.model.SecurityAction;
import account.model.User;
import account.model.UserIntern;
import account.repository.AccountRepository;
import account.repository.HrManagerRepository;
import account.repository.InternRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class HrService {
    private HrManagerRepository managerRepository;
    private InternRepository internRepository;
    private static final String USER_MESSAGE = "Congratulations! You have passed the selection. Now you are a developer.";
    private AccountRepository accountRepository;
    private static final int MAX_COUNT_USER = 10;
    private LogService logService;
    @Autowired
    private AccountService accountService;

    public HrService(HrManagerRepository managerRepository, InternRepository internRepository, AccountRepository accountRepository, LogService logService) {
        this.managerRepository = managerRepository;
        this.internRepository = internRepository;
        this.accountRepository = accountRepository;
        this.logService = logService;
    }

    public List<User> checkUserIntern(){
        List<UserIntern> internList = internRepository.findAll();
        Collections.sort(internList, Comparator.comparingInt(UserIntern::getPercentSolvedTasks).reversed());
        List<User> userList = new ArrayList<>();
        for(UserIntern userIntern:internList){
            if(userIntern.getPercentSolvedTasks()>80 && userIntern.getQuantityAttempts() == 10){
                HrManager manager = new HrManager();
                manager.getInternQueue().add(userIntern);
                if(accountService.checkCountUser()){
                    log.info(USER_MESSAGE);
                    internRepository.delete(userIntern);
                    logService.addEvent(SecurityAction.INTERN_BECAME_USER,userIntern.getEmail(),userIntern.getEmail(),"api/hr/addIntern");
                    User user = convertInternToUser(userIntern);
                    userList.add(user);
                }
            }
        }
        return userList;
    }
    public User convertInternToUser(UserIntern userIntern){
        User user = new User();
        user.setName(userIntern.getName());
        user.setEmail(userIntern.getEmail());
        user.setLastname(userIntern.getLastname());
        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        accountRepository.save(user);
        return user;
    }
}
