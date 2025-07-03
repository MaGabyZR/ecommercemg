package com.magabyzr.ecommercemg.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter                                                 //So Spring can get the data from these fields and create Json objects.
public class UserDto {
    private Long id;
    private String name;
    private String email;

}
