package com.paintfinder.palette_paint.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paintfinder.mypalette.entity.MyPalette;
import com.paintfinder.paint.entity.Paint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "Palette_Paint")
public class Palette_Paint {
    @Id
    @Column(name = "pp_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ppIdx;

    @Column(name = "pp_paint_order", nullable = false)
    private int ppPaintOrder;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="pal_idx_fk", referencedColumnName = "pal_idx")
    MyPalette myPalette;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="paint_idx_fk", referencedColumnName = "paint_idx")
    Paint paint;

    @Builder
    public Palette_Paint(int ppPaintOrder, MyPalette myPalette, Paint paint){
        this.ppPaintOrder = ppPaintOrder;
        this.myPalette = myPalette;
        this.paint = paint;
    }
}
