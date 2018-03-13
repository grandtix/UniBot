package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class SerialPrintlnBlock extends TranslatorBlock
{
	public SerialPrintlnBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String ret="";
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		
		ret = translatorBlock.toCode();

		if (translator.isFromArduino())
		{
		translator.addSetupCommand("Serial.begin(9600);\n");
		ret = "Serial.println("+ret+");\n";
		}
		else
			ret="println("+ret+");\n";
		
		return ret;
	}
}
