package final_20211216;
// 21 Fall Introduction to JAVA Final Project of zh2441, ly2173

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Main extends JFrame {
	
	JMenuBar menu;
	JPanel mainPanel, upperPanel, boardPanel;
	static JLabel status;
	static DataServer server;
	GridPanel grid1, grid2, grid3;

	
	public Main() {
		// TODO Auto-generated constructor stub
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.LIGHT_GRAY);
		setupPanels();
		setSize(480, 220);
		setVisible(true);
		
		server= new DataServer(grid1, grid2, grid3);
		grid1.setServer(server);
		grid2.setServer(server);
		grid3.setServer(server);
		
		Thread t = new Thread(server);
		Thread t1 = new Thread(grid1);
		Thread t2 = new Thread(grid2);
		Thread t3 = new Thread(grid3);
		
		t.start();
		t1.start();
		t2.start();
		t3.start();
	}

	private void setupPanels() {
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(0,3,5,5));
		boardPanel.setPreferredSize(new Dimension(470, 162));
		
		upperPanel = new JPanel(new GridLayout(2,1));
		createMenuBar();
		status = new JLabel("New game, start from O.");
		upperPanel.add(menu);
		upperPanel.add(status);
		
		grid1 = new GridPanel(1);
		grid2 = new GridPanel(2);
		grid3 = new GridPanel(3);
		
		mainPanel.add(upperPanel, BorderLayout.NORTH);
		boardPanel.add(grid1);
		boardPanel.add(grid2);
		boardPanel.add(grid3);
		mainPanel.add(boardPanel, BorderLayout.CENTER);
		
		this.add(mainPanel);
	}
	
	public void createMenuBar(){
		JMenu menuFile;
		JMenuItem menuNewItem;
		JMenuItem menuLoadItem;
		JMenuItem menuSaveItem;
		JMenuItem menuExitItem;
		menu = new JMenuBar();
		menuNewItem = new JMenuItem("New");
		menuNewItem.addActionListener((e)->this.restart());			
		menuLoadItem = new JMenuItem("Load");
		menuLoadItem.addActionListener((e)->this.load());			
		menuSaveItem = new JMenuItem("Save");
		menuSaveItem.addActionListener((e)->this.save());				
		menuExitItem = new JMenuItem("Exit");
		menuExitItem.addActionListener((e)->System.exit(0));
		menuFile = new JMenu("Menu");
		menuFile.add(menuNewItem);
		menuFile.add(menuLoadItem);
		menuFile.add(menuSaveItem);
		menuFile.add(menuExitItem);
		menu.add(menuFile);
	}

	public void save() {
		if(server.getStatusCode()!=0) server.save();
		else System.out.println("You cannot save an end game!");
	
		
	}
	
	public void load() {
		server.load();
		grid1.refreshButtonImage();
		grid2.refreshButtonImage();
		grid3.refreshButtonImage();
		refreshStatus();
		
	}
	
	public void restart() {	
		server.resetData();
		status.setText("New game, start from O.");
	}
	
	static public void refreshStatus() {
		
		if(server.getStatusCode()==1) status.setText("Now, it is O's round.");
		else if(server.getStatusCode()==-1) status.setText("Now, it is X's round.");
		else {
			if(server.getWinner()==1) status.setText("O is the winner!");
			else status.setText("X is the winner!");
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();

	}

}

