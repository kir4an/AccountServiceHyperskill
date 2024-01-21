package account.controller;

import account.model.*;
import account.service.AccountService;
import account.service.LogService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {
    private AccountService accountService;

    private LogService logService;

    public AdminController(AccountService accountService, LogService logService) {
        this.accountService = accountService;
        this.logService = logService;
    }

    @GetMapping("api/admin/user/")
    public List<SignupResponse> getAllUsers() {
        return accountService.convertToSignupResponse();
    }

    @Transactional
    @DeleteMapping("api/admin/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email, @AuthenticationPrincipal SecurityUser securityUser) {
        return accountService.deleteUserByEmail(email, securityUser);
    }

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<?> getRole(@RequestBody ChangeRole role, @AuthenticationPrincipal SecurityUser user) {
        var response = accountService.removeOrGrantRole(role, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("api/admin/user/access")
    public ResponseEntity<?> LockUser(@RequestBody ChangeLockStatus lockStatus, @AuthenticationPrincipal SecurityUser user) {
        accountService.checkLockeUser(user);
        return accountService.adminSelection(lockStatus, user);
    }

    @GetMapping("api/security/events/")
    public List<SecurityEvent> getAllEvents(@AuthenticationPrincipal SecurityUser user) {
        accountService.checkLockeUser(user);
        return logService.getAllEvents();
    }
}
