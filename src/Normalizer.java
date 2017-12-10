import java.util.ArrayList;

public class Normalizer {
    String patternRozdzial = "^Rozdział \\w*$";
    String patternDzial = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    String patternArtykul = "^(Art. )(\\d)+(.)$";
    String patternUstep = "^(\\d+\\. )(.)*$";
    String patternPunkt = "^(\\d+)\\)(.)*$";
    String patternKoniecMyslnikiem = "^(.)*-$";
    String patternKoniecNormalnie = "^(.)*[^-]$";

    public ArrayList<String> cleanFile(ArrayList<String> file) {
        ArrayList<String> cleanedFile = new ArrayList<>();
        boolean contentStarted = false;

        for(String line : file) {
            if(line.matches(patternRozdzial)) {
                contentStarted = true;
            }

            if(line.equals("©Kancelaria Sejmu") || line.equals("2009-11-16")) {
                continue;
            }

            if(line.length() == 1) {
                continue;
            }

            if(!contentStarted) {
                continue;
            }

            cleanedFile.add(line);
        }

        return cleanedFile;
    }

    public ArrayList<String> connectLines(ArrayList<String> file) {
        ArrayList<String> fileWithConnctedLines = new ArrayList<>();

        for(int i = 0; i < file.size()-1; i++) {

            String currentLine = file.get(i);
            String nextLine = file.get(i+1);
            String connectedLines = null;
            boolean wasConnecting = false;

            while((currentLine.matches(patternKoniecNormalnie) ||
                    currentLine.matches(patternKoniecMyslnikiem)) &&
                    !currentLine.matches(patternDzial) &&
                    !currentLine.matches(patternRozdzial) &&
                    !currentLine.matches(patternArtykul) &&
                    (nextLine.matches(patternKoniecMyslnikiem) ||
                            nextLine.matches(patternKoniecNormalnie)) &&
                    !nextLine.matches(patternDzial) &&
                    !nextLine.matches(patternRozdzial) &&
                    !nextLine.matches(patternArtykul) &&
                    !nextLine.matches(patternUstep) &&
                    !nextLine.matches(patternPunkt)) {

                if(currentLine.matches(patternKoniecNormalnie)) {
                    connectedLines = currentLine + " " + nextLine;
                } else {
                    String currentLineWithoutDash = currentLine.substring(0, currentLine.length()-1);
                    connectedLines = currentLineWithoutDash + nextLine;
                }

                wasConnecting = true;
                i++;
                if(i == file.size()-1) break;

                currentLine = connectedLines;
                nextLine = file.get(i+1);
            }

            if(wasConnecting) {
                fileWithConnctedLines.add(connectedLines);
            } else {
                fileWithConnctedLines.add(currentLine);
            }
        }

        return fileWithConnctedLines;
    }

    public String normalizeString(String string) {
        return string.replaceAll("\\s+","").toLowerCase();
    }
}
