import java.util.ArrayList;

public class Parser {
    private ArrayList<String> storedFile;
    String patternRozdzial = "^Rozdział \\w*$";
    String patternDzial = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    String patternArtykul = "^(Art. )(\\d)+(.)$";
    String patternUstep = "^(\\d+. )(.)*$";
    String patternPunkt = "^(\\d+)\\)(.)*$";
    String patternKoniecMyslnikiem = "^(.)*-$";
    String patternKoniecNormalnie = "^(.)*[^-]$";

    public Parser(ArrayList<String> storedFile) {
        this.storedFile = storedFile;
    }

    public Node parseToNodes(Node node) {
        for(String line : storedFile) {
            System.out.println(line);

            boolean lineIsRodzial = line.matches(patternRozdzial);
            boolean lineIsDzial = line.matches(patternDzial);
            boolean lineIsArtykul = line.matches(patternArtykul);
            boolean lineIsUstep = line.matches(patternUstep);

            int lastRozdzialIndex = 0;
            int lastDzialIndex = 0;
            int lastArtykulIndex = 0;
            int lastUstepIndex = 0;

            if(lineIsRodzial) {
                node.addNewChild(1, line);
            }

            if(lineIsDzial) {
                node.addNewChild(1, line);
            }

            if(lineIsArtykul) {
                node.addNewChild(1, line);
            }

            if(lineIsUstep) {
                node.addNewChild(1, line);
            }
        }

        for(Node childrenNode : node.getChildren()) {
            //System.out.println(childrenNode.getData());
        }

        return node;
    }
}
