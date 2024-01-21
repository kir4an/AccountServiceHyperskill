package account.service;

import account.model.Role;
import account.model.User;
import account.model.UserRoleType;
import account.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class RolesDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private RoleRepository roleRepository;

    public RolesDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initializeRole();

    }
    @Transactional
    public void initializeRole(){
        for(UserRoleType role:UserRoleType.values()){
            if(roleRepository.findByName(role)==null){
                Role role1 = new Role();
                role1.setName(role);
                roleRepository.save(role1);
            }
        }
    }
}
