/*
 * Entry point for the revised and Java version of BZMakeMap
 * Massive improvement to the general quality of the code, and
 * uses an actual GUI library. Not a hacked in one of sorts.
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import backend.Actions;
import backend.BzoneFileFilter;
import backend.Dictionary;
import backend.Utility;

public class Window extends JFrame
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new Window();
			}
		});
	}
	
	private static final long serialVersionUID = -3317813288766774217L;

	// dictionary for strings
	private Dictionary dic;
	
	// main window contents
	private JPanel panel;
	
	// gui variables
	private Box planetsBox;
	private JLabel planetsLabel;
	private JComboBox<String> planets;
	
	private Box sizesBox;
	private JLabel sizesLabel;
	private JComboBox<Integer> sizes;
	
	private Box fileNameBox;
	private JLabel fileNameLabel;
	private JTextField fileName;
	
	private Box checkBoxesBox;
	private JCheckBox autoPaint;
	private JCheckBox startEdit;
	private JCheckBox asciiSave;
	private JCheckBox addNetmis;
	
	private Box buttonBox;
	private JButton createButton;
	private JButton editButton;
	
	// file chooser dialog for setting BZ Path
	private JFileChooser fcDialog;
	
	public Window()
	{
		super("BZMakeMap");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		setupMenuBar();
		
		dic = new Dictionary(Locale.getDefault().getLanguage());
		
		panel = new JPanel();
		
		// get the planets we can use
		
		File[] planetFilesINI = Utility.getFilesInDir(Utility.getBZInstallDir() + "/Edit/ini/");
		String[] planetNames = new String[planetFilesINI.length];
		for(int i = 0; i < planetNames.length; i++ )
		{
			String name = planetFilesINI[i].getName();
			planetNames[i] = name.substring(0, name.length() - 4); // cut off file extension
		}
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		planetsBox = new Box(BoxLayout.X_AXIS);
		planetsLabel = new JLabel(dic.get("planets"));
		planets = new JComboBox<String>(planetNames);
		planets.setMaximumSize(planets.getPreferredSize());
		planetsBox.add(planetsLabel);
		planetsBox.add(Box.createHorizontalGlue());
		planetsBox.add(planets);
		
		sizesBox = new Box(BoxLayout.X_AXIS);
		sizesLabel = new JLabel(dic.get("size"));
		sizes = new JComboBox<Integer>(new Integer[] {1280, 2560, 3840, 5120});
		sizes.setMaximumSize(sizes.getPreferredSize());
		sizesBox.add(sizesLabel);
		sizesBox.add(Box.createHorizontalGlue());
		sizesBox.add(sizes);
		
		fileNameBox = new Box(BoxLayout.X_AXIS);
		fileNameLabel = new JLabel(dic.get("file"));
		fileName = new JTextField(10);
		fileName.setMaximumSize(fileName.getPreferredSize());
		fileNameBox.add(fileNameLabel);
		fileNameBox.add(Box.createHorizontalGlue());
		fileNameBox.add(fileName);
		
		checkBoxesBox = new Box(BoxLayout.X_AXIS);
		autoPaint = new JCheckBox(dic.get("autopaint"), true);
		autoPaint.setToolTipText(dic.get("autopaintToolTip"));
		startEdit = new JCheckBox("startedit", true);
		startEdit.setToolTipText(dic.get("starteditToolTip"));
		asciiSave = new JCheckBox(dic.get("saveAscii"), false);
		asciiSave.setToolTipText(dic.get("saveAsciiToolTip"));
		addNetmis = new JCheckBox(dic.get("netmis"), false);
		addNetmis.setToolTipText(dic.get("netmisToolTip"));
		
		checkBoxesBox.add(autoPaint);
		checkBoxesBox.add(Box.createHorizontalGlue());
		checkBoxesBox.add(startEdit);
		checkBoxesBox.add(Box.createHorizontalGlue());
		checkBoxesBox.add(asciiSave);
		checkBoxesBox.add(Box.createHorizontalGlue());
		checkBoxesBox.add(addNetmis);
		
		buttonBox = new Box(BoxLayout.X_AXIS);
		createButton = new JButton("Create");
		createButton.setToolTipText("Creates the map files with the given information");
		createButton.addActionListener(new Actions.Create(this));
		
		editButton = new JButton("Edit");
		editButton.setToolTipText("Runs the BZ editor with the given file name and set options");
		editButton.addActionListener(new Actions.Edit(this));
		
		buttonBox.add(createButton);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(editButton);
		
		panel.add(planetsBox);
		panel.add(Box.createVerticalGlue());
		panel.add(sizesBox);
		panel.add(Box.createVerticalGlue());
		panel.add(fileNameBox);
		panel.add(Box.createVerticalGlue());
		panel.add(checkBoxesBox);
		panel.add(Box.createVerticalGlue());
		panel.add(buttonBox);
		
		panel.setSize(panel.getPreferredSize());
		
		this.add(panel);
		this.pack();
		
		fcDialog = new JFileChooser();
		fcDialog.setFileFilter(new BzoneFileFilter());
	}
	
	public void showDoneDialog()
	{
		JOptionPane.showMessageDialog(this, "Map file creation complete.", "Complete", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showBZPathChooser()
	{
		fcDialog.showDialog(this, "Select");
	}
	
	public String getSelectedBZPath()
	{
		try
		{
			return fcDialog.getSelectedFile().getCanonicalPath();
		}
		catch(IOException | NullPointerException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getFileName()
	{
		return fileName.getText();
	}
	
	public int getSelectedSize()
	{
		return (int) sizes.getSelectedItem();
	}
	
	public String getSelectedPlanet()
	{
		return (String) planets.getSelectedItem();
	}
	
	public boolean isAutoPaintOn()
	{
		return autoPaint.isSelected();
	}
	
	public boolean isStartEditOn()
	{
		return startEdit.isSelected();
	}
	
	public boolean isAsciiSaveOn()
	{
		return asciiSave.isSelected();
	}
	
	private void setupMenuBar()
	{
		JMenuBar mb = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		
		JMenuItem mi;
		
		// option to set the Battlezone path
		mi = new JMenuItem("Set Path...");
		mi.addActionListener(new Actions.SetBZPath(this));
		
		menu.add(mi);
		
		mb.add(menu);
		
		this.setJMenuBar(mb);
	}
}
