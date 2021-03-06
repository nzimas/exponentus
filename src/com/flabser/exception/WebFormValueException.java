package com.flabser.exception;


public class WebFormValueException extends Exception{
	private static final long serialVersionUID = -9024759964509486388L;
	public ServerServiceExceptionType id;
	public String fieldName;

	private String errorText;


	public WebFormValueException(ServerServiceExceptionType error, String fieldName) {
		super();
		id = error;
		this.fieldName = fieldName;
		switch(id){
		case FORMDATA_INCORRECT:
			errorText = "Value of form has not match to pattern, field=" + fieldName;
			break;
		case FORMDATA_INCORRECT_FOR_PARSER:
			errorText = "Unable parse webform value, field=" + fieldName;
			break;
		case OLD_PWD_INCORRECT:
			errorText = "Old password has not match with new";
			break;
		}
	}

	@Override
	public String getMessage(){
		return errorText;
	}

	@Override
	public String toString(){

		return errorText;
	}

}
