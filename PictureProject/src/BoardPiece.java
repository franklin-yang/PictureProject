
public class BoardPiece {
	private int xPosition;
	private int yPosition;
	
	private int size;
	
	public int getX() {
		return xPosition;
	}
	public int getY() {
		return yPosition;
	}
	public void setX(int x) {
		xPosition = x;
	}
	public void setY(int y) {
		yPosition = y;
	}
	public void setPosition(int x, int y) {
		xPosition = x;
		yPosition = y;
	}
	public void setSize(int x) {
		size = x;
	}
	
}
