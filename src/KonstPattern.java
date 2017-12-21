final class KonstPattern {
    static final String ROZDZIAL = "^Rozdział .*$";
    static final String DZIAL = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    static final String ARTYKUL = "^(Art. )(\\d)+(.)$";
    static final String USTEP = "^(\\d+\\. )(.)*$";
    static final String PUNKT = "^(\\d+)\\)(.)*$";
    static final String KONIEC_MYSLNIKIEM = "^(.)*-$";
    static final String ZWYKLA_LINIA = "(?!" + ROZDZIAL + "|" + DZIAL + "|" + ARTYKUL + "|" + USTEP + "|" + PUNKT + "*$).*";
    static final String USTEP_PUNKT_OR_ZWYKLA_LINIA = "(" + USTEP + "|" + PUNKT + "|" + ZWYKLA_LINIA + "$).*";
    static final String NOTHING_EXCEPT_USTEP = "(?!" + ROZDZIAL + "|" + DZIAL + "|" + ARTYKUL + "|" + PUNKT + "$).*";
    static final String IS_KONSTYTUCJA = "©Kancelaria Sejmu";
}