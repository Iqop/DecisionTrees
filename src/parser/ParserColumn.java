package parser;
import java.util.LinkedList;
import java.util.Scanner;

public class ParserColumn {
	
	private LinkedList<String> data;
	
	ParserColumn(String data){
		Scanner dataScan = new Scanner(data);
		this.data =new LinkedList<>();
		dataScan.useDelimiter(",");
		while(dataScan.hasNext()){
			this.data.addLast(dataScan.next());
		}
		
		dataScan.close();
	}
	
	String get(int i){
		return data.get(i);
	}

	void set(int i,String value){
		data.set(i,value);
	}

	LinkedList<String> getAll(){
		return data;
	}
	
	public String toString(){
		return data.toString()+"\n";
	}
	
	public int size(){
		return data.size();
	}
}

