package com.unibot.translator.block;

import com.unibot.translator.Translator;

public abstract class ConstBlock extends TranslatorBlock
{
	

	private String code;
	protected ConstBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
		this.code = "undefined";
	}

	

	protected void setCode(String code)
	{
		this.code = code;
	}
	
	public String toCode()
	{
		return codePrefix + code + codeSuffix;
	}

}
