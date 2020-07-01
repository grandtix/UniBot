package com.unibot.translator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.unibot.UniBot;
import com.unibot.translator.adaptor.BlockAdaptor;
import com.unibot.translator.adaptor.OpenBlocksAdaptor;
import com.unibot.translator.block.TranslatorBlock;
import com.unibot.translator.block.TranslatorBlockFactory;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNameDuplicatedException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;


public class Translator
{
	private static final String variablePrefix = "";
		
	private Set<String> headerFileSet;
	private Set<String> definitionSet;
	private Set<String> setupSet;
	
	private Set<String> drawSet;
	
//	private Set<String> initSet;
	private Set<String> functionNameSet;
	
	private Set<String> methodCustomSet;
	
	private BlockAdaptor blockAdaptor;
	
	private Set<Long> inputPinSet;
	private Set<Long> outputPinSet;
	
	private Map<String, String> numberVariableSet;
	private Map<String, String> booleanVariableSet;
	
	private Workspace workspace;
	
	private int variableCnt;
	private String sketchFolderName;

	public String getRobotName() {
		return sketchFolderName;
	}

	public void setSketchFolderName(String sketchFolderName) {
		this.sketchFolderName = sketchFolderName;
	}

	private Map<String, String> stringVariableSet;
	
	boolean fromArduino=true;
	//true =arduino
	//false =processing	
	

	public boolean isFromArduino()
	{
		return fromArduino;
	}

	public Translator(Workspace ws)
	{
		workspace = ws;
		reset();
//		if (UniBot.editor!=null)
	//rix	if (UniBot.editor.getClass().toString().equals("class processing.app.Editor"))
			fromArduino=true;

	}
	public String generateDrawCustomCode()
	{
		StringBuilder st=new StringBuilder();
		if (!drawSet.isEmpty())
		{
			for (String g:drawSet)
				st.append(g+"\n");
		}
		return st.toString();
	}
	
	
	
	public String generateMethodCustomCode()
	{
		StringBuilder st=new StringBuilder();
		if (!methodCustomSet.isEmpty())
		{
			for (String g:methodCustomSet)
				st.append(g+"\n");
		}
		return st.toString();
	}
	
	public String generateSetupCustomCode()
	{
		StringBuilder st=new StringBuilder();
		if (!setupSet.isEmpty())
		{
			for (String g:setupSet)
				st.append(g+"\n");
		}
		return st.toString();
	}
	public String genreateHeaderCommand()
	{
		StringBuilder headerCommand = new StringBuilder();
//		StringBuilder headerCommandDeclaration = new StringBuilder();

	

		if (!headerFileSet.isEmpty())
		{
			headerCommand.append("// declaration des librairies utilisées");
			headerCommand.append("\n");
			
			for (String file:headerFileSet)
			{
				headerCommand.append( file);
			}
			headerCommand.append("\n");
		}
		
		return headerCommand.toString();
	}
	
	public String translate(Long blockId) throws SocketNullException, SubroutineNotDeclaredException
	{
		
		
		TranslatorBlockFactory translatorBlockFactory = new TranslatorBlockFactory();
		Block block = workspace.getEnv().getBlock(blockId);
		TranslatorBlock rootTranslatorBlock = translatorBlockFactory.buildTranslatorBlock(this, blockId, block.getGenusName(), "", "", block.getBlockLabel(), fromArduino, block.getBlockDescription());
		
		return rootTranslatorBlock.toCode();
	}
	
	
	public String translate(Long blockId, boolean forglobal) throws SocketNullException, SubroutineNotDeclaredException
	{
		
		
		TranslatorBlockFactory translatorBlockFactory = new TranslatorBlockFactory();
		Block block = workspace.getEnv().getBlock(blockId);
		
		TranslatorBlock rootTranslatorBlock = translatorBlockFactory.buildTranslatorBlock(this, blockId, block.getGenusName(),  "","", block.getBlockLabel(), forglobal, block.getBlockDescription());
		
		return rootTranslatorBlock.toCode();
	}
	

	public BlockAdaptor getBlockAdaptor()
	{
		return blockAdaptor;
	}
	
	public void reset()
	{
		headerFileSet = new HashSet<String>();
		definitionSet = new HashSet<String>();
		setupSet = new HashSet<String>();
		drawSet = new HashSet<String>();
		methodCustomSet = new HashSet<String>();
		
		functionNameSet = new HashSet<String>();
		inputPinSet = new HashSet<Long>();
		outputPinSet = new HashSet<Long>();
		
		numberVariableSet = new HashMap<String, String>();
		booleanVariableSet = new HashMap<String, String>();
		stringVariableSet = new HashMap<String, String>();
		blockAdaptor = buildOpenBlocksAdaptor();
		
		variableCnt = 0;
	}
	
	private BlockAdaptor buildOpenBlocksAdaptor()
	{
		return new OpenBlocksAdaptor();
	}
	
	public void addHeaderFile(String headerFile)
	{
		headerFileSet.add(headerFile);
	}
	
	public void addSetupCommand(String command)
	{
		setupSet.add(command);
	}

	public void addCustomDawCode(String code)
	{
		drawSet.add(code);
	}
	public void addMethodCustomCode(String code)
	{
		methodCustomSet.add(code);
	}
	
	
	public void addDefinitionCommand(String command)
	{
		definitionSet.add(command);
	}
	
	public void addInputPin(Long pinNumber)
	{
		inputPinSet.add(pinNumber);
	}
	
	public void addOutputPin(Long pinNumber)
	{
		outputPinSet.add(pinNumber);
	}
	
	public String getNumberVariable(String userVarName)
	{
		return numberVariableSet.get(userVarName);
	}
	
	public String getBooleanVariable(String userVarName)
	{
		return booleanVariableSet.get(userVarName);
	}
	public String getStringVariable(String userVarName)
	{
		return stringVariableSet.get(userVarName);
	}
	public void addNumberVariable(String userVarName, String internalName)
	{
		numberVariableSet.put(userVarName, internalName);
	}
	
	public void addBooleanVariable(String userVarName, String internalName)
	{
		booleanVariableSet.put(userVarName, internalName);
	}
	
	public void addFunctionName(Long blockId, String functionName) throws SubroutineNameDuplicatedException
	{
		if (functionName.equals("loop") ||functionName.equals("setup") || functionNameSet.contains(functionName))
		{
			throw new SubroutineNameDuplicatedException(blockId);
		}
	//	//System.out.println("in add function name: "+functionName);
		functionNameSet.add(functionName);
	}
	
	public boolean containFunctionName(String name)
	{
		return functionNameSet.contains(name.trim());
	}
	
	
	public String buildVariableName()
	{
		return buildVariableName("");
	}
	
	public String buildVariableName(String reference)
	{
		variableCnt = variableCnt + 1;
		String varName = variablePrefix;//+ variableCnt + "_";
		int i;
		for (i=0; i<reference.length(); ++i)
		{
			char c = reference.charAt(i);
			if (Character.isLetter(c) || Character.isDigit(c) || (c == '_'))
			{
				varName = varName + c;
			}
		}
		return varName+"_"+variableCnt;
	}
	
	

	public Workspace getWorkspace() {
		return workspace;
	}
	
	public Block getBlock(Long blockId) {
		return workspace.getEnv().getBlock(blockId);
	}

	public void addStringVariable(String userVarName, String internalName)
	{
		stringVariableSet.put(userVarName, internalName);
		
	}
}
