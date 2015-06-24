import java.util.*;
import java.text.*;
import java.io.*;

public class TableWriter {
	
	public static void main(String[] args) {

		File file = new File("final.txt");
		
		String tableWithCheck = "";
		

		File firstCSV = new File("F1_lin_ASD52.csv");
		int lines = 0;
		
		try {
			Scanner counter = new Scanner(firstCSV);
			while(counter.hasNextLine()) {
				counter.nextLine();
				lines++;
			}
			counter.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String[][] lineCSV = new String[lines][27];
		
		try {
			Scanner scan2 = new Scanner(firstCSV);
			scan2.useDelimiter(",");
			for(int i = 0; i < lines; i++) {
				for(int k = 0; k < 27; k++) {
					lineCSV[i][k] = scan2.next();
				}
			}
			scan2.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Scanner scan = new Scanner(file);
			scan.nextLine();
			while(scan.hasNextLine()) {
				String temp = scan.nextLine();
				String intensity = temp.substring(0, 1);
				double wavelength = Double.parseDouble(temp.substring(2, 8));
				double wavenumber = Double.parseDouble(temp.substring(9,15));
				double check = wavelength - (Math.pow(10, 8) / wavenumber);
				DecimalFormat f = new DecimalFormat("##.000");
				
				double difference = Math.abs(wavelength - Double.parseDouble(lineCSV[1][2]));
				int index = 1;
				for(int i = 2; i < lines; i++) {
					if(Math.abs(wavelength - Double.parseDouble(lineCSV[i][2])) < difference) {
						index = i;
					}
				}
				System.out.println(index);
				String lowID = lineCSV[index][24];
				String highID = lineCSV[index][25];
				
				tableWithCheck += intensity + "," + wavelength + "," + wavenumber + 
									"," + f.format(check) + "," + lowID + "," + 
									highID + "," + ",\n";
			}
			scan.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(tableWithCheck);
		
		try {  
			FileWriter newTxt = new FileWriter("table.csv");
			newTxt.write("Int.,λ I.A. Vac.,wn,wl_check,id1, id2\n" + tableWithCheck);
			newTxt.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
