package view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import model.Field;
import model.FieldEvent;
import model.FieldObserver;

@SuppressWarnings("serial")
public class ButtonField extends JButton implements FieldObserver,MouseListener {
	
	private Field field;
	private final Color BG_DEFAULT = new Color(184,184,184);
	private final Color BG_MARKED = new Color(8,179,247);
	private final Color BG_EXPLODED = new Color(189,66,68);
	private final Color TEXT_GREEN = new Color(0,100,0);
	
	public ButtonField(Field field) {
		
		this.field = field;
		
		setBorder(BorderFactory.createBevelBorder(0));
		
		setBackground(BG_DEFAULT);
		
		
		setOpaque(true);
		
		addMouseListener(this);
		
		field.registerObserver(this);
		
	}

	
	private void aplicationStylesDefault() {
		setBackground(BG_DEFAULT);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
		
	}

	private void aplicationStylesExploded() {
		setBackground(BG_EXPLODED);
		setForeground(Color.WHITE);
		setText("X");
		
	}

	private void aplicationStylesMarked() {
		
		setBackground(BG_MARKED);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicationStylesOpen() {
		
		setBorder(BorderFactory.createLineBorder(Color.gray));
		
		if(field.isMined()) {
			setBackground(BG_EXPLODED);
			return;
		}
		
		setBackground(BG_DEFAULT);
		
		switch (field.minedNeighbors()) {
			case 1: 
				setForeground(TEXT_GREEN);
				break;
			case 2:
				setForeground(Color.BLUE);
				break;
			case 3:
				setForeground(Color.YELLOW);
				break;
			case 4:
			case 5:
			case 6:
				setForeground(Color.RED);
				break;
			default:
				setForeground(Color.PINK);	
		}
	
		String value = !field.safeNeighbors() ? Integer.toString(field.minedNeighbors()) : "";
		
		setText(value);
		
	}

	@Override
	public void event(Field field, FieldEvent event) {
		
		switch(event) {
			case ABRIR:
				aplicationStylesOpen();
				break;
			case MARCAR:
				aplicationStylesMarked();
				break;
			case EXPLODIR:
				aplicationStylesExploded();
				break;
			default:
				aplicationStylesDefault();
				break;
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.getButton() == 1) {
			field.open();
		}else {
			field.changeMarkedField();
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	
	

}
