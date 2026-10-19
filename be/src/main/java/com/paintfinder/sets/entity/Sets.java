package com.paintfinder.sets.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paintfinder.sets_paint.entity.Sets_Paint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Sets")
public class Sets {
    @Id
    @Column(name = "sets_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int setsIdx;

    @Column(name="sets_name_en")
    private String setsNameEN;

    @Column(name="sets_name_kr")
    private String setsNameKR;

    @Column(name="sets_price")
    private double setsPrice;

    @Column(name = "currency")
    private String currency;

    @OneToMany(mappedBy="sets", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Sets_Paint> setsPaintList;

}
