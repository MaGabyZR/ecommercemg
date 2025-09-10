package com.magabyzr.ecommercemg.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
