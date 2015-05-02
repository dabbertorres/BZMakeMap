/*
 * Entry point for the revised and Java version of BZMakeMap
 * Massive improvement to the general quality of the code, and
 * uses an actual GUI library. Not a hacked in one of sorts.
 */

package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import backend.Utility;

public class Panel extends JPanel
{
	private static final long serialVersionUID = -3317813288766774217L;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame = new JFrame("BZMakeMap");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new Panel());
				frame.pack();
				frame.setVisible(true);
				frame.setResizable(false);
			}
		});
	}
	
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
	private JCheckBox saveAscii;
	private JCheckBox addNetmis;
	
	private Box buttonBox;
	private JButton createButton;
	private JButton editButton;
	
	Panel()
	{
		// get the planets we can use
		File[] planetFilesINI = Utility.getFilesInDir(Utility.getBZInstallDir() + "/Edit/ini/");
		
		String[] planetNames = new String[planetFilesINI.length];
		for(int i = 0; i < planetNames.length; i++ )
		{
			String name = planetFilesINI[i].getName();
			planetNames[i] = name.substring(0, name.length() - 4);	// cut off file extension
		}
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		planetsBox = new Box(BoxLayout.X_AXIS);
		planetsLabel = new JLabel("Planets:");
		planets = new JComboBox<String>(planetNames);
		planets.setMaximumSize(planets.getPreferredSize());
		planetsBox.add(planetsLabel);
		planetsBox.add(Box.createHorizontalGlue());
		planetsBox.add(planets);
		
		sizesBox = new Box(BoxLayout.X_AXIS);
		sizesLabel = new JLabel("Size:");
		sizes = new JComboBox<Integer>(new Integer[] {1280, 2560, 3840, 5120});
		sizes.setMaximumSize(sizes.getPreferredSize());
		sizesBox.add(sizesLabel);
		sizesBox.add(Box.createHorizontalGlue());
		sizesBox.add(sizes);
		
		fileNameBox = new Box(BoxLayout.X_AXIS);
		fileNameLabel = new JLabel("File:");
		fileName = new JTextField(10);
		fileName.setMaximumSize(fileName.getPreferredSize());
		fileNameBox.add(fileNameLabel);
		fileNameBox.add(Box.createHorizontalGlue());
		fileNameBox.add(fileName);
		
		checkBoxesBox = new Box(BoxLayout.X_AXIS);
		autoPaint = new JCheckBox("Auto Paint", true);
		autoPaint.setToolTipText("Autopaints the terrain for the map if checked");
		startEdit = new JCheckBox("startedit", true);
		startEdit.setToolTipText("Launches the editor with /startedit, pausing gameplay, if checked");
		saveAscii = new JCheckBox("Save Ascii", false);
		saveAscii.setToolTipText("Saves the bzn file in ascii format");
		addNetmis = new JCheckBox("Add to Netmis", false);
		addNetmis.setToolTipText("Appends a line to the netmis file for the map");
		checkBoxesBox.add(autoPaint);
		checkBoxesBox.add(Box.createHorizontalGlue());
		checkBoxesBox.add(startEdit);
		checkBoxesBox.add(Box.createHorizontalGlue());
		checkBoxesBox.add(saveAscii);
		checkBoxesBox.add(Box.createHorizontalGlue());
		checkBoxesBox.add(addNetmis);
		
		buttonBox = new Box(BoxLayout.X_AXIS);
		createButton = new JButton("Create");
		createButton.setToolTipText("Creates the map files with the given information");
		createButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(fileName.getText().length() > 0)
				{
					List<String> command = new ArrayList<String>();
					command.add(Utility.getBZInstallDir() + "Edit/makeTRN.exe");
					command.add(fileName.getText());
					command.add("/c");
					command.add("/w=" + sizes.getSelectedItem());
					command.add("/h=" + sizes.getSelectedItem());
					
					if(autoPaint.isSelected())
						command.add("/p=" + Utility.getBZInstallDir() + "Edit/ini/" + planets.getSelectedItem() +
									".ini");
					
					Utility.launchExe(command, Utility.getBZInstallDir() + "/addon", true);
				}
			}
		});
		
		editButton = new JButton("Edit");
		editButton.setToolTipText("Runs the BZ editor with the given file name and set options");
		editButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				List<String> command = new ArrayList<String>();
				command.add(Utility.getBZInstallDir() + "bzone.exe");
				command.add(fileName.getText() + ".trn");
				command.add(startEdit.isSelected() ? "/startedit" : "/edit");
				command.add("/win");
				command.add(saveAscii.isSelected() ? "/asciisave" : "");
				
				Utility.launchExe(command, Utility.getBZInstallDir(), false);
			}
		});
		
		buttonBox.add(createButton);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(editButton);
		
		this.add(planetsBox);
		this.add(Box.createVerticalGlue());
		this.add(sizesBox);
		this.add(Box.createVerticalGlue());
		this.add(fileNameBox);
		this.add(Box.createVerticalGlue());
		this.add(checkBoxesBox);
		this.add(Box.createVerticalGlue());
		this.add(buttonBox);
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(360, 160);
	}
}
