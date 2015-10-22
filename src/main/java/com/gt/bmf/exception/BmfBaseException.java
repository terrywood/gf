package com.gt.bmf.exception;

public class BmfBaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected String code;
    protected String value;

	public BmfBaseException() {
		super();
		this.code = BmfErrorConstants.BASE_ERROR_CODE;
	}

	public BmfBaseException(String code) {
		super();
		this.code = code;
	}

	public BmfBaseException(String code, String message) {
		super(message);
		this.code = code;
        this.value=message;
	}

	public String getCode() {
		return code;
	}

    public String getValue() {
        return value;
    }
}
