package final_20211216;
//21 Fall Introduction to JAVA Final Project of zh2441, ly2173

import java.sql.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataServer implements Runnable {
	
	int statusCode;	// 1: O's turn, -1: X's turn, 0: end
	int winner; // 1: O win, -1: X win, 0: unfinished
	static int [][] layer1, layer2, layer3;
	GridPanel grid1, grid2, grid3;
	
	Connection connection;
	Statement statement, loadStatement, saveStatement;
	PreparedStatement prepStatement;
	ResultSet result, data;
	

	public DataServer() {
		statusCode=1;
		winner=0;
		setupDatabase();
	}
	
	public DataServer(GridPanel grid1, GridPanel grid2, GridPanel grid3) {
		// TODO Auto-generated constructor stub
		this();
		this.grid1 = grid1;
		this.grid2 = grid2;
		this.grid3 = grid3;
		
	}
	
	public void resetData() {
		statusCode = 1;
		winner = 0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				layer1[i][j] = 0;
				grid1.setButtonImage(i, j);
				layer2[i][j] = 0;
				grid2.setButtonImage(i, j);
				layer3[i][j] = 0;
				grid3.setButtonImage(i, j);
	
			}
		}
		
	}
	
	public void setupDatabase() {
		
		try {
		connection = DriverManager.getConnection ("jdbc:sqlite:layer.db");
		System.out.println("Database connected");
		
		statement = connection.createStatement();
		statement.execute("DELETE FROM layers");
		
		for(int i =0; i<9; i++) statement.execute("INSERT INTO layers VALUES(0,0,0)");
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);;
		}
		
	}
	
	public void save() {
		
		try {
			prepStatement = connection.prepareStatement("UPDATE layers SET layer1=?, layer2=?, layer3=? WHERE rowid=?");
			
			int rowid=1;
			for(int i = 0; i<3; i++) {
				for(int j = 0 ; j<3; j++) {
					
					prepStatement.setInt(1, layer1[i][j]);
					prepStatement.setInt(2, layer2[i][j]);
					prepStatement.setInt(3, layer3[i][j]);
					prepStatement.setInt(4, rowid++);
					prepStatement.execute();
				}
			}
				
		} catch(SQLException e) {
			e.printStackTrace();
			System.exit(0);;
		}
		
		
		System.out.println("saved!");
	}
	
	
	public void load() {
		try {
			
			data = statement.executeQuery("SELECT * FROM layers");
			int i = 0;
			int j = 0;
			int sum = 0;
			while(data.next()) {
				
				layer1[i][j] = data.getInt(1);
				layer2[i][j] = data.getInt(2);
				layer3[i][j] = data.getInt(3);
				
				j++;
				if(j>2) {
					j=0;
					i++;
				}
				if(i>2) break;
				
				sum+=data.getInt(1)+data.getInt(2)+data.getInt(3);
				
			}
			
			if(sum==0) statusCode = 1;
			else statusCode = -1;
			
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.exit(0);;
		}
		
	}
	
	
	public int[][] getLayer1() {
		return layer1;
	}
	
	public int[][] getLayer2() {
		return layer2;
	}
	
	public int[][] getLayer3() {
		return layer3;
	}	
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Data server is running...");
		
		while(true) {		
			layer1=grid1.getData();
			layer2=grid2.getData();
			layer3=grid3.getData();			
		}

	}
	
}
