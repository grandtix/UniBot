package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.BlockException;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class DeclareVariableNumberFloatBlock extends TranslatorBlock
{
	public DeclareVariableNumberFloatBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		String nom=tb.toCode();
		

		if (!(tb instanceof VariableNumberBlock))
		{
			throw new BlockException(blockId, "float var must be float var");
		}

		String ret ="";
		tb = this.getRequiredTranslatorBlockAtSocket(1);
		ret ="float "+ nom + " = " + tb.toCode() + " ;\n";
		
		return ret;
	}

}
