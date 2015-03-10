package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class NoToneBlock extends TranslatorBlock
{
	public NoToneBlock(Long blockId, Translator translator, String codePrefix,	String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock pinBlock = this.getRequiredTranslatorBlockAtSocket(0);
		String ret = "noTone(" + pinBlock.toCode() + ");\n";
		return ret;
	}
}
