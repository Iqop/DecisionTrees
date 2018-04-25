package decisionTree;

import parser.CSVParser;
import parser.ParserLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class DecisionTree {

    static int[] sortedEntropy;

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

        buildDecisionTree(table);
    }

    private static void buildDecisionTree(LinkedList<ParserLine> table) {
        int idOfBest = DataAnalysis.attributeChoice(sortedEntropy);
        String nameOfBest = DataAnalysis.nameOfChoice(idOfBest);
//        System.out.println("Nome: \t" + nameOfBest);
        LinkedList<String> children = DataAnalysis.numberOfDifferentAttributes(idOfBest);
//        System.out.println("Tamanho: \t" + children.size());
        if (children == null) {
            System.out.println("Some error occurred");
        } else {
            int numberOfChildren = children.size();
            Tree.Node root = new Tree.Node(nameOfBest, null, null, 0);
        }
    }

}
