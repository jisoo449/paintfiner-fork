package com.paintfinder.pigment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paintfinder.paint_pigment.entity.Paint_Pigment;
import com.paintfinder.pigment.dto.PigmentRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Pigment")
public class Pigment {
    @Id
    @Column(name = "pigment_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pigmentIdx;

    @Column(name = "ci_name")
    private String ciName;

    @Column(name = "common_name")
    private String commonName;

    @Column(name = "pigment_color")
    private String pigmentColor;

    @Column(name = "opacity")
    private String opacity;

    @Column(name = "lightfastness")
    private String lightfastness;

    @Column(name = "toxic_level")
    private String toxicLevel;

    @Column(name = "ci_number")
    private String ciNumber;

    @OneToMany(mappedBy = "pigment" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Paint_Pigment> paintPigmentList; //안료


    @Builder
    public Pigment(String ciName, String commonName, String ciNumber, String pigmentColor, String opacity, String lightfastness, String toxicLevel){
        this.ciName = ciName;
        this.commonName = commonName;
        this.ciNumber = ciNumber;
        this.pigmentColor = pigmentColor;
        this.opacity = opacity;
        this.lightfastness = lightfastness;
        this.toxicLevel = toxicLevel;
    }

    public void changePigmentInfo(PigmentRequestDto pigment){
        this.ciName = pigment.getCiName();
        this.commonName = pigment.getCommonName();
        this.ciNumber = pigment.getCiNumber();
        this.pigmentColor = pigment.getPigmentColor();
        this.opacity = pigment.getOpacity();
        this.lightfastness = pigment.getLightfastness();
        this.toxicLevel = pigment.getToxicLevel();
    }
}
