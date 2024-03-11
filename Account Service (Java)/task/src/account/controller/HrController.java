package account.controller;

import account.Dto.UserDto;
import account.Mapper.UserMapper;
import account.Utils.JwtUtils;
import account.request.HrRequest;
import account.model.User;
import account.service.HrService;
import account.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HrController {
    private JwtUtils jwtUtils;
    private HrService hrService;
    private LogService logService;
    @Autowired
    public HrController(JwtUtils jwtUtils, HrService hrService, LogService logService) {
        this.jwtUtils = jwtUtils;
        this.hrService = hrService;
        this.logService = logService;
    }

    @PostMapping("api/hr/signup")
    public ResponseEntity<String> signup(@RequestBody HrRequest request){
        String token = jwtUtils.generateJwtByHR(request);
        return ResponseEntity.ok(token);
    }
    @GetMapping("api/hr/addIntern")
    public ResponseEntity<List<UserDto>> checkUserInternResult() {
        List<User> resultUser = hrService.checkUserIntern();
        List<UserDto> resultUserDto = new ArrayList<>();
        for (User user : resultUser) {
            UserDto userDto = UserMapper.INSTANCE.convertUserToUserDto(user);
            resultUserDto.add(userDto);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resultUserDto);
    }
}
