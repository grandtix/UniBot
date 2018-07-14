package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class CustomCommandBlock extends TranslatorBlock
{
	
	
	public CustomCommandBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
String stb0 = this.getRequiredTranslatorBlockAtSocket(0).toCode();
String stb = "";

try{
		stb = this.getRequiredTranslatorBlockAtSocket(1).toCode();
	}
	catch(Exception e)
{
	
}

		
		for (int i=2;i<10;i++)
		{
		try
		{
			stb+=","+this.getRequiredTranslatorBlockAtSocket(i).toCode();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		}
		
		String code=stb0+"."+translator.getBlock(blockId).getInitialLabel()+"("+stb+");\n";
	//	String code=stb0+"."+translator.getBlock(blockId).getGenusName()+"("+stb+");\n";
		return code;
	}

	


}
