package decisionTree;

import parser.CSVParser;
import parser.ParserLine;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import static utils.TableUtils.*;

public class DecisionTree {

    static int counter;

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

        System.out.println(table.toString());

        System.out.println("Building the decision tree");

        LinkedList<String> atributes = new LinkedList<>(table.get(0).getAll());
        atributes.remove(0);
        atributes.remove(atributes.size()-1);
        counter=1;
        buildDecisionTree2(table,atributes,0);
    }

    private static void buildDecisionTree(LinkedList<ParserLine> table) {
        LinkedList<String> atributes = new LinkedList<>(table.get(0).getAll());
        atributes.remove(0);
        atributes.remove(atributes.size()-1);
        int[] sortedEntropy = DataAnalysis.sortEntropy(DataAnalysis.entropy(table,atributes));
        /*
            O que é suposto o atributeChoice fazer?
            Se for escolher o de menor entropia seria só selecionar o segundo indice (1) da sortedEntropy
         */
        //int idOfBest = sortedEntropy[1];

        int idOfBest = DataAnalysis.attributeChoice(sortedEntropy);

        Tree.Node root = new Tree.Node(getColumnName(table, idOfBest), null, null, 0, idOfBest, table);
        //Se calhar podes usar o construtor
        //Tree t = new Tree(idOfBest,null,table);
        System.out.println("Root: \t" + root.nameOfAttribute);


        for (Tree.Arc a : root.children) {
            System.out.println("Nome arco: \t" + a.name);
//            LinkedList<Integer> temp = new LinkedList<>();
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
                    a.values.addFirst(i);
                    System.out.println("Valor pertencente: " + a.values.getFirst());
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
        }
    }

    private static void buildDecisionTree2(LinkedList<ParserLine> table,LinkedList<String> remainingAtributes,int depth){
        String tabs="";
        for(int i=0;i<depth;i++) {
            tabs+="\t";
        }

        LinkedList<String> classValues = getClassUniqueValuesInColumn(table);
        if (classValues.size()==1){
            System.out.println(classValues.get(0)+"(counter"+counter+")");
            counter++;
        }else if (remainingAtributes.size()==0){
            System.out.println(getMostCommonValueInClass(table)+"(counter" + counter+")");
            counter++;
        }else{
            System.out.println();
            int bestAtributeId = DataAnalysis.sortEntropy(DataAnalysis.entropy(table,remainingAtributes))[1];
            remainingAtributes.remove(getColumnName(table,bestAtributeId));
            System.out.println(tabs + "< " + getColumnName(table, bestAtributeId) + " >");
            for (String value : getUniqueValuesInColumn(table, bestAtributeId)) {
                System.out.print(tabs + "\t" + value + ":");
                LinkedList<ParserLine> tableWithRestrictions = cutTableBasedOnRestriction(table, bestAtributeId, value);
                if ((tableWithRestrictions.size() - 1) == 0) {
                    System.out.println(getMostCommonValueInClass(table)+"(counter" + counter+")");
                    counter++;
                } else {
                    LinkedList<String> newAtrib = new LinkedList<>(remainingAtributes);
                    newAtrib.remove(getColumnName(table,bestAtributeId));
                    buildDecisionTree2(tableWithRestrictions, newAtrib, depth + 2);
                }
            }
        }
    }

}

