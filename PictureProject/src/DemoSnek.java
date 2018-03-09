import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;

public class DemoSnek extends FlexiblePictureExplorer{
	//ui
	private SimplePicture playArea;
	private int playAreaHeight, playAreaWidth;
	private Graphics2D g2dView;

	//objects on board
	private ArrayList<SnekUnit> snake;
	private SnekUnit head;
	private int size = 12;
	private int length = 50;
	private int dx, dy;
	private int startingSpicySize = 15;
	private Spicy spicy;
	private Button randomDirection;
	private int btnSize = 30;

	//movement
	private boolean up, down, right, left;

	//warning
	private boolean acknowledgeDisc = false, clickPlay = false;
	private Rectangle disclaimerBtn, playButton;
	private int minDim;
	private double wiggleMin;

	//splash
	private SimplePicture splashImg, menuScreen;
	private Graphics2D gSplash, menuGraphics;
	private Font fSplash;
	private boolean playedOnce;

	//game options
	private boolean testing = false;
	private boolean start = false;
	private boolean fab = false;
	private boolean end = false;
	private boolean ezpz = true;
	
	//endscreen
	private SimplePicture endScreenPic;
	private Graphics2D endScreenGraphics;
	private Rectangle returnToMainScreenBtn;
	private Rectangle customSettingsBtn;
	private Rectangle playAgainBtn;
	
	
	//rainbow
	private int[] baseColor = {255,0,255,5};
	private int red = baseColor[0];
	private int green = baseColor[1];
	private int blue = baseColor[2];
	private int colorIndex = 5;
	private int baseColorIndex;
	private String[] rgbProgression = {"g","r","b","g","r","b"};
	private String[] baseColorProgression = {"g","r","b","g","r","b"};

	
	//milliseconds between each movement/screen refresh
	private int updateDelay = 2;
	private int splashTime = 3000;


	DemoSnek(int w, int h){
		super(new SimplePicture(w,h,Color.black));
		playAreaHeight = h;
		playAreaWidth = w;
		minDim = Math.min(playAreaWidth, playAreaHeight);
		playArea = new SimplePicture(w,h);
		g2dView = playArea.createGraphics();
		endScreenPic = new SimplePicture(w,h);
		playedOnce = false;
		showWarning();
	}

	private void showWarning() {
		acknowledgeDisc = false;
		int btnWidth = (int)(playAreaWidth*.3);
		int btnHeight = (int)(playAreaHeight*.1);
		disclaimerBtn = new Rectangle(playAreaWidth/2-btnWidth/2, (int)(playAreaHeight/2.2), btnWidth, btnHeight);
		SimplePicture warning = new SimplePicture(playAreaWidth,playAreaHeight,Color.BLACK);
		Graphics2D warnG = warning.createGraphics();
		warnG.setColor(Color.WHITE);
		warnG.draw(disclaimerBtn);

		try{
			Font roboto = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Roboto-Regular.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(roboto);
		}catch(Exception e) {
			System.out.println(e);
		}

		Font warnFont = new Font("Roboto-Regular",Font.PLAIN,25);
		FontMetrics metrics = warnG.getFontMetrics(warnFont);
		int warnWidth = metrics.stringWidth("I understand");
		int warnHeight = metrics.getAscent();
		int WARNING = metrics.stringWidth("WARNING");
		int warnNote = metrics.stringWidth("Game contains rapidly flashing lights.");
		warnG.setFont(warnFont);
		warnG.drawString("WARNING", playAreaWidth/2 - WARNING/2, playAreaHeight/3);
		warnG.drawString("Game contains rapidly flashing lights.", playAreaWidth/2 - warnNote/2, playAreaHeight/5*2);
		warnG.setFont(warnFont);
		warnG.drawString("I understand.", (playAreaWidth/2-warnWidth/2), (int)(playAreaHeight/2.2+btnHeight/2)+(warnHeight/2));
		setImage(warning);

	}
	
	public void mouseDragged(MouseEvent e)
	  {
//	    displayPixelInformation(e);
	  }
	  
	private SimplePicture drawRainbowBackground(){
		SimplePicture rainbow = new SimplePicture(playAreaWidth,playAreaHeight);
		int[] changeColor = Arrays.copyOf(baseColor, 4);
		for(int i = 0; i <rainbow.getHeight(); i++){
			if(i==0){
				;
			} else{
				changeColor = getNextColor(changeColor);
			}

			for(int x=0; x<rainbow.getWidth(); x++){
				rainbow.getPixel(x,i).setColor(new Color(changeColor[0],changeColor[1],changeColor[2]));
			}
			
		}
		
		return rainbow;
	}
	
	private int[] getNextColor(int[] rgb){
		int[] newRgb = new int[4];
		newRgb = Arrays.copyOf(rgb, 4);

		if(rgb[0]==255 && rgb[1]==0 && rgb[2]==0 && rgb[3]==5){
			newRgb[3]=0;
		}
		if(rgb[0]==255 && rgb[1]==255 && rgb[2]==0 && rgb[3]==0){
			newRgb[3]=1;
		}
		if(rgb[0]==0 && rgb[1]==255 && rgb[2]==0 && rgb[3]==1){
			newRgb[3]=2;
		}
		if(rgb[0]==0 && rgb[1]==255 && rgb[2]==255 && rgb[3]==2){
			newRgb[3]=3;
		}
		if(rgb[0]==0 && rgb[1]==0 && rgb[2]==255 && rgb[3]==3){
			newRgb[3]=4;
		}
		if(rgb[0]==255 && rgb[1]==0 && rgb[2]==255 && rgb[3]==4){
			newRgb[3]=5;
		}

		if(newRgb[3]%3==2){
			if(newRgb[3]>3)
				newRgb[2] = rgb[2]-1;
			else
				newRgb[2] = rgb[2]+1;
		}
		
		if(newRgb[3]%3==1){
			if(newRgb[3]>3)
				newRgb[0] = rgb[0]+1;
			else
				newRgb[0] = rgb[0]-1;
		}
		if(newRgb[3]%3==0){
			if(newRgb[3]>=3)
				newRgb[1] = rgb[1]-1;
			else
				newRgb[1] = rgb[1]+1;
		}
		return newRgb;
	}
	
	public void letsGo() {
		Random positionPicker = new Random();
		spicy.setPosition(positionPicker.nextInt(playAreaWidth-spicy.getSize()),positionPicker.nextInt(playAreaHeight-spicy.getSize()));
		dy = 0;
		left = true;
		end = false;
		while(!end) {
			playArea.setAllPixelsToAColor(Color.BLACK);
			if(fab){
				playArea = drawRainbowBackground();
				g2dView = playArea.createGraphics();
				baseColor = getNextColor(baseColor);
			}
			if(spicy.isEaten(head)) {
				spicy.setPosition(positionPicker.nextInt(playAreaWidth-spicy.getSize()),positionPicker.nextInt(playAreaHeight-spicy.getSize()));
				snake.add(new SnekUnit(size));
			}
			render(g2dView);
			update();
			randomDirection.render(g2dView);
			isGameOver();
			if(end) {
				setImage(endScreenPic);
			}
			else{
				setImage(playArea);
			}
			try {
				Thread.sleep(updateDelay*8);
			} catch(Exception e){
				System.out.println(e);
			}
		}
	}
	private void setUpMenu(int width, int height){
		clickPlay = false;
		Picture menuPic = new Picture(height, width);
		Picture DDN = new Picture("images/the real instructiosn.png");
		Font menuFont = new Font("Comics Sans",Font.PLAIN,25);
		int w = playAreaWidth;
		int h = playAreaHeight;
		int btnW = (int)(playAreaWidth*.2);
		int btnH = (int)(playAreaHeight*.1);
		menuGraphics = menuPic.createGraphics();
		menuGraphics.drawImage(DDN.getImage(), 0, 0, w, h, null);
		playButton = new Rectangle(5*w/7, 3*h/4, btnW, btnH);
		FontMetrics metrics = menuGraphics.getFontMetrics(menuFont);
		menuGraphics.setColor(Color.BLACK);
		int playWidth = metrics.stringWidth("PLAY");
		menuGraphics.setFont(menuFont);
		menuGraphics.drawString("PLAY", (5*w/7)+ (btnW/2) - playWidth/2, (3*h/4)+(btnH*3/4));
		menuGraphics.setColor(Color.BLACK);
		menuGraphics.draw(playButton);
		setImage(menuPic);
		
	}
	private void setUp() {
		class SplashThread extends Thread {
			private int w;
			private int h;

			SplashThread(int width, int height){
				splashImg = new SimplePicture(width,height,Color.BLACK);
				gSplash = splashImg.createGraphics();
				try {
					fSplash = new Font("Comic Sans MS",Font.PLAIN,Math.min(height, width)/3);
				} catch (Exception e) {
					System.out.println(e);
				}
				w = width;
				h = height;
			}
			public void run() {
				gSplash.setFont(fSplash);
				Random colorPicker = new Random();
				gSplash.setBackground(Color.BLACK);
				while(!start) {
					FontMetrics metrics = gSplash.getFontMetrics(fSplash);
					int sWidth = metrics.stringWidth("s");
					int nWidth = metrics.stringWidth("n");
					int eWidth = metrics.stringWidth("e");
					//					int kWidth = metrics.stringWidth("k");
					Random wiggleSet = new Random();
					wiggleMin = minDim/30;
					int sPos = (int)(w/30 - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin));
					int nPos = (int)((w/30)+sWidth - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin));
					int ePos = (int)((w/30)+sWidth+nWidth - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin));
					int kPos = (int)((w/30)+sWidth+nWidth+eWidth - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin));
					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					splashImg.setAllPixelsToAColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					gSplash.drawString("s", sPos, (int)(h/3 - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin)));
					setImage(splashImg);
					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					gSplash.drawString("n", nPos, (int)(h/3 - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin)));
					setImage(splashImg);

					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(255)+1,colorPicker.nextInt(256)));
					gSplash.drawString("e", ePos, (int)(h/3 - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin)));
					setImage(splashImg);

					gSplash.setColor(new Color(colorPicker.nextInt(256),colorPicker.nextInt(256),colorPicker.nextInt(256)));
					gSplash.drawString("k", kPos, (int)(h/3 - wiggleMin + wiggleSet.nextDouble()*(2*wiggleMin)));
					setImage(splashImg);
					try {
						Thread.sleep(15);
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
				snake = new ArrayList<SnekUnit>();
				head = new SnekUnit(size);
				head.setPosition(playAreaWidth / 2, playAreaHeight / 2);
				snake.add(head);
				for(int i = 1; i < length; i++){
					SnekUnit e = new SnekUnit(size);
					e.setPosition(head.getX() + (i * size), head.getY());
					snake.add(e);
				}
				spicy = new Spicy(startingSpicySize);
				randomDirection = new Button(btnSize);

				makeEndScreen();
				try{
					Thread.sleep(splashTime);
					start = true;
				} catch(Exception e){
					System.out.println(e);
				}
				randomDirection.setRandomLoc(playAreaWidth, playAreaHeight);
				try{
					Thread.sleep(500);
				}
				catch(Exception e){
					System.out.println(e);
				}
				letsGo();
			}
		}
		
		if(!playedOnce) {
			SplashThread splashIt = new SplashThread(playAreaWidth,playAreaHeight);
			splashIt.start();
			System.out.println("splash");
		}
		BoardThread loadBoard = new BoardThread();
		loadBoard.start();
	}
	
	private void makeEndScreen(){
		endScreenGraphics = endScreenPic.createGraphics();
		endScreenGraphics.setColor(getRandomColor());
		endScreenGraphics.fillRect(0,(3*playAreaHeight)/4,playAreaWidth/3,playAreaWidth/4);
		endScreenGraphics.setColor(getRandomColor());
		endScreenGraphics.fillRect(playAreaWidth/3,(3*playAreaHeight)/4,playAreaWidth/3,playAreaWidth/4);
		endScreenGraphics.setColor(getRandomColor());
		endScreenGraphics.fillRect((2*playAreaWidth)/3,(3*playAreaHeight)/4,playAreaWidth/3,playAreaWidth/4);
//		endScreenGraphics.setColor(getRandomColor());
//		endScreenGraphics.fillRect((3*playAreaWidth)/4,(3*playAreaHeight)/4,playAreaWidth/4,playAreaWidth/4);
	}
	
	private Color getRandomColor(){
		Random getColor = new Random();
		return new Color(getColor.nextInt(256),getColor.nextInt(256),getColor.nextInt(256));
	}
	
	public void render(Graphics2D g2d){
		if(fab == true){
			for(SnekUnit e : snake)
				e.renderFab(g2d);
		}
		else{
			g2d.setColor(Color.yellow);
			for(SnekUnit e : snake){
				e.render(g2d);
			}
		}
		spicy.render(g2d);
		randomDirection.render(g2d);
		
	}

	private void isGameOver() {
		for(int s = 1; s < snake.size(); s++){
			if(snake.get(s).hitObj(head)){
				end = true;
			}
		}
	}

	private void update() {
		if(up && dy == 0){
			dy = -size;
			dx = 0;
		}

		if(down && dy == 0){
			dy = size;
			dx = 0;
		}

		if(left && dx == 0){
			dy = 0;
			dx = -size;
		}

		if(right && dx == 0){
			dy = 0;
			dx = size;
		}



		up = false;
		down = false;
		left = false;
		right = false;
		if(dx != 0 || dy != 0){
			for(int i = snake.size() - 1; i > 0; i--){
				(snake.get(i)).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
			}
			head.move(dx, dy);
		}


		if(head.getX() < 0)
			head.setX(playAreaWidth);
		if(head.getY() < 0)
			head.setY(playAreaHeight);
		if(head.getX() > playAreaWidth)
			head.setX(0);
		if(head.getY() > playAreaHeight)
			head.setY(0);
	}

	public void mouseClickedAction(DigitalPicture pict, Pixel pix){
		System.out.println(end);
		
		if(!acknowledgeDisc && !start) {
			if(disclaimerBtn.contains(pix.getX(),pix.getY())) {
				acknowledgeDisc = true;
				setUpMenu(playAreaWidth, playAreaHeight);
			}
		}
		else if(!clickPlay){
			if(playButton.contains(pix.getX(), pix.getY())){
				System.out.println("14");
				clickPlay = true;
				setUp(playAreaWidth,playAreaHeight);
			}
		}
		randomDirection.render(g2dView);

		if(testing && !end && start) {
			int dx = head.getX()-pix.getX();
			int dy = head.getY()-pix.getY();
			if(Math.abs(dx)<Math.abs(dy)) {
				if(dy>0 && !down)
					up = true;
				else if(!up)
					down = true;
			}
			else if(Math.abs(dy)<Math.abs(dx)) {
				if(dx>0 && !right)
					left = true;
				else if(!left)
					right = true;
			}
		}

		if(start && randomDirection.isClicked(pix) && !end) {
			randomDirection.setRandomLoc(playAreaWidth, playAreaHeight);
			Random pickDirection = new Random();
			int rand = pickDirection.nextInt(4);
			if(rand==0)
				up = true;
			if(rand==1)
				down = true;
			if(rand==2)
				left = true;
			if(rand==3)
				right = true;
			
			
			
//			randomDirection.render(g2dView);

		}
		if(end) {
			System.out.println("clicked");
			playedOnce = true;
			setUp();
			end = false;
		}
	}

	public static void main(String[] args){
		DemoSnek test = new DemoSnek(800, 400);
	}
}