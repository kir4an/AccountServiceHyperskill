package account.controller;

import account.request.InternRequest;
import account.Dto.JwtResponseDto;
import account.request.RefreshJwtRequest;
import account.service.InternService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternController {
    private InternService internService;

    public InternController(InternService internService) {
        this.internService = internService;
    }

    @PostMapping("api/intern/signup")
    public ResponseEntity<JwtResponseDto> signupWithJwt(@RequestBody InternRequest internRequest){
        var jwt = internService.signup(internRequest);
        return ResponseEntity.ok(jwt);
    }
    @PostMapping("api/intern/{task}")
    public ResponseEntity<?> solvedTasks(@RequestBody String UserSolution, @PathVariable String task){
        return internService.solvingProblem(UserSolution,task);
    }
    @PostMapping("api/intern/access")
    public ResponseEntity<JwtResponseDto> updateAccessToken(@RequestBody RefreshJwtRequest request){
        var refreshToken = internService.getAccessToken(request.getRefreshRequest());
        return ResponseEntity.ok(refreshToken);
    }
    @PostMapping("api/intern/refresh")
    public ResponseEntity<JwtResponseDto> updateRefreshToken(@RequestBody RefreshJwtRequest request) {
        var refreshToken = internService.getRefreshToken(request.getRefreshRequest());
        return ResponseEntity.ok(refreshToken);
    }
}
