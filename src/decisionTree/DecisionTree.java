package decisionTree;

import parser.CSVParser;
import parser.ParserLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;

import static utils.TableUtils.getColumnName;

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

        System.out.println((DataAnalysis.entropy(table)));

//        System.out.println((DataAnalysis.entropy2(table)));

        sortedEntropy = DataAnalysis.sortEntropy(DataAnalysis.entropy(table));


        System.out.println("Building the decision tree");

        buildDecisionTree(table);
    }

    private static void buildDecisionTree(LinkedList<ParserLine> table) {
        int idOfBest = DataAnalysis.attributeChoice(sortedEntropy);
        Tree.Node root = new Tree.Node(getColumnName(table, idOfBest), null, null, 0, idOfBest, table);
        System.out.println("Root: \t" + root.nameOfAttribute);


        /////////////////// fazer aqui análise

        LinkedList<Integer> IDs = new LinkedList<>();
        for (int i = 0; i <= DataAnalysis.numberOfLines; i++) {
            IDs.addLast(i);
        }

        for (Tree.Arc a : root.children) {
            System.out.println("Nome arco: \t" + a.name);
            LinkedList<Integer> temp = new LinkedList<>();
            boolean allTheSame = true;
            String aux = null;

            System.out.println("Ultima: \t" + DataAnalysis.listsOfLines[0].getLast());
/*
            for (int i=0; i<DataAnalysis.numberOfLines; i++) {
                System.out.println("valor: \t" + DataAnalysis.listsOfLines[i].getLast());
            }
*/
            /* verifica se na ultima coluna as opcoes sao iguais para cada classificacao*/
            for (int i = 1; i <= DataAnalysis.numberOfLines; i++) {
                if (DataAnalysis.listsOfLines[i].get(idOfBest).equals(a.name)) {
                    temp.addFirst(i);
                    System.out.println("Valor pertencente: " + i);
                    if (allTheSame) {
                        if (aux == null) {
                            aux = DataAnalysis.listsOfLines[i].getLast();
                        } else {
                            if (!DataAnalysis.listsOfLines[i].getLast().equals(aux)) {
                                allTheSame = false;
                            }
                        }
                    }
                }
            }

            System.out.println("All the same: " + allTheSame + "\n");

            a.values = temp;
            temp.clear();
        }

//        System.out.println("Consistencia: " + DataAnalysis.checkConsistency(idOfBest));

    }
}

