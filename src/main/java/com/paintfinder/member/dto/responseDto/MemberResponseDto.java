package com.paintfinder.member.dto.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDto {
    private String email;
    private String nickname;
    private String gender;
    private String ageRange;
}
