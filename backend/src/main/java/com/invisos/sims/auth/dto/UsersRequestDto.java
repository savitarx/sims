package com.invisos.sims.auth.dto;

import com.invisos.sims.common.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersRequestDto {

    private String loginId;

    private String password;

    private String email;

    private UserRole role;

}
