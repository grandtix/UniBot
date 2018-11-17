package com.unibot.translator.block;

import com.unibot.translator.Translator;
import com.unibot.translator.block.exception.SocketNullException;
import com.unibot.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.BlockConnector.PositionType;
import edu.mit.blocks.workspace.Workspace;

public class CustomVariableBlock extends TranslatorBlock {
	public CustomVariableBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix,
			String label) {
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	public String toCode() throws SocketNullException, SubroutineNotDeclaredException {

		String stb0 = null;

		try {
			stb0 = this.getRequiredTranslatorBlockAtSocket(0).toCode();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			if (!translator.isFromArduino())
				return label + "_" + translator.getRobotName();
			return label;
		}

		String stb1 = "";
		String stb2 = "";
		try {
			stb1 = this.getRequiredTranslatorBlockAtSocket(1).toCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		int i = 2;
		boolean ok = false;
		String args = "";
		while (!ok)
			try {
				stb2 = "," + this.getRequiredTranslatorBlockAtSocket(i).toCode();
				args += stb2;
				i++;
			} catch (Exception e) {
				ok = true;
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		System.out.println("YES ON Y EST");
		translator.addClassTypeFile(stb0);
	//	System.out.println(translator.getBlock(blockId).getGenusName()+" "+translator.getBlock(blockId).getSocketAt(0).getPositionType());
		if (translator.getBlock(blockId).getSocketAt(0).getPositionType().equals(PositionType.BOTTOM)) {
			if (stb1 != null)
				return (stb0 + "." + translator.getBlock(blockId).getInitialLabel() + "(" + stb1 + args + ")");
			else
				return stb0 + "." + translator.getBlock(blockId).getInitialLabel();
		} else if (stb1 != "")
			return (translator.getBlock(blockId).getInitialLabel() + "(" + stb0 + "," + stb1 + args + ")");
		else
			return translator.getBlock(blockId).getInitialLabel() + "(" + stb0 + ")";
	}

}
