package com.game.engine;

import java.io.*;
import java.net.*;

import javax.sound.sampled.*;

public class SoundEngine {
	private Clip clip;
	private FloatControl gainControl;
	private InputStream audioSource;
	private URL url;
	private InputStream bufferedIn;
	private AudioInputStream ais,dais;
	private AudioFormat baseFormat,decodeFormat;
	public SoundEngine(String path){
		try{
			File file = new File(path);
			ais = AudioSystem.getAudioInputStream(file);
			baseFormat = ais.getFormat();
			decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false);
			dais = AudioSystem.getAudioInputStream(decodeFormat,ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			gainControl =(FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void stop(){
		if(clip.isRunning()){
			clip.stop();
		}
	}
	public void close(){
		stop();
		clip.drain();
		clip.close();
	}
	public void setVolume(float value){
		gainControl.setValue(value);
	}
	public void loop(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		while(!clip.isRunning()){
			clip.start();
		}
	}
	public void play(){
		if(clip == null){
			return;
		}
		stop();
		clip.setFramePosition(0);
		while(!clip.isRunning()){
			clip.start();
		}
	}
}
