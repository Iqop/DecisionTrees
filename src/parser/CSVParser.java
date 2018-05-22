package parser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;
import static utils.TableUtils.*;

public class CSVParser {

    public static LinkedList<ParserLine> parseData(File f) throws FileNotFoundException{
        Scanner scan = new Scanner(f);
        LinkedList<ParserLine> table = new LinkedList<>();
        while(scan.hasNext()){
            table.addLast(new ParserLine(scan.nextLine()));
        }
        scan.close();

        LinkedList<Integer> colWithNumbers = getColumnsThatContainsNumerals(table);

        for (int columnId : colWithNumbers){
            LinkedList<String> columnData = getColumn(table,columnId);
            double maxValue = getMax(columnData);
            double minValue = getMin(columnData);
            int numberOfIntervals = calcSturges(columnData.size())+1;
            double valuesInsideInterval = ((maxValue - minValue)/numberOfIntervals);

            for(int i=1;i<table.size();i++){
                Double value = Double.parseDouble(getFromPosition(table,i,columnId));
                for(double min = minValue;min<=maxValue;min+=valuesInsideInterval){
                    if (value>=min && value<(min+valuesInsideInterval)){
                        //Used Locale.US to force the double value to be in format ___.____
                        setToPosition(table,i,columnId,String.format(Locale.US,"(%.2f - %.2f)",min,(min+valuesInsideInterval)));
                    }
                }
            }

        }


        return table;
    }


    private static String getFromPosition(LinkedList<ParserLine> table, int line, int column){
        return table.get(line).get(column);
    }


    private static void setToPosition(LinkedList<ParserLine> table, int line, int column, String value){
        table.get(line).set(column,value);
    }



    private static int calcSturges(int size){
        return (int) (1+(Math.log(size)/Math.log(2)));
    }



    private static double getMin(LinkedList<String> column){
        double min = Double.MAX_VALUE;
        for(int i=1;i<column.size();i++){
            String s = column.get(i);
            if (min > Double.parseDouble(s)){
                min = Double.parseDouble(s);
            }
        }
        return min;
    }


    private static double getMax(LinkedList<String> column){
        double max = Double.MIN_VALUE;
        for(int i=1;i<column.size();i++){
            String s = column.get(i);
            if (max < Double.parseDouble(s)){
                max = Double.parseDouble(s);
            }
        }
        return max;
    }

}