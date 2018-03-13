package com.unibot.translator.adaptor;

import com.unibot.translator.Translator;
import com.unibot.translator.block.TranslatorBlock;

public interface BlockAdaptor 
{
	public TranslatorBlock nextTranslatorBlock(Translator translator, Long blockId, String codePrefix, String codeSuffix);
	public TranslatorBlock getTranslatorBlockAtSocket(Translator translator, Long blockId, int i, String codePrefix, String codeSuffix);
}
