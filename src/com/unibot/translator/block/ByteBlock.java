package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class ByteBlock extends TranslatorBlock
{
	public ByteBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode()
	{
		return codePrefix+ "(byte)" +  label + codeSuffix;
	}

}
