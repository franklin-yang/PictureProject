import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class SnekUnit extends BoardPiece{
	private boolean up = false, down = false, left = false, right = false;
	private int x, y, size;
	SnekUnit(int size){
		this.size = size;
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
	public void move(int dx, int dy){
		x += dx;
		y += dy;
	}
	public Rectangle getBound(){
		return new Rectangle(x, y, size, size);
	}
	public boolean hitObj(SnekUnit o){
		if(o == this)
			return false;
		return getBound().intersects(o.getBound());
	}
	public void render(Graphics2D g2d){
		g2d.fillRect(x + 1, y + 1, size - 2, size - 2);
	}
	public void renderFab(Graphics2D g2d){
		int randR, randG, randB;
		for(int i = 0; i < size; i++){
			randR = (int) (Math.random() * 255);
			randG = (int) (Math.random() * 255);
			randB = (int) (Math.random() * 255);
			for(int c = 0; c < size; c++){
				randR = (int) (Math.random() * 255);
				randG = (int) (Math.random() * 255);
				randB = (int) (Math.random() * 255);
				g2d.setColor(new Color(randR, randG, randB));
				g2d.fillRect(x + 1 + i, y + 1 + c, 1, 1);
			}
		}
		
	}
}

