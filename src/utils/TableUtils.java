package utils;

import parser.ParserLine;

import java.util.*;

public class TableUtils {
    public static LinkedList<Integer> getColumnsThatContainsNumerals(LinkedList<ParserLine> table) {


        LinkedList<Integer> columnsWithNumerals = new LinkedList<>();

        boolean foundNumberColumn = false;

        ParserLine e = table.get(1);
        LinkedList<String> info = e.getAll();

        for (int i = 1; i < info.size(); i++) {
            String s = info.get(i);
            try {
                Double.parseDouble(s);
                foundNumberColumn = true;
            } catch (NumberFormatException nfe) {
                //nothing happens
            } finally {
                if (foundNumberColumn) {
                    columnsWithNumerals.add(i);
                    foundNumberColumn = false;
                }
            }
        }
        return columnsWithNumerals;
    }

    public static LinkedList<String> getColumn(LinkedList<ParserLine> table, int index) {
        LinkedList<String> column = new LinkedList<>();
        for (ParserLine p : table) {
            column.addLast(p.get(index));
        }
        return column;
    }

    public static int getClassColumnPosition(LinkedList<ParserLine> table) {
        return table.get(0).size() - 1;
    }

    public static String getColumnName(LinkedList<ParserLine> table, int index) {
        return table.get(0).get(index);
    }

    private static int getColumnFromName(LinkedList<ParserLine> table, String name) {
        for (int i = 0; i < table.get(0).size(); i++) {
            if (table.get(0).get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public static LinkedList<String> getClassUniqueValuesInColumn(LinkedList<ParserLine> table) {
        return getUniqueValuesInColumn(table, getClassColumnPosition(table));
    }

    public static LinkedList<String> getUniqueValuesInColumn(LinkedList<ParserLine> table, int index) {
        Set<String> toRet = new HashSet<>(getColumn(table, index));
        toRet.remove(getColumnName(table, index));
        return new LinkedList<>(toRet);
    }

    public static LinkedList<ParserLine> cutTableBasedOnRestriction(LinkedList<ParserLine> table, String atribute, String atributeValue) {
        LinkedList<ParserLine> newTable = new LinkedList<>(table);
        int colNumber = getColumnFromName(table, atribute);
        for (int i = 1; i < table.size(); i++) {
            if (!table.get(i).get(colNumber).equals(atributeValue)) {
                ParserLine parserLine = table.get(i);
                newTable.remove(parserLine);
            }
        }
        return newTable;
    }

    public static LinkedList<ParserLine> cutTableBasedOnRestriction(LinkedList<ParserLine> table, int atributeIndex, String atributeValue) {
        LinkedList<ParserLine> newTable = new LinkedList<>(table);
        for (int i = 1; i < table.size(); i++) {
            if (!table.get(i).get(atributeIndex).equals(atributeValue)) {
                ParserLine parserLine = table.get(i);
                newTable.remove(parserLine);
            }
        }
        return newTable;
    }

    public static String getMostCommonValueInClass(LinkedList<ParserLine> table) {
        Map<String, Integer> m = new HashMap<>();
        for (String s : getClassUniqueValuesInColumn(table)) {
            m.put(s, 0);
        }
        LinkedList<String> column = new LinkedList<>(getColumn(table, getClassColumnPosition(table)));
        column.remove(0);
        for (String s : column) {
            m.put(s, m.get(s) + 1);
        }
        int max = -1;
        String maxId = "";
        for (String s : getClassUniqueValuesInColumn(table)) {
            if (m.get(s) > max) {
                max = m.get(s);
                maxId = s;
            }
        }
        return maxId;
    }

}
