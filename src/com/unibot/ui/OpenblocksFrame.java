package com.unibot.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import processing.app.Editor;

import com.unibot.UniBot;
import com.unibot.core.Context;
import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.BlockException;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNameDuplicatedException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;
import com.unibot.ui.listener.DisplayCodeButtonListener;
import com.unibot.ui.listener.LoadCodeToArduinoProcessingButtonListener;
import com.unibot.ui.listener.ManageImportCodeButtonListener;
import com.unibot.ui.listener.NewABPButtonListener;
import com.unibot.ui.listener.OpenABPButtonListener;
import com.unibot.ui.listener.OpenblocksFrameListener;
import com.unibot.ui.listener.SaveABPButtonListener;
import com.unibot.ui.listener.SaveAsABPButtonListener;
import com.unibot.ui.listener.TeleverserCodeButtonListener;
import com.unibot.ui.listener.UniBotWorkspaceListener;
import com.unibot.util.LibraryLoader;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.codeblocks.BlockGenus;
import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.workspace.PageDrawerLoadingUtils;
import edu.mit.blocks.workspace.Workspace;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpenblocksFrame extends JFrame {

	public static boolean isLaunched = false;

	private static final long serialVersionUID = 2841155965906223806L;

	public Context context;
	public String saveFilePath;
	private String saveFileName;
	private JFileChooser fileChooser;
	private FileFilter ffilter;
	private ResourceBundle uiMessageBundle;

	private Editor editor;
	private String pathConf = "librairies.conf";

	public String getPathConf() {
		return pathConf;
	}

	public void addListener(OpenblocksFrameListener ofl) {
		context.registerOpenblocksFrameListener(ofl);
	}

	public String makeFrameTitle() {
		String title = Context.APP_NAME + " " + saveFileName;
		if (context.isWorkspaceChanged()) {
			title = title + " *";
		}
		return title;

	}

	public OpenblocksFrame(Editor editor) {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {

				doCloseWindows();

			}
		});
		this.editor = editor;
		saveFilePath = null;
		saveFileName = "sans titre";

		context = Context.getContext();
		this.setTitle(makeFrameTitle());
		this.setSize(new Dimension(800, 600));
//		setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
		getContentPane().setLayout(new BorderLayout());
		// put the frame to the center of screen
		this.setLocationRelativeTo(null);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		uiMessageBundle = ResourceBundle.getBundle("com/unibot/block/unibot");

		fileChooser = new JFileChooser();
		ffilter = new FileNameExtensionFilter(
				uiMessageBundle.getString("unibot.file.suffix"), "abp");
		fileChooser.setFileFilter(ffilter);
		fileChooser.addChoosableFileFilter(ffilter);
		context.setWorkspaceChanged(true);

		initOpenBlocks();
	}

	private void initOpenBlocks() {
		Context context = Context.getContext();

		/*
		 * WorkspaceController workspaceController =
		 * context.getWorkspaceController(); JComponent workspaceComponent =
		 * workspaceController.getWorkspacePanel();
		 */

		Workspace workspace = context.getWorkspace();

		// WTF I can't add worksapcelistener by workspace contrller
		workspace.addWorkspaceListener(new UniBotWorkspaceListener(this));
		workspace.setBackground(Color.black);
		JPanel buttons = new JPanel();
		JButton saveButton = new JButton();// uiMessageBundle.getString("unibot.ui.save"));
		saveButton.setToolTipText("Enregistrer");
		saveButton.setIcon(new ImageIcon(OpenblocksFrame.class
				.getResource("/com/unibot/block/save.png")));

		saveButton.addActionListener(new SaveABPButtonListener(this));

		JButton manageImportButton = new JButton();
		manageImportButton.setToolTipText("G\u00E9rer les librairies");
		manageImportButton.setIcon(new ImageIcon(OpenblocksFrame.class
				.getResource("/com/unibot/block/gererLib.png")));
		ManageImportCodeButtonListener tt2 = new ManageImportCodeButtonListener(
				this, context);
		manageImportButton.addActionListener(tt2);
		buttons.setLayout(new GridLayout(1, 5, 0, 0));

		JButton buttonBlank = new JButton();
		buttonBlank.setToolTipText("Nouveau");
		buttonBlank.setIcon(new ImageIcon(OpenblocksFrame.class.getResource("/com/unibot/block/new_file.png")));

		buttonBlank.addActionListener(new NewABPButtonListener(this));
		buttons.add(buttonBlank);
		JButton openButton = new JButton();// uiMessageBundle.getString("unibot.ui.load"));
		openButton.setToolTipText("Ouvrir");
		openButton.setIcon(new ImageIcon(OpenblocksFrame.class
				.getResource("/com/unibot/block/open.png")));
		openButton.addActionListener(new OpenABPButtonListener(this));
		buttons.add(openButton);

		buttons.add(saveButton);
		
		JButton button = new JButton();
		button.setToolTipText("Enregistrer sous...");
		button.addActionListener(new SaveAsABPButtonListener(this));
		
		button.setIcon(new ImageIcon(OpenblocksFrame.class.getResource("/com/unibot/block/saveas.png")));
		buttons.add(button);

		if (!(editor == null)) {
			JButton loadCodeToIDEButton = new JButton();
			loadCodeToIDEButton.setToolTipText("Générer le code Arduino");

			LoadCodeToArduinoProcessingButtonListener tt3 = new LoadCodeToArduinoProcessingButtonListener(
					this, context);
			loadCodeToIDEButton.addActionListener(tt3);

			if (editor.getClass().toString()
					.equals("class processing.app.Editor"))
				loadCodeToIDEButton.setIcon(new ImageIcon(OpenblocksFrame.class
						.getResource("/com/unibot/block/dessinToArduino.png")));
			else
				loadCodeToIDEButton
						.setIcon(new ImageIcon(
								OpenblocksFrame.class
										.getResource("/com/unibot/block/dessinToProcessing.png")));

			buttons.add(loadCodeToIDEButton);

			JButton generateButton = new JButton();// uiMessageBundle.getString("unibot.ui.upload"));
			
			if (editor.getClass().toString()
					.equals("class processing.app.Editor"))
			{	generateButton
						.setIcon(new ImageIcon(
								OpenblocksFrame.class
										.getResource("/com/unibot/block/dessinTeleverserArduino.png")));
			generateButton.setToolTipText("Téléverser le programme sur la carte Arduino");

			}
			else
			{
				generateButton
						.setIcon(new ImageIcon(
								OpenblocksFrame.class
										.getResource("/com/unibot/block/dessinTeleverserProcessing.png")));
				generateButton.setToolTipText("Téléverser le programme dans le monde virtuel");

			}
			generateButton.addActionListener(new TeleverserCodeButtonListener(
					this, context));

			if (Context.getContext().isInArduino())
				buttons.add(generateButton);

		}
		else
		{
			JButton generateButton = new JButton();// uiMessageBundle.getString("unibot.ui.upload"));
			generateButton
			.setIcon(new ImageIcon(
					OpenblocksFrame.class
							.getResource("/com/unibot/block/dessinTeleverserProcessing.png")));
	generateButton.setToolTipText("Téléverser le programme dans le monde virtuel");


		generateButton.addActionListener(new DisplayCodeButtonListener(
		this, context));	
		buttons.add(generateButton);
		}

		// buttons.add(importButton);
		buttons.add(manageImportButton);

		getContentPane().add(buttons, BorderLayout.NORTH);
		getContentPane().add(workspace, BorderLayout.CENTER);
	}

	public void loadLibs() {
		BufferedReader br;
		try {

			File file = new File(getPathConf());
			if (!file.exists())
				file.createNewFile();
System.out.println(file.getAbsolutePath());
			br = new BufferedReader(new FileReader(getPathConf()));
			String line;

			while ((line = br.readLine()) != null) {
				// process the line.
				line = line.trim();
				if (line.split("=")[2].equals("true"))
					getFile(line.split("=")[1]);

			}
			br.close();
			context.setWorkspaceChanged(false);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void doCloseWindows() {
		if (!context.isWorkspaceChanged()) {
			int reponse = JOptionPane.showConfirmDialog(OpenblocksFrame.this,
					"Voulez-vous quitter Unibot?", "Confirmation",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (reponse == JOptionPane.YES_OPTION) {
				OpenblocksFrame.this.context.getWorkspaceController()
						.resetWorkspace();
				OpenblocksFrame.this.context.getWorkspaceController()
						.loadFreshWorkspace();
			//	System.exit(0);
				saveFilePath=null;
				saveFileName = "sans titre";
			context.setWorkspaceChanged(true);
				this.setTitle(this.makeFrameTitle());
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		} else {
			int reponse = JOptionPane
					.showConfirmDialog(
							OpenblocksFrame.this,
							"Voulez-vous sauvegarder votre programme avant de quitter Unibot?",
							"Confirmation", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);
			if (reponse == JOptionPane.YES_OPTION) {
				doSaveUniBotFile();
				OpenblocksFrame.this.context.getWorkspaceController()
						.resetWorkspace();
				OpenblocksFrame.this.context.getWorkspaceController()
						.loadFreshWorkspace();
				//System.exit(0);
				saveFilePath=null;
				saveFileName = "sans titre";
				context.setWorkspaceChanged(true);
				this.setTitle(this.makeFrameTitle());
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			} else 
				if (reponse == JOptionPane.CANCEL_OPTION) 
			{
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			} else if (reponse == JOptionPane.NO_OPTION) 
			{
				OpenblocksFrame.this.context.getWorkspaceController()
						.resetWorkspace();
				OpenblocksFrame.this.context.getWorkspaceController()
						.loadFreshWorkspace();
			//	System.exit(0);
				saveFilePath=null;
				saveFileName = "sans titre";
				context.setWorkspaceChanged(true);
				this.setTitle(this.makeFrameTitle());
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}
		}
	}

	public void doOpenUniBotFile() {
		if (context.isWorkspaceChanged()) {
			int optionValue = JOptionPane.showOptionDialog(this,
					uiMessageBundle.getString("message.content.open_unsaved"),
					uiMessageBundle.getString("message.title.question"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null,
					JOptionPane.YES_OPTION);
			if (optionValue == JOptionPane.YES_OPTION) {
				doSaveUniBotFile();
				this.loadFile();
			} else {
				if (optionValue == JOptionPane.NO_OPTION) {
					this.loadFile();
				}
			}
		} else {
			this.loadFile();
		}
		this.setTitle(makeFrameTitle());
	}

	public void doBlankUniBotFile() {
		if (context.isWorkspaceChanged()) {
			int optionValue = JOptionPane.showOptionDialog(this,
					uiMessageBundle.getString("message.content.open_unsaved"),
					uiMessageBundle.getString("message.title.question"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null,
					JOptionPane.YES_OPTION);
			if (optionValue == JOptionPane.YES_OPTION) {
				doSaveUniBotFile();
				context.getWorkspaceController().resetWorkspace();
				context.getWorkspaceController().loadFreshWorkspace();
				saveFilePath=null;
				saveFileName = "sans titre";
				context.setWorkspaceChanged(true);
				this.setTitle(makeFrameTitle());
				loadLibs();
				
			} else {
				if (optionValue == JOptionPane.NO_OPTION) {
					context.getWorkspaceController().resetWorkspace();
					context.getWorkspaceController().loadFreshWorkspace();
					saveFilePath=null;
					saveFileName = "sans titre";
					context.setWorkspaceChanged(true);
					this.setTitle(this.makeFrameTitle());
					loadLibs();
					
				} else {
					if (optionValue == JOptionPane.CANCEL_OPTION) {

					}
				}
			}
		} else {
			context.getWorkspaceController().resetWorkspace();
			context.getWorkspaceController().loadFreshWorkspace();
			saveFilePath=null;
			saveFileName = "sans titre";
			context.setWorkspaceChanged(true);
			this.setTitle(this.makeFrameTitle());
			loadLibs();
			
		}
		this.setTitle(makeFrameTitle());
	}

	private void loadFile() {
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File savedFile = fileChooser.getSelectedFile();
			if (!savedFile.exists()) {
				JOptionPane.showOptionDialog(this,
						uiMessageBundle.getString("message.file_not_found"),
						uiMessageBundle.getString("message.title.error"),
						JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
						null, JOptionPane.OK_OPTION);
				return;
			}

			saveFilePath = savedFile.getAbsolutePath();
			saveFileName = savedFile.getName();
			try {
				context.loadUniBotFile(savedFile);
				context.setWorkspaceChanged(false);
				loadLibs();

			} catch (IOException e) {
				JOptionPane.showOptionDialog(this,
						uiMessageBundle.getString("message.file_not_found"),
						uiMessageBundle.getString("message.title.error"),
						JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null,
						null, JOptionPane.OK_OPTION);
				e.printStackTrace();
			}
		}
	}

	public void doSaveAsUniBotFile()
	{
		saveFilePath=null;
		save();
		
	}
	public void doSaveUniBotFile() {
		if (context.isWorkspaceChanged()) {
			save();
		}
	}
private void save()
{
	
	try {
		WorkspaceController workspaceController = context
				.getWorkspaceController();
		String saveString = workspaceController.getSaveString();
//
		if (saveFilePath == null) {
			int chooseResult;
			chooseResult = fileChooser.showSaveDialog(this);
			if (chooseResult == JFileChooser.APPROVE_OPTION) {
				File saveFile = fileChooser.getSelectedFile();
				saveFile = checkFileSuffix(saveFile);
				if (saveFile != null) {
					if (saveFile.exists()) {
						int optionValue = JOptionPane
								.showOptionDialog(
										this,
										uiMessageBundle
												.getString("message.content.overwrite"),
										uiMessageBundle
												.getString("message.title.question"),
										JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE,
										null, null,
										JOptionPane.YES_OPTION);
						if (optionValue != JOptionPane.YES_OPTION) {
							return;
						}
					}
					context.saveUniBotFile(saveFile, saveString);
					saveFilePath = saveFile.getAbsolutePath();
					saveFileName = saveFile.getName();
					context.setWorkspaceChanged(false);
					this.setTitle(this.makeFrameTitle());

				}
			}
		} else {
			File saveFile = new File(saveFilePath);
			context.saveUniBotFile(saveFile, saveString);
			saveFilePath = saveFile.getAbsolutePath();
			saveFileName = saveFile.getName();
			context.setWorkspaceChanged(false);
			this.setTitle(this.makeFrameTitle());

		}
	} catch (Exception ex) {
		ex.printStackTrace();
	}

}
	public void genererCode(Workspace workspace, Context context,
			JFrame parentFrame, ResourceBundle uiMessageBundle,
			boolean toTeleverser) {
		boolean success;
		success = true;
		Translator translator = new Translator(workspace);
		try {
			translator.setSketchFolderName(editor.getName().replace("robot", "").replace(".pde", ""));
		} catch (Exception e4) {
			// TODO si on est hors editeur
			//e4.printStackTrace();
		}
		translator.reset();

		Iterable<RenderableBlock> renderableBlocks = workspace
				.getRenderableBlocks();
		Set<RenderableBlock> setupBlockSet = new HashSet<RenderableBlock>();
		Set<RenderableBlock> declareBlockSet = new HashSet<RenderableBlock>();
		Set<RenderableBlock> loopBlockSet = new HashSet<RenderableBlock>();
		Set<RenderableBlock> subroutineBlockSet = new HashSet<RenderableBlock>();
		StringBuilder code = new StringBuilder();

		Iterator<RenderableBlock> it = renderableBlocks.iterator();

		while (it.hasNext()) {
			RenderableBlock renderableBlock = it.next();

			Block block = renderableBlock.getBlock();

			if (!block.hasPlug()
					&& (Block.NULL.equals(block.getBeforeBlockID()))) {

				if (block.getGenusName().equals("setupData")) {
					setupBlockSet.add(renderableBlock);
				}
				if (block.getGenusName().equals("declareData")) {
					declareBlockSet.add(renderableBlock);
				}
				if (block.getGenusName().equals("loop")) {
					loopBlockSet.add(renderableBlock);
				}

				// System.out.println("block genus name :"+block.getGenusName());
				if (block.getGenusName().equals("subroutine")) {

					String functionName = translator
							.getBlock(block.getSocketAt(0).getBlockID())
							.getBlockLabel().trim();
					try {
						if (translator.isFromArduino())
							translator.addFunctionName(block.getBlockID(),
									functionName);
						else
							translator.addFunctionName(
									block.getBlockID(),
									functionName + "_"
											+ translator.getRobotName());

					} catch (SubroutineNameDuplicatedException e1) {
						context.highlightBlock(renderableBlock);
						// find the second subroutine whose name is defined, and
						// make it highlight. though it cannot happen due to
						// constraint of OpenBlocks -_-
						JOptionPane
								.showMessageDialog(
										parentFrame,
										uiMessageBundle
												.getString("unibot.translator.exception.subroutineNameDuplicated"),
										uiMessageBundle
												.getString("unibot.translator.exception.title"),
										JOptionPane.ERROR_MESSAGE);
						return;
					}
					subroutineBlockSet.add(renderableBlock);
				}
			}
		}

		if (setupBlockSet.size() == 0) {
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle
					.getString("unibot.translator.exception.noSetupFound"),
					uiMessageBundle
							.getString("unibot.translator.exception.title"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (loopBlockSet.size() == 0) {
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle
					.getString("unibot.translator.exception.noLoopFound"),
					uiMessageBundle
							.getString("unibot.translator.exception.title"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (loopBlockSet.size() > 1) {
			for (RenderableBlock rb : loopBlockSet) {
				context.highlightBlock(rb);
			}
			JOptionPane
					.showMessageDialog(
							parentFrame,
							uiMessageBundle
									.getString("unibot.translator.exception.multipleLoopFound"),
							uiMessageBundle
									.getString("unibot.translator.exception.title"),
							JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {

			for (RenderableBlock renderableBlock : declareBlockSet) {
				if (!translator.isFromArduino())
					code.append("/*\n code généré pour PROCESSING par UniBot!!! \n*/\n\n");

				Block declareBlock = renderableBlock.getBlock();
				code.append("//declaration des variables\n");
				code.append(translator.translate(declareBlock.getBlockID(),
						true));
			}

			StringBuilder tmp = new StringBuilder();
			for (RenderableBlock renderableBlock : loopBlockSet) {
				Block loopBlock = renderableBlock.getBlock();
				if (!translator.isFromArduino()) {
					// System.out.println();
					// tmp.append("void draw()\n{\n");
					tmp.append(translator.generateDrawCustomCode());
					// tmp.append("\n}\n}\n");
				}
				
				tmp.append(translator.translate(loopBlock.getBlockID()));
			}
			for (RenderableBlock renderableBlock : setupBlockSet) {
				Block setBlock = renderableBlock.getBlock();
				if (translator.isFromArduino()) {
					code.append("//initialisation des données du programme\n");
					code.append("void setup(){\n");
				} else {
					code.append("//methode appelée en thread par le programme principal\n");

					code.append("void programme" + translator.getRobotName()
							+ "()\n{\n");
				}
				// System.out.println("*****************");
				// System.out.println("1"+editor.getSketch().getPrimaryFile().getName());
				// System.out.println("2" +
				// editor.getSketch().getMainFilePath());
				// System.out.println("3"+editor.getSketch().getPrimaryFile().getName());
				// System.out.println("4"
				// + editor.getSketch().getCurrentCode().getFileName());
				// System.out.println("*****************");

				code.append(translator.translate(setBlock.getBlockID(), true));
				code.append(translator.generateSetupCustomCode());
				if (translator.isFromArduino())
					code.append("\n}\n");
			}

			code.append(tmp.toString());

			for (RenderableBlock renderableBlock : subroutineBlockSet) {
				Block subroutineBlock = renderableBlock.getBlock();
				code.append(translator.translate(subroutineBlock.getBlockID()));
			}

			code.append(translator.generateMethodCustomCode());
			code.insert(0, translator.genreateHeaderCommand());
			if (translator.isFromArduino())
				code.insert(0,
						"/*\n code généré pour ARDUINO par UniBot!!! \n*/\n\n");

		} catch (SocketNullException e1) {
			e1.printStackTrace();
			success = false;
			Long blockId = e1.getBlockId();
			Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
			for (RenderableBlock renderableBlock2 : blocks) {
				Block block2 = renderableBlock2.getBlock();
				if (block2.getBlockID().equals(blockId)) {
					context.highlightBlock(renderableBlock2);
					break;
				}
			}
			JOptionPane.showMessageDialog(parentFrame, uiMessageBundle
					.getString("unibot.translator.exception.socketNull"),
					uiMessageBundle
							.getString("unibot.translator.exception.title"),
					JOptionPane.ERROR_MESSAGE);
		} catch (BlockException e2) {
			e2.printStackTrace();
			success = false;
			Long blockId = e2.getBlockId();
			Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
			for (RenderableBlock renderableBlock2 : blocks) {
				Block block2 = renderableBlock2.getBlock();
				if (block2.getBlockID().equals(blockId)) {
					context.highlightBlock(renderableBlock2);
					break;
				}
			}
			JOptionPane
					.showMessageDialog(
							parentFrame,
							uiMessageBundle
									.getString("unibot.translator.exception.subroutineNameDuplicated"),
							uiMessageBundle
									.getString("unibot.translator.exception.title"),
							JOptionPane.ERROR_MESSAGE);
		} catch (SubroutineNotDeclaredException e3) {
			e3.printStackTrace();
			success = false;
			Long blockId = e3.getBlockId();
			Iterable<RenderableBlock> blocks = workspace.getRenderableBlocks();
			for (RenderableBlock renderableBlock3 : blocks) {
				Block block2 = renderableBlock3.getBlock();
				if (block2.getBlockID().equals(blockId)) {
					context.highlightBlock(renderableBlock3);
					break;
				}
			}
			JOptionPane
					.showOptionDialog(
							parentFrame,
							uiMessageBundle
									.getString("unibot.translator.exception.subroutineNotDeclared"),
							"Error", JOptionPane.OK_OPTION,
							JOptionPane.ERROR_MESSAGE, null, null,
							JOptionPane.OK_OPTION);

		}

		if (success) {

			if (context.isInArduino())
				try {
					UniBot.editor.getCurrentTab().setText(code.toString());
				} catch (Exception e1) {
					// TODO gerer si on est hors editeur (eclipse ou exec directe)
					System.out.println(code.toString());
				}
			else {
				System.out.println(code.toString());
			}
			if (!translator.isFromArduino())
				try {
					editor.getCurrentTab().setText(code.toString());
				//tix2018	editor.getCurrentTab().gete;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(code.toString());
				}
			if (toTeleverser) {
				context.didGenerate(code.toString());

			}
		}

	}

	public void getFile(String path) {
		System.out.println(path);
		InputStream stream = null;
		if (path == null) {
			context.getWorkspaceController().setLangDefDtd(
					this.getClass().getResourceAsStream(
							"/com/unibot/block/lang_def.dtd"));
			FileNameExtensionFilter ffilter = new FileNameExtensionFilter(
					uiMessageBundle.getString("unibot.libraryloader.suffix"),
					"h");

			fileChooser.setFileFilter(ffilter);
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File savedFile = fileChooser.getSelectedFile();
				stream = new LibraryLoader().toInputStream(savedFile
						.getAbsolutePath());
			}
		} else
			stream = new LibraryLoader().toInputStream(path);

		if (stream != null) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder builder;
			final Document doc;
			try {

				builder = factory.newDocumentBuilder();
/*				if (context.getWorkspaceController().getLangDefDtd() != null) {
					builder.setEntityResolver(new EntityResolver() {
						public InputSource resolveEntity(String publicId,
								String systemId) throws SAXException,
								IOException {
							return new InputSource(context
									.getWorkspaceController().getLangDefDtd());
						}
					});
				}

	*/			doc = builder.parse(stream);
				// TODO modify the L10N text and style here
				try {
					context.getWorkspaceController().ardublockLocalize(doc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				context.getWorkspaceController().ardublockStyling(doc);

				Element langDefRoot = doc.getDocumentElement();

				BlockGenus.loadBlockGenera(context.getWorkspace(), langDefRoot);
				PageDrawerLoadingUtils.loadBlockDrawerSets(context
						.getWorkspace(), langDefRoot, context.getWorkspace()
						.getFactoryManager());

			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	private File checkFileSuffix(File saveFile) {
		String filePath = saveFile.getAbsolutePath();
		if (filePath.endsWith(".abp")) {
			return saveFile;
		} else {
			return new File(filePath + ".abp");
		}
	}

	public void setPathConf(String _pathConf) {
		this.pathConf = _pathConf;
	}
}
