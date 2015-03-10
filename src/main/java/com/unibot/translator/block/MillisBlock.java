package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class MillisBlock extends TranslatorBlock
{
	public MillisBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode()
	{
		String ret = "millis()";
		return codePrefix + ret + codeSuffix;
	}
	
}
