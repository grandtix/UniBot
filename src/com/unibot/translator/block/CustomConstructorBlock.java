package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;
import com.unibot.util.HeaderIncludeGetter;

public class CustomConstructorBlock extends TranslatorBlock {
	public CustomConstructorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix,
			String label, String desc) {
		super(blockId, translator, codePrefix, codeSuffix, label, desc);
	}

	public CustomConstructorBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix,
			String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException {
		String t = "";
		String tb ="";
		try {
			tb=this.getRequiredTranslatorBlockAtSocket(0).toCode();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}

		String label = translator.getBlock(blockId).getInitialLabel();
		String nomclass = label.substring(0, label.indexOf('(')).replaceAll("nouveau ", "");

		if (!this.getTranslator().isFromArduino()) { // processing

			t = nomclass + " " + tb + " = new " + nomclass + "();\n";
			//TODO ajout generation code de params constructeur pour processing
		} else { // arduino

			t = nomclass + " " + tb + "";
			String stb = "";
			try {
				stb = this.getRequiredTranslatorBlockAtSocket(1).toCode();
			} catch (Exception e) {

			}

			for (int i = 2; i < 10; i++) {
				try {
					stb += "," + this.getRequiredTranslatorBlockAtSocket(i).toCode();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
			if (stb.length() > 0)
				t += "(" + stb + ")";
			t += ";\n";

			translator.addHeaderFile("#include<" + nomclass + ".h>\n");
	//		translator.addHeaderFile(new HeaderIncludeGetter().getIncludes(getComment()));

		}
		return t;
	}

}
