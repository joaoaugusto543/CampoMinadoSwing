package model;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private boolean mined;
    private boolean open;

    private boolean flagField;

    private final int line;
    private final int column;

    private List<Field> neighbors = new ArrayList<>();
    private List<FieldObserver> observers = new ArrayList<>();

    public Field(int line, int column){
        this.line=line;
        this.column=column;
    }
    
    public void registerObserver(FieldObserver observer) {
    	observers.add(observer);
    }
    
    private void notifyObservers(FieldEvent event) {
    	observers.forEach(o -> o.event(this, event) );
    } 

    public boolean isMined() {
        return mined;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isFlagField() {
        return flagField;
    }

    public boolean addNeighbors(Field neighbor){

        boolean isDifferentLine = line != neighbor.line;
        boolean isDifferentColumn = column != neighbor.column;
        boolean isDiagonal = isDifferentLine && isDifferentColumn;

        int deltaLine = Math.abs(line - neighbor.line);
        int deltaColumn = Math.abs(column - neighbor.column);

        int generalDelta = deltaColumn + deltaLine;

        if(generalDelta == 1 && !isDiagonal){
            neighbors.add(neighbor);
            return true;
        }else if(generalDelta == 2 && isDiagonal){
            neighbors.add(neighbor);
            return true;
        }else{
            return false;
        }

    }

    public void setOpen(boolean open) {
        this.open = open;
        
        if(open) {
        	notifyObservers(FieldEvent.ABRIR);
        	return;
        }
    }

    public void changeMarkedField(){
    	
        if(!isOpen()){
        	
            flagField = !isFlagField();
            
            if(isFlagField()) {
            	                                                                                                 
            	notifyObservers(FieldEvent.MARCAR);                                                              
            	                                                                                                 
            }else {                                                                                              
            	                                                                                                 
            	notifyObservers(FieldEvent.DESMARCAR);                                                           
            	                                                                                                 
            }                                                                                                    
                                                                                                                 
        }                                                                                                        
    }                                                                                                            
                                                                                                                 
    public void undermine(){                                                                                     
        if(!mined){                                                                                              
            setMined(true);                                                                                     
        }                                                                                                        
    }                                                                                                            
                                                                                                                 
                                                                                                                 
                                                                                                                 
    public void setMined(boolean mined) {
		this.mined = mined;
	}

	public boolean open(){    
                                                                                                      
        if(!isOpen() && !isFlagField()){                                                                         
        	                                                                                                     
            if(isMined()){     
                notifyObservers(FieldEvent.EXPLODIR);                                                            
				return true;                                                                                     
            }                                                                                                    
                  
            setOpen(true);    
                                                                                                                             
            if(safeNeighbors()){                                                                                 
                neighbors.forEach(Field::open);                                                                  
            }                                                                                                    
                                                                                                                 
            return true;                                                                                         
        }else {                                                                                                  
        	return false;                                                                                        
		}                                                                                                        
                                                                                                                 
        
    }

    public boolean safeNeighbors(){

        return neighbors.stream().noneMatch(Field::isMined);

    }

    public int minedNeighbors(){
        return (int) neighbors.stream().filter((field) -> field.mined).count();
    }

    public void restart(){
        open=false;
        mined=false;
        flagField=false;
        notifyObservers(FieldEvent.RESTART);
    }



    boolean goalAchieved(){
        boolean unraveled = !mined && open;
        boolean safe = mined && flagField;
        return unraveled || safe;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}


