package com.paintfinder.paint.util;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaintUtil {

    /**
     * 셀 데이터 읽어오는 로직. 문자열, 숫자 형식만 존재
     * @param row
     * @param cellIndex
     * @return
     */
    public String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return ""; // 셀이 비어있으면 빈 문자열 반환
        }

        // 숫자로 올 때 구분을 위함
        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue().trim(); // 문자열 앞뒤 공백 제거 후 반환
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

    /**
     * 셀의 vegan 값을 체크해 0 또는 1 반환
     * @param row
     * @param cellIndex
     * @return
     */
    public Integer getVeganValue(Row row, int cellIndex){
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return 0;
        } else if(cell.getCellType() == CellType.NUMERIC){
            if(cell.getNumericCellValue() == 0.0){
                return 0;
            } else {
                return (int) cell.getNumericCellValue();
            }
        } else if (cell.getCellType() == CellType.BLANK){
            return 0;
        } else if (cell.getCellType() == CellType.STRING){
            if(cell.getStringCellValue().isEmpty()) {
                return 0;
            } else {
                return Integer.parseInt(cell.getStringCellValue());
            }
        }
        return 0;
    }

    /**
     * 페인트 코드 생성 로직
     * "brand명-페인트명-color명-UUID8자리" 로 생성됨
     * @param brand
     * @param paintName
     * @param color
     * @return
     */
    public String generatePaintCode(String brand, String paintName, String color) {
        // UUID 생성
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // 8자리로 축소

        // 브랜드, 페인트명, 컬러명을 대문자로 처리하고, 공백은 언더스코어로 치환
        String brandPart = brand.trim().toLowerCase().replace(" ", "");
        String namePart = paintName.trim().toLowerCase().replace(" ", "");
        String colorPart = color.trim().toLowerCase().replace(" ", "");

        // 최종 paintCode 조합: BRAND-PAINTNAME-COLOR-UUID
        return brandPart + "_" + namePart + "_" + colorPart + "_" + uuid;
    }
}
