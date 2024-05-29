package view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Board;

@SuppressWarnings("serial")
public class BoardPanel extends JPanel{

	public BoardPanel(Board board) {
		setLayout(new GridLayout(board.getLines(),board.getColumns()));
			
		board.forField(f -> add(new ButtonField(f)));
		
		board.registerObserver(e -> {
			
			SwingUtilities.invokeLater(() -> {
				if(e.isWon()) {
					JOptionPane.showMessageDialog(null, "Ganhou :)");
				}else {
					JOptionPane.showMessageDialog(null, "Perdeu :)");
				}
				
				board.restart();
			});
			
			
			
		});
	}
	
}
