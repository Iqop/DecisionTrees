package decisionTree;

import parser.CSVParser;
import parser.ParserLine;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static utils.TableUtils.*;

public class DecisionTreeBuilder {

    //    private static int counter = 0;
    private static Tree tree;
    private static int bestAttributeId = -1;

    public static void main(String[] args) throws IOException {
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
        System.out.println("Printing table, now its numerical values inside classes");
        System.out.println(table.toString());

        System.out.println("\n\nBuilding the decision tree");

        LinkedList<String> attributes = new LinkedList<>(table.get(0).getAll());
        attributes.remove(0);
        attributes.remove(attributes.size() - 1);
//        counter = 1;
        Map<String, LinkedList<String>> attributeOptions = new HashMap<>();
        for (String s : attributes) {
            attributeOptions.put(s, getUniqueValuesInColumn(table, getAttributeId(table, s)));
        }


        tree = new Tree(attributes, attributeOptions);
        buildDecisionTree(table, attributes, 0, null);

        String name = args[0].split("/")[args[0].split("/").length - 1].split("\\.")[0];

        String decisionTreeSaveFile = "decisionTree(" + name + ").dt";

        System.out.println("\nSaving decision tree into " + decisionTreeSaveFile);


        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(decisionTreeSaveFile));
        objectOutputStream.writeObject(tree);
        objectOutputStream.close();

        System.out.println("Done, you can now use DecisionTreeAnnalist");

    }

    private static int getCounter(LinkedList<ParserLine> table, int columnID, String v1, String v2) {
        int columnClass = table.get(0).size() - 1, counter = 0;
//        System.out.println("Best ID: " + columnA);
//        System.out.println("Table counter   : " + table.toString());
//        System.out.println("teste1: " + table.size()); // linhas
//        System.out.println("Teste2: " + table.get(0).size());   // colunas
        if (columnID > -1) {
            if (v1 == null) {
                for (ParserLine value : table) {
                    if (value.get(columnClass).equals(v2))
                        counter++;
                }
            } else {
//                System.out.println("Entrou");
                for (ParserLine value : table) {
                    if (value.get(columnID).equals(v1) && value.get(columnClass).equals(v2))
                        counter++;
                }
            }
        }
        return counter;
    }

    private static void buildDecisionTree(LinkedList<ParserLine> table, LinkedList<String> remainingAttributes, int depth, Tree.Arc arc) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            tabs.append("\t");
        }

        LinkedList<String> classValues = getClassUniqueValuesInColumn(table);

/*        System.out.println("Lista : " + classValues.toString());
        if (arc != null)
            System.out.println("Nome: " + arc.value);
*/
        if (classValues.size() == 1) {
            tree.insertNode(arc, classValues.get(0));
            //Classe Encontrada (Criado nó leaf)
//            System.out.println("1: " + classValues.get(0) + " \t(counter" + counter + ")");
//            System.out.println("1: " + classValues.get(0) + " \t(counter" + getCounter(table, bestAttributeId, arc != null ? arc.value : null, classValues.get(0)) + ")");
            System.out.println(classValues.get(0) + " \t(counter" + getCounter(table, bestAttributeId, arc != null ? arc.value : null, classValues.get(0)) + ")");
//            counter=0;

        } else if (remainingAttributes.size() == 0) {
            //Classe Encontrada (Criado nó leaf)
            String commonValue = getMostCommonValueInClass(table);

//            System.out.println("Table 2: " + table.toString());

            tree.insertNode(arc, commonValue);
//            System.out.println("2: " + commonValue + " \t(counter " + counter + ")");
//            System.out.println("2: " + commonValue + " \t(counter" + getCounter(table,bestAttributeId ,arc != null ? arc.value : null, commonValue) + ")");
            System.out.println(commonValue + " \t(counter" + getCounter(table, bestAttributeId, arc != null ? arc.value : null, commonValue) + ")");
//            counter=0;

        } else {
            System.out.println();
            //Classe não encontrada (Criado nó com filhos)
            bestAttributeId = DataAnalysis.sortEntropy(table, DataAnalysis.entropy(table, remainingAttributes))[1];

            Tree.Node n = tree.insertNode(arc, bestAttributeId, 0, table);
            System.out.println(tabs + "< " + getColumnName(table, bestAttributeId) + " >");


            for (String value : n.getAttributeOptions()) {
                bestAttributeId = DataAnalysis.sortEntropy(table, DataAnalysis.entropy(table, remainingAttributes))[1];
                LinkedList<String> uniqueValuesInColumn = getUniqueValuesInColumn(table, bestAttributeId);
                Tree.Arc selectedArc = n.getArc(value);
                System.out.print(tabs + "\t" + value + ":" + " ");


//                System.out.println("Valor pretendido: " + value);
//                System.out.println("Valores unicos: " + uniqueValuesInColumn.toString());

                if (uniqueValuesInColumn.contains(value)) {
                    LinkedList<ParserLine> tableWithRestrictions = cutTableBasedOnRestriction(table, bestAttributeId, value);
                    int newIndex = getColumnFromName(tableWithRestrictions, getColumnName(table, bestAttributeId));

                    if ((tableWithRestrictions.size() - 1) == 0) {
                        //Restrição impossivel (cria nó leaf)
                        String commonValue = getMostCommonValueInClass(table);
                        tree.insertNode(selectedArc, commonValue);
//                        System.out.println("3: " + commonValue + " \t(counter " + counter + ")");
//                        System.out.println("3: " + commonValue + " \t(counter" + getCounter(tableWithRestrictions,newIndex, arc != null ? arc.value : null, commonValue) + ")");
                        System.out.println(commonValue + " \t(counter" + getCounter(tableWithRestrictions, newIndex, arc != null ? arc.value : null, commonValue) + ")");
//                        counter++;

                    } else {
                        //Prossegue a criação da arvore através do arco
                        LinkedList<String> newAttribute = new LinkedList<>(remainingAttributes);
                        newAttribute.remove(getColumnName(table, bestAttributeId));
                        buildDecisionTree(tableWithRestrictions, newAttribute, depth + 2, selectedArc);
                    }
                } else {
                    bestAttributeId = DataAnalysis.sortEntropy(table, DataAnalysis.entropy(table, remainingAttributes))[1];
                    String commonValue = getMostCommonValueInClass(table);
                    tree.insertNode(selectedArc, commonValue);
//                    System.out.println("Value: " + value);
//                    System.out.println("Maior: " + commonValue);
//                    System.out.println("Table: " + table.toString());
//                    System.out.println("4: " + commonValue + " \t(counter " + counter + ")");
//                    System.out.println("4: " + commonValue + " \t(counter" + getCounter(table, bestAttributeId,value, commonValue) + ")");
                    System.out.println(commonValue + " \t(counter" + getCounter(table, bestAttributeId, value, commonValue) + ")");
//                    counter++;
                }
            }
        }
    }

}

