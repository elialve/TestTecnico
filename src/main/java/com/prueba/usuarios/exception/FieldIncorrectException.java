package com.prueba.usuarios.exception;

public class FieldIncorrectException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2956647972160589353L;

	public FieldIncorrectException() {}

    public FieldIncorrectException(String message)
    {
       super(message);
    }
}
