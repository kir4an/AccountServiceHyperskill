package account.repository;

import account.model.UserIntern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternRepository extends JpaRepository<UserIntern,Integer> {
    boolean existsByEmailIgnoreCase(String email);
    UserIntern findByEmailIgnoreCase(String email);
}
