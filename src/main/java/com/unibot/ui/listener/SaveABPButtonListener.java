package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.unibot.ui.OpenblocksFrame;

public class SaveABPButtonListener implements ActionListener
{
//	private Context context;

	private OpenblocksFrame parentFrame;
	public SaveABPButtonListener(OpenblocksFrame frame)
	{
//		context = Context.getContext();
		parentFrame = frame;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		parentFrame.doSaveUniBotFile();
	}


}
