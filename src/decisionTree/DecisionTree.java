package decisionTree;

import parser.CSVParser;
import parser.ParserColumn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class DecisionTree {

    public static void main(String[] args) {
        System.out.printf("Performing parsing of %s to a data structure\n\n", args[0]);
        LinkedList<ParserColumn> table;

        try {
            File csvFile = new File(args[0]);
            table = CSVParser.parseData(csvFile);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
            return;
        }


//		System.out.println("Size: \t" + table.size());


        int[] entropy = DataAnalysis.sortEntropy(DataAnalysis.entropy(table));

//		System.out.println("\n\n" + table.toString());
        System.out.println("\nFile successfully loaded");

        System.out.println("Building the decision tree");

        buildDecisionTree(table);
    }

    static void buildDecisionTree(LinkedList<ParserColumn> table) {

    }
}
