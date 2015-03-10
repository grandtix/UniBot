package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.BlockException;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class DeclareVariableStringBlock extends TranslatorBlock
{
	public DeclareVariableStringBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		if (!(tb instanceof VariableStringBlock))
		{
			throw new BlockException(blockId, "string var must be string var");
		}

		String ret ="";
		
			ret ="String "+tb.toCode();
			

			
		tb = this.getRequiredTranslatorBlockAtSocket(1);
		ret =ret +" = " + tb.toCode() + " ;\n";
		
		return ret;
		}

}
