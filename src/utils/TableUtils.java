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
                //nothing happens
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

    public static int getColumnFromName(LinkedList<ParserLine> table,String name){
        for(int i=0;i<table.get(0).size();i++){
            if (table.get(0).get(i).equals(name)){
                return i;
            }
        }
        return -1;
    }

    public static LinkedList<String> getClassUniqueValuesInColumn(LinkedList<ParserLine> table) {
        return getUniqueValuesInColumn(table, getClassColumnPosition(table));
    }
    public static LinkedList<String> getUniqueValuesInColumn(LinkedList<ParserLine> table, int index){
        Set<String> toRet = new HashSet<>(getColumn(table,index));
        toRet.remove(getColumnName(table,index));
        return new LinkedList<>(toRet);
    }

    public LinkedList<ParserLine> cutTableBasedOnRestriction(LinkedList<ParserLine> table,String atribute,String atributeValue){
        LinkedList<ParserLine> newTable = new LinkedList<>(table);
        int colNumber = getColumnFromName(table,atribute);
        for(int i=1;i<table.size();i++){
            if (!table.get(i).get(colNumber).equals(atributeValue)){
                ParserLine parserLine = table.get(i);
                newTable.remove(parserLine);
            }
        }
        return newTable;
    }

    public LinkedList<ParserLine> cutTableBasedOnRestriction(LinkedList<ParserLine> table,int atributeIndex,String atributeValue){
        LinkedList<ParserLine> newTable = new LinkedList<>(table);
        for(int i=1;i<table.size();i++){
            if (!table.get(i).get(atributeIndex).equals(atributeValue)){
                ParserLine parserLine = table.get(i);
                newTable.remove(parserLine);
            }
        }
        return newTable;
    }

}
