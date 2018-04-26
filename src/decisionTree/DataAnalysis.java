package decisionTree;

import parser.ParserLine;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;

import static utils.TableUtils.*;

class DataAnalysis {

    private static int numberOfColumns;
    private static int numberOfLines;
    private static LinkedList<String>[] listsOfLines;
//    private static LinkedList<Character> unwanted = new LinkedList<>();
    private static LinkedList<String> optionsLastColumn = new LinkedList<>();


    static LinkedList<ParserLine> tableCopy=null;


    /* caracteres não pretendidos */
/*
    private static void restrictions() {
        unwanted.addFirst('[');
        unwanted.addFirst(']');
        unwanted.addFirst(' ');
        unwanted.addFirst(',');
        unwanted.addFirst('\n');
    }
*/

    // cálculo de entropia

    static double[] entropy2(LinkedList<ParserLine> table){

        int numberOfColumns = table.get(0).size();
        double entropy[] = new double[numberOfColumns-1];

        LinkedList<String> uniqueValuesInClass =  getClassUniqueValuesInColumn(table);

        int columnSize = getColumn(table,0).size()-1;

        for(int i=1;i<numberOfColumns-1;i++){
            double entropyValueForColumn=0;

            LinkedList<String> uniqueValuesInColumn = getUniqueValuesInColumn(table,i);

            for(String uniqueValue : uniqueValuesInColumn){

                LinkedList<ParserLine> tableWithRestriction = new LinkedList<>(cutTableBasedOnRestriction(table, i , uniqueValue));
                double lines = tableWithRestriction.size()-1;
                double innerEntropy = 0;
                for(String uniqueClassValues : uniqueValuesInClass){
                    double numberOfLines = new LinkedList<>(cutTableBasedOnRestriction(tableWithRestriction,getClassColumnPosition(table),uniqueClassValues)).size() -1;
                    if (numberOfLines!=0) {
                        innerEntropy += (numberOfLines / lines) * (Math.log(numberOfLines / lines) / Math.log(2.0));
                    }
                }
                entropyValueForColumn+=Math.abs((lines/columnSize) * innerEntropy);
            }


            entropy[i] = entropyValueForColumn;
        }

        return entropy;
    }





    static double[] entropy(LinkedList<ParserLine> table) {
        numberOfColumns = 0;                                // num de atributos
        numberOfLines = table.size() - 1;
        tableCopy=new LinkedList<>(table);
        listsOfLines = readLines(table);
/*
        for (int i=0; i<=numberOflines; i++) {
            System.out.println("Teste: \t" + arrayOfLists[i].getFirst());
        }
*/

        System.out.println("Options: \t" + optionsLastColumn.size());
        System.out.println("Number of columns: \t" + numberOfColumns);

        double[] array = new double[numberOfColumns - 1];
        String valueLastColumn;

        // entropia dos atributos
        for (int k = 1; k < numberOfColumns - 1; k++) {
            array[k] = 0;
            LinkedList<String> temp = new LinkedList<>();
            double aux = 0, possiveis, favoraveis;
            for (int j = 1; j <= numberOfLines; j++) {
                if (!temp.contains(listsOfLines[j].get(k))) {
                    String attributeValue = listsOfLines[j].get(k);
                    temp.addFirst(attributeValue);
                    possiveis = 0;
                    for (int m = 0; m < optionsLastColumn.size(); m++) {
                        favoraveis = 0;
                        valueLastColumn = optionsLastColumn.get(m);
                        System.out.println("A ver: " + attributeValue + valueLastColumn);
                        for (int l = j; l <= numberOfLines; l++) {
                            if (listsOfLines[l].get(k).equals(attributeValue)) {
                                if (m == 0) {
                                    possiveis++;
                                }
                                if (listsOfLines[l].getLast().equals(valueLastColumn)) {
                                    favoraveis++;
                                }
                            }
                        }
//                        System.out.println("Favoraveis: \t" + favoraveis);
//                        System.out.println("Possiveis: \t" + possiveis);
//                        System.out.println("Conta: \t" + favoraveis + "/" + possiveis);
                        if (possiveis != 0 && favoraveis != 0) {
                            aux += (favoraveis / possiveis) * Math.log(favoraveis / possiveis) / Math.log(2);
                        }
//                        System.out.println("Primeiro: \t" + aux);
                    }
                    aux = Math.abs(possiveis / numberOfLines * aux);
//                    System.out.println("Segundo: \t" + aux);
                    array[k] += aux;
                    aux = 0;
//                    System.out.println("Prob: \t" + array[k]);
                }
            }
            temp.clear();
        }
/*
        for (int i = 0; i < numberOfColumns; i++) {
            System.out.println(listsOfLines[i + 1].getFirst() + "\t" + array[i]);
        }
*/
/*        for (String s : listsOfLines[0])
            System.out.println(s);

        for (Double i : array)
            System.out.println("Valor: " + i);
*/
//        System.out.println("Line: \t" + listsOfLines[0].get(2));
        return array;
    }


    // ordenação ascendente do vetor por entropia
    static int[] sortEntropy(double[] array) {
        int[] idSorted = new int[array.length];

        for (int i = 0; i < idSorted.length; i++) {
            idSorted[i] = i;
        }

        // bubbleSort
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    double temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    int temp2 = idSorted[j];
                    idSorted[j] = idSorted[j + 1];
                    idSorted[j + 1] = temp2;
                }
            }
        }

        for (int i = 0; i < array.length; i++) {
            System.out.println("V: \t" + idSorted[i] + "\t" + array[i]);
        }

        return idSorted;
    }

    /* lê linha a linha e passa cada linha para uma lista */
    //Nao necessario para entropy2
    private static LinkedList<String>[] readLines(LinkedList<ParserLine> table) {

        LinkedList<String>[] listsOfLines = new LinkedList[numberOfLines + 1];

        for (int i = 0; i <= numberOfLines; i++) {
            listsOfLines[i] = new LinkedList<>(table.get(i).getAll());
            System.out.println("Line: \t" + listsOfLines[i]);

            for (int j = 0; j < listsOfLines[i].size(); j++) {
                listsOfLines[i].add(j, "\u0000" + listsOfLines[i].remove(j));
            }
        }

        //Exemplificação do que disse em cima

        System.out.println("Teste de comparação: "+(listsOfLines[0].get(0).compareTo("ID")==0));



        optionsLastColumn = getClassUniqueValuesInColumn(table);
        /*
            Mesma cena de cima
         */
        for(int i=0;i<optionsLastColumn.size();i++){
            optionsLastColumn.add(i,"\u0000"+optionsLastColumn.remove(i));
        }
        numberOfColumns = listsOfLines[1].size();
        return listsOfLines;
    }


    /* lista com as classificações possíveis de um atributo */
    static LinkedList<String> numberOfDifferentOptions(int columnID) {
        return getUniqueValuesInColumn(tableCopy,columnID);

    }


    /* escolha do melhor atributo */
    static int attributeChoice(int[] array) {
        int value = -1;
        for (int i = 1; i < numberOfColumns && value == -1; i++) {
            if (array[i] < Integer.MAX_VALUE) {
                value = array[i];
                array[i] = Integer.MAX_VALUE;
            }
        }
        return value;
    }


    static String nameOfChoice(int id) {
        return listsOfLines[0].get(id);
    }
}
