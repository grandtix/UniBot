package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.BlockException;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class SetterVariableBooleanBlock extends TranslatorBlock
{
	public SetterVariableBooleanBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String tb = "";
		String ret="";
		
		try {
			tb=this.getRequiredTranslatorBlockAtSocket(0).toCode();
			ret = tb;
			tb = this.getRequiredTranslatorBlockAtSocket(1).toCode();
			ret =ret + " = " + tb + ";\n";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return ret;
	}

}
