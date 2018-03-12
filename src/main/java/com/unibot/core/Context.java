package com.unibot.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.unibot.ui.listener.OpenblocksFrameListener;

import controller.WorkspaceController;
import renderable.RenderableBlock;
import workspace.Workspace;

public class Context
{
	
	public final static String LANG_DTD_PATH = "/com/unibot/block/lang_def.dtd";
	public final static String UNIBOT_LANG_PATH = "/com/unibot/block/unibot.xml";
	public final static String ARDUINO_VERSION_UNKNOWN = "unknown";
	
	private static Context singletonContext;
	
	private boolean workspaceChanged;
	
	private Set<RenderableBlock> highlightBlockSet;
	private Set<OpenblocksFrameListener> ofls;
	private boolean isInArduino = true;
	private String arduinoVersionString = ARDUINO_VERSION_UNKNOWN;
	private OsType osType; 
			
	final public static String APP_NAME = "UniBot";
	
	
	
	public enum OsType
	{
		LINUX,
		MAC,
		WINDOWS,
		UNKNOWN,
	};
	
	
	
	
	//final public static String VERSION_STRING = " ";
	
	public static Context getContext()
	{
		if (singletonContext == null)
		{
			synchronized (Context.class)
			{
				if (singletonContext == null)
				{
					singletonContext = new Context();
				}
			}
		}
		return singletonContext;
	}
	
	private WorkspaceController workspaceController;
	private Workspace workspace;
	
	public Context() {
		/*
		 * workspace = new Workspace(); workspace.reset(); workspace.setl
		 */

		// Style list
		List<String[]> list = new ArrayList<String[]>();
		String[][] styles = {};
		
		//		{ "//BlockGenus[@name[starts-with(.,\"Tinker\")]]/@color", "128 0 0" },
		//		{ "//BlockGenus[@name[starts-with(.,\"df_\")]]/@color",	"0 128 0" } };

		for (String[] style : styles) {
			list.add(style);
		}
		functionZero();
		functionOne(list);
		
	}
	public String readFromJARFile(String filename)
			throws IOException
			{
			  InputStream is = getClass().getResourceAsStream(filename);
			  InputStreamReader isr = new InputStreamReader(is);
			  BufferedReader br = new BufferedReader(isr);
			  StringBuffer sb = new StringBuffer();
			  String line;
			  while ((line = br.readLine()) != null) 
			  {
			    sb.append(line);
			  }
			  br.close();
			  isr.close();
			  is.close();
			  return sb.toString();
			}
	
	public void functionZero()
	{
		
	}
	public void functionOne(List<String[]> list)
	{
		workspaceController = new WorkspaceController();
		workspaceController.resetWorkspace();
  		workspaceController.resetLanguage();
	//	workspaceController.setLangResourceBundle(ResourceBundle.getBundle("com/unibot/block/unibot"));
	//	workspaceController.setStyleList(list);
//		workspaceController.setLangDefDtd(this.getClass().getResourceAsStream(LANG_DTD_PATH));
		//tix loading genuses from xml
  		
		try {

			workspaceController.setLangDefFileString(readFromJARFile(UNIBOT_LANG_PATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workspaceController.loadFreshWorkspace();
	//	workspace =  new Workspace();
		workspaceChanged = false;
		highlightBlockSet = new HashSet<RenderableBlock>();
		ofls = new HashSet<OpenblocksFrameListener>();
	//	this.workspace = workspaceController.getWorkspace();
		
		isInArduino = true;
		
		//determine OS
		String osName = System.getProperty("os.name");
		osName = osName.toLowerCase();
		if (osName.contains("win"))
		{
			osType = Context.OsType.WINDOWS;
		}
		else
		{
			if (osName.contains("linux"))
			{
				osType = Context.OsType.LINUX;
			}
			else
			{
				if(osName.contains("mac"))
				{
					osType = Context.OsType.MAC;
				}
				else
				{
					osType = Context.OsType.UNKNOWN;
				}
			}
		}
		//
	}
	
	public File getArduinoFile(String name)
	{
		String path = System.getProperty("user.dir");
		if (osType.equals(OsType.MAC))
		{
			String javaroot = System.getProperty("javaroot");
			if (javaroot != null)
			{
				path = javaroot;
			}
		}
		File workingDir = new File(path);
		return new File(workingDir, name);
	}

	public WorkspaceController getWorkspaceController() {
		return workspaceController;
	}
	
	public Workspace getWorkspace()
	{
		return workspace;
	}

	public boolean isWorkspaceChanged()
	{
		return workspaceChanged;
	}

	public void setWorkspaceChanged(boolean workspaceChanged) {
		this.workspaceChanged = workspaceChanged;
	}
	
	public void highlightBlock(RenderableBlock block)
	{
		block.updateInSearchResults(true);
		highlightBlockSet.add(block);
	}
	
	public void cancelHighlightBlock(RenderableBlock block)
	{
		block.updateInSearchResults(false);
		highlightBlockSet.remove(block);
	}
	
	public void resetHightlightBlock()
	{
		for (RenderableBlock rb : highlightBlockSet)
		{
			rb.updateInSearchResults(false);
		}
		highlightBlockSet.clear();
	}

	
	public void saveUniBotFile(File saveFile, String saveString) throws IOException
	{
		if (!saveFile.exists())
		{
			saveFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(saveFile, false);
		fos.write(saveString.getBytes("UTF8"));
		fos.flush();
		fos.close();
		didSave();
	}
	
	public void loadUniBotFile(File savedFile) throws IOException
	{
		if (savedFile != null)
		{
			String saveFilePath = savedFile.getAbsolutePath();
			workspaceController.resetWorkspace();
			workspaceController.loadProjectFromPath(saveFilePath);
			didLoad();
		}
	}
	
	
	
	public boolean isInArduino() {
		return isInArduino;
	}

	public void setInArduino(boolean _isInArduino) {
		this.isInArduino = _isInArduino;
	}

	public String getArduinoVersionString() {
		return arduinoVersionString;
	}

	public void setArduinoVersionString(String arduinoVersionString) {
		this.arduinoVersionString = arduinoVersionString;
	}

	public OsType getOsType() {
		return osType;
	}

	public void registerOpenblocksFrameListener(OpenblocksFrameListener ofl)
	{
		ofls.add(ofl);
	}
	
	public void didSave()
	{
		for (OpenblocksFrameListener ofl : ofls)
		{
			ofl.didSave();
		}
	}
	
	public void didLoad()
	{
		for (OpenblocksFrameListener ofl : ofls)
		{
			ofl.didLoad();
		}
	}
	
	public void didGenerate(String sourcecode)
	{
		for (OpenblocksFrameListener ofl : ofls)
		{
			ofl.didGenerate(sourcecode);
		}
	}
}
