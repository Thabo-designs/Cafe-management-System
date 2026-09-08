package com.jaws.cafe.cafe.management.system.restImpl;

import com.jaws.cafe.cafe.management.system.constants.CafeConstants;
import com.jaws.cafe.cafe.management.system.rest.UserRest;
import com.jaws.cafe.cafe.management.system.service.UserService;
import com.jaws.cafe.cafe.management.system.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signup(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
