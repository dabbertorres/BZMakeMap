package backend;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public class Dictionary
{
	private String language;
	
	private HashMap<String, String> strings;
	
	public Dictionary(String l)
	{
		language = l;
		
		strings = new HashMap<String, String>();
		
		loadFile();
	}
	
	public String getLanguage()
	{
		return language;
	}
	
	public String get(String k)
	{
		return strings.get(k);
	}
	
	private void loadFile()
	{
		strings.clear();
		
		try
		{
			InputStream in = getClass().getResourceAsStream("src/res/" + language + ".dic");
			Scanner scan = new Scanner(in);
			
			while(scan.hasNextLine())
			{
				String line = scan.nextLine();
				
				String[] keyAndVal = line.split("=");
				strings.put(keyAndVal[0], keyAndVal[1]);
			}
			
			scan.close();
		}
		catch(NullPointerException e)
		{
			System.err.println("Could not find dictionary: " + language + ". Using \"en\"");
			language = "en";
			loadFile();
		}
	}
}
