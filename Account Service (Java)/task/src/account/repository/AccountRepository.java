package account.repository;

import account.model.Role;
import account.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<User,Integer> {
   User findByEmailIgnoreCase(String email);

   boolean existsByEmailIgnoreCase(String email);

   List<User> findAll();
   List<User> findAllByOrderById();
   void deleteByEmailIgnoreCase(String email);

}
