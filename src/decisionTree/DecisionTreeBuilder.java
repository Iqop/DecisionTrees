package decisionTree;

import parser.CSVParser;
import parser.ParserLine;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static utils.TableUtils.*;

public class DecisionTreeBuilder {

    private static int counter;
    private static Tree tree;
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


        System.out.println("Building the decision tree");

        LinkedList<String> atributes = new LinkedList<>(table.get(0).getAll());
        atributes.remove(0);
        atributes.remove(atributes.size() - 1);
        counter = 1;
        Map<String,LinkedList<String>> attributeOptions = new HashMap<>();
        for(String s: atributes){
            attributeOptions.put(s,getUniqueValuesInColumn(table,getAttributeId(table,s)));
        }


        tree = new Tree(atributes,attributeOptions);
        buildDecisionTree(table, atributes, 0, null);

        String name = args[0].split("/")[args[0].split("/").length -1].split("\\.")[0];

        String decisionTreeSaveFile = "decisionTree(" + name + ").dt";

        System.out.println("\nSaving decision tree into " + decisionTreeSaveFile);


        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(decisionTreeSaveFile));
        objectOutputStream.writeObject(tree);
        objectOutputStream.close();

        System.out.println("Done, you can now use DecisionTreeAnalist");

    }

    private static void buildDecisionTree(LinkedList<ParserLine> table, LinkedList<String> remainingAtributes, int depth, Tree.Arc arc) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            tabs.append("\t");
        }


        LinkedList<String> classValues = getClassUniqueValuesInColumn(table);
        if (classValues.size() == 1) {
            tree.insertNode(arc, classValues.get(0));
            //Classe Encontrada (Criado nó leaf)
            System.out.println(classValues.get(0) + " \t(counter" + counter + ")");
            counter++;

        } else if (remainingAtributes.size() == 0) {
            //Classe Encontrada (Criado nó leaf)
            String commonValue = getMostCommonValueInClass(table);
            tree.insertNode(arc, commonValue);
            System.out.println(commonValue + " \t(counter " + counter + ")");
            counter++;

        } else {
            System.out.println();
            //Classe não encontrada (Criado nó com filhos)
            int bestAttributeId = DataAnalysis.sortEntropy(table, DataAnalysis.entropy(table, remainingAtributes))[1];

            Tree.Node n = tree.insertNode(arc, bestAttributeId, 0, table);
            System.out.println(tabs + "< " + getColumnName(table, bestAttributeId) + " >");


            for (String value : n.getAttributeOptions()) {
                LinkedList<String> uniqueValuesInColumn = getUniqueValuesInColumn(table,bestAttributeId);
                Tree.Arc selectedArc = n.getArc(value);
                System.out.print(tabs + "\t" + value + ":" + " ");
                if (uniqueValuesInColumn.contains(value)) {

                    LinkedList<ParserLine> tableWithRestrictions = cutTableBasedOnRestriction(table, bestAttributeId, value);

                    if ((tableWithRestrictions.size() - 1) == 0) {
                        //Restrição impossivel (cria nó leaf)
                        String commonValue = getMostCommonValueInClass(table);
                        tree.insertNode(selectedArc, commonValue);
                        System.out.println(commonValue + " \t(counter " + counter + ")");
                        counter++;

                    } else {
                        //Prossegue a criação da arvore através do arco
                        LinkedList<String> newAtrib = new LinkedList<>(remainingAtributes);
                        newAtrib.remove(getColumnName(table, bestAttributeId));
                        buildDecisionTree(tableWithRestrictions, newAtrib, depth + 2, selectedArc);
                    }
                }else{
                    String commonValue = getMostCommonValueInClass(table);
                    tree.insertNode(selectedArc, commonValue);
                    System.out.println(commonValue + " \t(counter " + counter + ")");
                    counter++;
                }
            }
        }
    }

}

