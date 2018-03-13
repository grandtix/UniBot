package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class NumberFloatBlock extends TranslatorBlock
{
	public NumberFloatBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode()
	{
		return codePrefix + label + codeSuffix;
	}

}
