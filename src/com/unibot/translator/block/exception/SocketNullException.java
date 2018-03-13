package com.unibot.translator.block.exception;

import com.unibot.core.exception.UniBotException;

public class SocketNullException extends UniBotException
{


	private Long blockId;
	
	
	public SocketNullException(Long blockId)
	{
		this.blockId = blockId;
	}
	
	
	public Long getBlockId()
	{
		return blockId;
	}
	private static final long serialVersionUID = -3386587749080938964L;

}
