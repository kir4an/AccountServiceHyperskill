package account.Mapper;

import account.Dto.InfoResponseDto;
import account.Dto.SignupDto;
import account.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DtoMapper {
    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class );
    SignupDto toSignupResponse(User user, List<Role> roles);
    InfoResponseDto toInfoResponse(User user, Payment payment, String salaryFormatted);
}
