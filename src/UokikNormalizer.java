import java.util.LinkedList;
import java.util.List;

public class UokikNormalizer extends Normalizer {
    public List<String> cleanFile(List<String> file) {
        List<String> cleanedFile = new LinkedList<>();
        boolean contentStarted = false;

        for (String line : file) {
            if (line.matches(UokikPattern.DZIAL)) {
                contentStarted = true;
            }

            if (line.matches("^©Kancelaria Sejmu s. (.)*$") || line.equals("2017-06-28")) {
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

            if ((currentLine.matches(UokikPattern.DZIAL) || currentLine.matches(UokikPattern.ROZDZIAL))
                    && nextLine.matches(UokikPattern.TYTUL_DZIALU)) {
                currentLine = currentLine + " - \"" + nextLine + "\"";
                i++;
            } else while (
                    (currentLine.matches(UokikPattern.USTEP) ||
                    currentLine.matches(UokikPattern.PUNKT) ||
                    currentLine.matches(UokikPattern.LITERA) ||
                    currentLine.matches(UokikPattern.ZWYKLA_LINIA)) &&
                    nextLine.matches(UokikPattern.ZWYKLA_LINIA)
                    ) {

                if (currentLine.matches(UokikPattern.KONIEC_MYSLNIKIEM)) {
                    String currentLineWithoutDash = currentLine.substring(0, currentLine.length() - 1);
                    connectedLines = currentLineWithoutDash + nextLine;
                } else {
                    connectedLines = currentLine + " " + nextLine;

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

    public List<String> moveUstepsToNewLine(List<String> file) {
        List<String> fileWithMovedLines = new LinkedList<>();

        for (int i = 0; i < file.size(); i++) {
            String currentLine = file.get(i);

            if (currentLine.matches(UokikPattern.ARTYKUL)) {
                int indexOfSecondDot = -1;
                for (int j = 0; j < 2; j++) {
                    indexOfSecondDot = currentLine.indexOf('.', indexOfSecondDot + 1);
                }

                String artykulTitle = currentLine.substring(0, indexOfSecondDot + 2);
                String artykulData = currentLine.substring(indexOfSecondDot + 2);

                fileWithMovedLines.add(artykulTitle);
                fileWithMovedLines.add(artykulData);
            } else {
                fileWithMovedLines.add(currentLine);
            }
        }

        return fileWithMovedLines;
    }
}
