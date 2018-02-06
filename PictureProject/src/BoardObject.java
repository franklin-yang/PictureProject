
public class BoardObject {
	
	private int x, y, size;
	
	BoardObject(){
		
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	
	public void setSize(int sizeToSet) {
		size = sizeToSet;
	}
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
}
