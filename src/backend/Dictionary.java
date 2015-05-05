/*
 * Dictionary.java
 * Wrapper class for making multi-language support easier
 */

package backend;

import java.io.File;
import java.io.FileNotFoundException;
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
			Scanner scan = new Scanner(new File("src/res/" + language + ".dic"));
			
			while(scan.hasNextLine())
			{
				String line = scan.nextLine();
				
				String[] keyAndVal = line.split("=");
				strings.put(keyAndVal[0], keyAndVal[1]);
			}
			
			scan.close();
		}
		catch(NullPointerException | FileNotFoundException e)
		{
			System.err.println("Could not find dictionary: \"" + language + "\". Using \"en\"");
			language = "en";
			loadFile();
		}
	}
}
