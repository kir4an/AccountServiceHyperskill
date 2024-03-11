package account.controller;

import account.Exception.IncorrectMonthException;
import account.Exception.NotAuthenticationException;
import account.Dto.InfoResponseDto;
import account.model.Payment;
import account.model.SecurityUser;
import account.model.User;
import account.repository.AccountRepository;
import account.repository.PaymentRepository;
import account.service.AccountService;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class PaymentController {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private PaymentRepository paymentRepository;

    public PaymentController(AccountService accountService, AccountRepository accountRepository, PaymentRepository paymentRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("api/acct/payments")
    public ResponseEntity<?> loadPayments(@RequestBody List<Payment> payments,@AuthenticationPrincipal SecurityUser user) {
        accountService.checkLockeUser(user);
        for (Payment payment : payments) {
            User user1 = accountRepository.findByEmailIgnoreCase(payment.getEmail());
            accountService.validationUserAndPayment(user1, payment);
        }
        accountService.createPayments(payments);

        Map<String, String> map = new HashMap<>();
        map.put("status", "Added successfully!");
        return ResponseEntity.ok(map);
    }

    @PutMapping("api/acct/payments")
    public ResponseEntity<?> updatePayment(@RequestBody Payment updatePayment,@AuthenticationPrincipal SecurityUser user) {
        accountService.checkLockeUser(user);
        accountService.updatePayment(updatePayment);
        Map<String, String> map = new HashMap<>();
        map.put("status", "Updated successfully!");
        return ResponseEntity.ok(map);
    }

    @GetMapping("api/empl/payment")
    public ResponseEntity<?> getPaymentByPeriod(@RequestParam @Nullable String period, @AuthenticationPrincipal SecurityUser user) {
        if (user == null ) {
            throw new NotAuthenticationException();
        }
        accountService.checkLockeUser(user);
        if (period == null) {
            List<InfoResponseDto> responseList = accountService.getFormattedPayments(user.getUsername());
            return ResponseEntity.ok(responseList);
        }
        Payment payment = paymentRepository.findByPeriodAndEmailIgnoreCase(period, user.getUsername());
        if (payment != null) {
            InfoResponseDto infoResponseDto = accountService.convertToInfoResponse(payment, user.getUsername());
            return ResponseEntity.ok(infoResponseDto);
        } else {
            throw new IncorrectMonthException("Incorrect month");
        }
    }
}
