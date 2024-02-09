package account.controller;

import account.Exception.IncorrectMonthException;
import account.Exception.NotAuthenticationException;
import account.Exception.RemoveAdminException;
import account.Exception.UserNotFoundException;
import account.Utils.JwtUtils;
import account.model.*;
import account.repository.AccountRepository;
import account.repository.PaymentRepository;
import account.repository.RoleRepository;
import account.service.AccountService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth/")
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("signup")
    public SignupResponse signup(@RequestBody @Valid SignupRequest request) {
       return accountService.signup(request);
    }


    @PostMapping("changepass")
    public ChangePasswordResponse changePassword(@AuthenticationPrincipal SecurityUser user, @RequestBody @Valid NewPassword password) {
        if (user == null) {
            throw new NotAuthenticationException();
        }
        return accountService.changePass(user, password);
    }

}














