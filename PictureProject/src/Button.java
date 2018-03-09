import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.util.Random;


public class Button {
	private int x, y, size;
	private Color btnColor = Color.BLUE;
	private Ellipse2D.Double button;
	private Rectangle2D.Double boundingBox;
	
	Button(int size){
		this.size = size;
		button = new Ellipse2D.Double(x,y,size,size);
		boundingBox = new Rectangle2D.Double(x, y, size, size);
	}
	
	private void update() {
		boundingBox = new Rectangle2D.Double(x, y, size, size);
		button = new Ellipse2D.Double(x,y,size,size);
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		this.x = x;
		update();
		
	}
	public void setY(int y){
		this.y = y;
		update();
	}
	
	public void setSize(int sizeToSet) {
		size = sizeToSet;
		update();
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
		update();
	}
	
	public void move(int dx, int dy){
		x += dx;
		y += dy;
		update();
	}
	
	public void setRandomLoc(int xBound, int yBound) {
		Random positionPicker = new Random();
		x = positionPicker.nextInt(xBound-size);
		y = positionPicker.nextInt(yBound-size);
		update();
	}
	
	public boolean isClicked(Pixel pix) {
		if(button.contains(pix.getX(),pix.getY()))
			return true;
		else
			return false;
	}
	
	public void setColor(Color col) {
		btnColor = col;
	}
	
	public void render(Graphics2D g2d){
		g2d.setColor(btnColor);
		g2d.fill(button);
	}
}
