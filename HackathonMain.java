import java.io.*;

class HackathonMain{
	public static void main(String args[]){
		String fileData = "";
		try{
			StringBuilder builder = new StringBuilder();
			File inFile = new File(args[0]);
			FileReader inReader = new FileReader(inFile);
			char[] inData = new char[1];
			while (inReader.read(inData) != -1){
				builder.append(inData);
			}
			inReader.close();
			fileData = builder.toString();
		}catch(FileNotFoundException e){
			System.out.println("Could not find " + args[0]);
		}catch(Exception e){
			System.out.println("No idea why but we done muffed up");
		}
		System.out.println(fileData);
	}
}