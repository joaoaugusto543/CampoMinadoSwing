package view;

import javax.swing.JFrame;

import model.Board;

@SuppressWarnings("serial")
public class MainScreen extends JFrame{
	
	public MainScreen() {
		
		Board board = new Board(16,30,50);
		
		add(new BoardPanel(board));
		
		setVisible(true);
		setTitle("Campo Minado");
		setSize(690,438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		
		new MainScreen();
		
	}
}
