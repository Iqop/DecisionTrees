package decisionTree;

import parser.ParserLine;

import java.util.LinkedList;

import static utils.TableUtils.*;

class DataAnalysis {

    static double[] entropy(LinkedList<ParserLine> table, LinkedList<String> availableAtributes) {

        int numberOfColumns = table.get(0).size();
        double entropy[] = new double[numberOfColumns - 1];

        LinkedList<String> uniqueValuesInClass = getClassUniqueValuesInColumn(table);

        int columnSize = getColumn(table, 0).size() - 1;

        for (int i = 1; i < numberOfColumns - 1; i++) {
            double entropyValueForColumn = 0;

            LinkedList<String> uniqueValuesInColumn = getUniqueValuesInColumn(table, i);

            for (String uniqueValueInColumn : uniqueValuesInColumn) {

                LinkedList<ParserLine> tableWithIRest = new LinkedList<>(cutTableBasedOnRestriction(table, i, uniqueValueInColumn));
                double possiveis = tableWithIRest.size() - 1;
                double innerEntropy = 0;
                for (String uniqueClassValue : uniqueValuesInClass) {
                    double favoraveis = new LinkedList<>(cutTableBasedOnRestriction(tableWithIRest, getClassColumnPosition(table), uniqueClassValue)).size() - 1;
                    if (favoraveis != 0) {
                        innerEntropy += (favoraveis / possiveis) * (Math.log(favoraveis / possiveis) / Math.log(2.0));
                    }
                }
                entropyValueForColumn += Math.abs((possiveis / columnSize) * innerEntropy);
            }

            entropy[i] = entropyValueForColumn;
        }

        for (int i = 1; i < numberOfColumns - 1; i++) {
            if (!availableAtributes.contains(getColumnName(table, i))) {
                entropy[i] = Double.MAX_VALUE;
            }
        }

        entropy[0]=-1;
        return entropy;
    }


    /* ordenação ascendente do vetor por entropia */
    static int[] sortEntropy(LinkedList<ParserLine> table, double[] array) {
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

                } else if (array[j] == array[j + 1]) {
                    if (getUniqueValuesInColumn(table, j).size() > getUniqueValuesInColumn(table, j + 1).size()) {
                        double temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;

                        int temp2 = idSorted[j];
                        idSorted[j] = idSorted[j + 1];
                        idSorted[j + 1] = temp2;
                    }
                }
            }
        }
        return idSorted;
    }

}
