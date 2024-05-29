package model;

@FunctionalInterface
public interface FieldObserver {

	public void  event(Field field , FieldEvent event);
	
}
