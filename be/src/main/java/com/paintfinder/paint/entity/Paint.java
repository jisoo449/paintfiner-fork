package com.paintfinder.paint.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paintfinder.paint_pigment.entity.Paint_Pigment;
import com.paintfinder.sets_paint.entity.Sets_Paint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Paint", indexes = @Index(name = "idx_paint_code", columnList = "paint_code"))
public class Paint {

    @Id
    @Column(name = "paint_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paintIdx; 			//기본키	//not null

    @Column(name = "paint_code", nullable = false)
    private String paintCode;

    @Column(name = "brand_kr")
    private String brandKR;			//브랜드 한국이름

    @Column(name = "brand_en")
    private String brandEN;			//브랜드 영어이름

    @Column(name = "paint_name_en", nullable = false)
    private String paintNameEN;		//물감 영어이름
    @Column(name = "paint_name_kr")
    @ColumnDefault("0")
    private String paintNameKR;		//물감 한국이름

    @Column(name="color_en")
    private String colorEN;         //물감 색상 이름 영어(ex. Original White)
    @Column(name="color_kr")
    private String colorKR;         //물감 색상 이름 한글(ex. 오리지널 화이트)

    @Column(name = "colorgroup_en")
    private String colorgroupEN;			//색 분류(ex.검정, 하양, 파랑...) - 영어
    @Column(name = "colorgroup_kr")
    private String colorgroupKR;			//색 분류(ex.검정, 하양, 파랑...) - 한글

    @Column(name = "series")
    private String series;			//시리즈

    @Column(name = "transparency_en")
    private String transparencyEN;	    //투명도 - 영어
    @Column(name = "transparency_kr")
    private String transparencyKR;	    //투명도 - 한글

    @Column(name = "lightfastness_en")
    private String lightfastnessEN;    //내광성 - 영어
    @Column(name = "lightfastness_kr")
    private String lightfastnessKR;	 //내광성 - 한글

    @Column(name = "quality_en")
    private String qualityEN;			//퀄리티 - 영어
    @Column(name = "quality_kr")
    private String qualityKR;			//퀄리티 - 한글

    @Column(name = "texture_en")
    private String textureEN;			//질감 - 영어
    @Column(name = "texture_kr")
    private String textureKR;			//질감 - 한글

    @Column(name = "cate1_en")
    private String cate1EN;			//대분류 - 영어
    @Column(name = "cate2_en")
    private String cate2EN;			//소분류 - 영어

    @Column(name = "cate1_kr")
    private String cate1KR;			//대분류 - 한글
    @Column(name = "cate2_kr")
    private String cate2KR;			//소분류 - 한글

    @Column(name = "binder")
    private String binder;			//용매

    @Column(name = "unit")
    private String unit;			//용량 단위

    @Column(name = "vegan")
    @ColumnDefault("0")
    private int vegan;				//비건(0: 모름, 1: Not vegan, 2:vegan)
    @Column(name = "description_en", length = 3000)
    private String descriptionEN;		//설명 영문ver
    @Column(name = "description_kr", length = 3000)
    private String descriptionKR;		//설명 한글ver

    @Column(name = "texture_img1")
    private String textureImg1;             // 물감 질감 이미지
    @Column(name = "texture_img2")
    private String textureImg2;
    @Column(name = "texture_img3")
    private String textureImg3;

    @Column(name="size")
    @ColumnDefault("0.0")
    private float size;                // 물감 사이즈
    @Column(name="prod_img")
    private String prodImg;            // 제품 이미지
    @Column(name="price")
    @ColumnDefault("0.0")
    private float price;               // 가격
    @Column(name="sizes")
    private String sizes;              // 사이즈 문자열

    //------- 하위 테이블 -----------------------------------
    @OneToMany(mappedBy = "paint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Sets_Paint> setsPaintList;    //세트리스트

//    @OneToMany(mappedBy = "paint" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    @JsonIgnore
//    private List<Paint_Pigment> paintPigmentList; //안료

    @Builder
    public Paint(String paintCode, String brandKR,
                 String brandEN,
                 String paintNameKR,
                 String paintNameEN,
                 String colorKR,
                 String colorEN,
                 String colorgroupEN,
                 String colorgroupKR,
                 String series,
                 String transparencyEN,
                 String transparencyKR,
                 String lightfastnessEN,
                 String lightfastnessKR,
                 String qualityEN,
                 String qualityKR,
                 String textureEN,
                 String textureKR,
                 String cate1EN,
                 String cate1KR,
                 String cate2EN,
                 String cate2KR,
                 String binder,
                 String unit,
                 int vegan,
                 String descriptionEN,
                 String descriptionKR,
                 String textureImg1,
                 String textureImg2,
                 String textureImg3,
                 float size,
                 String prodImg,
                 float price,
                 String sizes){
        this.paintCode = paintCode;
        this.brandKR = brandKR;
        this.brandEN = brandEN;
        this.paintNameKR = paintNameKR;
        this.paintNameEN = paintNameEN;
        this.colorKR = colorKR;
        this.colorEN = colorEN;
        this.colorgroupEN = colorgroupEN;
        this.colorgroupKR = colorgroupKR;
        this.series = series;
        this.transparencyEN = transparencyEN;
        this.transparencyKR = transparencyKR;
        this.lightfastnessEN = lightfastnessEN;
        this.lightfastnessKR = lightfastnessKR;
        this.qualityEN = qualityEN;
        this.qualityKR = qualityKR;
        this.textureEN = textureEN;
        this.textureKR = textureKR;
        this.cate1EN = cate1EN;
        this.cate1KR = cate1KR;
        this.cate2EN = cate2EN;
        this.cate2KR = cate2KR;
        this.binder = binder;
        this.unit = unit;
        this.vegan = vegan;
        this.descriptionKR = descriptionKR;
        this.descriptionEN = descriptionEN;
        this.textureImg1 = textureImg1;
        this.textureImg2 = textureImg2;
        this.textureImg3 = textureImg3;
        this.size = size;
        this.prodImg = prodImg;
        this.price = price;
        this.sizes = sizes;
    }

    //------- 검색어, 페이징 정보 ------------------------------
//    private String search;
//
//    private int current_page;

    public void updateProdImg(String prodImg){
        if(prodImg == null) {
            prodImg = "";
        }
        this.prodImg = prodImg;
    }

    public void updateTexture1Img(String textureImg1){
        if(textureImg1 == null) {
            textureImg1 = "";
        }
        this.textureImg1 = textureImg1;
    }
}
