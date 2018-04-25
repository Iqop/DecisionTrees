package decisionTree;

import parser.CSVParser;
import parser.ParserLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class DecisionTree {

    private static int[] sortedEntropy;

    public static void main(String[] args) {
        System.out.printf("Performing parsing of %s to a data structure\n\n", args[0]);
        LinkedList<ParserLine> table;

        try {
            File csvFile = new File(args[0]);
            table = CSVParser.parseData(csvFile);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
            return;
        }
        System.out.println("File successfully loaded");


        sortedEntropy = DataAnalysis.sortEntropy(DataAnalysis.entropy(table));


        System.out.println("Building the decision tree");

        buildDecisionTree();
    }

    private static void buildDecisionTree() {
        int idOfBest = DataAnalysis.attributeChoice(sortedEntropy);
        Tree.Node root = new Tree.Node(DataAnalysis.nameOfChoice(idOfBest), null, null, 0, idOfBest);
        System.out.println("Root: \t" + root.nameOfAttribute);
        for (Tree.Arc a : root.children) {
            System.out.println(a.name);

            // fazer aqui análise

        }
    }
}

