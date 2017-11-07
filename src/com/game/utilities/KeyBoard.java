package com.game.utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener{
	private static KeyBoard instance;
	private boolean[] keys;
	
	private KeyBoard() {
		keys = new boolean[256];
	}

	public static KeyBoard getInstance() {
		if (instance == null) 
			instance = new KeyBoard();
		return instance;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) 
			keys[e.getKeyCode()] = true;
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) 
			keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {}

	public boolean isDown(int key) {
		if (key >= 0 && key < keys.length) 
			return keys[key];
		return false;
	}
	
}
