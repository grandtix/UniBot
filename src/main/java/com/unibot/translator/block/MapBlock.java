package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class MapBlock extends TranslatorBlock
{
	public MapBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String ret = "map ( ";
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		ret = ret + tb.toCode();
		ret = ret + " , ";
		tb = this.getRequiredTranslatorBlockAtSocket(1);
		ret = ret + tb.toCode();
		ret = ret + " , ";
		tb = this.getRequiredTranslatorBlockAtSocket(2);
		ret = ret + tb.toCode();
		ret = ret + " , ";
		tb = this.getRequiredTranslatorBlockAtSocket(3);
		ret = ret + tb.toCode();
		ret = ret + " , ";
		tb = this.getRequiredTranslatorBlockAtSocket(4);
		ret = ret + tb.toCode();
		ret = ret + " ) ";
		return codePrefix + ret + codeSuffix;
	}
	
}
