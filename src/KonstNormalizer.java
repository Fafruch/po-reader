import java.util.ArrayList;

public class KonstNormalizer {
    public ArrayList<String> cleanFile(ArrayList<String> file) {
        ArrayList<String> cleanedFile = new ArrayList<>();
        boolean contentStarted = false;

        for(String line : file) {
            if(line.matches(KonstPattern.ROZDZIAL)) {
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

            while((currentLine.matches(KonstPattern.KONIEC_MYSLNIKIEM) || currentLine.matches(KonstPattern.KONIEC_NORMALNIE)) &&
                    !currentLine.matches(KonstPattern.DZIAL) &&
                    !currentLine.matches(KonstPattern.ROZDZIAL) &&
                    !currentLine.matches(KonstPattern.ARTYKUL) &&
                    (nextLine.matches(KonstPattern.KONIEC_MYSLNIKIEM) || nextLine.matches(KonstPattern.KONIEC_NORMALNIE)) &&
                    !nextLine.matches(KonstPattern.DZIAL) &&
                    !nextLine.matches(KonstPattern.ROZDZIAL) &&
                    !nextLine.matches(KonstPattern.ARTYKUL) &&
                    !nextLine.matches(KonstPattern.USTEP) &&
                    !nextLine.matches(KonstPattern.PUNKT)) {

                wasConnecting = true;

                if(currentLine.matches(KonstPattern.KONIEC_NORMALNIE)) {
                    connectedLines = currentLine + " " + nextLine;
                } else {
                    // Matches KonstPattern.KONIEC_MYSLNIKIEM
                    String currentLineWithoutDash = currentLine.substring(0, currentLine.length()-1);
                    connectedLines = currentLineWithoutDash + nextLine;
                }

                if(i+1 == file.size()-1) break;

                i++;
                currentLine = connectedLines;
                nextLine = file.get(i+1);
            }

            if(wasConnecting) {
                fileWithConnctedLines.add(connectedLines);
            } else {
                fileWithConnctedLines.add(currentLine);
            }
        }

        fileWithConnctedLines.add(file.get(file.size()-1));

        return fileWithConnctedLines;
    }

    public String normalizeString(String string) {
        return string.replaceAll("\\s+","").toLowerCase();
    }
}
