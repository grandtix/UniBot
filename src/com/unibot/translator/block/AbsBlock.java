package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class AbsBlock extends TranslatorBlock
	{

		public AbsBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
		{
			super(blockId, translator, codePrefix, codeSuffix, label);
		}

		public String toCode() throws SocketNullException, SubroutineNotDeclaredException
		{
			String ret = "abs( ";
			TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
			ret = ret + translatorBlock.toCode();
			ret = ret + " )";
			return codePrefix + ret + codeSuffix;
		}
		
	}
