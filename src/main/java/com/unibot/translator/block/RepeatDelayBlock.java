package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class RepeatDelayBlock extends TranslatorBlock
{

	public RepeatDelayBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
	
		
		
		String varName = translator.buildVariableName("totime");
	//	translator.addDefinitionCommand("int " + varName + ";");
		
		
		
		String ret ="";
		
		if (!translator.isFromArduino())
			ret="long "+varName+" = System.currentTimeMillis()";
		else
			ret="long "+varName+" = millis()";
		
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		ret = ret + "+"+translatorBlock.toCode()+";\ndo {\n";
			
		
		translatorBlock = getTranslatorBlockAtSocket(1);
		while (translatorBlock != null)
		{
			ret = ret + translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		
		if (!translator.isFromArduino())
			ret = ret + "}while (System.currentTimeMillis()<"+varName+");\n\n";
		else
			ret = ret + "}while (millis()<"+varName+");\n\n";


		
		return ret;
	}

}
