package com.paintfinder.global.util;

import com.paintfinder.mypalette.entity.QMyPalette;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * querydsl 에서 정렬을 위한 유틸리티 클래스.
 * 대부분의 페이징의 정렬 기준을 이 유틸 클래스를 통해 정렬한다.
 */
public class QueryDslSortingUtil {

    public static OrderSpecifier<?> getOrderSpecifiers(Sort sort, PathBuilder<?> entityPath) {

        // Sort 객체에서 첫 번째 정렬 기준을 가져옴. 여기서는 단일 조건만 사용중임.
        Sort.Order order = sort.iterator().next();

        String prop = order.getProperty();

        // 필드명이 DB 컬럼명으로 들어왔을 시("_" 가 포함된 스네이크케이스 일시) java 필드명(카멜케이스)으로 변경
        if(prop.contains("_")) {
            StringTokenizer st = new StringTokenizer(prop, "_");
            StringBuilder prop2 = new StringBuilder(st.nextToken());
            while (st.hasMoreTokens()) {
                String s = st.nextToken();

                prop2.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
            }

            prop = prop2.toString();
        }

        // 동적 정렬 방법
        PathBuilder orderByExpression = entityPath.get(prop);

        // 정적 정렬 방법
//        PathBuilder orderByExpression = new PathBuilder(Object.class, "pal_idx");

        return new OrderSpecifier<>(
                order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                orderByExpression
        );
    }
}
