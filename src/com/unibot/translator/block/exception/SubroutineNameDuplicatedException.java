package com.unibot.translator.block.exception;

import com.unibot.core.exception.UniBotException;

public class SubroutineNameDuplicatedException extends UniBotException
{

	
	private static final long serialVersionUID = 882306487358983819L;
	
	
	private Long blockId;

	

	public Long getBlockId() {
		return blockId;
	}
	
	public SubroutineNameDuplicatedException(Long blockId)
	{
		this.blockId = blockId;
	}

}
