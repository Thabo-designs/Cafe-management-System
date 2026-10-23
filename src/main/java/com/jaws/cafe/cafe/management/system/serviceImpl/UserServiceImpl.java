package com.jaws.cafe.cafe.management.system.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaws.cafe.cafe.management.system.JWT.CustomUserDetailsService;
import com.jaws.cafe.cafe.management.system.JWT.JwtUtil;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    String response;


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

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        ObjectMapper mapper = new ObjectMapper();
        log.info("inside login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (authentication.isAuthenticated()) {
                if (customUserDetailsService.getUser().getStatus().equalsIgnoreCase("true")) {
                    response = mapper.writeValueAsString(Map.of(
                            "token",
                            jwtUtil.generateToken(customUserDetailsService.getUser().getEmail(),
                                                  customUserDetailsService.getUser().getRole())
                                                        ));
                    return ResponseEntity
                            .ok(response);
                } else {
                    response = mapper.writeValueAsString(Map.of(
                            "message",
                           "Wait for admin approval."
                    ));
                    return ResponseEntity.badRequest().body(response);
                }
            }
        } catch (Exception e){
            log.error("",e);
        }

        return CafeUtils.getResponseEntity(CafeConstants.BAD_CREDENTIALS, HttpStatus.INTERNAL_SERVER_ERROR);
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
       user.setPassword(passwordEncoder.encode(requestMap.get("password")));
       user.setRole(requestMap.get("role"));
       user.setStatus("false");

       return user;
    }
}
