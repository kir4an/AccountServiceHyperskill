package account.controller;

import account.Dto.ChangePasswordDto;
import account.Dto.SignupDto;
import account.Exception.NotAuthenticationException;
import account.model.*;
import account.request.NewPasswordRequest;
import account.request.SignupRequest;
import account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/")
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("signup")
    public ResponseEntity<SignupDto> signup(@RequestBody @Valid SignupRequest request) {
        SignupDto response = accountService.signup(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PostMapping("changepass")
    public ResponseEntity<ChangePasswordDto> changePassword(@AuthenticationPrincipal SecurityUser user, @RequestBody @Valid NewPasswordRequest password) {
        if (user == null) {
            throw new NotAuthenticationException();
        }
        ChangePasswordDto passwordDto = accountService.changePass(user,password);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(passwordDto);
    }

}














