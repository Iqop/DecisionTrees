package utils;

import parser.ParserLine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TableUtils {
    public static LinkedList<Integer> getColumnsThatContainsNumerals(LinkedList<ParserLine> table){


        LinkedList<Integer> columnsWithNumerals = new LinkedList<>();

        boolean foundNumberColumn=false;

        ParserLine e = table.get(1);
        LinkedList<String> info = e.getAll();

        for (int i=1;i<info.size();i++) {
            String s = info.get(i);
            try {
                Double.parseDouble(s);
                foundNumberColumn=true;
            } catch (NumberFormatException nfe) {
                foundNumberColumn=false;
            }finally{
                if (foundNumberColumn){
                    columnsWithNumerals.add(i);
                    foundNumberColumn=false;
                }
            }
        }
        return columnsWithNumerals;
    }

    public static LinkedList<String> getColumn(LinkedList<ParserLine> table, int index){
        LinkedList<String> column = new LinkedList<>();
        for(ParserLine p : table){
            column.addLast(p.get(index));
        }
        return column;
    }

    public static int getClassColumnPosition(LinkedList<ParserLine> table){
        return table.get(0).size() -1;
    }


    public static String getColumnName(LinkedList<ParserLine> table,int index){
        return table.get(0).get(index);
    }


    public static LinkedList<String> getClassDiferentColumnValues(LinkedList<ParserLine> table){
        Set<String> toRet = new HashSet<>(getColumn(table,getClassColumnPosition(table)));
        toRet.remove(getColumnName(table,getClassColumnPosition(table)));

        return new LinkedList<>(toRet);
    }

}
