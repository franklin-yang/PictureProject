import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
public class Board extends JPanel implements Runnable, KeyListener{
	public static final int width = 400;
	public static final int height = 400;
	
	//graphics
	private Graphics2D g2d;
	private BufferedImage image;
	
	//run loop
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	//stuff
	private int size = 10;
	SnekUnit head, spicy;
	ArrayList<SnekUnit> snake;
	int score;
	
	//movement
	private int dx, dy;
	
	//input
	private boolean up, down, right, left,start;
	Board(){
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
	}
	public void addNotify(){
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	private void setFPS(int frames){
		targetTime = 1000 / frames;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_UP)
			up = true;
		if(k == KeyEvent.VK_DOWN)
			down = true;
		if(k == KeyEvent.VK_RIGHT)
			right = true;
		if(k == KeyEvent.VK_LEFT)
			left = true;
		if(k == KeyEvent.VK_ENTER){
			start = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_UP)
			up = false;
		if(k == KeyEvent.VK_DOWN)
			down = false;
		if(k == KeyEvent.VK_RIGHT)
			right = false;
		if(k == KeyEvent.VK_LEFT)
			left = false;
		if(k == KeyEvent.VK_ENTER)
			start = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(running)
			return;
		start();
		long startTime;
		long elapsed;
		long wait;
		while(running){
			startTime = System.nanoTime();
			
			update();
			requestRender();
			
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			if(wait > 0){
				try{
					Thread.sleep(wait);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}

		}
	}
	private void setUplevel(){
		snake = new ArrayList<SnekUnit>();
		head = new SnekUnit(size);
		head.setPosition(width / 2, height / 2);
		snake.add(head);
		for(int i = 1; i < 3; i++){
			SnekUnit e = new SnekUnit(size);
			e.setPosition(head.getX() + (i * size), head.getY());
			snake.add(e);
		}
		spicy = new SnekUnit(size);
		setSpicy();
		score = 0;
	}
	public void setSpicy(){
		int x = (int)(Math.random() * (width - size));
		int y = (int)(Math.random() * (height - size));
		x = x - (x% size);
		y = y - (y % size);
		spicy.setPosition(x, y);
	}
	private void start(){
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUplevel();
		setFPS(10);
	}
	private void requestRender() {
		// TODO Auto-generated method stub
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	private void update() {
		// TODO Auto-generated method stub
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
		if(dx != 0 || dy != 0){
			for(int i = snake.size() - 1; i > 0; i--){
				snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
			}
			head.move(dx, dy);
		}

		if(spicy.isCollision(head)){
			score++;
			setSpicy();
			SnekUnit e = new SnekUnit(size);
			e.setPosition(-100, -100);
			snake.add(e);
		}
		
		if(head.getX() < 0)
			head.setX(width);
		if(head.getY() < 0)
			head.setY(height);
		if(head.getX() > width)
			head.setX(0);
		if(head.getY() > height)
			head.setY(0);
	}
	public void render(Graphics2D g2d){
		g2d.clearRect(0, 0, width, height);
		g2d.setColor(Color.green);
		for(SnekUnit e : snake){
			e.render(g2d);
		}
		
		g2d.setColor(Color.RED);
		spicy.render(g2d);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score: " + score, 10, 10);
	}
	
}
