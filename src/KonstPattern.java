final class KonstPattern {
    static final String ROZDZIAL = "^Rozdział \\w*$";
    static final String DZIAL = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    static final String ARTYKUL = "^(Art. )(\\d)+(.)$";
    static final String USTEP = "^(\\d+\\. )(.)*$";
    static final String PUNKT = "^(\\d+)\\)(.)*$";
    static final String KONIEC_MYSLNIKIEM = "^(.)*-$";
    static final String KONIEC_NORMALNIE = "^(.)*[^-]$";
}