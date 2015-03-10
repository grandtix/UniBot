package com.unibot.translator.block.exception;

import com.unibot.core.exception.UniBotException;

public class SubroutineNotDeclaredException extends UniBotException
{



	private static final long serialVersionUID = -2621233841585294257L;
	


	private Long blockId;
	
	public SubroutineNotDeclaredException(Long blockId)
	{
		this.blockId = blockId;
	}
	


	public Long getBlockId()
	{
		return this.blockId;
	}
}
