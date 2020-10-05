package connectors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Properties {
	private String filepath;
	
	public static void main(String[] args) {
		Properties prop = new Properties();
		prop.setFilePath("C:\\Users\\juanb\\Desktop\\Java Workspace\\recordsview\\src\\props\\properties");
		String url = prop.getProperty("db.url");
		System.out.println(url);
	}
	
	public String getProperty(String property) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			while(line != null) {
				//do something with line
				String[] splitted = line.split("=", 2);
				if (splitted[0].equals(property.toString())) {
					reader.close();
					return splitted[1];
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setFilePath(String filepath) {
		this.filepath = filepath;
	}
}
