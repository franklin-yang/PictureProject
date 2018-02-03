import java.awt.Color;
import java.awt.*;

public class testBtn extends FlexiblePictureExplorer {
	private SimplePicture warning;
	private Graphics2D gWarn;
	private int windowWidth;
	private int windowHeight;
	private Rectangle warnBtn;
	
	testBtn(int w, int h){
		super(new SimplePicture(w,h,Color.BLACK));
		windowWidth = w;
		windowHeight = h;
		makeWarn();
		
		
	}
	
	private void makeWarn() {
		warning = new SimplePicture(windowWidth,windowHeight,Color.BLACK);
		gWarn = warning.createGraphics();
		gWarn.setColor(Color.WHITE);
		warnBtn = new Rectangle(50,50,500,300);
		gWarn.draw(warnBtn);
		gWarn.drawString("I understand", 80, 80);
		setImage(warning);
	}
	
	public void mouseClickedAction(DigitalPicture pict, Pixel pix){
		if(warnBtn.contains(pix.getX(), pix.getY()))
			setImage(new SimplePicture(1000,800));
	}
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,int arg4, int arg5) {
		//method from implementing ImageObserver (the input is not needed)
		return false;
	}
	
	public static void main(String[] args) {
		testBtn test = new testBtn(1000,800);
	}
	
}
