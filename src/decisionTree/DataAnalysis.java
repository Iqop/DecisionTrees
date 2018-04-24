package decisionTree;

import parser.ParserColumn;

import java.util.LinkedList;

class DataAnalysis {

    static int numberOfColumns;
    private static int numberOfLines;
    private static LinkedList<String>[] listsOfLines;
    private static LinkedList<Character> unwanted = new LinkedList<>();
    private static LinkedList<String> optionsLastColumn = new LinkedList<>();


    private static void restrictions() {
        unwanted.addFirst('[');
        unwanted.addFirst(']');
        unwanted.addFirst(' ');
        unwanted.addFirst(',');
        unwanted.addFirst('\n');
    }


    // cálculo de entropia
    static double[] entropy(LinkedList<ParserColumn> table) {
        restrictions();                                     // caracteres não pretendidos
        numberOfColumns = 0;                                // num de atributos
        numberOfLines = table.size() - 1;

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


    // conta o numero de vez que aparecem as opcoes da ultima coluna
    private static int[] lastColumnCount(LinkedList<String>[] listsOfLines) {
        String valueLastColumn;
        int[] counts = new int[optionsLastColumn.size()];
        for (int i = 0; i < optionsLastColumn.size(); i++) {
            valueLastColumn = optionsLastColumn.get(i);
//            System.out.println("Value: \t" + valueLastColumn);
            counts[i] = 0;
            for (int j = 1; j <= numberOfLines; j++) {
//                System.out.println("Array: \t" + listsOfLines[j].getLast());
                if (listsOfLines[j].getLast().equals(valueLastColumn)) {
                    counts[i]++;
                }
            }
        }
//        System.out.println("Counts: \t" + counts[0] + "\t" + counts[1] + "\t" + counts[2]);
        return counts;
    }


    // lê linha a linha e passa cada linha para uma lista
    private static LinkedList<String>[] readLines(LinkedList<ParserColumn> table) {
        @SuppressWarnings("unchecked")
        LinkedList<String>[] listsOfLines = new LinkedList[numberOfLines + 1];
        String valueLastColumn = "\0";
        for (int i = 0; i <= numberOfLines; i++) {
            listsOfLines[i] = new LinkedList<>();
            String line = table.get(i).toString();
//            System.out.println("Line: " + line);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == ',' || line.charAt(j) == ']') {
                    if (i == 0) {
                        numberOfColumns++;
                    }
                    if (i > 0 && line.charAt(j) == ']' && !optionsLastColumn.contains(valueLastColumn)) {
                        // título da ultima coluna nao entra
                        optionsLastColumn.addLast(valueLastColumn);
                    }
                    listsOfLines[i].addLast(valueLastColumn);
//                    System.out.println("Value: \t" + valueLastColumn);
                    valueLastColumn = "\0";
                } else if (!unwanted.contains(line.charAt(j))) {
                    valueLastColumn += line.charAt(j);
                }
            }
        }
        return listsOfLines;
    }


    // lista com os nós filhos
    private static LinkedList<String> numberOfDifferentAttributes(int columnID) {
        if (columnID < 1) {
            System.out.println("Coluna errada quase de certeza");
            return null;
        } else {
            LinkedList<String> temp = new LinkedList<>();
            for (int i = 1; i <= listsOfLines.length; i++) {
                String value = listsOfLines[i].get(columnID);
                if (!temp.contains(value)) {
                    temp.addFirst(value);
                }
            }
            return temp;
        }
    }
}
