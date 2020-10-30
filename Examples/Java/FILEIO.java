import java.io.*;
import java.util.Scanner;
import java.util.HashMap;

/*
Simple demonstration of file i/o and HashMaps
*/
class FILEIO{
	public static void main(String args[]) throws Exception{
		File inputFile = new File("2791_2019dar.csv");
		Scanner reader = new Scanner(inputFile);

		//String builder is designed for adding stuff to the end, whereas String becomes really expensive when strings get long
		StringBuilder str = new StringBuilder();
		while(reader.hasNextLine()) {
			str.append(reader.nextLine() + '\n');
		}
		String data = str.toString();

		HashMap<Integer, String> teamData = new HashMap<Integer, String>();

		//(for each line in the data)
		for (String line : data.split("\n")){
			//skip the headers
			if (line.contains("Dead"))
				continue;
			//if we have seen this team before, add another game to their data
			if (teamData.containsKey(Integer.parseInt(line.split(",")[0]))){
				teamData.replace(Integer.parseInt(line.split(",")[0]), teamData.get(Integer.parseInt(line.split(",")[0])) + "\n" + line);
			//otherwise make new data
			}else{
				teamData.put(Integer.parseInt(line.split(",")[0]), line);
			}
		}
		//print team 51's games
		System.out.println(teamData.get(51));
	}
}