/*
 * Actions.java
 * Contains operations called by the GUI
 * to perform using makeTRN.exe or the Battlezone Editor
 */

package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
				
				boolean dirMade = new File(Utility.getBZInstallDir() + "addon/" + fileName + "/").mkdir();
				String mapPath = null;	// save our map path
				
				// make working directory the folder for the map files if it exists
				if(dirMade)
				{
					mapPath = Utility.getBZInstallDir() + "addon/" + fileName + "/";
					Utility.launchExe(command, mapPath, true);
				}
				else
				{
					mapPath = Utility.getBZInstallDir() + "addon/";
					Utility.launchExe(command, mapPath, true);
				}
				
				// grab our new trn file
				File trnFile = new File(mapPath + fileName + ".trn");
				
				// save the "[Size]" section from newly created trn
				String[] sizeSection = new String[6];
				try(Scanner scan = new Scanner(trnFile))
				{
					boolean found = false;
					while(scan.hasNextLine() && !found)
					{
						String line = scan.nextLine();
						
						if(line.equals("[Size]"))
						{
							sizeSection[0] = line;
							
							for(int i = 1; i < sizeSection.length; i++ )
							{
								sizeSection[i] = scan.nextLine();
							}
							
							found = true;
						}
					}
				}
				catch(FileNotFoundException ex)
				{
					ex.printStackTrace();
				}
				
				// copy the chosen planet trn over our new trn
				try
				{
					File planetTrn = new File(Utility.getBZInstallDir() + "Edit/trn/" + win.getSelectedPlanet() +
												".trn");
					Files.copy(planetTrn.toPath(), trnFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				
				// append our "[Size]" section to our new trn
				try(FileWriter fw = new FileWriter(trnFile, true))
				{
					for(String s : sizeSection)
					{
						fw.write(s + System.lineSeparator());
					}
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
				
				win.showDoneDialog();
			}
		}
	}
	
	public static class AutoPainter implements ActionListener
	{
		private gui.Window win;
		
		public AutoPainter(gui.Window w)
		{
			win = w;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			String fileName = win.getFileName();
			
			if(fileName.length() > 0)
			{
				boolean dirExists = new File(Utility.getBZInstallDir() + "addon/" + fileName).exists();
				
				String mapPath = null;
				if(dirExists)
				{
					mapPath = Utility.getBZInstallDir() + "addon/" + fileName + "/";
				}
				else
				{
					mapPath = Utility.getBZInstallDir() + "addon/";
				}
				
				List<String> command = new ArrayList<String>();
				command.add(Utility.getBZInstallDir() + "Edit/makeTRN.exe");
				command.add(mapPath + fileName + ".trn");
				command.add("/p=" + Utility.getBZInstallDir() + "Edit/ini/" + win.getSelectedPlanet() + ".ini");
				
				Utility.launchExe(command, mapPath, true);
				
				win.showAutoPaintDoneDialog();
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
				
				// if a bzn exists, use it. If not, use the trn.
				if(new File(Utility.getBZInstallDir() + "addon/" + fileName + "/" + fileName + ".bzn").exists() ||
					new File(Utility.getBZInstallDir() + "addon/" + fileName + ".bzn").exists())
				{
					command.add(fileName + ".bzn");
				}
				else
				{
					command.add(fileName + ".trn");
				}
				
				command.add(win.isStartEditOn() ? "/startedit" : "/edit");
				command.add("/win");
				
				if(win.isAsciiSaveOn())
					command.add("/asciisave");
				
				Utility.launchExe(command, Utility.getBZInstallDir(), true);
				
				// if folder for the map files exists, move the lgt file there
				// otherwise, leave it be
				if(new File(Utility.getBZInstallDir() + "addon/" + fileName).exists())
				{
					File old = new File(Utility.getBZInstallDir() + "addon/" + fileName + ".LGT");
					old.renameTo(new File(Utility.getBZInstallDir() + "addon/" + fileName + "/" + fileName + ".LGT"));
				}
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
	
	public static class BZPathGetter implements ActionListener
	{
		private gui.Window win;
		
		public BZPathGetter(gui.Window w)
		{
			win = w;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			win.showPathDialog();
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
