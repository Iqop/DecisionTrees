package decisionTree;

import parser.ParserLine;

import java.util.LinkedList;

import static utils.TableUtils.*;

class DataAnalysis {

    private static int numberOfColumns;
    static int numberOfLines;
    static LinkedList<String>[] listsOfLines;
    private static LinkedList<String> optionsLastColumn = new LinkedList<>();
    private static LinkedList<ParserLine> tableCopy = null;

    /* cálculo de entropia */
    static double[] entropy(LinkedList<ParserLine> table) {
        numberOfColumns = 0;                                // num de atributos
        numberOfLines = table.size() - 1;
        tableCopy = new LinkedList<>(table);
        listsOfLines = readLines(table);

//        System.out.println("Options: \t" + optionsLastColumn.size());
//        System.out.println("Number of columns: \t" + numberOfColumns);

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
//                        System.out.println("A ver: " + attributeValue + valueLastColumn);
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
        return array;
    }

    static double[] entropy2(LinkedList<ParserLine> table) {

        int numberOfColumns = table.get(0).size();
        double entropy[] = new double[numberOfColumns - 1];

        LinkedList<String> uniqueValuesInClass = getClassUniqueValuesInColumn(table);

        int columnSize = getColumn(table, 0).size() - 1;

        for (int i = 1; i < numberOfColumns - 1; i++) {
            double entropyValueForColumn = 0;

            LinkedList<String> uniqueValuesInColumn = getUniqueValuesInColumn(table, i);

            for (String uniqueValueInColumn : uniqueValuesInColumn) {

                LinkedList<ParserLine> tableWithIRest = new LinkedList<>(cutTableBasedOnRestriction(table, i, uniqueValueInColumn));
                double nLinesOnIRest = tableWithIRest.size() - 1;
                double innerEntropy = 0;
                for (String uniqueClassValue : uniqueValuesInClass) {
                    double nLinesOnCRest = new LinkedList<>(cutTableBasedOnRestriction(tableWithIRest, getClassColumnPosition(table), uniqueClassValue)).size() - 1;
                    if (nLinesOnCRest != 0) {
                        innerEntropy += (nLinesOnCRest / nLinesOnIRest) * (Math.log(nLinesOnCRest / nLinesOnIRest) / Math.log(2.0));
                    }
                }
                entropyValueForColumn += Math.abs((nLinesOnIRest / columnSize) * innerEntropy);
            }

            entropy[i] = entropyValueForColumn;
        }

        return entropy;
    }


    /* ordenação ascendente do vetor por entropia */
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
    private static LinkedList<String>[] readLines(LinkedList<ParserLine> table) {
        @SuppressWarnings("unchecked")
        LinkedList<String>[] listsOfLines = new LinkedList[numberOfLines + 1];

        for (int i = 0; i <= numberOfLines; i++) {
            listsOfLines[i] = new LinkedList<>(table.get(i).getAll());
//            System.out.println("Line: \t" + listsOfLines[i]);
            for (int j = 0; j < listsOfLines[i].size(); j++) {
                listsOfLines[i].add(j, "\0" + listsOfLines[i].remove(j));
            }
        }

        optionsLastColumn = getClassUniqueValuesInColumn(table);
        for (int i = 0; i < optionsLastColumn.size(); i++) {
            optionsLastColumn.add(i, "\0" + optionsLastColumn.remove(i));
        }
        numberOfColumns = listsOfLines[1].size();
        return listsOfLines;
    }


    /* lista com as classificações possíveis de um atributo */
    static LinkedList<String> numberOfDifferentOptions(int columnID) {
        return getUniqueValuesInColumn(tableCopy, columnID);
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


    /* nome do atributo dada coluna */
    static String nameOfChoice(int id) {
        return listsOfLines[0].get(id);
    }


    /* PLURALIDADE */
    static String[] getPluralityValue(LinkedList<Integer> IDs) {
        int max = -1, idOfMax = -1, count;
        LinkedList<String> temp = numberOfDifferentOptions(numberOfColumns - 1);
        int size = temp.size();
        for (int i = 0; i < size; i++) {
            temp.addLast('\0' + temp.removeFirst());
        }
//        System.out.println("Nome da coluna: \t" + nameOfChoice(numberOfColumns - 1));
//        System.out.println("Num de escolhas: \t" + temp.size());
        for (int i = 0; i < size; i++) {
            count = 0;
//            System.out.println("Entrou 1");
            for (Integer id : IDs) {
//                System.out.println("Entrou 2");
//                System.out.println("Na lista: \t" + temp.get(i));
//                System.out.println("No array: \t" + listsOfLines[id].getLast());
                if (listsOfLines[id].getLast().equals(temp.get(i))) {
                    count++;
                }
            }
            if (count > max) {
//                System.out.println("Entrou 3");
                max = count;
                idOfMax = i;
            }
        }
        String[] aux = new String[2];
        aux[0] = temp.get(idOfMax);
        aux[1] += max;
//        System.out.println("Max: " + max);
//        System.out.println("Id: " + idOfMax);
        return aux;
    }


    /* Pluralidade do pai */
    static String getDadPlurality(Tree.Node node) {
        String[] major = new String[2];
        for (Tree.Arc a : node.children) {
            String[] value = getPluralityValue(a.values);
            if (Integer.parseInt(value[1]) > Integer.parseInt(major[1])) {
                major[0] = value[0];
                major[1] = value[1];
            }
        }
        return major[0];
    }


    /* verifica se na ultima coluna as opcoes sao iguais para cada classificacao*/
    static boolean checkSameClassification(Tree.Arc a, int idOfBest) {
        boolean allTheSame = true;
        String aux = null;

        for (int i = 1; i <= numberOfLines; i++) {
            if (listsOfLines[i].get(idOfBest).equals(a.name)) {
                a.values.addFirst(i);
//                System.out.println("Valor pertencente: " + a.values.getFirst());
                if (allTheSame) {
                    if (aux == null) {
                        aux = listsOfLines[i].getLast();
                    } else {
                        if (!listsOfLines[i].getLast().equals(aux)) {
                            allTheSame = false;
                        }
                    }
                }
            }
        }
        System.out.println("All the same: " + allTheSame + "\n");
        return allTheSame;
    }
}
