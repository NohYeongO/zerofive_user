package com.zerofive.user.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private Long userId;
    private String name;
    private String phone;
    private String address;
}
