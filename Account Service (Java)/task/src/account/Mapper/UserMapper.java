package account.Mapper;

import account.Dto.UserDto;
import account.model.Role;
import account.model.User;
import account.model.UserIntern;
import account.request.InternRequest;
import account.request.SignupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto convertUserToUserDto(User user);
    @Mapping(target = "failedAttempts", constant = "0")
    @Mapping(target = "isAccountLocked", constant = "false")
    User convertInternToUser(UserIntern intern);


    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles",source = "roles")
    User signupRequestToUser(SignupRequest request, String password,List<Role> roles);
    @Mapping(target = "roles", source = "list")
    UserIntern internRequestToUserIntern(InternRequest request, List<Role> list);
}
