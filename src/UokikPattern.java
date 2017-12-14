final class UokikPattern {
    static final String ROZDZIAL = "^Rozdział \\d{1,2}$";
    static final String DZIAL = "^DZIAŁ \\w+$";
    static final String ARTYKUL = "^Art. \\d{1,3}[a-z]{0,3}\\. (\\d{1,2}[a-z]{0,3}\\. )?(.)*$";
    static final String USTEP = "^(\\d+\\. )(.)*$";
    static final String PUNKT = "^(\\d+)\\)(.)*$";
    static final String KONIEC_MYSLNIKIEM = "^(.)*-$";
    static final String KONIEC_NORMALNIE = "^(.)*[^-]$";
    static final String TYTUL_DZIALU = "^([A-Z,ŻŹĆĄŚĘŁÓŃ])+(.)*[^.]$";
}