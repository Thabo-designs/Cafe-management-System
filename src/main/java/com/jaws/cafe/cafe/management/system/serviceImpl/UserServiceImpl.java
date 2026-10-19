package com.jaws.cafe.cafe.management.system.serviceImpl;

import com.jaws.cafe.cafe.management.system.constants.CafeConstants;
import com.jaws.cafe.cafe.management.system.dao.UserDao;
import com.jaws.cafe.cafe.management.system.dto.UserDto;
import com.jaws.cafe.cafe.management.system.model.User;
import com.jaws.cafe.cafe.management.system.service.UserService;
import com.jaws.cafe.cafe.management.system.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        log.info("Inside signup: {}", requestMap);

        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Succefully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String,String> requestMap) {
       User user = new User();
       user.setName(requestMap.get("name"));
       user.setContactNumber(requestMap.get("contactNumber"));
       user.setEmail(requestMap.get("email"));
       user.setPassword(requestMap.get("password"));
       user.setRole(requestMap.get("role"));
       user.setStatus("false");

       return user;
    }
}
