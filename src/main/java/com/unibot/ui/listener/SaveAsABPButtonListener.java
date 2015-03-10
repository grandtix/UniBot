package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.unibot.ui.OpenblocksFrame;

public class SaveAsABPButtonListener implements ActionListener
{
//	private Context context;

	private OpenblocksFrame parentFrame;
	public SaveAsABPButtonListener(OpenblocksFrame frame)
	{
//		context = Context.getContext();
		parentFrame = frame;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		parentFrame.doSaveAsUniBotFile();
	}


}
