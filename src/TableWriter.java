import java.util.*;
import java.text.*;
import java.io.*;

public class TableWriter {
	
	private static String tableWithCheck;
	private static String[][] lineCSV;
	private static String[][] levelCSV;
	private static String lowID;
	private static String highID;
	private static String lowE;
	private static String highE;
	
	public static int numDigits(double num) {
		String s = Double.toString(num);
		int functionReturnValue = 0;
		int i = s.indexOf(".") + 1;
		int L = s.length();
		if(num % 1 == 0) {
			return 0;
		}
		if(i > 0) {
			functionReturnValue = L - i;
		}
		else {
			int k = L;
			for(int J = 1; J <= L; J++) {
				i = L - J + 1;
				k = i;
				if(!s.substring(i, i+1).equals("0")) {
					break;
				}
			}
			
			functionReturnValue = k - L;
			
		}
		
		return functionReturnValue;
		
	}
	
	public static void main(String[] args) {

		File file = new File("final.txt");
		
		tableWithCheck = "";

		File firstCSV = new File("C1_lin_ASD52.csv");
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
		
		lineCSV = new String[lines][27];
		
		try {
			Scanner scan2 = new Scanner(firstCSV);
			scan2.useDelimiter(",");
			for(int i = 0; i < lines; i++) {
				for(int k = 0; k < 26; k++) {
					if(scan2.hasNext()) {
						lineCSV[i][k] = scan2.next();
					}
				}
				
				lineCSV[i][26] = scan2.nextLine().replace(",", "");
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
				String[] temp = scan.nextLine().split(",");
				String intensity = temp[0];
				double wavelength = Double.parseDouble(temp[1]);
				double wavenumber = Double.parseDouble(temp[2]);
				double check = wavelength - (Math.pow(10, 8) / wavenumber);
				DecimalFormat f = new DecimalFormat("##.000");
				
				double difference = Math.abs(wavelength - Double.parseDouble(lineCSV[1][4]));
				int index = 1;
				for(int i = 2; i < lines; i++) {
					if(!lineCSV[i][4].equals("") && Math.abs(wavelength - Double.parseDouble(lineCSV[i][4])) < difference) {
						index = i;
						difference = Math.abs(wavelength - Double.parseDouble(lineCSV[i][4]));
					}
				}
				
				lowID = lineCSV[index][24];
				highID = lineCSV[index][25];
				
				File secondCSV = new File("C1_lev_ASD52.csv");
				
				int levels = 0;
				
				try {
					Scanner counter = new Scanner(secondCSV);
					while(counter.hasNextLine()) {
						counter.nextLine();
						levels++;
					}
					counter.close();
				}
				catch(FileNotFoundException e) {
					e.printStackTrace();
				}
				
				levelCSV = new String[levels][19];
				
				try {
					
					Scanner scann = new Scanner(secondCSV);
					scann.useDelimiter(",");
					for(int i = 0; i < levels; i++) {
						for(int k = 0; k < 18; k++) {
							if(scann.hasNext()) {
								levelCSV[i][k] = scann.next();
							}
						}
						
						levelCSV[i][18] = scann.nextLine().replace(",", "");
					}
					
					scann.close();
					
				}
				catch(FileNotFoundException e) {
					e.printStackTrace();
				}
				
				int lowEIndex = 0;
				int highEIndex = 0;
				
				while(!levelCSV[lowEIndex][17].equals(lowID)) {
					lowEIndex++;
				}
				
				while(!levelCSV[highEIndex][17].equals(highID)) {
					highEIndex++;
				}
				
				lowE = levelCSV[lowEIndex][6];
				highE = levelCSV[highEIndex][6];
				
				double p1 = Math.pow(10, -TableWriter.numDigits(wavelength))/wavelength;
				double p2 = Math.pow(10, -TableWriter.numDigits(wavenumber))/wavenumber;
				
				double dp = p2 - p1;
				
				double uncertainty = 0.05/wavelength * wavenumber;
				
				double eCheck = wavenumber - (Double.parseDouble(highE) - Double.parseDouble(lowE));
				
				double uncCheck = Math.abs(eCheck/uncertainty);
				
				tableWithCheck += intensity + "," + wavelength + "," + wavenumber + 
									"," + p1 + "," + p2 + "," + dp + "," + f.format(check) + 
									"," + lowID + "," + highID + "," + lowE + "," + highE + 
									"," + uncertainty + "," + eCheck + "," + uncCheck + ",\n";
			}
			scan.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		try {  
			FileWriter newTxt = new FileWriter("table.txt");
			newTxt.write("Int.,λ I.A. Vac.,wn,p1,p2,dp,wl_check,id1,id2,e1,e2,unc_wn,e_check,unc_check\n" + tableWithCheck);
			newTxt.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
