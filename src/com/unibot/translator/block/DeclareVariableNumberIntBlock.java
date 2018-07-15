package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.BlockException;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class DeclareVariableNumberIntBlock extends TranslatorBlock
{
	public DeclareVariableNumberIntBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		String nom=tb.toCode();
		
		if (!(tb instanceof VariableBlock))
		{
			throw new BlockException(blockId, "int var must be int var");
		}

		
		String ret ="";
		tb = this.getRequiredTranslatorBlockAtSocket(1);
		ret ="int "+ nom + " = " + tb.toCode() + " ;\n";
		
		return ret;
	}

}
