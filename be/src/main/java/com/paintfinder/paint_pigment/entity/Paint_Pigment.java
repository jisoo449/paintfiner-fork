package com.paintfinder.paint_pigment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.pigment.entity.Pigment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Paint_Pigment")
public class Paint_Pigment {
    @Id
    @Column(name = "paint_pigment_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int paintPigmentIdx;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "paint_code", referencedColumnName = "paint_code") // paintIdx 가 아닌 paintCode 로 매핑
//    @JsonBackReference // 순환참조 방지를 위한 어노테이션
//    private Paint paint;  	//물감FK
    @Column(name="paint_code")
    private String paintCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pigment_idx")
    @JsonBackReference
    private Pigment pigment;    //안료FK

    @Builder
    public Paint_Pigment(String paintCode, Pigment pigment) {
        this.paintCode = paintCode;
        this.pigment = pigment;
    }
}
