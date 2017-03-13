package com.hflrobotics.scouting;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.sarxos.webcam.Webcam;
import com.hflrobotics.scouting.schedule.Schedule;
import com.hflrobotics.scouting.scraper.Scraper;
import com.hflrobotics.scouting.scraper.Team;
import com.hflrobotics.scouting.tablets.ConfigWriter;
import com.hflrobotics.scouting.tablets.TabletManager;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import java.awt.SystemColor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextPane;

public class GUI extends JFrame
{
	JTextField matchDataFile;
	JTextField pitDataFile;
	JTextField driverDataFile;
	JTextField teamDataFile;
	JProgressBar transferProgress;
	JButton btnSubmit;
	JButton btnStop;
	JButton btnClear;
	JList currentDataList;
	JComboBox<Webcam> cameraSelector;
	private Extractor extractor;
	private JButton matchDataBtn;
	private JFrame frame;
	
	private File currentDirectory = null;
	public JTable scheduleTable;
	
	public GUI() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Extractor");
		setSize(500, 300);
		setResizable(false);
		setVisible(true);
		frame = this;
		
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
		}
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnBlueAlliance = new JMenu("Blue Alliance");
		menuBar.add(mnBlueAlliance);
		
		JMenuItem mnBlueAllianceScrape = new JMenuItem("Scrape");
		mnBlueAllianceScrape.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Enter in event key [YYYY(EVENT_CODE)]",
	                    "Blue Alliance Scraper",
	                    JOptionPane.PLAIN_MESSAGE);
				
				try
				{
					ArrayList<Team> teams = new ArrayList<Team>();
					Scraper.getOPRs(s, teams);
					Scraper.getRankings(s, teams);
					ArrayList<String[]> data = Scraper.getCSVWriteableData(teams);
					FileInterface.writeAllData(teamDataFile.getText(), data);
					
					JOptionPane.showMessageDialog(null, "Data retrieved, file written to.");
				}
				catch(IOException ex)
				{
					JOptionPane.showMessageDialog(null, "Error in connection, file, or event key.");
				}
			}
		});
		mnBlueAllianceScrape.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		mnBlueAlliance.add(mnBlueAllianceScrape);
		
		JMenu mnSchedule = new JMenu("Schedule");
		menuBar.add(mnSchedule);
		
		JMenuItem mnScheduleSetFile = new JMenuItem("Set File");
		mnScheduleSetFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Schedule.loadSchedule(chooseFile());
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(null, "Error in reading file.");
				}
			}
		});
		mnScheduleSetFile.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));
		mnSchedule.add(mnScheduleSetFile);
		
		JMenu mnTablets = new JMenu("Tablets");
		mnTablets.setEnabled(false);
		menuBar.add(mnTablets);
		
		JMenuItem mnTabletsAdd = new JMenuItem("Add");
		mnTabletsAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] s = ((String)JOptionPane.showInputDialog(
	                    null,
	                    "Enter: [ADDRESS,ID]",
	                    "Add tablet.",
	                    JOptionPane.PLAIN_MESSAGE)).split(",");
				
				TabletManager.addTablet(s[0], s[1]);
			}
		});
		mnTablets.add(mnTabletsAdd);
		
		JMenuItem mnTabletsDelete = new JMenuItem("Delete");
		mnTabletsDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Enter tablet ID",
	                    "Remove tablet.",
	                    JOptionPane.PLAIN_MESSAGE);
				
				TabletManager.removeTablet(s);
			}
		});
		mnTablets.add(mnTabletsDelete);
		
		JMenuItem mnTabletsSetTeam = new JMenuItem("Set Team");
		mnTabletsSetTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] s = ((String)JOptionPane.showInputDialog(
	                    null,
	                    "Enter: [ID,TEAM]",
	                    "Set team.",
	                    JOptionPane.PLAIN_MESSAGE)).split(",");
				
				TabletManager.setTeam(s[0], s[1]);
			}
		});
		mnTablets.add(mnTabletsSetTeam);
		
		JMenuItem mnTabletsSetFile = new JMenuItem("Set File");
		mnTabletsSetFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TabletManager.tabletDataFile = chooseFile();
			}
		});
		mnTablets.add(mnTabletsSetFile);
		
		JMenuItem mntmLoadFile = new JMenuItem("Load File");
		mntmLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TabletManager.loadFile(chooseFile());
			}
		});
		mnTablets.add(mntmLoadFile);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel dataTransferPanel = new JPanel();
		tabbedPane.addTab("Data Transfer", null, dataTransferPanel, null);
		SpringLayout sl_dataTransferPanel = new SpringLayout();
		dataTransferPanel.setLayout(sl_dataTransferPanel);
		
		JScrollPane currentDataListScroll = new JScrollPane();
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, currentDataListScroll, 10, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, currentDataListScroll, 10, SpringLayout.WEST, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, currentDataListScroll, 212, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, currentDataListScroll, 240, SpringLayout.WEST, dataTransferPanel);
		currentDataListScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		currentDataListScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		dataTransferPanel.add(currentDataListScroll);
		
		currentDataList = new JList();
		currentDataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		currentDataListScroll.setViewportView(currentDataList);
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, currentDataList, 10, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, currentDataList, 197, SpringLayout.WEST, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, currentDataList, 159, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, currentDataList, -129, SpringLayout.EAST, dataTransferPanel);
		currentDataList.setBackground(SystemColor.info);
		
		JSeparator dataTransferSep = new JSeparator();
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, dataTransferSep, 10, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, dataTransferSep, 6, SpringLayout.EAST, currentDataListScroll);
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, dataTransferSep, 11, SpringLayout.SOUTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, dataTransferSep, -242, SpringLayout.EAST, dataTransferPanel);
		dataTransferSep.setOrientation(SwingConstants.VERTICAL);
		dataTransferPanel.add(dataTransferSep);
		
		JLabel lblCamera = new JLabel("Camera:");
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, lblCamera, 12, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, lblCamera, 253, SpringLayout.WEST, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, lblCamera, 47, SpringLayout.EAST, dataTransferSep);
		dataTransferPanel.add(lblCamera);
		
		cameraSelector = new JComboBox();
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, cameraSelector, 32, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, lblCamera, -6, SpringLayout.NORTH, cameraSelector);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, cameraSelector, 6, SpringLayout.EAST, dataTransferSep);
		cameraSelector.setModel(new DefaultComboBoxModel(Webcam.getWebcams().toArray()));
		dataTransferPanel.add(cameraSelector);
		
		transferProgress = new JProgressBar();
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, transferProgress, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, transferProgress, -10, SpringLayout.EAST, dataTransferPanel);
		transferProgress.setIndeterminate(true);
		dataTransferPanel.add(transferProgress);
		
		btnSubmit = new JButton("Submit");
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, btnSubmit, 0, SpringLayout.SOUTH, currentDataListScroll);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extractor.writeAllData();
			}
		});
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, btnSubmit, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, dataTransferPanel);
		btnSubmit.setEnabled(false);
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, btnSubmit, 74, SpringLayout.SOUTH, transferProgress);
		dataTransferPanel.add(btnSubmit);
		
		btnStop = new JButton("Stop");
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, btnStop, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, btnStop, -6, SpringLayout.NORTH, btnSubmit);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, btnStop, -10, SpringLayout.EAST, dataTransferPanel);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				extractor.state = "complete";
				btnStop.setEnabled(false);
				btnSubmit.setEnabled(true);
				btnClear.setEnabled(true);
			}
		});
		btnStop.setEnabled(false);
		dataTransferPanel.add(btnStop);
		
		btnClear = new JButton("Clear");
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, btnStop, 6, SpringLayout.SOUTH, btnClear);
		sl_dataTransferPanel.putConstraint(SpringLayout.SOUTH, transferProgress, -6, SpringLayout.NORTH, btnClear);
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, btnClear, 102, SpringLayout.NORTH, dataTransferPanel);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, btnClear, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, btnClear, -10, SpringLayout.EAST, dataTransferPanel);
		btnClear.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				btnClear.setEnabled(false);
				btnStop.setEnabled(false);
				btnSubmit.setEnabled(false);
				transferProgress.setIndeterminate(true);
				currentDataList.setListData(new String[]{});
				
				extractor.state = "searching";
				extractor.data.clear();
				extractor.matchData.clear();
				extractor.pitData.clear();
				extractor.driverData.clear();
			}
		});
		btnClear.setEnabled(false);
		dataTransferPanel.add(btnClear);
		
		JButton btnNewButton = new JButton("\u21BB");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCameraList();
			}
		});
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, transferProgress, 4, SpringLayout.SOUTH, btnNewButton);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, cameraSelector, -6, SpringLayout.WEST, btnNewButton);
		sl_dataTransferPanel.putConstraint(SpringLayout.NORTH, btnNewButton, -1, SpringLayout.NORTH, cameraSelector);
		sl_dataTransferPanel.putConstraint(SpringLayout.WEST, btnNewButton, -43, SpringLayout.EAST, transferProgress);
		sl_dataTransferPanel.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, transferProgress);
		dataTransferPanel.add(btnNewButton);
		
		JPanel dataFilesPanel = new JPanel();
		tabbedPane.addTab("Data Files", null, dataFilesPanel, null);
		SpringLayout sl_dataFilesPanel = new SpringLayout();
		dataFilesPanel.setLayout(sl_dataFilesPanel);
		
		JLabel lblMatchDatacsv = new JLabel("Match Data [CSV]");
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, lblMatchDatacsv, 10, SpringLayout.NORTH, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, lblMatchDatacsv, 10, SpringLayout.WEST, dataFilesPanel);
		dataFilesPanel.add(lblMatchDatacsv);
		
		matchDataFile = new JTextField();
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, matchDataFile, 6, SpringLayout.SOUTH, lblMatchDatacsv);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, matchDataFile, 10, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.SOUTH, matchDataFile, -169, SpringLayout.SOUTH, dataFilesPanel);
		matchDataFile.setEditable(false);
		dataFilesPanel.add(matchDataFile);
		matchDataFile.setColumns(10);
		
		matchDataBtn = new JButton("...");
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, matchDataBtn, 412, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, matchDataBtn, -10, SpringLayout.EAST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, matchDataFile, -6, SpringLayout.WEST, matchDataBtn);
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, matchDataBtn, 0, SpringLayout.NORTH, matchDataFile);
		matchDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFile(matchDataFile);
			}
		});
		
		dataFilesPanel.add(matchDataBtn);
		
		JLabel lblNewLabel = new JLabel("Pit Data [CSV]");
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, lblNewLabel, 6, SpringLayout.SOUTH, matchDataFile);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, lblMatchDatacsv);
		dataFilesPanel.add(lblNewLabel);
		
		pitDataFile = new JTextField();
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, pitDataFile, 6, SpringLayout.SOUTH, lblNewLabel);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, pitDataFile, 10, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.SOUTH, pitDataFile, -120, SpringLayout.SOUTH, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, pitDataFile, 0, SpringLayout.EAST, matchDataFile);
		pitDataFile.setEditable(false);
		dataFilesPanel.add(pitDataFile);
		pitDataFile.setColumns(10);
		
		JButton pitDataBtn = new JButton("...");
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, pitDataBtn, 0, SpringLayout.NORTH, pitDataFile);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, pitDataBtn, 412, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, pitDataBtn, 0, SpringLayout.EAST, matchDataBtn);
		pitDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFile(pitDataFile);
			}
		});
		dataFilesPanel.add(pitDataBtn);
		
		JLabel lblDriverDatacsv = new JLabel("Driver Data [CSV]");
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, lblDriverDatacsv, 6, SpringLayout.SOUTH, pitDataFile);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, lblDriverDatacsv, 0, SpringLayout.EAST, lblMatchDatacsv);
		dataFilesPanel.add(lblDriverDatacsv);
		
		driverDataFile = new JTextField();
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, driverDataFile, 6, SpringLayout.SOUTH, lblDriverDatacsv);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, driverDataFile, 10, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.SOUTH, driverDataFile, -71, SpringLayout.SOUTH, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, driverDataFile, 0, SpringLayout.EAST, matchDataFile);
		driverDataFile.setEditable(false);
		dataFilesPanel.add(driverDataFile);
		driverDataFile.setColumns(10);
		
		JButton driverDataBtn = new JButton("...");
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, driverDataBtn, 26, SpringLayout.SOUTH, pitDataBtn);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, driverDataBtn, 412, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, driverDataBtn, 0, SpringLayout.EAST, matchDataBtn);
		driverDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseFile(driverDataFile);
			}
		});
		dataFilesPanel.add(driverDataBtn);
		
		JLabel lblTeamDatacsv = new JLabel("Team Data [CSV]");
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, lblTeamDatacsv, 6, SpringLayout.SOUTH, driverDataFile);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, lblTeamDatacsv, 0, SpringLayout.WEST, lblMatchDatacsv);
		dataFilesPanel.add(lblTeamDatacsv);
		
		teamDataFile = new JTextField();
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, teamDataFile, 6, SpringLayout.SOUTH, lblTeamDatacsv);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, teamDataFile, 10, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.SOUTH, teamDataFile, -22, SpringLayout.SOUTH, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, teamDataFile, 0, SpringLayout.EAST, matchDataFile);
		teamDataFile.setEditable(false);
		teamDataFile.setColumns(10);
		dataFilesPanel.add(teamDataFile);
		
		JButton teamDataBtn = new JButton("...");
		sl_dataFilesPanel.putConstraint(SpringLayout.SOUTH, driverDataBtn, -26, SpringLayout.NORTH, teamDataBtn);
		sl_dataFilesPanel.putConstraint(SpringLayout.NORTH, teamDataBtn, 0, SpringLayout.NORTH, teamDataFile);
		sl_dataFilesPanel.putConstraint(SpringLayout.WEST, teamDataBtn, 412, SpringLayout.WEST, dataFilesPanel);
		sl_dataFilesPanel.putConstraint(SpringLayout.EAST, teamDataBtn, 0, SpringLayout.EAST, matchDataBtn);
		teamDataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFile(teamDataFile);
			}
		});
		dataFilesPanel.add(teamDataBtn);
		
		JPanel managerPanel = new JPanel();
		tabbedPane.addTab("Manager", null, managerPanel, null);
		SpringLayout sl_managerPanel = new SpringLayout();
		managerPanel.setLayout(sl_managerPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_managerPanel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, managerPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		managerPanel.add(scrollPane);
		
		scheduleTable = new JTable();
		scrollPane.setViewportView(scheduleTable);
		sl_managerPanel.putConstraint(SpringLayout.NORTH, scheduleTable, 10, SpringLayout.NORTH, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.WEST, scheduleTable, 56, SpringLayout.WEST, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.SOUTH, scheduleTable, 212, SpringLayout.NORTH, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.EAST, scheduleTable, -291, SpringLayout.EAST, managerPanel);
		scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scheduleTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"M", "R1", "R2", "R3", "B1", "B2", "B3"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scheduleTable.getColumnModel().getColumn(0).setResizable(false);
		scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		scheduleTable.getColumnModel().getColumn(0).setMinWidth(30);
		scheduleTable.getColumnModel().getColumn(0).setMaxWidth(30);
		scheduleTable.getColumnModel().getColumn(1).setResizable(false);
		scheduleTable.getColumnModel().getColumn(2).setResizable(false);
		scheduleTable.getColumnModel().getColumn(3).setResizable(false);
		scheduleTable.getColumnModel().getColumn(4).setResizable(false);
		scheduleTable.getColumnModel().getColumn(5).setResizable(false);
		scheduleTable.getColumnModel().getColumn(6).setResizable(false);
		
		JLabel lblSchedule = new JLabel("Match Schedule:");
		sl_managerPanel.putConstraint(SpringLayout.NORTH, lblSchedule, 10, SpringLayout.NORTH, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.WEST, lblSchedule, 10, SpringLayout.WEST, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, lblSchedule);
		managerPanel.add(lblSchedule);
		
		JLabel lblTablets = new JLabel("Team:");
		sl_managerPanel.putConstraint(SpringLayout.NORTH, lblTablets, 0, SpringLayout.NORTH, lblSchedule);
		sl_managerPanel.putConstraint(SpringLayout.EAST, lblTablets, -57, SpringLayout.EAST, managerPanel);
		managerPanel.add(lblTablets);
		
		JComboBox configTeam = new JComboBox();
		sl_managerPanel.putConstraint(SpringLayout.NORTH, configTeam, 0, SpringLayout.NORTH, scrollPane);
		sl_managerPanel.putConstraint(SpringLayout.WEST, configTeam, 0, SpringLayout.WEST, lblTablets);
		sl_managerPanel.putConstraint(SpringLayout.EAST, configTeam, -8, SpringLayout.EAST, managerPanel);
		configTeam.setModel(new DefaultComboBoxModel(new String[] {"red1", "red2", "red3", "blue1", "blue2", "blue3"}));
		configTeam.setMaximumRowCount(6);
		managerPanel.add(configTeam);
		
		JLabel lblCurrentMatchBeing = new JLabel("Current Match Being Played:");
		sl_managerPanel.putConstraint(SpringLayout.NORTH, lblCurrentMatchBeing, 0, SpringLayout.NORTH, lblSchedule);
		sl_managerPanel.putConstraint(SpringLayout.WEST, lblCurrentMatchBeing, 169, SpringLayout.EAST, lblSchedule);
		managerPanel.add(lblCurrentMatchBeing);
		
		JSpinner configCurrentMatch = new JSpinner();
		sl_managerPanel.putConstraint(SpringLayout.EAST, scrollPane, -27, SpringLayout.WEST, configCurrentMatch);
		sl_managerPanel.putConstraint(SpringLayout.NORTH, configCurrentMatch, 0, SpringLayout.NORTH, scrollPane);
		sl_managerPanel.putConstraint(SpringLayout.WEST, configCurrentMatch, 0, SpringLayout.WEST, lblCurrentMatchBeing);
		sl_managerPanel.putConstraint(SpringLayout.EAST, configCurrentMatch, -93, SpringLayout.EAST, managerPanel);
		configCurrentMatch.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		managerPanel.add(configCurrentMatch);
		
		JTextPane configPitList = new JTextPane();
		sl_managerPanel.putConstraint(SpringLayout.EAST, configPitList, 0, SpringLayout.EAST, lblCurrentMatchBeing);
		sl_managerPanel.putConstraint(SpringLayout.WEST, configPitList, 258, SpringLayout.WEST, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.SOUTH, configPitList, 0, SpringLayout.SOUTH, scrollPane);
		managerPanel.add(configPitList);
		
		JSeparator separator = new JSeparator();
		sl_managerPanel.putConstraint(SpringLayout.NORTH, separator, 6, SpringLayout.SOUTH, configCurrentMatch);
		sl_managerPanel.putConstraint(SpringLayout.WEST, separator, 27, SpringLayout.EAST, scrollPane);
		sl_managerPanel.putConstraint(SpringLayout.EAST, separator, -10, SpringLayout.EAST, managerPanel);
		managerPanel.add(separator);
		
		JLabel lblPitList = new JLabel("Pit List:");
		sl_managerPanel.putConstraint(SpringLayout.SOUTH, separator, -6, SpringLayout.NORTH, lblPitList);
		sl_managerPanel.putConstraint(SpringLayout.NORTH, lblPitList, 64, SpringLayout.NORTH, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.NORTH, configPitList, 6, SpringLayout.SOUTH, lblPitList);
		sl_managerPanel.putConstraint(SpringLayout.WEST, lblPitList, 27, SpringLayout.EAST, scrollPane);
		sl_managerPanel.putConstraint(SpringLayout.EAST, lblPitList, -196, SpringLayout.EAST, managerPanel);
		managerPanel.add(lblPitList);
		
		JLabel lblNewLabel_1 = new JLabel("Write to File");
		sl_managerPanel.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 0, SpringLayout.NORTH, lblPitList);
		sl_managerPanel.putConstraint(SpringLayout.WEST, lblNewLabel_1, 0, SpringLayout.WEST, lblTablets);
		sl_managerPanel.putConstraint(SpringLayout.EAST, lblNewLabel_1, -29, SpringLayout.EAST, managerPanel);
		managerPanel.add(lblNewLabel_1);
		
		JButton configWriteBtn = new JButton(">>");
		configWriteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConfigWriter.writeToConfig(chooseJSONFile(), configCurrentMatch, configTeam, configPitList);
			}
		});
		sl_managerPanel.putConstraint(SpringLayout.NORTH, configWriteBtn, 6, SpringLayout.SOUTH, lblNewLabel_1);
		sl_managerPanel.putConstraint(SpringLayout.WEST, configWriteBtn, 0, SpringLayout.WEST, lblTablets);
		sl_managerPanel.putConstraint(SpringLayout.SOUTH, configWriteBtn, -10, SpringLayout.SOUTH, managerPanel);
		sl_managerPanel.putConstraint(SpringLayout.EAST, configWriteBtn, -10, SpringLayout.EAST, managerPanel);
		managerPanel.add(configWriteBtn);
	}

	
	private String chooseJSONFile()
	{
		final JFileChooser fc = new JFileChooser(currentDirectory);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileNameExtensionFilter("JSON file (*.json)", "json"));
		
		if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile().toString();
		}
		
		return "";
	}
	
	private String chooseFile()
	{
		final JFileChooser fc = new JFileChooser(currentDirectory);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileNameExtensionFilter("Comma-separated values file (*.csv)", "csv"));
		
		if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile().toString();
		}
		
		return "";
	}
	
	
	private void chooseFile(JTextField field)
	{
		final JFileChooser fc = new JFileChooser(currentDirectory);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileNameExtensionFilter("Comma-separated values file (*.csv)", "csv"));
		
		if(fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
		{
			field.setText(fc.getSelectedFile().toString());
			currentDirectory = fc.getSelectedFile().getParentFile();
		}
	}
	
	
	public void updateCameraList()
	{
		cameraSelector.setModel(new DefaultComboBoxModel<Webcam>((Webcam[]) Webcam.getWebcams().toArray()));
	}
	
	public void passExtractor(Extractor extractor)
	{
		this.extractor = extractor;
	}
}
