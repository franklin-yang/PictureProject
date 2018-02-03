import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;


import javax.swing.JFrame;
public class Demo extends FlexiblePictureExplorer{
	Demo(int width, int height){
		super(new SimplePicture(width,height,Color.BLACK));
		JFrame frame = new JFrame("Snek, de danjr newdle");
		frame.setContentPane(new Board());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		frame.setResizable(false);
		frame.pack();
		frame.setPreferredSize(new Dimension(Board.width,Board.height));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void updateGraphicToImage(Graphics2D g2d) {
		
	}
	
	public void mouseClickedAction(DigitalPicture pict, Pixel pix){
		
	}
	
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,int arg4, int arg5) {
		//method from implementing ImageObserver (the input is not needed)
		return false;
	}
	
	public static void main(String[] args){
		
	}
}
