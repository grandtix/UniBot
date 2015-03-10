package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class LoopBlock extends TranslatorBlock {
	
	public LoopBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String ret="";
		if (this.getTranslator().isFromArduino())

			ret = "//methode principale, le programme boucle dessus\nvoid loop()\n{\n";
		else
		{
			ret = "while (true){\n";
		
		//	translator.addSetupCommand("//appel du thread\nthread(\"programme\");");
		}
		
		TranslatorBlock translatorBlock = getTranslatorBlockAtSocket(0);
		
		
		while (translatorBlock != null)
		{
			ret = ret + translatorBlock.toCode();
			translatorBlock = translatorBlock.nextTranslatorBlock();
		}
		
		if (!getTranslator().isFromArduino())
			ret=ret+"\n}\n";
		ret = ret + "}\n\n";
		return ret;
	}
}
