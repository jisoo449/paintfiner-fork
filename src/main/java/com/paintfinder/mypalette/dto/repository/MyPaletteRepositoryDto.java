package com.paintfinder.mypalette.dto.repository;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
public class MyPaletteRepositoryDto {
    private Integer palIdx;
    private String palName;
    private Integer palOrder;
    private Integer open;
    private String imgSrc;
    private Integer bookmark;
    private String createDate;
    private String lastEditDate;
    private List<PaintInMyPaletteRepositoryDto> paintList;

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Builder
    @QueryProjection
    public MyPaletteRepositoryDto(Integer palIdx, String palName, Integer palOrder, Integer open, String imgSrc, Integer bookmark, Date createDate, Date lastEditDate, List<PaintInMyPaletteRepositoryDto> paintList){
        this.palIdx = palIdx;
        this.palName = palName;
        this.palOrder = palOrder;
        this.open = open;
        this.imgSrc = imgSrc;
        this.bookmark = bookmark;
        this.createDate = formatToKST(createDate);
        this.lastEditDate = formatToKST(lastEditDate);
        this.paintList = paintList;
    }

    // UTC 시간을 KST로 변환 후 문자열 포맷 적용
    private String formatToKST(Date date) {
        if (date == null) return "";
        DATE_FORMAT.setTimeZone(java.util.TimeZone.getTimeZone(KST_ZONE));
        return DATE_FORMAT.format(date);
    }
}
