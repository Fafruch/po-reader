import java.util.LinkedList;
import java.util.List;

public class KonstNormalizer {
    public List<String> normalize(List<String> file) {
        List<String> cleanedFile = cleanFile(file);
        List<String> fileWithConnectedLines = connectLines(cleanedFile);

        return fileWithConnectedLines;
    }

    private List<String> cleanFile(List<String> file) {
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

    private List<String> connectLines(List<String> file) {
        List<String> fileWithConnctedLines = new LinkedList<>();

        for (int i = 0; i < file.size() - 1; i++) {

            String currentLine = file.get(i);
            String nextLine = file.get(i + 1);
            String connectedLines;

            if (currentLine.matches(KonstPattern.ROZDZIAL) && nextLine.matches(KonstPattern.DZIAL)) {
                currentLine = currentLine + " - \"" + nextLine + "\"";
                i++;
            } else while (currentLine.matches(KonstPattern.USTEP_PUNKT_LUB_ZWYKLA_LINIA) && nextLine.matches(KonstPattern.ZWYKLA_LINIA)) {

                if (currentLine.matches(KonstPattern.KONIEC_MYSLNIKIEM)) {
                    String currentLineWithoutDash = currentLine.substring(0, currentLine.length() - 1);
                    connectedLines = currentLineWithoutDash + nextLine;
                } else {
                    connectedLines = currentLine + " " + nextLine;
                }

                currentLine = connectedLines;

                if (i + 1 == file.size() - 1) break;
                i++;
                nextLine = file.get(i + 1);
            }

            fileWithConnctedLines.add(currentLine);
        }

        return fileWithConnctedLines;
    }
}
