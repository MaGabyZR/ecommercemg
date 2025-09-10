package com.magabyzr.ecommercemg.users;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
}
