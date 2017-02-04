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
	
	public GUI() 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		
		JMenu mnTablets = new JMenu("Tablets");
		menuBar.add(mnTablets);
		
		JMenuItem mnTabletsAdd = new JMenuItem("Add");
		mnTablets.add(mnTabletsAdd);
		
		JMenuItem mnTabletsDelete = new JMenuItem("Delete");
		mnTablets.add(mnTabletsDelete);
		
		JMenuItem mnTabletsSetTeam = new JMenuItem("Set Team");
		mnTablets.add(mnTabletsSetTeam);
		
		JMenu mnSchedule = new JMenu("Schedule");
		menuBar.add(mnSchedule);
		
		JMenuItem mnScheduleSetFile = new JMenuItem("Set File");
		mnScheduleSetFile.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));
		mnSchedule.add(mnScheduleSetFile);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel dataTransfer = new JPanel();
		tabbedPane.addTab("Data Transfer", null, dataTransfer, null);
		SpringLayout sl_dataTransfer = new SpringLayout();
		dataTransfer.setLayout(sl_dataTransfer);
		
		JScrollPane currentDataListScroll = new JScrollPane();
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, currentDataListScroll, 10, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, currentDataListScroll, 10, SpringLayout.WEST, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, currentDataListScroll, 212, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, currentDataListScroll, 240, SpringLayout.WEST, dataTransfer);
		currentDataListScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		currentDataListScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		dataTransfer.add(currentDataListScroll);
		
		currentDataList = new JList();
		currentDataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		currentDataListScroll.setViewportView(currentDataList);
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, currentDataList, 10, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, currentDataList, 197, SpringLayout.WEST, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, currentDataList, 159, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, currentDataList, -129, SpringLayout.EAST, dataTransfer);
		currentDataList.setBackground(SystemColor.info);
		
		JSeparator dataTransferSep = new JSeparator();
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, dataTransferSep, 10, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, dataTransferSep, 6, SpringLayout.EAST, currentDataListScroll);
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, dataTransferSep, 11, SpringLayout.SOUTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, dataTransferSep, -242, SpringLayout.EAST, dataTransfer);
		dataTransferSep.setOrientation(SwingConstants.VERTICAL);
		dataTransfer.add(dataTransferSep);
		
		JLabel lblCamera = new JLabel("Camera:");
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, lblCamera, 12, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, lblCamera, 253, SpringLayout.WEST, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, lblCamera, 47, SpringLayout.EAST, dataTransferSep);
		dataTransfer.add(lblCamera);
		
		cameraSelector = new JComboBox();
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, cameraSelector, 32, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, lblCamera, -6, SpringLayout.NORTH, cameraSelector);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, cameraSelector, 6, SpringLayout.EAST, dataTransferSep);
		cameraSelector.setModel(new DefaultComboBoxModel(Webcam.getWebcams().toArray()));
		dataTransfer.add(cameraSelector);
		
		transferProgress = new JProgressBar();
		sl_dataTransfer.putConstraint(SpringLayout.WEST, transferProgress, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, transferProgress, -10, SpringLayout.EAST, dataTransfer);
		transferProgress.setIndeterminate(true);
		dataTransfer.add(transferProgress);
		
		btnSubmit = new JButton("Submit");
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, btnSubmit, 0, SpringLayout.SOUTH, currentDataListScroll);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extractor.writeAllData();
			}
		});
		sl_dataTransfer.putConstraint(SpringLayout.WEST, btnSubmit, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, dataTransfer);
		btnSubmit.setEnabled(false);
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, btnSubmit, 74, SpringLayout.SOUTH, transferProgress);
		dataTransfer.add(btnSubmit);
		
		btnStop = new JButton("Stop");
		sl_dataTransfer.putConstraint(SpringLayout.WEST, btnStop, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, btnStop, -6, SpringLayout.NORTH, btnSubmit);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, btnStop, -10, SpringLayout.EAST, dataTransfer);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				extractor.state = "complete";
				btnStop.setEnabled(false);
				btnSubmit.setEnabled(true);
				btnClear.setEnabled(true);
			}
		});
		btnStop.setEnabled(false);
		dataTransfer.add(btnStop);
		
		btnClear = new JButton("Clear");
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, btnStop, 6, SpringLayout.SOUTH, btnClear);
		sl_dataTransfer.putConstraint(SpringLayout.SOUTH, transferProgress, -6, SpringLayout.NORTH, btnClear);
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, btnClear, 102, SpringLayout.NORTH, dataTransfer);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, btnClear, 6, SpringLayout.EAST, dataTransferSep);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, btnClear, -10, SpringLayout.EAST, dataTransfer);
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
		dataTransfer.add(btnClear);
		
		JButton btnNewButton = new JButton("\u21BB");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCameraList();
			}
		});
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, transferProgress, 4, SpringLayout.SOUTH, btnNewButton);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, cameraSelector, -6, SpringLayout.WEST, btnNewButton);
		sl_dataTransfer.putConstraint(SpringLayout.NORTH, btnNewButton, -1, SpringLayout.NORTH, cameraSelector);
		sl_dataTransfer.putConstraint(SpringLayout.WEST, btnNewButton, -43, SpringLayout.EAST, transferProgress);
		sl_dataTransfer.putConstraint(SpringLayout.EAST, btnNewButton, 0, SpringLayout.EAST, transferProgress);
		dataTransfer.add(btnNewButton);
		
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
