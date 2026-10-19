package com.paintfinder.mypalette.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paintfinder.palette_paint.entity.Palette_Paint;
import com.paintfinder.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "MyPalette")
@EntityListeners(AuditingEntityListener.class)
public class MyPalette {
    @Id
    @Column(name = "pal_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int palIdx;

    @Column(name = "pal_name", nullable = false)
    @ColumnDefault("0")
    private String palName;

    @Column(name = "pal_order")
    private int palOrder;

    @Column(name = "open", nullable = false)
    @ColumnDefault("0")
    private int open;

    @Column(name = "img_src")
    private String imgSrc;

    @Column(name = "create_date")
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createDate;

    @Column(name = "last_edit_date")
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastEditDate;

    @Column(name = "bookmark", nullable = false)
    @ColumnDefault("0")
    private int bookmark;    // 팔레트 즐겨찾기(즐겨찾기 개수제한 여부 모름)

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="member_idx_fk", referencedColumnName = "member_idx")
    Member member;


    // https://velog.io/@yeoonnii/Caused-by-org.hibernate.AnnotationException-mappedBy-reference-an-unknown-target-entity-property
    // mapped by 사용시 ManyToOne 에서 선언된 변수명이랑 일치해야 함
    @OneToMany(mappedBy = "myPalette" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Palette_Paint> palettePaintsList = new ArrayList<>();

    @Builder
    public MyPalette(String palName, int palOrder, int open, String imgSrc, int bookmark, Member member){
        this.palName = palName;
        this.palOrder = palOrder;
        this.open = open;
        this.imgSrc = imgSrc;
        this.bookmark = bookmark;
        this.member = member;
    }

    /**
     * 이미지 경로 수정
     * @param newImgSrc
     */
    public void updateImgSrc(String newImgSrc) {
        this.imgSrc = newImgSrc;
    }

    /**
     * 팔레트 즐겨찾기 수정
     * @param newBookmark
     */
    public void updateBookmark(int newBookmark){
        this.bookmark = newBookmark;
    }

    /**
     * 팔레트 공개/비공개 설정
     * @param newOpen
     */
    public void updateOpen(int newOpen){
        this.open = newOpen;
    }

    /**
     * 팔레트 이름 수정
     * @param palName
     */
    public void updatePalName(String palName){
        if(palName == null) {
            palName = "";
        }
        this.palName = palName;
    }

}
