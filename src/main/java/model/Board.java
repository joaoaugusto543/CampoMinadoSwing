package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver{

    private int lines;
    private int columns;
    private int mines;
    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<ResultEvent>> observers = new ArrayList<>();

    public Board(int lines, int columns, int mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        gatherNeighbors();
        drawMines();
        
    }
    
    public void registerObserver(Consumer<ResultEvent> observer) {
    	
    	observers.add(observer);
    	
    }
    
    private void notifyObservers(Boolean result) {
    	
    	observers.forEach(o -> o.accept(new ResultEvent(result)));
    	
    } 

    private void drawMines() {

    	long armedMines = 0;

        Predicate<Field> mined = Field::isMined;

        do {

            int randow = (int) (Math.random() * fields.size());

            fields.get(randow).undermine();
            
            armedMines = fields.stream().filter(mined).count();

        }while (armedMines < mines);

    }

    private void gatherNeighbors() {

        for (Field f1: fields) {
            for (Field f2: fields) {
                f1.addNeighbors(f2);
            }
        }


    }
    
    public void forField(Consumer<Field> function) {
    	fields.forEach(function);
    }


    public int getLines() {
		return lines;
	}

	public int getColumns() {
		return columns;
	}

	private void generateFields() {

		for (int line = 0; line < lines; line++) {
			
			for (int column = 0; column < columns; column++) {
				
				Field field = new Field(line, column);
				
				field.registerObserver(this);
				
				fields.add(field);
			}
		}

    }

    public boolean goalAchieved(){
       return fields.stream().allMatch(Field::goalAchieved);
    }

    public void restart(){
    	fields.stream().forEach(c -> c.restart());
		drawMines();
    }

    public void open(int line, int column){

           fields.parallelStream()
                   .filter(f -> f.getLine() == line && f.getColumn() == column)
                   .findFirst()
                   .ifPresent(f-> f.isOpen());
 
    }
    
    private void showMines() {
    	 fields.stream()
    	 	.filter(c -> c.isMined())
    	 	.filter(f -> !f.isFlagField())
    	 	.forEach(f -> f.setOpen(true));
    }

    public void changeMarkedField(int line, int column){
        fields.parallelStream()
                .filter(f -> f.getLine() == line && f.getColumn() == column)
                .findFirst()
                .ifPresent(Field::changeMarkedField);

    }

	@Override
	public void event(Field field, FieldEvent event) {
		
		if(event == FieldEvent.EXPLODIR) {
			
			showMines();
			notifyObservers(false);
			
		}else if(goalAchieved()) {

			notifyObservers(true);
			
		}
		
		
	}
    
    

   
}
