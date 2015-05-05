
package backend;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public final class Utility
{
	private static final String BZ_REG_KEY = "SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{B3B61934-313A-44A2-B589-700FDAA6C758}_is1";
	
	private static String bzPath;
	
	public static String getBZInstallDir()
	{
		if(bzPath == null)
		{
			if(Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, BZ_REG_KEY, "InstallLocation"))
			{
				bzPath = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, BZ_REG_KEY, "InstallLocation");
			}
			else
			{
				// implement ask user for location and set the key to that
			}
		}
		
		return bzPath;
	}
	
	public static void setBZPath(String p)
	{
		bzPath = p;
	}
	
	public static File[] getFilesInDir(String dirStr)
	{
		return new File(dirStr).listFiles();
	}
	
	public static void launchExe(List<String> c, String wd, boolean wait)
	{
		ProcessBuilder pb = new ProcessBuilder(c);
		pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		pb.directory(new File(wd));
		
		try
		{
			Process p = pb.start();
			
			if(wait)
				p.waitFor();
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private Utility()
	{}
}
