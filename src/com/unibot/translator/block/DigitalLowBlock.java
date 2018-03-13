package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class DigitalLowBlock extends ConstBlock
{

	public DigitalLowBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setCode("LOW");
	}
}