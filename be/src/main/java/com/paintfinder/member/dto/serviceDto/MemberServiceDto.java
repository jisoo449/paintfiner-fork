package com.paintfinder.member.dto.serviceDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberServiceDto {
    private int memberIdx;
    private String email;
    private String nickname;
    private String gender;
    private String ageRange;
}
