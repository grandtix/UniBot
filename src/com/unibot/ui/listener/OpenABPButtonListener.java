package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;

import com.unibot.ui.OpenblocksFrame;

public class OpenABPButtonListener implements ActionListener
{

//	private Context context;
	
	private OpenblocksFrame parentFrame;
	
	public OpenABPButtonListener(OpenblocksFrame frame)
	{
	//	context = Context.getContext();
		
		this.parentFrame = frame;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		parentFrame.doOpenUniBotFile();
	
	}

}
