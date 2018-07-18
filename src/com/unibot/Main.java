package com.unibot;

import java.io.IOException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.unibot.core.Context;
//import com.unibot.ui.ConsoleFrame;
import com.unibot.ui.OpenblocksFrame;

public class Main
{
	public static void main(String args[]) throws SAXException, IOException, ParserConfigurationException
	{
		Main me = new Main();
		me.startUniBot();
	}

	private OpenblocksFrame openblocksFrame;
	
	public void startUniBot() throws SAXException, IOException, ParserConfigurationException
	{
		startOpenblocksFrame();
		//startConsoleFrame();
	}
	
	private void startOpenblocksFrame() throws SAXException, IOException, ParserConfigurationException
	{

		Context context = Context.getContext();
		context.setInArduino(true);
		openblocksFrame = new OpenblocksFrame(null);
	//	openblocksFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		openblocksFrame.setVisible(true);	
			openblocksFrame.loadLibs();
	}
	
	/*	
	private void startConsoleFrame()
	{
		ConsoleFrame consoleFrame = new ConsoleFrame();
		consoleFrame.setVisible(true);
	}
	*/
}
