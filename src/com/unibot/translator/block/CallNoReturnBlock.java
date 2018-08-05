package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.BlockException;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class CallNoReturnBlock extends TranslatorBlock
{
	public CallNoReturnBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		String ret ="";

		ret = tb.toCode()+";\n";
		return ret;
		}

}
