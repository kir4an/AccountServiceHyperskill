package account.service;

import account.Utils.JwtUtils;
import account.model.*;
import account.repository.InternRepository;
import account.repository.RoleRepository;
import account.repository.SolutionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternService {
    private InternRepository internRepository;

    @Autowired
    private LogService logService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private SolutionRepository solutionRepository;
    private final String CORRECT_ANSWER = "The problem was solved correctly. Your percentage of solved problems is increased by 10.";

    public InternService(InternRepository internRepository) {
        this.internRepository = internRepository;
    }

    public JwtResponse signup(InternRequest internRequest){
        String accessToken = jwtUtils.generateJwtToken(internRequest.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(internRequest.getEmail());
        JwtResponse jwtResponse = new JwtResponse(accessToken,refreshToken);
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleRepository.findByName(UserRoleType.ROLE_INTERN));
        UserIntern intern = new UserIntern();
        intern.setEmail(internRequest.getEmail());
        intern.setName(internRequest.getName());
        intern.setLastname(internRequest.getLastname());
        intern.setRoles(roleList);
        internRepository.save(intern);
        logService.addEvent(SecurityAction.CREATE_USER,internRequest.getEmail(),internRequest.getEmail(),"api/intern/signup");
        return jwtResponse;
    }
    public ResponseEntity<?> solvingProblem(String userSolution, String task){
        if(solutionRepository.existsByTaskIgnoreCase(task)){
            String databaseSolution = solutionRepository.findSolutionByTask(task);
            if (databaseSolution.equals(userSolution)){
                UserIntern userIntern = internRepository.findByEmailIgnoreCase(getCurrentUser());
                if(userIntern.getQuantityAttempts()==10){
                    throw new RuntimeException("Your attempts are spent");
                }
                userIntern.setQuantityAttempts(userIntern.getQuantityAttempts()+1);
                userIntern.setPercentSolvedTasks(userIntern.getPercentSolvedTasks()+10);
                return ResponseEntity.ok(CORRECT_ANSWER);
            }
        }
        return ResponseEntity.badRequest().body("Your answer is incorrect");
    }
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    public JwtResponse getAccessToken(String refreshToken){
        String email = jwtUtils.getUsernameRefreshToken(refreshToken);
        String token =  jwtUtils.generateJwtToken(email);
        return new JwtResponse(token,null);
    }
    public JwtResponse getRefreshToken(String refresh){
        String email = jwtUtils.getUsernameRefreshToken(refresh);
        String token = jwtUtils.generateRefreshToken(email);
        return new JwtResponse(null,token);
    }
}
