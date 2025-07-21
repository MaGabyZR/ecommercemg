package com.magabyzr.ecommercemg.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
