package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.unibot.core.Context;
import com.unibot.ui.OpenblocksFrame;

//import edu.mit.blocks.workspace.Workspace;

public class ImportLibrariesButtonListener implements ActionListener
{
	private JFrame parentFrame;
	private OpenblocksFrame bot;
//	private Context context;
/*	private Workspace workspace; 
	private ResourceBundle uiMessageBundle;
	private JFileChooser fileChooser;
	*/
	public ImportLibrariesButtonListener(JFrame _frame, OpenblocksFrame bot, Context _context)
	{
		this.parentFrame = _frame;
//		this.context = _context;
/*		workspace = context.getWorkspaceController().getWorkspace();
		uiMessageBundle = ResourceBundle.getBundle("com/unibot/block/unibot");
		fileChooser = new JFileChooser();
		*/
	}
	
	public void actionPerformed(ActionEvent e)
	{
		((OpenblocksFrame) this.bot).getFile(null);
	}
	
	

}
