/*
 * Simple class acting as the filter on accepted file types for getting
 * a user set Battlezone path
 */

package backend;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class BzoneFileFilter extends FileFilter
{
	public boolean accept(File f)
	{
		if(f.isDirectory())
			return true;
		
		String n = f.getName();
		
		return n == "bzone.exe" || n == "bzint.exe";
	}
	
	public String getDescription()
	{
		return "Battlezone Executable";
	}
}
