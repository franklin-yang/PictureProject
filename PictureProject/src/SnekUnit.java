import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;

public class SnekUnit extends BoardPiece{
	private boolean up = false, down = false, left = false, right = false;
	private int x, y, unitSize;
	private int[] startingRgb = {0,255,0,2};

	SnekUnit(int size){
		unitSize = size;
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
		unitSize = sizeToSet;
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
		return new Rectangle(x, y, unitSize, unitSize);
	}
	public boolean hitObj(SnekUnit o){
		return this.getBound().intersects(o.getBound());
	}
	public void render(Graphics2D g2d){
		g2d.fillRect(x + 1, y + 1, unitSize - 2, unitSize - 2);
	}
	public void renderFab(Graphics2D g2d){
		int[] rgb = Arrays.copyOf(startingRgb,4);
		for(int i = 0; i < unitSize; i++){
			int[] innerRgb = Arrays.copyOf(rgb, 4);
			for(int c = 0; c < unitSize; c++){
				//				randR = (int) (Math.random() * 255);
				//				randG = (int) (Math.random() * 255);
				//				randB = (int) (Math.random() * 255);
				g2d.setColor(new Color(innerRgb[0], innerRgb[1], innerRgb[2]));
				g2d.fillRect(x + 1 + i, y + 1 + c, 1, 1);
			}

			rgb = DemoSnek.getNextColor(rgb);
		}
		startingRgb = DemoSnek.getNextColor(startingRgb);
	}
}

