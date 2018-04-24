package parser;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.PasswordAuthentication;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.Scanner;

public class CSVParser {
	
	public static LinkedList<ParserColumn> parseData(File f) throws FileNotFoundException{
		Scanner scan = new Scanner(f);
		LinkedList<ParserColumn> table = new LinkedList<>();
		while(scan.hasNext()){
			table.addLast(new ParserColumn(scan.nextLine()));
		}
		scan.close();

		LinkedList<Integer> colWithNumbers = getColumnsThatContainsNumerals(table);

		for (int columnId : colWithNumbers){
			LinkedList<String> columnData = getColumn(table,columnId);
			double maxValue = getMax(columnData);
			double minValue = getMin(columnData);
			int numberOfIntervals = calcSturges(columnData.size());
			double valuesInsideInterval = (double)((maxValue - minValue)/numberOfIntervals);

			for(int i=1;i<table.size();i++){
				Double value = Double.parseDouble(getFromPosition(table,i,columnId));
				for(double min = minValue;min<=maxValue;min+=valuesInsideInterval){
					if (value>=min && value<(min+valuesInsideInterval)){
						setToPosition(table,i,columnId,String.format("%.2f - %.2f",min,(min+valuesInsideInterval)));
					}
				}
			}

		}


		return table;
	}


	static String getFromPosition(LinkedList<ParserColumn> table,int line,int column){
		return table.get(line).get(column);
	}


	static void setToPosition(LinkedList<ParserColumn> table,int line,int column,String value){
		table.get(line).set(column,value);
	}



	static int calcSturges(int size){
		return (int) (1+(Math.log(size)/Math.log(2)));
	}



	static double getMin(LinkedList<String> column){
		double min = Double.MAX_VALUE;
		for(int i=1;i<column.size();i++){
			String s = column.get(i);
			if (min > Double.parseDouble(s)){
				min = Double.parseDouble(s);
			}
		}
		return min;
	}


	static double getMax(LinkedList<String> column){
		double max = Double.MIN_VALUE;
		for(int i=1;i<column.size();i++){
			String s = column.get(i);
			if (max < Double.parseDouble(s)){
				max = Double.parseDouble(s);
			}
		}
		return max;
	}



	public static LinkedList<Integer> getColumnsThatContainsNumerals(LinkedList<ParserColumn> table){


		LinkedList<Integer> columnsWithNumerals = new LinkedList<>();

		boolean foundNumberColumn=false;

		ParserColumn e = table.get(1);
		LinkedList<String> info = e.getAll();

		for (int i=1;i<info.size();i++) {
			String s = info.get(i);
			try {
				Double.parseDouble(s);
				foundNumberColumn=true;
			} catch (NumberFormatException nfe) {
			}finally{
				if (foundNumberColumn){
					columnsWithNumerals.add(i);
					foundNumberColumn=false;
				}
			}
		}
		return columnsWithNumerals;
	}
	
	public static LinkedList<String> getColumn(LinkedList<ParserColumn> table,int index){
		LinkedList<String> column = new LinkedList<>();
		for(ParserColumn p : table){
			column.addLast(p.get(index));
		}
		return column;
	}

	public static int getClassColumnPosition(LinkedList<ParserColumn> table){
		return table.get(0).size() -1;
	}

	public static String getClassColumnName(LinkedList<ParserColumn> table){
		return table.get(0).get(getClassColumnPosition(table));
	}
}
