package com.unibot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import processing.app.Editor;
import processing.app.Preferences;
import processing.app.tools.Tool;
import processing.mode.java.JavaEditor;

import com.unibot.core.Context;
import com.unibot.ui.UniBotToolFrame;
import com.unibot.ui.listener.OpenblocksFrameListener;

public class UniBot implements Tool, OpenblocksFrameListener
{
	public static Editor editor;
	public static Preferences prefs;
	public static processing.mode.java.JavaEditor javaEditor;
	static UniBotToolFrame openblocksFrame;
	public boolean editorIsArduino=false;
	private BufferedReader reader;
	
	
	
	
	public void init(Editor editor) {
		
		if (editor.getClass().toString().equals("class processing.app.Editor"))
			editorIsArduino=true;
		else
			UniBot.javaEditor = (JavaEditor) editor;
		
		if (UniBot.editor == null )
		{
			System.out.println("ehooooo "+editor.getClass().toString());
			UniBot.editor = editor;
			UniBot.openblocksFrame = new UniBotToolFrame(editor);
			UniBot.openblocksFrame.addListener(this);
			Context context = Context.getContext();
			String arduinoVersion = this.getArduinoVersion();
			
						context.setInArduino(true);
			context.setArduinoVersionString(arduinoVersion);
			System.out.println("Arduino Version: " + arduinoVersion);
		}
	}
	

	public void run() {
		try {
			UniBot.editor.toFront();
			UniBot.openblocksFrame.setVisible(true);
			UniBot.openblocksFrame.toFront();
			try {
				UniBot.openblocksFrame.setPathConf(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent()+File.separatorChar+"librairies.conf");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			openblocksFrame.loadLibs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMenuTitle() {
		return Context.APP_NAME;
	}

	public void didSave() {
		
	}
	
	public void didLoad() {
		
	}
	
	public void didGenerate(String source) {
		System.out.println(source);
		UniBot.editor.getCurrentTab().setText(source);
		System.out.println(UniBot.editor.getClass().toString());
		if (UniBot.editor.getClass().toString().equals("class processing.app.Editor"))
				UniBot.editor.handleExport(false);
		
		
		else
			UniBot.javaEditor.handleRun();
	}
	
	
	private String getArduinoVersion()
	{
		Context context = Context.getContext();
		File versionFile = context.getArduinoFile("lib/version.txt");
		if (versionFile.exists())
		{
			try
			{
				InputStream is = new FileInputStream(versionFile);
				reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line = reader.readLine();
				if (line == null)
				{
					return Context.ARDUINO_VERSION_UNKNOWN;
				}
				line = line.trim();
				if (line.length() == 0)
				{
					return Context.ARDUINO_VERSION_UNKNOWN;
				}
				return line;
				
			}
			catch (FileNotFoundException e)
			{
				return Context.ARDUINO_VERSION_UNKNOWN;
			}
			catch (UnsupportedEncodingException e)
			{
				return Context.ARDUINO_VERSION_UNKNOWN;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return Context.ARDUINO_VERSION_UNKNOWN;
			}
		}
		else
		{
			return Context.ARDUINO_VERSION_UNKNOWN;
		}
		
	}
}