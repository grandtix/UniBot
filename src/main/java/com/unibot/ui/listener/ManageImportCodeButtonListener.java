package com.unibot.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.unibot.core.Context;
import com.unibot.ui.ManageLibraries;

public class ManageImportCodeButtonListener implements ActionListener
{
	private JFrame parentFrame;
	private Context context;

	public ManageImportCodeButtonListener(JFrame frame, Context context)
	{
		this.parentFrame = frame;
		this.context = context;
//		workspace = context.getWorkspaceController().getWorkspace();
//		uiMessageBundle = ResourceBundle.getBundle("com/unibot/block/unibot");
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
	//	((OpenblocksFrame)(parentFrame)).genererCode(workspace, context, parentFrame, uiMessageBundle, false);
			
new ManageLibraries(this.parentFrame,context).setVisible(true);	
}
	
	

}
