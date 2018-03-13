package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class DigitalHighBlock extends ConstBlock
{

	public DigitalHighBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.setCode("HIGH");
	}
}
