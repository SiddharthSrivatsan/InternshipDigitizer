import java.util.*;
import java.io.*;

public class TxtEditor {
	
	public static void main(String[] args) {
		
		File file = new File("tester.txt");
		String table = "Int.,λ I.A. Vac.,wn,Designation,\n";
		String finalTable = "";
	
		try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			
			while(scanner.hasNextLine()) {
				String temp = scanner.nextLine();
				if(!Character.isDigit(temp.charAt(0)) || temp.charAt(1) != ' ') {
					continue;
				}
				else {
					temp = temp.replace(",", "");
					temp = temp.replace(' ', ',');
					table += temp + ",\n";
				}
			}
			scanner.close();
			
			char[] tableChar = table.toCharArray();
			for(char i : tableChar) {
				if(Character.isDigit(i) || Character.isAlphabetic(i) || i == ',' || i == '.' || i == '\n') {
					finalTable += "" + i;
				}	
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(finalTable);
		
		try {  
			FileWriter newTxt = new FileWriter("final.txt");
			newTxt.write(finalTable);
			newTxt.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
