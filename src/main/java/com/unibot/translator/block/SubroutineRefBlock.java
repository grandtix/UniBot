package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class SubroutineRefBlock extends TranslatorBlock
{

	public SubroutineRefBlock(Long blockId, Translator translator,
			String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String subroutineName = label.trim();
		if (!translator.isFromArduino())
			subroutineName+="_"+translator.getRobotName();
	//	System.out.println(subroutineName);
		if (!translator.containFunctionName(subroutineName))
		{
			throw new SubroutineNotDeclaredException(blockId);
		}
		return subroutineName + "();\n";
	}

}
