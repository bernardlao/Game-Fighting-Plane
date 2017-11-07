package com.game.utilities;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

public class Util {
	private static HashMap<String,Image> cache = new HashMap<String,Image>();
	public static Image LoadImage(String path){
		Image image = null;
		if(cache.containsKey(path)){
			return cache.get(path);
		}
		try{
			image = ImageIO.read(new File(path));
			if(!cache.containsKey(path)){
				cache.put(path, image);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return image;
	}
}
