package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class VariableDigitalBlock extends TranslatorBlock
{
	public VariableDigitalBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode()
	{
		if (translator.isFromArduino())
		{
			String internalVariableName = (label);
		
		
		return internalVariableName;
		}
		else
		//String ret = " ( " + internalVariableName + " ? true : false )";
		return label+"_"+translator.getRobotName();
		
	}

}
