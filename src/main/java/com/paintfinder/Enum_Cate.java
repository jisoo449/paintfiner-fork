package com.paintfinder;

public enum Enum_Cate {
    Brush(new String[] {}),
    Brand(new String[] {"A. Gallo", "Ampersand", "Amsterdam", "Arches"}),
    Surface(new String[] {"Canvas", "Paper", "Board", "Panel", "Card"}),
    Paint(new String[][] {
            {"Oil", "Traditional Oil Paint", "Watermixable Oil Paint", "Alkyd Oil Paint"},
            {"Acrylic","Acrylic Paint", "Acrylic Gouache", "Acrylic Ink", "Acrylic Marker", "Acrylic Spray Paint"},
            {"Watercolor", "Watercolor Paint", "Watercolor Paint Stick", "Watercolor Pencil", "Watercolor Marker", "Watercolor Ink"},
    });

    private String[] tool_subcate;
    private String[][] paint_subcate;

    Enum_Cate(String[] tool_subcate) {
        this.tool_subcate = tool_subcate;
    }
    Enum_Cate(String[][] paint_subcate) {
        this.paint_subcate = paint_subcate;
    }
}
