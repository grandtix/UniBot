package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class NumberBlock extends TranslatorBlock
{
	public NumberBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode()
	{
		return codePrefix + label + codeSuffix;
	}

}
