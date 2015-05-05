
package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

public class Actions
{
	public static class Create implements ActionListener
	{
		private gui.Window win;
		
		public Create(gui.Window w)
		{
			win = w;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			String fileName = win.getFileName();
			
			if(fileName.length() > 0)
			{
				int size = win.getSelectedSize();
				
				List<String> command = new ArrayList<String>();
				command.add(Utility.getBZInstallDir() + "Edit/makeTRN.exe");
				command.add(fileName);
				command.add("/c");
				command.add("/w=" + size);
				command.add("/h=" + size);
				
				if(win.isAutoPaintOn())
					command.add("/p=" + Utility.getBZInstallDir() + "Edit/ini/" + win.getSelectedPlanet() + ".ini");
				
				Utility.launchExe(command, Utility.getBZInstallDir() + "addon/", true);
			}
		}
	}
	
	public static class Edit implements ActionListener
	{
		private gui.Window win;
		
		public Edit(gui.Window w)
		{
			win = w;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			String fileName = win.getFileName();
			
			if(fileName.length() > 0)
			{
				List<String> command = new ArrayList<String>();
				command.add(Utility.getBZInstallDir() + "bzone.exe");
				command.add(fileName + ".trn");
				command.add(win.isStartEditOn() ? "/startedit" : "/edit");
				command.add("/win");
				
				if(win.isAsciiSaveOn())
					command.add("/asciisave");
				
				Utility.launchExe(command, Utility.getBZInstallDir(), false);
			}
		}
	}
	
	public static class BZPathSetter implements ActionListener
	{
		private gui.Window win;
		
		public BZPathSetter(gui.Window w)
		{
			win = w;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			win.showBZPathChooser();
			String p = win.getSelectedBZPath();
			
			if(p == null)
				return;
			
			// + 1 in below statements to include the directory separator character
			if(p.lastIndexOf('/') == -1)
				p = p.substring(0, p.lastIndexOf('\\') + 1);
			else
				p = p.substring(0, p.lastIndexOf('/') + 1);
			
			Utility.setBZPath(p);
			Utility.saveBZPath();
		}
	}
	
	public static class LanguageSetter implements ActionListener
	{
		private gui.Window win;
		
		public LanguageSetter(gui.Window w)
		{
			win = w;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			JMenuItem d = (JMenuItem) e.getSource();
			
			win.setLanguage(d.getText());
		}
	}
}
