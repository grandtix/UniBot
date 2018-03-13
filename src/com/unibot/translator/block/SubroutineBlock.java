package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class SubroutineBlock extends TranslatorBlock
{

	public SubroutineBlock(Long blockId, Translator translator,
			String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
				TranslatorBlock translatorBlock = getRequiredTranslatorBlockAtSocket(0);
				String nomFunction=translatorBlock.toCode().replaceAll("\"", "");
		String ret;
		if (translator.isFromArduino())
		ret = "void " + nomFunction + "()\n{\n";
		else
			ret = "void " + nomFunction+"_"+translator.getRobotName() + "()\n{\n";
				
		translatorBlock =  getTranslatorBlockAtSocket(1);
		while (translatorBlock != null)
		{
			ret = ret + translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		ret = ret + "}\n\n";
		return ret;
	}
}
