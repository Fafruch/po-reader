final class Pattern {
    static final String DZIAL = "dzia[łl]\\w+";
    static final String ROZDZIAL = "rozdzia[łl]\\w+";
    static final String ARTYKUL = "art\\d+[a-z]*";
    static final String ZAKRES_ARTYKULOW = ARTYKUL + "-\\d+[a-z]*";
    static final String USTEP = "ust\\d+[a-z]*";
    static final String PUNKT = "pkt\\d+[a-z]*";
    static final String LITERA = "lit[a-z]+";
}