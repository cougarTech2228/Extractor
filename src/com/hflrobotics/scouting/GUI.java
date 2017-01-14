package com.hflrobotics.scouting;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.SpringLayout;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import java.awt.SystemColor;

public class GUI extends JFrame
{
	private JTextField txtSomeFile;
	private JTextField textField_1;
	private JTextField textField_2;
	public GUI() {
		setTitle("Extractor");
		setSize(500, 300);
		setResizable(false);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Data Transfer", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane, 233, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane, 240, SpringLayout.WEST, panel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);
		
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		sl_panel.putConstraint(SpringLayout.NORTH, list, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, list, 197, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, list, 159, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, list, -129, SpringLayout.EAST, panel);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBackground(SystemColor.info);
		
		JSeparator separator = new JSeparator();
		sl_panel.putConstraint(SpringLayout.NORTH, separator, 0, SpringLayout.NORTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, separator, 6, SpringLayout.EAST, scrollPane);
		sl_panel.putConstraint(SpringLayout.SOUTH, separator, 233, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, separator, 7, SpringLayout.EAST, scrollPane);
		separator.setOrientation(SwingConstants.VERTICAL);
		panel.add(separator);
		
		JLabel lblCamera = new JLabel("Camera:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblCamera, 2, SpringLayout.NORTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.WEST, lblCamera, 6, SpringLayout.EAST, separator);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblCamera, 16, SpringLayout.NORTH, scrollPane);
		sl_panel.putConstraint(SpringLayout.EAST, lblCamera, 47, SpringLayout.EAST, separator);
		panel.add(lblCamera);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Camera 1", "Camera 2"}));
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox, 6, SpringLayout.SOUTH, lblCamera);
		sl_panel.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.EAST, separator);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox, 232, SpringLayout.EAST, separator);
		panel.add(comboBox);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		sl_panel.putConstraint(SpringLayout.NORTH, progressBar, 6, SpringLayout.SOUTH, comboBox);
		sl_panel.putConstraint(SpringLayout.WEST, progressBar, 6, SpringLayout.EAST, separator);
		sl_panel.putConstraint(SpringLayout.SOUTH, progressBar, 44, SpringLayout.SOUTH, comboBox);
		sl_panel.putConstraint(SpringLayout.EAST, progressBar, 0, SpringLayout.EAST, comboBox);
		panel.add(progressBar);
		
		JButton btnNewButton = new JButton("Submit");
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton, 74, SpringLayout.SOUTH, progressBar);
		sl_panel.putConstraint(SpringLayout.WEST, btnNewButton, 6, SpringLayout.EAST, separator);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewButton, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, comboBox);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Stop");
		sl_panel.putConstraint(SpringLayout.WEST, btnNewButton_1, 6, SpringLayout.EAST, separator);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNewButton_1, -6, SpringLayout.NORTH, btnNewButton);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton_1, 0, SpringLayout.EAST, comboBox);
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Clear");
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton_1, 6, SpringLayout.SOUTH, btnNewButton_2);
		sl_panel.putConstraint(SpringLayout.NORTH, btnNewButton_2, 6, SpringLayout.SOUTH, progressBar);
		sl_panel.putConstraint(SpringLayout.WEST, btnNewButton_2, 6, SpringLayout.EAST, separator);
		sl_panel.putConstraint(SpringLayout.EAST, btnNewButton_2, 0, SpringLayout.EAST, comboBox);
		panel.add(btnNewButton_2);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Data Files", null, panel_1, null);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		JLabel lblMatchDatacsv = new JLabel("Match Data [CSV]");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblMatchDatacsv, 10, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblMatchDatacsv, 10, SpringLayout.WEST, panel_1);
		panel_1.add(lblMatchDatacsv);
		
		txtSomeFile = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, txtSomeFile, 6, SpringLayout.SOUTH, lblMatchDatacsv);
		sl_panel_1.putConstraint(SpringLayout.WEST, txtSomeFile, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, txtSomeFile, -173, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, txtSomeFile, -83, SpringLayout.EAST, panel_1);
		txtSomeFile.setEditable(false);
		panel_1.add(txtSomeFile);
		txtSomeFile.setColumns(10);
		
		JButton button = new JButton("...");
		sl_panel_1.putConstraint(SpringLayout.NORTH, button, 30, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, button, 6, SpringLayout.EAST, txtSomeFile);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, button, 0, SpringLayout.SOUTH, txtSomeFile);
		sl_panel_1.putConstraint(SpringLayout.EAST, button, -10, SpringLayout.EAST, panel_1);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panel_1.add(button);
		
		JLabel lblNewLabel = new JLabel("Pit Data [CSV]");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblNewLabel, 17, SpringLayout.SOUTH, txtSomeFile);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, lblMatchDatacsv);
		panel_1.add(lblNewLabel);
		
		textField_1 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_1, 6, SpringLayout.SOUTH, lblNewLabel);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, lblMatchDatacsv);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, textField_1, -96, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_1, -83, SpringLayout.EAST, panel_1);
		textField_1.setEditable(false);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_3 = new JButton("...");
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnNewButton_3, 0, SpringLayout.NORTH, textField_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnNewButton_3, 0, SpringLayout.WEST, button);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnNewButton_3, -10, SpringLayout.EAST, panel_1);
		panel_1.add(btnNewButton_3);
		
		JLabel lblDriverDatacsv = new JLabel("Driver Data [CSV]");
		sl_panel_1.putConstraint(SpringLayout.EAST, lblDriverDatacsv, 0, SpringLayout.EAST, lblMatchDatacsv);
		panel_1.add(lblDriverDatacsv);
		
		textField_2 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.SOUTH, lblDriverDatacsv, -6, SpringLayout.NORTH, textField_2);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_2, 10, SpringLayout.WEST, panel_1);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("...");
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnNewButton_3, -46, SpringLayout.NORTH, btnNewButton_4);
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnNewButton_4, 193, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnNewButton_4, -10, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_2, 0, SpringLayout.NORTH, btnNewButton_4);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, textField_2, 0, SpringLayout.SOUTH, btnNewButton_4);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_2, -6, SpringLayout.WEST, btnNewButton_4);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnNewButton_4, 412, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnNewButton_4, -10, SpringLayout.EAST, panel_1);
		panel_1.add(btnNewButton_4);
	}
}
