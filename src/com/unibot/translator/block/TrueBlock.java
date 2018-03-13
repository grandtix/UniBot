package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class TrueBlock extends ConstBlock
{
	public TrueBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setCode("true");
	}
}
