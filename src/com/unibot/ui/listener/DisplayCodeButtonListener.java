package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import com.unibot.core.Context;
import com.unibot.ui.OpenblocksFrame;

import edu.mit.blocks.workspace.Workspace;

public class DisplayCodeButtonListener implements ActionListener
{
	private JFrame parentFrame;
	private Context context;
	private Workspace workspace; 
	private ResourceBundle uiMessageBundle;
	
	public DisplayCodeButtonListener(JFrame frame, Context context)
	{
		this.parentFrame = frame;
		this.context = context;
		workspace = context.getWorkspaceController().getWorkspace();
		uiMessageBundle = ResourceBundle.getBundle("com/unibot/block/unibot");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		((OpenblocksFrame)(parentFrame)).genererCode(workspace, context, parentFrame, uiMessageBundle, true);
	}
}
