import java.util.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Spicy {
	private int x, y;
	private int size;
	private Rectangle boundingBox;
	
	private Picture spicy = new Picture("images/spicy.gif");

	
	Spicy(int size){
		this.size = size;
		updateBoundingBox();
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		this.x = x;
		updateBoundingBox();
	}
	public void setY(int y){
		this.y = y;
		updateBoundingBox();
	}
	
	private void updateBoundingBox() {
		boundingBox = new Rectangle(x,y,size,size);
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
		updateBoundingBox();
	}
	
	public boolean isEaten(SnekUnit unit) {
		if(boundingBox.intersects(unit.getBound()))
			return true;
		else
			return false;
	}
	
	public int getSize() {
		return size;
	}
	
	public void render(Graphics2D g2d) {
		g2d.drawImage(spicy.getImage(), x, y,size,size,null);
	}
}
