package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;
import com.unibot.util.HeaderIncludeGetter;

public class CustomConstructorBlock extends TranslatorBlock {
	public CustomConstructorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label, String desc)
	{
		super(blockId, translator, codePrefix, codeSuffix, label, desc);
	}
	public CustomConstructorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String t = "";
		TranslatorBlock tb = this.getRequiredTranslatorBlockAtSocket(0);
		
		String label=translator.getBlock(blockId).getGenusName();
		String nomclass=label.substring(0, label.indexOf('(')).replaceAll("nouveau ","");
		
		if (!this.getTranslator().isFromArduino())
		{ //processing

			t = nomclass + " " + tb.toCode() + " = new " + nomclass + "();\n";
		//	String tt = "// controles dela camera par la souris\nvoid mouseDragged() \n{\n\n" + tb.toCode() + ".onMouseDragged();\n}\n\nvoid mouseWheel(MouseEvent event)"
		//			+ " \n{\nfloat e = event.getCount();\n" + tb.toCode() + ".onMouseWheel(e);\n}\n	";
		
			
		//	translator.addCustomDawCode(tb.toCode() + ".paint();\n");
		//	translator.addMethodCustomCode(tt);
		}
		else
		{ //arduino
			
			t = nomclass + " " + tb.toCode() + ";\n";
			
			translator.addHeaderFile("#include<" + nomclass + ".h>\n");
			translator.addHeaderFile(new HeaderIncludeGetter().getIncludes(getComment()));
			


		}
		return t;
	}

}
