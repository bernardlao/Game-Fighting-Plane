package com.game.entities;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.*;

import com.game.engine.GameEngine;

public class EnemyBullet {
	public Rectangle bullet;
	public int x,y;
	public EnemyBullet(Rectangle body){
		x = body.x - 30;
		y = body.y + (body.height / 2);
		bullet = new Rectangle(x,y,30,5);
	}
	public void drawBullet(Graphics g){
		g.setColor(Color.RED);
		g.drawRoundRect(x, y, bullet.width, bullet.height, 5, 5);
		g.setColor(Color.yellow);
		g.fillRoundRect(x, y, bullet.width, bullet.height, 5, 5);
	}

	public void update(){
		x-=30;
		bullet.x = x;
	}
}
