import java.util.ArrayList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private int size = 10;
	private int length = 5;
	private int dx, dy;
	private int startingSpicySize = 15;
	private Spicy spicy;


	//movement
	private boolean up, down, right, left;

	//warning
	private boolean testing = true;
	private boolean acknowledgeDisc = false;
	private Rectangle disclaimerBtn;
	private int minDim;
	private double wiggleMin;

	//splash
	private SimplePicture splashImg;
	private Graphics2D gSplash;
	private Font fSplash;

	//game options
	private boolean start = false;
	private boolean fab = false;
	private boolean end = false;

	//milliseconds between each time the snek moves
	private int updateDelay = 30;

	DemoSnek(int w, int h){
		super(new SimplePicture(w,h,Color.black));
		playAreaHeight = h;
		playAreaWidth = w;
		minDim = Math.min(playAreaWidth, playAreaHeight);
		playArea = new SimplePicture(w,h,Color.BLUE);
		g2dView = playArea.createGraphics();
		snake = new ArrayList<SnekUnit>();
		head = new SnekUnit(size);
		head.setPosition(playAreaWidth / 2, playAreaHeight / 2);
		snake.add(head);
		for(int i = 1; i < length; i++){
			SnekUnit e = new SnekUnit(size);
			e.setPosition(head.getX() + (i * size), head.getY());
			snake.add(e);
		}
		showWarning();
		spicy = new Spicy(startingSpicySize);

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
		int warnNote = metrics.stringWidth("Game contains flashing lights.");
		warnG.setFont(warnFont);
		warnG.drawString("WARNING", playAreaWidth/2 - WARNING/2, playAreaHeight/3);
		warnG.drawString("Game contains flashing lights.", playAreaWidth/2 - warnNote/2, playAreaHeight/5*2);
		warnG.setFont(warnFont);
		warnG.drawString("I understand.", (playAreaWidth/2-warnWidth/2), (int)(playAreaHeight/2.2+btnHeight/2)+(warnHeight/2));
		setImage(warning);

	}

	public void letsGo() {
		Random positionPicker = new Random();
		spicy.setPosition(positionPicker.nextInt(playAreaWidth-spicy.getSize()),positionPicker.nextInt(playAreaHeight-spicy.getSize()));
		while(true) {
			if(spicy.isEaten(head)) {
				spicy.setPosition(positionPicker.nextInt(playAreaWidth-spicy.getSize()),positionPicker.nextInt(playAreaHeight-spicy.getSize()));
				snake.add(new SnekUnit(size));
			}
			render(g2dView);
			update();
			setImage(playArea);
			try {
				Thread.sleep(updateDelay);
			} catch(Exception e){
				System.out.println(e);
			}
			playArea.setAllPixelsToAColor(Color.BLACK);
		}
	}

	private void setUpSplash(int width, int height) {
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
				try{
					Thread.sleep(3000);
					start = true;
				} catch(Exception e){
					System.out.println(e);
				}
				letsGo();
			}
		}

		SplashThread splashIt = new SplashThread(width,height);
		splashIt.start();
		BoardThread loadBoard = new BoardThread();
		loadBoard.start();
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
		spicy.render(g2dView);
		if(end){
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, playAreaWidth, playAreaHeight);
			g2d.setColor(Color.red);
			g2d.drawString("You bLoGaL!", playAreaWidth / 2, playAreaHeight / 2);
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
		for(SnekUnit s : snake){
			if(s.hitObj(head)){
				end = true;
				break;
			}
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
		if(!acknowledgeDisc) {
			if(disclaimerBtn.contains(pix.getX(),pix.getY())) {
				acknowledgeDisc = true;
				setUpSplash(playAreaWidth,playAreaHeight);

			}
		}

		if(testing) {
			int dx = head.getX()-pix.getX();
			int dy = head.getY()-pix.getY();
			if(Math.abs(dx)<Math.abs(dy)) {
				if(dy>0)
					up = true;
				else
					down = true;
			}
			else if(Math.abs(dy)<Math.abs(dx)) {
				if(dx>0)
					left = true;
				else
					right = true;
			}
			else
				head.move(0, size);
		}

		//		boolean xCheck = (pix.getX()-btnLoc[0])>0 && (pix.getX()-btnLoc[0])<btnSize;
		//		boolean yCheck = (pix.getY()-btnLoc[1])>0 && (pix.getY()-btnLoc[1])<btnSize;
		//		if(xCheck && yCheck) {
		//			redrawMovedBtn();
		//			randomDirection();
	}

	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,int arg4, int arg5) {
		//method from implementing ImageObserver (the input is not needed)
		return false;
	}



	public static void main(String[] args){
		DemoSnek test = new DemoSnek(800,400);
	}
}
