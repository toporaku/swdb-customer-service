package com.customer_service.exception;

import org.springframework.dao.DataAccessException;

public class DBAccessException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private DataAccessException exception;

	public DBAccessException() {
		
	}
		public DBAccessException(DataAccessException e) {
			this.exception = e;
		}

}
