package com.paintfinder.member.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.member.dto.requestDto.MemberRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Column(name = "member_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberIdx;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name="gender", nullable=true)
    private String gender;

    @Column(name="age_range", nullable = true)
    private String ageRange;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "kakao_access_token")
    private String kakaoAccessToken;
    @Column(name = "kakao_refresh_token")
    private String kakaoRefreshToken;
    @Column(name = "latest_login_datetime")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime latestLoginDatetime;
    @Column(name = "latest_logout_datetime")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime latestLogoutDatetime;
    @Column(name = "join_datetime")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime joinDateime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    @OneToMany(mappedBy = "member" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // orphanRemoval : 고아 객체 제거 설정
    @JsonManagedReference
    @JsonIgnore
    private List<MyPalette> myPaletteList = new ArrayList<>();

    @Builder
    public Member(String email, String nickname, String accessToken, String refreshToken, String kakaoAccessToken, String kakaoRefreshToken, String ageRange, String gender, LocalDateTime latestLoginDatetime, LocalDateTime latestLogoutDatetime){
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
        this.gender = gender;
        this.ageRange = ageRange;
        this.latestLoginDatetime = latestLoginDatetime;
        this.latestLogoutDatetime = latestLogoutDatetime;
    }

    public void changeMemberNickname(String nickname){
        this.nickname = nickname;
    }

}
