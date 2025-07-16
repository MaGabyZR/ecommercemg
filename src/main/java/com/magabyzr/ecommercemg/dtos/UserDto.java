package com.magabyzr.ecommercemg.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;

@AllArgsConstructor
@Getter                                                 //So Spring can get the data from these fields and create Json objects.
public class UserDto {
    //@JsonProperty("user_id")
    private Long id;
    //@JsonIgnore
    private String name;
    private String email;
/*    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;*/

}
