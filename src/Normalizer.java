public class Normalizer {
    public String normalizeString(String string) {
        return string.replaceAll("\\s+|\\.|\\)", "").toLowerCase();
    }
}
