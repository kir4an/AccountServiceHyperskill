package account.controller;

import account.Utils.JwtUtils;
import account.model.HrManager;
import account.model.HrRequest;
import account.model.User;
import account.repository.HrManagerRepository;
import account.service.HrService;
import account.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RestController
public class HrController {
    private JwtUtils jwtUtils;
    private HrService hrService;
    private LogService logService;
    @Autowired
    public HrController(JwtUtils jwtUtils, HrService hrService, LogService logService) {
        this.jwtUtils = jwtUtils;
        this.hrService = hrService;
        this.logService = logService;
    }

    @PostMapping("api/hr/signup")
    public ResponseEntity<String> signup(@RequestBody HrRequest request){
        String token = jwtUtils.generateJwtByHR(request);
        return ResponseEntity.ok(token);
    }
    @GetMapping("api/hr/addIntern")
    public ResponseEntity<List<User>> checkUserInternResult(){
        List<User> resultUser = hrService.checkUserIntern();
        return ResponseEntity.ok(resultUser);
    }
}
