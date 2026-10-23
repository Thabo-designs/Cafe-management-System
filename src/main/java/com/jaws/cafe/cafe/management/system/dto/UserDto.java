package com.jaws.cafe.cafe.management.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String name;
    private String contactNumber;
    private String email;
    private String password;
    private String role;
    private String status;

}
