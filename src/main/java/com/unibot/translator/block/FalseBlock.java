package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class FalseBlock extends ConstBlock
{
	public FalseBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setCode("false");
	}
}
