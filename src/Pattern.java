final class Pattern {
    static final String ROZDZIAL = "^Rozdział \\w*$";
    static final String DZIAL = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    static final String ARTYKUL = "^(Art. )(\\d)+(.)$";
    static final String USTEP = "^(\\d+\\. )(.)*$";
    static final String PUNKT = "^(\\d+)\\)(.)*$";
    static final String KONIEC_MYSLNIKIEM = "^(.)*-$";
    static final String KONIEC_NORMALNIE = "^(.)*[^-]$";
    static final String KAZDA_LINIA = "^(.)*$";
}