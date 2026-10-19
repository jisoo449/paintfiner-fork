package com.paintfinder.sets_paint.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paintfinder.paint.entity.Paint;
import com.paintfinder.sets.entity.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Sets_Paint")
public class Sets_Paint {

    @Id
    @Column(name="sp_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spIdx;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="paint_idx", referencedColumnName = "paint_idx")
    @JsonBackReference
    Paint paint;        //물감 fk

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="sets_idx", referencedColumnName = "sets_idx")
    @JsonBackReference
    private Sets sets;           //물감 세트 fk


}
