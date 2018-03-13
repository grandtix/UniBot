package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

public class CustomVariableBlock extends TranslatorBlock
{
	public CustomVariableBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		
		String stb0 =null;
		
		try
		{
			stb0=this.getRequiredTranslatorBlockAtSocket(0).toCode();
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			if (!translator.isFromArduino())
				return label+"_"+translator.getRobotName();
			return label;
		}
	
		String stb1 = "";
		String stb2="";
		try
		{
			stb1=this.getRequiredTranslatorBlockAtSocket(1).toCode();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try
		{
			stb2=","+this.getRequiredTranslatorBlockAtSocket(2).toCode();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if (stb1!=null)
		return (stb0+"."+translator.getBlock(blockId).getGenusName()+"("+stb1+stb2+")");
		else
			return stb0+"."+translator.getBlock(blockId).getGenusName();
	
	}


}
