/*YANG 01/28/2018 18:35:24

	changelog:
		eatSpicy works, no grow function
*/
import java.awt.*;
import java.lang.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.image.ImageObserver;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class SnekDemo extends FlexiblePictureExplorer implements ImageObserver{
	
	private SimplePicture playScreen;
	private SimplePicture splash;
	private Color backdropColor;
	private SimplePicture warning;
	
	//higher number indicates slower speed, integer that sets the interval in milliseconds between snake movements
	int speed =100;
	

	private int score;
	
	private boolean warnBtnsVisible = true;
	
	private boolean testing = true;
	
	private int length;
	private int[][] directions = new int[4][2];
	private int[] btnLoc = new int[2];
	private int[] headLoc = new int[2];
	private int[] spicyLoc = new int[2];
	private boolean fab = false;
	private boolean btnPressed = false;
	private int btnSize = 30;
	private int spicySize = 10;
	private int unitSize = 5;

	private Graphics2D gSplash;
	private Graphics2D gPlay;
	
	private int[] direction = new int[2];
	private int[] up = {0,-unitSize};
	private int[] right = {unitSize,0};
	private int[] left = {-unitSize,0};
	private int[] down = {0,unitSize};
	private boolean start = false;
	
	private Font roboto;
	
	private static Font comic;
	private static Font fSplash;
	
	private ArrayList<int[]> snek = new ArrayList<int[]>();
	
	private boolean spicyEaten = false;
	private boolean gameOver;
	
	private Picture spicy = new Picture("images/spicy.gif");
	private Rectangle head;
	private Ellipse2D.Double btn;
	private Rectangle ok;
	
	private Color btnColor;
	private boolean ready = false;
		
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,int arg4, int arg5) {
		//method from implementing ImageObserver (the input is not needed)
		return false;
	}
	
	SnekDemo(int width, int height) {
		super(new SimplePicture(width,height,Color.BLACK));
		playScreen = new SimplePicture(width,height,Color.BLACK);		
		setUpSplash(width,height);
		length = 3;
		gameOver = false;
		gPlay = playScreen.createGraphics();
		
		
		
	}
	
	SnekDemo(){
		this(1280,720);
	}
	
	private void displayWarning() {
		warning = new SimplePicture(playScreen.getWidth(),playScreen.getHeight(),Color.BLACK);
		Graphics2D warnScreen = warning.createGraphics();	
		try{
			roboto = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Roboto-Regular.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(roboto);
		}catch(Exception e) {
			System.out.println(e);
		}
		Font robotoWarn = new Font("Roboto-Regular",Font.PLAIN,25);
		warnScreen.setFont(robotoWarn);
		warnScreen.drawString("This game contains flashing colors.", 100, 100);
		ok = new Rectangle(234,343,playScreen.getWidth()/8,playScreen.getHeight()/8);
		warnScreen.setColor(Color.WHITE);
		warnScreen.draw(ok);
		warnScreen.drawString("I understand", 300, 350);
		warnBtnsVisible = true;
		setImage(warning);
		
	}
	
	
	private void setUpSplash(int width, int height) {
		class SplashThread extends Thread {
			SplashThread(int width, int height){
				splash = new SimplePicture(width,height,Color.BLACK);
				gSplash = splash.createGraphics();
				System.out.println(8);
				try {
					fSplash = new Font("Comic Sans MS",Font.PLAIN,Math.min(height, width)/2);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			public void run() {
				gSplash.setFont(fSplash);
				Random colorPicker = new Random();
				gSplash.setBackground(Color.BLACK);
				while(!start) {
					gSplash.setColor(new Color(colorPicker.nextInt(255)+1,colorPicker.nextInt(255)+1,colorPicker.nextInt(255)+1));
					splash.setAllPixelsToAColor(new Color(colorPicker.nextInt(255)+1,colorPicker.nextInt(255)+1,colorPicker.nextInt(255)+1));
					gSplash.drawString("s", 0, 300);
//					setImage(splash);
					System.out.println(98);
					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					gSplash.drawString("N", 150, 350);
//					setImage(splash);

					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					gSplash.drawString("e", 320, 300);
//					setImage(splash);

					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					gSplash.drawString("K", 480, 350);
					setImage(splash);
					try {
						Thread.sleep(50);
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}	
			
		}

		class BoardThread extends Thread{
			BoardThread(){

			}
			public void run() {
				setUpSnekAndBtn();
			}
		}
		
		SplashThread splashIt = new SplashThread(width,height);
		splashIt.start();
		BoardThread loadBoard = new BoardThread();
		loadBoard.start();
	}
	
	private void setUpSnekAndBtn() {
		//determines the top left coordinate of the head of the initial snake by centering it
		int centerX = (playScreen.getWidth()-unitSize)/2;
		int centerY = (playScreen.getHeight()-unitSize)/2;
		headLoc[0] = centerX;
		headLoc[1] = centerY;
		direction = up;
		for(int i=0;i<length;i++) {
			snek.add(direction);
		}
		head = new Rectangle(headLoc[0],headLoc[1],unitSize,unitSize);
		gPlay.setColor(Color.YELLOW);
		gPlay.fill(head);
		
		
		drawNewSpicy();
		

//		btn.setFrame(btnLoc[0], btnLoc[1], 45, 4);
//		btn.setFrame(464,342,34,23);
//		gPlay.setColor(btnColor);
//		gPlay.fill(btn);
		btnLoc[0] = getRandCoord(btnSize)[0];
		btnLoc[1] = getRandCoord(btnSize)[1];
		btn = new Ellipse2D.Double(btnLoc[0],btnLoc[1],btnSize,btnSize);
		
		gPlay.setColor(Color.BLUE);
		gPlay.fill(btn);
		
		try {
			Thread.sleep(10000);
		} catch(Exception e){
		}
		start = true;
		setImage(playScreen);
	}
	
	private int[] getRandCoord(int size) {
		int[] randLoc = new int[2];
		int boardWidth = playScreen.getWidth()-size-1;
		int boardHeight = playScreen.getHeight()-size-1;
		randLoc[0] = (int) (Math.random() * boardWidth);
		randLoc[1] = (int) (Math.random() * boardHeight);
		return randLoc;	
	}
	
	private void moveButton() {
		btnLoc[0] = getRandCoord(btnSize)[0];
		btnLoc[1] = getRandCoord(btnSize)[1];
	}
	
	private void newSpicyCoord() {
		spicyLoc[0] = getRandCoord(spicySize)[0];
		spicyLoc[1] = getRandCoord(spicySize)[1];
	}
	
	private void randomDirection() {
		directions[0]=up;
		directions[1]=down;
		directions[2]=left;
		directions[3]=right;
		
		Random rand = new Random();
		int randIn = rand.nextInt(4);
		int[] newDir = directions[randIn];
//		if(newDir[0]==-direction[0] && newDir[1]==-direction[1])
//			gameOver = true;
//		else {
			direction = newDir;
//		}
	}

	private void redrawHead() {
//		button
		gPlay.setColor(Color.BLACK);
		gPlay.fill(head);
		headLoc[0]+=direction[0];
		headLoc[1]+=direction[1];
		head.setLocation(headLoc[0],headLoc[1]);
		gPlay.setColor(Color.YELLOW);
		gPlay.fill(head);
	}
	
	private void redrawMovedBtn() {
		gPlay.setColor(Color.BLACK);
		gPlay.fill(btn);
		moveButton();
		btn.setFrame(btnLoc[0], btnLoc[1], btnSize, btnSize);
		gPlay.setColor(Color.BLUE);
		gPlay.fill(btn);
		setImage(playScreen);
	}

		
	public int[] getPlayAreaDimensions() {
		int[] dim = {playScreen.getWidth(),playScreen.getHeight()};
		return dim;
	}
	
	private void gameOver() {
		playScreen.setAllPixelsToAColor(Color.white);
		setImage(playScreen);
	}
	
	private void checkSpicyEaten() {
		boolean xCheck = ((headLoc[0]-spicyLoc[0])>(-spicySize)) && ((headLoc[0]-spicyLoc[0])<unitSize);
		boolean yCheck = ((headLoc[1]-spicyLoc[1])>(-unitSize)) && ((headLoc[1]-spicyLoc[1])<spicySize);
		if(xCheck && yCheck) {
			
			
			score++;
			drawNewSpicy();
//			grow();
		}
	}

	private void drawNewSpicy() {
		gPlay.setColor(Color.BLACK);
		gPlay.fillRect(spicyLoc[0], spicyLoc[1], spicySize, spicySize);
		newSpicyCoord();
		gPlay.drawImage(spicy.getBufferedImage(),spicyLoc[0],spicyLoc[1],spicySize,spicySize,Color.BLACK, null);
	}
	
	
	private void letsGo() {
		while(gameOver==false){
			checkSpicyEaten();
			if(headLoc[0] <= 0 || headLoc [0] > playScreen.getWidth()-unitSize-1 || headLoc[1] < 0 || headLoc[1] > playScreen.getHeight()-unitSize-1)
				gameOver=true;
			redrawHead();
			setImage(playScreen);
			try{
				Thread.sleep(speed);
			} catch(Exception e){
				System.out.println(e);
			}
		}
		gameOver();
	}
	
	private void grow(){
		length++;
		snek.add(snek.get(snek.size()-1));
	}
	
	public void mouseClickedAction(DigitalPicture pict, Pixel pix){
		if(ready) {
			if(testing) {
		
			int dx = headLoc[0]-pix.getX();
			int dy = headLoc[1]-pix.getY();
			if(Math.abs(dx)<Math.abs(dy)) {
				if(dy>0)
					direction = up;
				else
					direction = down;
			}
			else if(Math.abs(dy)<Math.abs(dx)) {
				if(dx>0)
					direction = left;
				else
					direction = right;
			}
			else
				randomDirection();
		}
		boolean xCheck = (pix.getX()-btnLoc[0])>0 && (pix.getX()-btnLoc[0])<btnSize;
		boolean yCheck = (pix.getY()-btnLoc[1])>0 && (pix.getY()-btnLoc[1])<btnSize;
		if(xCheck && yCheck) {
			redrawMovedBtn();
			randomDirection();
		}
		
		}
		if(warnBtnsVisible) {
			setImage(new SimplePicture(1000,1000,Color.BLUE));
			ready = true;
			setUpSplash(playScreen.getWidth(),playScreen.getHeight());
			setUpSnekAndBtn();
			letsGo();
			btnColor = Color.BLUE;
			warnBtnsVisible = false;
		}
	}
	

	
	public static void main(String[] args) {
		SnekDemo snek = new SnekDemo();
		snek.setTitle("danejr noodlz");
	}
}



