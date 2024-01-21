package account.repository;

import account.model.Payment;
import account.model.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.YearMonth;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Payment findByPeriodAndEmailIgnoreCase(String period, String email);
    List<Payment> findAllByEmailIgnoreCase(String email);
    List<Payment>  findAllByEmailIgnoreCaseOrderByPeriodDesc(String email);
}
