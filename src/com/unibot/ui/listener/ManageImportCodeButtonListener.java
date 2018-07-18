package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.unibot.core.Context;
import com.unibot.ui.ManageLibraries;
import com.unibot.ui.OpenblocksFrame;

public class ManageImportCodeButtonListener implements ActionListener
{
	private JFrame parentFrame;
	private Context context;
	private OpenblocksFrame bot;

	public ManageImportCodeButtonListener(JFrame frame, OpenblocksFrame bot, Context context)
	{
		this.parentFrame = frame;
		this.bot=bot;
		this.context = context;
//		workspace = context.getWorkspaceController().getWorkspace();
//		uiMessageBundle = ResourceBundle.getBundle("com/unibot/block/unibot");
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
	//	((OpenblocksFrame)(parentFrame)).genererCode(workspace, context, parentFrame, uiMessageBundle, false);
			
new ManageLibraries(this.parentFrame,bot, context).setVisible(true);	
}
	
	

}
