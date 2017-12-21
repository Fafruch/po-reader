final class KonstPattern {
    static final String ROZDZIAL = "^Rozdział .*$";
    static final String DZIAL = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    static final String ARTYKUL = "^(Art. )(\\d)+(.)$";
    static final String USTEP = "^(\\d+\\. )(.)*$";
    static final String PUNKT = "^(\\d+)\\)(.)*$";
    static final String KONIEC_MYSLNIKIEM = "^(.)*-$";
    static final String ZWYKLA_LINIA = "(?!" + ROZDZIAL + "|" + DZIAL + "|" + ARTYKUL + "|" + USTEP + "|" + PUNKT + "*$).*";
    static final String USTEP_PUNKT_LUB_ZWYKLA_LINIA = "(" + USTEP + "|" + PUNKT + "|" + ZWYKLA_LINIA + "$).*";
    static final String NIC_OPROCZ_USTEPU = "(?!" + ROZDZIAL + "|" + DZIAL + "|" + ARTYKUL + "|" + PUNKT + "$).*";
}