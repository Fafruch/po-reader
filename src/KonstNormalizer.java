import java.util.LinkedList;
import java.util.List;

public class KonstNormalizer extends Normalizer {
    public List<String> cleanFile(List<String> file) {
        List<String> cleanedFile = new LinkedList<>();
        boolean contentStarted = false;

        for (String line : file) {
            if (line.matches(KonstPattern.ROZDZIAL)) {
                contentStarted = true;
            }

            if (line.equals("©Kancelaria Sejmu") || line.equals("2009-11-16")) {
                continue;
            }

            if (line.length() == 1) {
                continue;
            }

            if (!contentStarted) {
                continue;
            }

            cleanedFile.add(line);
        }

        return cleanedFile;
    }

    public List<String> connectLines(List<String> file) {
        List<String> fileWithConnctedLines = new LinkedList<>();

        for (int i = 0; i < file.size() - 1; i++) {

            String currentLine = file.get(i);
            String nextLine = file.get(i + 1);
            String connectedLines;

            while ((currentLine.matches(KonstPattern.KONIEC_MYSLNIKIEM) || currentLine.matches(KonstPattern.KONIEC_NORMALNIE)) &&
                    !currentLine.matches(KonstPattern.DZIAL) &&
                    !currentLine.matches(KonstPattern.ROZDZIAL) &&
                    !currentLine.matches(KonstPattern.ARTYKUL) &&
                    (nextLine.matches(KonstPattern.KONIEC_MYSLNIKIEM) || nextLine.matches(KonstPattern.KONIEC_NORMALNIE)) &&
                    !nextLine.matches(KonstPattern.DZIAL) &&
                    !nextLine.matches(KonstPattern.ROZDZIAL) &&
                    !nextLine.matches(KonstPattern.ARTYKUL) &&
                    !nextLine.matches(KonstPattern.USTEP) &&
                    !nextLine.matches(KonstPattern.PUNKT)) {

                if (currentLine.matches(KonstPattern.KONIEC_NORMALNIE)) {
                    connectedLines = currentLine + " " + nextLine;
                } else {
                    // matches KonstPattern.KONIEC_MYSLNIKIEM
                    String currentLineWithoutDash = currentLine.substring(0, currentLine.length() - 1);
                    connectedLines = currentLineWithoutDash + nextLine;
                }

                if (i + 1 == file.size() - 1) break;

                i++;
                currentLine = connectedLines;
                nextLine = file.get(i + 1);
            }

            fileWithConnctedLines.add(currentLine);
        }

        fileWithConnctedLines.add(file.get(file.size() - 1));

        return fileWithConnctedLines;
    }
}
