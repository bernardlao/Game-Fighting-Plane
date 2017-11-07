package com.game.entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.*;
import java.util.ArrayList;

import com.game.engine.GameEngine;
import com.game.utilities.*;

public class Obstacles {
	public String source = "";
	public int obsX,obsY;
	private Image obstacle;
	public AffineTransform transform;
	private double rotation = 20;
	public int wi  = 20,hei=20;
	public Rectangle body;
	private int middleY = 310, middleX = 600;
	private boolean isGoingDown = true, isGoingLeft = true;
	private int diffImage = 0;
	private int increment = 2;
	private boolean haltHorizontal = true, haltVertical = false;
	
	
	public Obstacles(){
		obsX = 1000;
		obsY = GetRandom(600);
		body = new Rectangle(obsX,obsY + 5,150,40);
	}
	public void drawObstacles(Graphics g){
		try{
			if(diffImage++ % 4 == 0)
				obstacle = Util.LoadImage((source.equals("")?"src/pictures/enemy.png":source));
			else
				obstacle = Util.LoadImage((source.equals("")?"src/pictures/enemy2.png":source));
			g.drawImage(obstacle,obsX,obsY,150,50,null);
		}catch(Exception e){
			e.printStackTrace();
		}
		//g.drawRoundRect(200, 200, 30, 30,5,5);
	}
	public void update(){
		if(!haltHorizontal){
			if(isGoingLeft){
				obsX-=increment;
			}else{
				obsX+=increment;
			}
		}
		if(!haltVertical){
			if(isGoingDown){
				obsY+=increment;
			}else{
				obsY-=increment;
			}
		}
		
		if(obsY > 650){
			isGoingDown = false;
		}else if(obsY < 0){
			isGoingDown = true;
		}
		if(obsX < middleX){
			isGoingLeft = false;
		}else if(obsX > 1050){
			isGoingLeft = true;
		}
		body.x = obsX;
		body.y = obsY + 5;
		if(diffImage % 100 == 0 && increment < 10){
			increment++;
		}
		if(diffImage % 100 == 0)
			isGoingLeft = (isGoingLeft?false:true);
		if(diffImage % 150 == 0)
			isGoingDown = (isGoingDown?false:true);
		if(diffImage % 200 == 0){
			int x = GetRandom(5);
			if(x > 3)
				haltHorizontal = true;
			else
				haltHorizontal = false;
		}
		if(diffImage % 200 == 0){
			int x = GetRandom(20);
			if(x > 14){
				haltVertical = true;
			}
			else
				haltVertical = false;
			//System.out.println(x);
		}
		if(haltVertical && haltHorizontal)
			haltVertical = !haltVertical;
		//doBullet();
	}
	
	public boolean isDestroyed(Rectangle b1, Rectangle b2){
		if(body.intersects(b1)){
			GameEngine.isB1Hit = true;
			source = "src/pictures/boom.jpg";
			return true;
		}else if(body.intersects(b2)){
			GameEngine.isB2Hit = true;
			source = "src/pictures/boom.jpg";
			return true;
		}
		else{
			source = "";
			return false;
		}
	}
	public int GetRandom(int max)
	{
		//setting of random location exactly within the grid.
		return (int)Math.floor(Math.random() * max);
	}
}
