final class UokikPattern {
    static final String ROZDZIAL = "^Rozdział \\d+(.)*$";
    static final String DZIAL = "^DZIAŁ (.)*$";
    static final String ARTYKUL = "^Art\\. \\d+[a-z]*\\. (\\d+[a-z]*\\. )?(.)*$";
    static final String USTEP = "^\\d+[a-z]*\\.(.)*$";
    static final String PUNKT = "^\\d+[a-z]*\\)(.)*$";
    static final String LITERA = "^[a-z]+\\)(.)*$";
    static final String KONIEC_MYSLNIKIEM = "^(.)*-$";
    static final String TYTUL_DZIALU = "^([A-Z,ŻŹĆĄŚĘŁÓŃ])+(.)*[^.]$";
    static final String ZWYKLA_LINIA = "(?!" + ROZDZIAL + "|" + DZIAL + "|" + ARTYKUL + "|" + USTEP + "|" + PUNKT + "|" + LITERA + "*$).*";
    static final String USTEP_PUNKT_LITERA_OR_ZWYKLA_LINIA = "(" + USTEP + "|" + PUNKT + "|" + LITERA + "|" + ZWYKLA_LINIA + "$).*";
    static final String NOTHING_EXCEPT_USTEP = "(?!" + ROZDZIAL + "|" + DZIAL + "|" + ARTYKUL + "|" + PUNKT + "|" + LITERA + "$).*";
    static final String IS_UOKIK = "©Kancelaria Sejmu s. 1/73";
}