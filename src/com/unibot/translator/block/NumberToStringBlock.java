package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class NumberToStringBlock extends TranslatorBlock {

	public NumberToStringBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String ret = "";
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		String nomString = translatorBlock.toCode();

		translatorBlock = this.getRequiredTranslatorBlockAtSocket(1);
		String nomint = translatorBlock.toCode();

		String tmpvar = "tempvar_" + System.currentTimeMillis() % 1000;
		ret = "char " + tmpvar + "[]=\"\";\n";
		ret = ret + "dtostrf(" + nomint + ",1,2," + tmpvar + ");\n";

		if (translator.isFromArduino())
		{

			ret = ret + nomString + " = String(" + tmpvar + ");\n";
		}
		
		else
		{	ret = nomString + "=" + "String.valueOf(" +nomint+ ");\n";
		}

		return ret;
	}
}
