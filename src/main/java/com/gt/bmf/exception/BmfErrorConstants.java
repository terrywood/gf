package com.gt.bmf.exception;

public class BmfErrorConstants {

	// system error code, start with S
	public static final String BASE_ERROR_CODE = "S999";
	public static final String DATE_FORMAT_CODE = "S001";

	// file error code, start with F
	public static final String FILE_EXIST_CODE = "F001";
	public static final String FILE_NOT_EXIST_CODE = "F002";
	public static final String FILE_TYPE_ERROR_CODE = "F003";

	// user
	public static final String USER_NOT_LOGIN_CODE = "U001";
	public static final String USER_NULL_LOGIN_CODE = "U002";
	public static final String USER_NOT_EXIST_CODE = "U003";
	public static final String USER_INVALID_CODE = "U004";
	public static final String USER_PASSWORD_CODE = "U005";
	public static final String USER_EXIST_CODE = "U006";
	public static final String USER_HAS_NOT_PERMISSION_CODE = "U007";
	public static final String USER_PASSWORD_NOT_SAME_CODE = "U008";
	public static final String USER_EXPORT_EXCEL_CODE = "U009";
	// role
	public static final String ROLE_NOT_EXIST_CODE = "R001";
	public static final String ROLE_ADD_USER_ID_CODE = "R002";
	public static final String ROLE_CODE_FORMAT_CODE = "R003";
	public static final String ROLE_EXIST_CODE = "R004";
	public static final String ROLE_CAN_BE_CHANGE = "R005";

	// permission
	public static final String PERMISSION_NOT_EXIST_CODE = "P001";
	public static final String PERMISSION_CODE_FORMAT_CODE = "P002";
	public static final String PERMISSION_EXIST_CODE = "P003";
	public static final String PERMISSION_NOT_DELETE_CODE = "P004";

	// settings
	public static final String GLOBAL_SETTINGS_CODE_EXIST_CODE = "G001";
	public static final String GLOBAL_SETTINGS_NAME_EXIST_CODE = "G002";

	// backUp
	public static final String BACKUP_CODE_ADD_MYSQL_PATH_CODE = "B001";

	// version
	public static final String VERSION_EXIST_CODE = "V001";
	public static final String VERSION_NOT_EXIST_CODE = "V002";

	// company
	public static final String COMPANY_EXIST_CODE = "C001";
	public static final String COMPANY_NOT_EXIST_CODE = "C002";
	public static final String COMPANY_CODE_FORMAT_CODE = "C003";

	// push message
	public static final String PUSH_MESSAGE_NOT_NULL_CODE = "PM001";
	public static final String PUSH_MESSAGE_NOT_EXIST_CODE = "PM002";
	public static final String PUSH_MESSAGE_EXCEL_FORMAT_ERROR = "PM003";

    public static final String IMAGE_FILE_TYPE_NOT_MATCH="IMG001";
    public static final String FTR_FLOOR_PLAN_IMAGE_EXIST_CODE="PLE001";

    public static final String FTR_FIXTURE_TYPE_IMAGE_MAX10_CODE="FTI001";
    public static final String FTR_FIXTURE_TYPE_EXIST_CODE="FTI002";


    public static final String FTR_AUDIT_TIMEOUT_CODE="FA001";


}
