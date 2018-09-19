package com.efm.exception;

public class InternalException extends RuntimeException {

	private static final long serialVersionUID = 3697140077367375664L;
	private String info;
	
	public InternalException(){
		super();
	}
	
	public InternalException(Throwable t){
		super(t);
	}

	public InternalException(Throwable t,String info){
		super(t);
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}
}
