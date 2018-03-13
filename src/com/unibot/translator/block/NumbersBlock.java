package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class NumbersBlock extends TranslatorBlock {

	
	public NumbersBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public NumbersBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, boolean forglobal)
	{
		super(blockId, translator, codePrefix, codeSuffix, label, forglobal);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		return null;
	}


}
