package com.game.engine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.game.form.PlayGame;
import com.game.utilities.*;
import com.game.entities.*;

public class GameEngine extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KeyBoard keys;
	int index;
	boolean erase = false;
	
	Image top1,top2,bottom1,bottom2,plane,bullet,bullet2,fire,fire2;
	ArrayList<Obstacles> obs;
	ArrayList<EnemyBullet> bullets;
	int bulletInterval = 0;
	
	int x1 = 0,y1 = 0,x3 = 0,x4 = PlayGame.frame.getWidth();
	int x2 = PlayGame.frame.getWidth();
	int y2 = PlayGame.frame.getHeight()/2;
	int height = PlayGame.frame.getHeight()/2;
	int width = PlayGame.frame.getWidth();
	
	int planeX = 20,planeY = 100;
	
	boolean bulletDisplayed = false, bulletDisplayed2 = false;
	int bulletX = 0,bulletY=0, bulletX2 = 0, bulletY2 = 0;
	int bulCounter = 0,bulCounter2 = 0, bulletDelay = 0;
	
	int fireCount = 0;
	
	Rectangle planeBody,bulletBody,bulletBody2;
	public static boolean isB1Hit = false, isB2Hit = false;
	public static boolean isGameOver = false;
	
	public static int score = 0;
	public static int highscore = 0;
	public static String name;
	int scoreDelay = 0;
	int enemyInterval = 0, enemyDelay = 50;
	
	private SoundEngine bgSound, launch, enemyDead,playerDead;
	
	public GameEngine(){
		keys = KeyBoard.getInstance();	
		new Thread(this).start();
		InitializeComponent();
	}
	private void InitializeComponent(){
		obs = new ArrayList<Obstacles>();
		planeBody = new Rectangle(planeX,planeY,150,20);
		bulletBody = new Rectangle(bulletX,bulletY+5,10,10);
		bulletBody2 = new Rectangle(bulletX2,bulletY2+5,10,10);
		bullets = new ArrayList<EnemyBullet>();
		score = 0;
		enemyDelay = 50;
		bulCounter = 0;bulCounter2 = 0; bulletDelay = 0;
		bulletX = 0;bulletY=0; bulletX2 = 0; bulletY2 = 0;
		bulletInterval = 0;
		bulletDisplayed = false; bulletDisplayed2 = false;
		planeX = 20;planeY = 100;
		x1 = 0;y1 = 0;x3 = 0;x4 = PlayGame.frame.getWidth();
		x2 = PlayGame.frame.getWidth();
		y2 = PlayGame.frame.getHeight()/2;
		height = PlayGame.frame.getHeight()/2;
		width = PlayGame.frame.getWidth();
		isGameOver = false;
		scoreDelay = 0;
		enemyInterval = 0;
		enemyDead = new SoundEngine("src/sounds/explode.wav");
		playerDead = new SoundEngine("src/sounds/dead.wav");
		launch = new SoundEngine("src/sounds/launch.wav");
	}
	public int GetRandomOnGrid(int high, int low)
	{
		//setting of random location exactly within the grid.
		return (int)(Math.floor(Math.random() * (1 + high - low)) + low) * 20;
	}
	private void changePlaneDirection(Graphics g) {
		if (keys.isDown(KeyEvent.VK_W)) {
			if(planeY > 0)
				planeY -= 10;
			drawPlaneMoving(g,"moveup.png");
		}
		else if (keys.isDown(KeyEvent.VK_S)) {
			if((planeY + 80) < PlayGame.frame.getHeight())
				planeY += 10;
			drawPlaneMoving(g,"movedown.png");
		}
		else
		{
			drawPlaneSteady(g);
		}
		
		if (keys.isDown(KeyEvent.VK_SPACE)){
			if(!bulletDisplayed && bulletDelay++ == 3){
				launch.play();
				bulletX = planeX + 80;
				bulletY = planeY + 20;
				bulletDisplayed = !bulletDisplayed;
				bulletDelay = 0;
			}
			if(!bulletDisplayed2 && bulletDelay++ == 3){
				launch.play();
				bulletX2 = planeX + 80;
				bulletY2 = planeY + 20;
				bulletDisplayed2 = !bulletDisplayed2;
				bulletDelay = 0;
			}
			
		}
	}
	private void incrementWallpaper(){
		x1 -= 2;
		x2 -= 2;
		x3 -= 5;
		x4 -= 5;
		if(x1 == 0)
			x2 = PlayGame.frame.getWidth();
		if(x2 == 0)
			x1 = PlayGame.frame.getWidth();
		if(x3 == 0)
			x4 = PlayGame.frame.getWidth();
		if(x4 == 0)
			x3 = PlayGame.frame.getWidth();
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		drawBackGroundTop(g);
		drawBackGroundBottom(g);
		changePlaneDirection(g);
		if(bulletDisplayed){
			drawBullet(g);
			if(bulCounter++ > 10){
				bulletX += 20;
			}else{
				bulletX += 2;
				bulletY += 2;
			}
			if(bulletX > PlayGame.frame.getWidth()){
				bulletDisplayed = !bulletDisplayed;
				bulCounter = 0;
			}
		}
		if(bulletDisplayed2){
			drawBullet2(g);
			if(bulCounter2++ > 10){
				bulletX2 += 20;
			}else{
				bulletX2 += 2;
				bulletY2 += 2;
			}
			if(bulletX2 > PlayGame.frame.getWidth()){
				bulletDisplayed2 = !bulletDisplayed2;
				bulCounter2 = 0;
			}
		}
		for(int i = obs.size() - 1; i >= 0; i--){
			obs.get(i).drawObstacles(g);
			if(!obs.get(i).source.equals(""))
				obs.remove(i);
		}
		
		for(EnemyBullet b : bullets){
			b.drawBullet(g);
		}
		if(isGameOver){
			g.drawString("Youre deads xD Press Enter to try again", 500, 100);
		}
		printStrings(g);
		//g.fillRect(planeBody.x, planeBody.y, planeBody.width, planeBody.height);
		//System.out.println(bulletBody.x + " " + bulletBody.y + " " + bulletBody2.x + " " + bulletBody2.y + " " + planeBody.y);
	}
	public void update(){
		if(!isGameOver){
			if(scoreDelay++ % 5 == 0)
				score++;
			repaint();
			incrementWallpaper();
			planeBody.y = planeY + 10;
			adjustBulletBody();
			for(int i = 0; i < obs.size();i++){
				obs.get(i).update();
				if(obs.get(i).isDestroyed(bulletBody, bulletBody2)){
					obs.get(i).source = "src/pictures/boom.jpg";
				}
				if(isB1Hit){
					bulletX = 1200;
					isB1Hit = false;
					score+=50;
					enemyDead.play();
				}
				if(isB2Hit){
					bulletX2 = 1200;
					isB2Hit = false;
					score+=50;
					enemyDead.play();
				}
			}
			if((enemyInterval++ % enemyDelay) == 0){
				obs.add(new Obstacles());
			}
			checkSpeedUp();
			doEnemyBullet();
		}else if(isGameOver && keys.isDown(KeyEvent.VK_ENTER)){
			InitializeComponent();
			PlayGame.getHighScore();
		}
	}
	private void checkSpeedUp(){
		if(score % 1000 == 0){
			if(enemyDelay > 20)
				enemyDelay -= 10;
		}
	}
	private void doEnemyBullet(){
		if(bulletInterval++ % 5 == 0){
			int x = GetRandom(bullets.size());
			try{
				bullets.add(new EnemyBullet(obs.get(x).body));
			}catch(Exception e){
				
			}
		}
		for(int i = 0; i < bullets.size();i++){
			if(bullets.get(i).bullet.intersects(planeBody)){
				playerDead.play();
				isGameOver = true;
				PlayGame.SaveScore();
				repaint();
			}
			bullets.get(i).update();
			if(bullets.get(i).x < 0)
				bullets.remove(i);
		}
	}
	private void adjustBulletBody(){
		bulletBody.x = bulletX;
		bulletBody.y = bulletY+5;
		bulletBody2.x = bulletX2;
		bulletBody2.y = bulletY2+5;
	}
	private void drawBackGroundTop(Graphics g){
		try{
			top1 = Util.LoadImage("src/pictures/top.png");
			top2 = Util.LoadImage("src/pictures/top.png");
			g.drawImage(top1,x1,y1,width,height,null);
			g.drawImage(top2,x2,y1,width,height,null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void drawBackGroundBottom(Graphics g){
		try{
			bottom1 = Util.LoadImage("src/pictures/bottom.png");
			bottom2 = Util.LoadImage("src/pictures/bottom.png");
			g.drawImage(bottom1,x3,y2,width,height,null);
			g.drawImage(bottom2,x4,y2,width,height,null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void drawPlaneSteady(Graphics g){
		String source = "";
		if(isGameOver)
			source = "src/pictures/boom.jpg";
		else
			source = "src/pictures/steady.png";
		try{
			plane = Util.LoadImage(source);
			if(fireCount == 0){
				fire = Util.LoadImage("src/pictures/fire1.png");
				fireCount++;
			}else{
				fire = Util.LoadImage("src/pictures/fire2.png");
				fireCount = 0;
			}
			if(!isGameOver){
				g.drawImage(fire,planeX - 10,planeY + 17,20,13,null);
				g.drawImage(plane,planeX,planeY,150,40,null);
			}else{
				g.drawImage(plane,planeX,planeY,150,120,null);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void drawPlaneMoving(Graphics g,String dest){
		String source = "";
		if(isGameOver)
			source = "src/pictures/boom.jpg";
		else
			source = "src/pictures/" + dest;
		try{
			plane = Util.LoadImage(source);
			if(fireCount == 0){
				fire = Util.LoadImage("src/pictures/fire1.png");
				fire2 = Util.LoadImage("src/pictures/fire1.png");
				fireCount++;
			}else {
				fire = Util.LoadImage("src/pictures/fire2.png");
				fire2 = Util.LoadImage("src/pictures/fire2.png");
				fireCount = 0;
			}
			if(!isGameOver){
				g.drawImage(fire,planeX - 10,(planeY - 40) + 43,20,13,null);
				g.drawImage(fire2,planeX - 10,(planeY - 40) + 63,20,13,null);
			}
			g.drawImage(plane,planeX,(planeY - 40),150,120,null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void drawBullet(Graphics g){
		try{
			bullet = Util.LoadImage("src/pictures/bullet.png");
			g.drawImage(bullet,bulletX,bulletY,50,20,null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void drawBullet2(Graphics g){
		try{
			bullet2 = Util.LoadImage("src/pictures/bullet.png");
			g.drawImage(bullet2,bulletX2,bulletY2,50,20,null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void printStrings(Graphics g){
		g.drawString("CURRRENT SCORE : " + score, 10, 10);
		g.drawString("HIGHSCORE : " + (name.equals("")?" NO Existing Highscore":name + " - " + highscore),800, 10);
	}
	@Override
	public void run() {
		try {
			bgSound = new SoundEngine("src/sounds/background.wav");
			bgSound.loop();
	        while (true) {
	        	update();
	           	Thread.sleep(40); 
	        }
		}catch (Exception e) {
	            e.printStackTrace();
	    }
	}
	public int GetRandom(int max)
	{
		return (int)Math.floor(Math.random() * max);
	}
}
