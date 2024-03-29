package account.service;

import account.Dto.ChangePasswordDto;
import account.Dto.InfoResponseDto;
import account.Dto.SignupDto;
import account.Exception.*;
import account.Mapper.DtoMapper;
import account.Mapper.UserMapper;
import account.model.*;
import account.repository.AccountRepository;
import account.repository.PaymentRepository;
import account.repository.RoleRepository;
import account.request.ChangeRoleRequest;
import account.request.LockStatusRequest;
import account.request.NewPasswordRequest;
import account.request.SignupRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final int MAX_COUNT_USER = 10;
    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    private PaymentRepository paymentRepository;
    private RoleRepository roleRepository;
    @Autowired
    private LogService logService;
    private final List<String> listPassword = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, PaymentRepository paymentRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.paymentRepository = paymentRepository;
        this.roleRepository = roleRepository;
    }

    public User getUser(SignupRequest request) {
        return accountRepository.findByEmailIgnoreCase(request.getEmail());
    }

    public SignupDto signup(SignupRequest request) {
        if (listPassword.contains(request.getPassword())) {
            throw new HackedPassword("The password is in the hacker's database!");
        }
        if (accountRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new UserAlreadyExistsException("User exist!");
        }
        List<Role> roles = new ArrayList<>();
        checkCountUser();
        User user = UserMapper.INSTANCE.signupRequestToUser(request,passwordEncoder.encode(request.getPassword()),roles);
        if (accountRepository.findAll().isEmpty()) {
            roles.add(roleRepository.findByName(UserRoleType.ROLE_ADMINISTRATOR));
            Collections.sort(roles);
            user.setRole(roles);
            accountRepository.save(user);
            logService.addEvent(SecurityAction.CREATE_USER, "Anonymous", user.getEmail().toLowerCase(), "api/auth/signup");
        } else {
            user.getRole().add(roleRepository.findByName(UserRoleType.ROLE_USER));
            accountRepository.save(user);
            logService.addEvent(SecurityAction.CREATE_USER, "Anonymous", user.getEmail().toLowerCase(), "api/auth/signup");
        }
        User user1 = accountRepository.findByEmailIgnoreCase(request.getEmail());

        return DtoMapper.INSTANCE.toSignupResponse(user1,user1.getRole());
    }

    public ChangePasswordDto changePass(SecurityUser user, NewPasswordRequest password) {
        User user1 = accountRepository.findByEmailIgnoreCase(user.getUsername());
        if (user1.isAccountLocked()) {
            throw new UserLockedException("User account is locked");
        }
        String passwordDB = user1.getPassword();
        if (passwordEncoder.matches(password.getNewPassword(), passwordDB)) {
            throw new SamePasswordException("The passwords must be different!");
        }
        if (listPassword.contains(password.getNewPassword())) {
            throw new HackedPassword("The password is in the hacker's database!");
        } else {
            user1.setPassword(passwordEncoder.encode(password.getNewPassword()));
            accountRepository.save(user1);
            logService.addEvent(SecurityAction.CHANGE_PASSWORD, user1.getEmail().toLowerCase(), user1.getEmail().toLowerCase(), "api/auth/changepass");
        }
        ChangePasswordDto passwordResponse = new ChangePasswordDto();
        passwordResponse.setEmail(user1.getEmail().toLowerCase());
        passwordResponse.setStatus("The password has been updated successfully");
        return passwordResponse;
    }

    @Transactional
    public void createPayments(List<Payment> payments) {
        for (Payment payment : payments) {
            List<Payment> paymentList = paymentRepository.findAllByEmailIgnoreCase(payment.getEmail());
            for (Payment payment1 : paymentList) {
                if (payment.getPeriod().equals(payment1.getPeriod())) {
                    throw new IncorrectMonthException("Payment for this month already exist!");
                }
            }
        }
        paymentRepository.saveAll(payments);
    }

    public void validationUserAndPayment(User user, Payment payment) {
        String month = payment.getPeriod();
        int monthValue = Integer.parseInt(month.substring(0, 2));
        if (user == null) {
            throw new UserNotFoundException("This employee doesn't work in this organization");
        } else if (payment.getSalary() < 0) {
            throw new NegativeSalary("Salary should not be negative!");
        } else if (monthValue > 12 || monthValue < 1) {
            throw new IncorrectMonthException("Month should be between 1 and 12!");
        }
    }

    public boolean checkCountUser() {
        List<User> userList = accountRepository.findAll();
        int count = 0;
        for (User user : userList) {
            Role userRole = roleRepository.findByName(UserRoleType.ROLE_USER);
            if (user.getRole().contains(userRole)) {
                count++;
            }
        }
        if (count > MAX_COUNT_USER) {
            throw new QuantityUserExceededException("The state is overcrowded");
        }
        return true;
    }

    public void updatePayment(Payment updatePayment) {
        String month = updatePayment.getPeriod();
        int monthValue = Integer.parseInt(month.substring(0, 2));
        if (monthValue > 12 || monthValue < 1) {
            throw new NegativeSalary("sosite");
        }
        User user = accountRepository.findByEmailIgnoreCase(updatePayment.getEmail());
        if (user == null) {
            throw new UserNotFoundException("This employee dont work in this organization");
        }
        if (updatePayment.getSalary() < 0) {
            throw new NegativeSalary("Salary should not be negative!");
        }
        Payment payment = paymentRepository.findByPeriodAndEmailIgnoreCase(updatePayment.getPeriod(), updatePayment.getEmail());
        payment.setSalary(updatePayment.getSalary());
        paymentRepository.save(payment);
    }

    public InfoResponseDto convertToInfoResponse(Payment payment, String email) {
        User user = accountRepository.findByEmailIgnoreCase(email);
        YearMonth yearMonth = YearMonth.parse(payment.getPeriod(), DateTimeFormatter.ofPattern("MM-yyyy"));
        String monthName = yearMonth.getMonth().toString().substring(0, 1).toUpperCase() + yearMonth.getMonth().toString().substring(1).toLowerCase();
        int dollars = payment.getSalary() / 100;
        int cents = payment.getSalary() % 100;
        String salaryFormatted = String.format("%d dollar(s) %d cent(s)", dollars, cents);
        return DtoMapper.INSTANCE.toInfoResponse(user, payment, salaryFormatted);
    }

    public List<InfoResponseDto> getFormattedPayments(String email) {
        List<Payment> payments = paymentRepository.findAllByEmailIgnoreCaseOrderByPeriodDesc(email);
        List<InfoResponseDto> formattedPayments = new ArrayList<>();
        for (Payment payment : payments) {
            InfoResponseDto info = convertToInfoResponse(payment, email);
            formattedPayments.add(info);
        }
        return formattedPayments;
    }

    public List<SignupDto> convertToSignupResponse() {
        List<User> userList = accountRepository.findAllByOrderById();
        return userList.stream()
                .map(user -> DtoMapper.INSTANCE.toSignupResponse(user,user.getRole()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> deleteUserByEmail(String email, SecurityUser securityUser) {
        checkLockeUser(securityUser);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("user", email);
        resultMap.put("status", "Deleted successfully!");
        User user = accountRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new UserNotFoundException("User not found!");
        }
        if (user.getRole().stream().anyMatch(role -> role.getName() == UserRoleType.ROLE_ADMINISTRATOR)) {
            throw new RemoveAdminException("Can't remove ADMINISTRATOR role!");
        } else {
            accountRepository.deleteByEmailIgnoreCase(email);
            logService.addEvent(SecurityAction.DELETE_USER, securityUser.getUsername().toLowerCase(), user.getEmail().toLowerCase(), "api/admin/user");
            return ResponseEntity.ok(resultMap);
        }
    }

    public SignupDto removeOrGrantRole(ChangeRoleRequest role, SecurityUser user) {
        User userDatabase = accountRepository.findByEmailIgnoreCase(role.getUser());
        checkLockeUser(user);
        UserRoleType roleValue;
        try {
            roleValue = UserRoleType.valueOf("ROLE_" + role.getRole());
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundException("Role not found!");
        }
        Role newRole = roleRepository.findByName(roleValue);
        if (!roleRepository.findAll().contains(newRole)) {
            throw new RoleNotFoundException("Role not found!");
        }
        if (accountRepository.findByEmailIgnoreCase(role.getUser()) == null) {
            throw new UserNotFoundException("User not found!");
        }
        Role UserRole = roleRepository.findByName(UserRoleType.ROLE_USER);
        Role AccountantRole = roleRepository.findByName(UserRoleType.ROLE_ACCOUNTANT);
        Role AdminRole = roleRepository.findByName(UserRoleType.ROLE_ADMINISTRATOR);
        Role AuditorRole = roleRepository.findByName(UserRoleType.ROLE_AUDITOR);
        if (userDatabase.getRole().contains(AdminRole) && (roleValue.equals(AccountantRole.getName()) || roleValue.equals(UserRole.getName()) || roleValue.equals(AuditorRole.getName()))) {
            throw new RoleGroupException("The user cannot combine administrative and business roles!");
        }
        if ((userDatabase.getRole().contains(UserRole) || userDatabase.getRole().contains(AccountantRole) || userDatabase.getRole().contains(AuditorRole)) && roleValue.equals(AdminRole.getName())) {
            throw new RoleGroupException("The user cannot combine administrative and business roles!");
        }
        if (role.getOperation().equals("GRANT")) {
            userDatabase.getRole().add(newRole);
            Collections.sort(userDatabase.getRole());
            accountRepository.save(userDatabase);
            logService.addEvent(SecurityAction.GRANT_ROLE, user.getUsername().toLowerCase(), "Grant role " + role.getRole() + " to " + userDatabase.getEmail().toLowerCase(), "/api/admin/user/role");
        } else if (role.getOperation().equals("REMOVE") && roleValue.name().equals(UserRoleType.ROLE_ADMINISTRATOR.name())) {
            throw new RemoveAdminException("Can't remove ADMINISTRATOR role!");
        } else if (role.getOperation().equals("REMOVE") && !userDatabase.getRole().contains(newRole)) {
            throw new RemoveAdminException("The user does not have a role!");
        } else if (role.getOperation().equals("REMOVE") && userDatabase.getRole().size() == 1) {
            throw new RemoveAdminException("The user must have at least one role!");
        } else if (role.getOperation().equals("REMOVE") && !userDatabase.getRole().contains(AdminRole)) {
            userDatabase.getRole().remove(newRole);
            accountRepository.save(userDatabase);
            logService.addEvent(SecurityAction.REMOVE_ROLE, user.getUsername().toLowerCase(), "Remove role " + role.getRole() + " from " + userDatabase.getEmail().toLowerCase(), "/api/admin/user/role");
        } else {
            throw new RemoveAdminException("Can't remove ADMINISTRATOR role!");
        }
        return DtoMapper.INSTANCE.toSignupResponse(userDatabase,userDatabase.getRole());
    }

    public ResponseEntity<Map<String, String>> adminSelection(LockStatusRequest lockStatus, SecurityUser securityUser) {
        Role AdminRole = roleRepository.findByName(UserRoleType.ROLE_ADMINISTRATOR);
        User user = accountRepository.findByEmailIgnoreCase(lockStatus.getUser());
        if (user == null) {
            throw new UserAlreadyExistsException("User not found!");
        }
        if (user.getRole().contains(AdminRole)) {
            throw new RemoveAdminException("Can't lock the ADMINISTRATOR!");
        }
        if (lockStatus.getOperation().equals("LOCK")) {
            user.setAccountLocked(true);
            logService.addEvent(SecurityAction.LOCK_USER, securityUser.getUsername().toLowerCase(), "Lock user " + user.getEmail().toLowerCase(), "/api/admin/user/role");
            return ResponseEntity.ok(Map.of("status", "User " + lockStatus.getUser().toLowerCase() + " locked!"));
        } else if (lockStatus.getOperation().equals("UNLOCK")) {
            user.setAccountLocked(false);
            logService.addEvent(SecurityAction.UNLOCK_USER, securityUser.getUsername().toLowerCase(), "Unlock user " + user.getEmail().toLowerCase(), "/api/admin/user/role");
            return ResponseEntity.ok(Map.of("status", "User " + lockStatus.getUser().toLowerCase() + " unlocked!"));
        } else {
            throw new HackedPassword("Incorrect operation");
        }
    }

    public void checkLockeUser(SecurityUser user) {
        if (accountRepository.findByEmailIgnoreCase(user.getUsername()).isAccountLocked()) {
            throw new UserLockedException("User account is locked");
        }
    }
//    @Scheduled(initialDelay = 30000,fixedRate = 30000)
//    public void checkTimeLocked(){
//        logger.info("Method checkTimeLocked was start");
//        List<User> userList = accountRepository.findAll();
//        for(User user:userList){
//                user.setAccountLocked(false);
//                user.setFailedAttempts(0);
//                user.setUnlockingTime(null);
//                accountRepository.save(user);
//        }
//    }
//    @Scheduled(fixedRate = 300000)
//    public void dropFailedAttempts(){
//        List<User> userList  = accountRepository.findAll();
//        for(User user:userList){
//            user.setFailedAttempts(0);
//        }
//    }

}
















