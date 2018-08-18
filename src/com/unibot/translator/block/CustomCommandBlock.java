package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.BlockConnector.PositionType;

public class CustomCommandBlock extends TranslatorBlock {

	public CustomCommandBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException {

		String stb0;
		try {
			stb0 = this.getRequiredTranslatorBlockAtSocket(0).toCode();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return "";
		}
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
		if (translator.getBlock(blockId).getSocketAt(0).getPositionType().equals(PositionType.BOTTOM))

			return stb0 + "." + translator.getBlock(blockId).getInitialLabel() + "(" + stb + ");\n";
		else
			return translator.getBlock(blockId).getInitialLabel() + "(" + stb0 + (stb != "" ? "," + stb : "") + ");\n";

		// String
		// code=stb0+"."+translator.getBlock(blockId).getGenusName()+"("+stb+");\n";

	}
}
