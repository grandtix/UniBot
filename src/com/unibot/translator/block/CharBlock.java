package com.unibot.translator.block;

import com.unibot.translator.Translator;

public class CharBlock extends TranslatorBlock
{
	public CharBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode()
	{
		try {
			int val=Integer.parseInt(label);
			return codePrefix+ "" +  val + codeSuffix;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return codePrefix+ "'" +  label +"'"+ codeSuffix;
		}
		
	}

}
