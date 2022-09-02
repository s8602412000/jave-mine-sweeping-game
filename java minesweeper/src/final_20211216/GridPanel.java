package final_20211216;
//21 Fall Introduction to JAVA Final Project of zh2441, ly2173

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GridPanel extends JPanel implements Runnable{

	int ROW = 3;
	int COLUMN = 3;
	int statusCode;
	int layerNumber;
	int[][] data;
	int[][] other1;
	int[][] other2;
	DataServer server;
	
	JButton [][] button;
	MouseListener buttonListener = new ButtonListener();
	
	class ButtonListener implements MouseListener{
		JButton buttonClicked;
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			buttonClicked = (JButton) e.getSource();
			for(int i=0;i<ROW;i++) {
				for(int j=0;j<COLUMN;j++) {
					if(buttonClicked==button[i][j]) {
						//left click;
						if (e.getButton()==MouseEvent.BUTTON1) {
							//read();
							if(data[i][j]==0) mark(i,j);
							//write();
						}
						//right click;
						if (e.getButton()==MouseEvent.BUTTON3) {
							
						}
					}
				}
			}
			
		}


		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			//read();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			//write();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public GridPanel() {
		// TODO Auto-generated constructor stub
		initialize();
		setLayout(new GridLayout(0,3,1,1));
		
		for(int i=0;i<ROW;i++) {
			for(int j=0;j<COLUMN;j++) {
				add(button[i][j]);
			}
		}

		setSize(154,162);
	}
	
	public GridPanel(int number) {
		this();
		layerNumber = number;
	}
	
	public void setServer(DataServer server) {
		this.server = server;
	}

	private void initialize() {

		button = new JButton[ROW][COLUMN];
		data = new int[ROW][COLUMN];
		
		for(int i=0;i<ROW;i++) {
			for(int j=0;j<COLUMN;j++) {
				data[i][j] = 0;
				JButton newButton = new JButton(new ImageIcon(data[i][j]+".png"));
				newButton.addMouseListener(buttonListener);
				button[i][j]=newButton;
			}
		}
	}
	
	private void mark(int i, int j) {
		// TODO Auto-generated method stub
		statusCode = server.getStatusCode();
		
		if(statusCode==1) {
			data[i][j]=1;
			statusCode=-1;
		}else if(statusCode==-1) {
			data[i][j]=-1;
			statusCode=1;
		}else return;
						
		setButtonImage(i, j);
		server.setStatusCode(statusCode);

		check(i, j);
		Main.refreshStatus();
	}
	
	public void refreshButtonImage() {
		for(int i=0; i<ROW; i++) {
			for(int j=0; j<COLUMN; j++) {
				setButtonImage(i,j);
			}
		}
	}
	
	
	public void setButtonImage(int i, int j) {
		button[i][j].setIcon(new ImageIcon(data[i][j]+".png"));
	}
	
	
	
	public void check(int i, int j) {
		System.out.println("Layer"+ layerNumber +": ("+i+", "+j+") = "+data[i][j]);
		
		int now = data[i][j];
		boolean end = false;
		
		// front diagonal
		if(data[0][0]==now && data[1][1]==now && data[2][2]==now) end = true;
		if(data[2][0]==now && data[1][1]==now && data[0][2]==now) end = true;
		
		// front vertical
		if(data[0][0]==now && data[1][0]==now && data[2][0]==now) end = true;
		if(data[0][1]==now && data[1][1]==now && data[2][1]==now) end = true;
		if(data[0][2]==now && data[1][2]==now && data[2][2]==now) end = true;
		
		// front horizontal
		if(data[0][0]==now && data[0][1]==now && data[0][2]==now) end = true;
		if(data[1][0]==now && data[1][1]==now && data[1][2]==now) end = true;
		if(data[2][0]==now && data[2][1]==now && data[2][2]==now) end = true;
		
		// cube diagonal
		if(firstLayer(0,0)==now && secondLayer(1,1)==now && thirdLayer(2,2)==now) end = true;
		if(firstLayer(0,2)==now && secondLayer(1,1)==now && thirdLayer(2,0)==now) end = true;
		if(firstLayer(2,0)==now && secondLayer(1,1)==now && thirdLayer(0,2)==now) end = true;
		if(firstLayer(2,2)==now && secondLayer(1,1)==now && thirdLayer(0,0)==now) end = true;
		
		// side diagonal
		if(firstLayer(0,0)==now && secondLayer(1,0)==now && thirdLayer(2,0)==now) end = true;
		if(firstLayer(0,1)==now && secondLayer(1,1)==now && thirdLayer(2,1)==now) end = true;
		if(firstLayer(0,2)==now && secondLayer(1,2)==now && thirdLayer(2,2)==now) end = true;
		if(firstLayer(2,0)==now && secondLayer(1,0)==now && thirdLayer(0,0)==now) end = true;
		if(firstLayer(2,1)==now && secondLayer(1,1)==now && thirdLayer(0,1)==now) end = true;
		if(firstLayer(2,2)==now && secondLayer(1,2)==now && thirdLayer(0,2)==now) end = true;
		
		// top diagonal
		if(firstLayer(0,0)==now && secondLayer(0,1)==now && thirdLayer(0,2)==now) end = true;
		if(firstLayer(1,0)==now && secondLayer(1,1)==now && thirdLayer(1,2)==now) end = true;
		if(firstLayer(2,0)==now && secondLayer(2,1)==now && thirdLayer(2,2)==now) end = true;
		if(firstLayer(0,2)==now && secondLayer(0,1)==now && thirdLayer(0,0)==now) end = true;
		if(firstLayer(1,2)==now && secondLayer(1,1)==now && thirdLayer(1,0)==now) end = true;
		if(firstLayer(2,2)==now && secondLayer(2,1)==now && thirdLayer(2,0)==now) end = true;
		
		// thru
		if(firstLayer(i,j)==now && secondLayer(i,j)==now && thirdLayer(i,j)==now) end = true;
	
		if(end) {
			server.setStatusCode(0);
			server.setWinner(now);
		}
		
	}
	
	public int firstLayer(int i, int j) {
		if(layerNumber==1) return data[i][j];
		else if(layerNumber==2) return other1[i][j];
		else return other1[i][j];
	}
	
	public int secondLayer(int i, int j) {
		if(layerNumber==1) return other1[i][j];
		if(layerNumber==2) return data[i][j];
		else return other2[i][j];
	}
	
	public int thirdLayer(int i, int j) {
		if(layerNumber==1) return other2[i][j];
		else if(layerNumber==2) return other2[i][j];
		else return data[i][j];
	}
	
	public int[][] getData() {
		return data;
	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Thread"+ layerNumber +" is running...");
		
		while(true) {
		
			if(layerNumber==1) {
				other1=server.getLayer2();//DataServer.layer2;
				other2=server.getLayer3();//DataServer.layer3;
			}else if(layerNumber==2) {
				other1=server.getLayer1();//DataServer.layer1;
				other2=server.getLayer3();//DataServer.layer3;
			}else {
				other1=server.getLayer1();//DataServer.layer1;
				other2=server.getLayer2();//DataServer.layer2;
			}
		
		}
				
	}

}
