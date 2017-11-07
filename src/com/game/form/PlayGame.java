package com.game.form;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.game.engine.GameEngine;
import com.game.utilities.KeyBoard;

public class PlayGame {
	JButton button;
	JTextField txtname;
	JLabel header,label1;
	static String name;
	
	public static int WIDTH = 1200;
	public static int HEIGHT = 700;
	static String textFile = "src/record.txt";
	public static ArrayList<String> scores = new ArrayList<String>();
	    
	public static JFrame frame = new JFrame();
	public static void main(String[] args) {
		CreateShowForm();
	}
	public PlayGame(){
		button = new JButton("Proceed");
		txtname = new JTextField();
		header = new JLabel("WELCOME TO SPACE RANGER!!!");
		label1 = new JLabel("Please enter your name here");
	}
	private static void CreateShowForm(){
		JFrame frame1 = new JFrame("Space Ranger");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PlayGame form = new PlayGame();
		form.AddControlToForm(frame1.getContentPane());
		frame1.setSize(300 , 300);
		frame1.setLocationRelativeTo(null);
		frame1.setResizable(false);
		frame1.setVisible(true);
	}
	public static int readFile(String filename) throws IOException{
		scores.clear();
		Path path = Paths.get(filename);
		if(!Files.exists(path)){
			GameEngine.name = "";
			return 0;
		}
		try(BufferedReader reader = Files.newBufferedReader(path,Charset.defaultCharset())){
			String line = null;
			while((line = reader.readLine()) != null)
			{
				scores.add(line);
			}
			reader.close();
		}
		int max = 0;
		for(String s : scores){
			if(Integer.valueOf(s.split(":")[1]) > max){
				max = Integer.valueOf(s.split(":")[1]);
				GameEngine.name = s.split(":")[0];
			}
		}
		return max;
	}
	public static void SaveScore(){
		scores.add(name + ":" + GameEngine.score);
		try {
			writeFile(textFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void getHighScore()
	{
		int max = 0; 
		for(String s : scores)
		{
			int x = Integer.valueOf(s.split(":")[1]);
			if(x > max){
				max = x;
				GameEngine.name = s.split(":")[0];
			}
		}
		GameEngine.highscore = max;
		
	}
	public static void writeFile(String filename) throws IOException{
		Path path = Paths.get(filename);

		try(BufferedWriter writer = Files.newBufferedWriter(path,Charset.defaultCharset())){
			for(String s : scores){
				writer.write(s);
				writer.newLine();
			}
			writer.close();
		}
	}
	public void AddControlToForm(Container container){
		container.setLayout(null);
		button.setBounds(100, 200, 100, 30); //x,y, width, height
		container.add(button);//button1
		button.addActionListener(new ButtonEventHandler());
		
		txtname.setBounds(70, 170, 160, 20);
		container.add(txtname);
		
		header.setBounds(55, 50, 200, 20);
		container.add(header);
		
		label1.setBounds(65,150,180,20);
		container.add(label1);
	}
	private class ButtonEventHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == button){
				if(txtname.getText().trim().equals("")){	
					JOptionPane.showMessageDialog(null, "Please provide your name for the score record.");
					
			    }else{
			    	try {
						GameEngine.highscore = readFile(textFile);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	name = txtname.getText();
					frame = new JFrame();
					frame.setTitle("Space Ranger");
				    frame.setVisible(true);
				    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    frame.setPreferredSize(new Dimension(WIDTH,HEIGHT));
				    frame.setSize(WIDTH, HEIGHT);
				    frame.setLocationRelativeTo(null);
				    frame.setResizable(false);				    
					KeyBoard keyboard = KeyBoard.getInstance();
					frame.addKeyListener(keyboard);
					GameEngine panel = new GameEngine();
					frame.add(panel);
					frame.pack();
			    }
			}
		}
	}
}