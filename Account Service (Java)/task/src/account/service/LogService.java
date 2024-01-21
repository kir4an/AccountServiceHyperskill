package account.service;

import account.model.SecurityAction;
import account.model.SecurityEvent;
import account.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LogService {
    private SecurityEventRepository securityEventRepository;

    public LogService(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }
    public void addEvent(SecurityAction action, String subject, String object, String path){
        securityEventRepository.save(new SecurityEvent(action,subject,object,path));
    }
    public List<SecurityEvent> getAllEvents(){
        return securityEventRepository.findAll();
    }
}
