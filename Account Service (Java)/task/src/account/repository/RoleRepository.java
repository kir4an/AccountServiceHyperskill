package account.repository;

import account.model.Role;
import account.model.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(UserRoleType name);
    List<Role> findAll();
    List<Role> findAllByName(UserRoleType name);

}
