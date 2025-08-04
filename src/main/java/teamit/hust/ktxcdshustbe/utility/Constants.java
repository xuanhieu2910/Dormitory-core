package teamit.hust.ktxcdshustbe.utility;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    public final static String SORT_ASC = "ASC";
    public final static String SORT_DESC = "DESC";


    //Execute update
    public final static int ROW_NOT_UPDATED = 0;

    public final static String CLAIMS_INFORMATION_USER = "informationUser";

    public final static String NAME_REPORT_STUDENT_HIRED_ROOM_LIST = "Bao cao danh sach sinh vien dang ky phong.xlsx";
    public static int VN = 1;
    public final static Integer ACCOUNT_IS_ACTIVED = 1;
    public  final static Integer ACCOUNT_IS_UN_ACTIVED = 0; // Not yet validate email
    public final static Integer ACCOUNT_IS_ACTIVED_LOCK = -1;

    public final static Integer FEMALE = 1;
    public final static Integer MALE = 2;

    public final static String[] TITLE_SEX = {"Nữ","Nam"};

    public final static Integer FATHER = 1;
    public final static Integer MOTHER = 2;
    public final static String[] LABEL_PARENTS = {"Bố","Mẹ"};
    public final static Integer STATUS_REMAIN_EMPTY = 1;
    public final static Integer STATUS_REMAIN_UN_EMPTY = 0;

    public final static Integer STATUS_ROOM_IS_ACTIVED = 1;
    public final static Integer STATUS_ROOM_UN_ACTIVED = -1;

    /******************************************************************/
    // Department
    public final static Integer STATUS_DEPARTMENT_IS_ACTIVE = 1;
    public final static Integer STATUS_DEPARTMENT_IN_ACTIVE = -1;
    /******************************************************************/

    public final static Integer[] YEAR_GRADE = {64,65,66,67,68,69,70,71,72,72,73};

    public final static String TITLE_YEAR_GRADE = "Khóa";

    public final static Integer DEFAULT_QUANTITY_HIRED = 0;
    public final static Integer DEFAULT_QUANTITY_REGISTER = 0;

    public final static Integer DEFAULT_REMAIN_AMOUNT = 0;


    // Student Register room
    public final static Integer STUDENT_REGISTER_ROOM_STATUS_ACCEPT = 1;
    public final static Integer STATUS_HOLD_STUDENT_ROOM_REGISTER = 2;
    public final static Integer STATUS_STUDENT_REGISTER_ROOM_CONFIRM_ORDER = 3;
    public final static Integer STATUS_SUCCESS_PAYMENT_STUDENT_ROOM_REGISTER = 4;
    public final static Integer STATUS_CANCEL_PAYMENT_STUDENT_ROOM_REGISTER = 5;
    public final static Integer STATUS_FALSE_PAYMENT_STUDENT_ROOM_REGISTER = 6;
    public final static Integer STATUS_EXPIRES_TIME_STUDENT_ROOM_REGISTER = 7;

    /****************************************************************/

    public final static Integer STUDENT_REGISTER_ROOM_STATUS_NOT_ACCEPT = -1;
    public final static Integer QUANTITY_UPDATE_ROOM_AND_REGISTER = 1;
    public final static Integer QUANTITY_UPDATE_HIRED_ROOM = 1;
    public final static Integer QUANTITY_REMAIN_AMOUNT_REGISTER = 0;

    public final static Integer STUDENT_REGISTER_ROOM_STATUS_NOT_FULL_FILL = -1;
    public final static Integer STUDENT_REGISTER_ROOM_STATUS_FULL_FILL = 1;

    public final static Integer STATUS_USER_NOT_REGISTER_ROOM = -1;
    public final static Integer STATUS_USER_REGISTER_ROOM = 1;

    public static final int MAX_FILE_SIZE = 50000000;

    public static String[] FILE_EXCEL = {"xls", "xlsx", "xlsm"};

    public static String[] FILE_IMAGES = {"JPEG","PNG","JPG","GIF","PSD","PDF"};

    public static String MESSAGE_ERROR_SIZE_FILE = "Size to large 50Mb!";

    public static String MESSAGE_UP_LOAD_FILE_SUCCESS = "Up load file success!";

    public static String MESSAGE_UP_LOAD_FILE_WRONG_TEMPLATE = "File không đúng định dạng!";

    public static String MESSAGE_FILE_EMPTY = "File không có dữ liệu!";

    public static String MESSAGE_ERROR_REPORT_FILE = "File error";

    /**
     * Constants PATTERN ROLE CAPABILITIES
     * */
    public static final String PATTERN_ROLE_CAPABILITIES = "ktx/";
    public static final String PATTERN_ROLE_SEPARATE = ":";
    public static final Integer ROLE_CAPABILITIES_ACTIVE = 1;
    public static final Integer ROLE_CAPABILITIES_IN_ACTIVE = -1;
    public static final Integer ROLE_CAPABILITIES_PERMISSION = 1;
    public static final Integer ROLE_CAPABILITIES_NOT_PERMISSION = 1;
    public static final Integer STATUS_NOT_PERMISSION = -1;

    /*-----------------------------------------------------*/

    /**
     * Constants STATUS ROLE
     * */
    public static final Integer STATUS_ROLE_ACTIVE = 1;
    public static final Integer STATUS_ROLE_IN_ACTIVE = -1;

    /*-----------------------------------------------------*/



    /**
     * Constants ROLE_USER
     * */
    public static final Integer ROLE_USER_PICKED = 1;
    public static final Integer ROLE_STATUS_ACTIVE = 1;
    public static final Integer ROLE_STATUS_IN_ACTIVE = -1;
    public static final Integer ROLE_USER_UN_PICKED = -1;
    public static final Integer ROLE_DEFAULT = 2;

    /*-----------------------------------------------------*/

    /**
     * Constants ROLE_ALLOW_ASSIGN
     * */

    public static final Integer ROLE_ALLOW_ASSIGN_STATUS = 1;
    public static final Integer ROLE_ALLOW_ASSIGN_UN_STATUS = -1;


    /*-----------------------------------------------------*/

    /**
     * Constants TIME_HIRED
     * */

    public static final Integer TIME_HIRED_STATUS_ACTIVE = 1;
    public static final Integer TIME_HIRED_STATUS_IN_ACTIVE = -1;


    /*-----------------------------------------------------*/

    /**
     * Constants Student room
     * */

    public final static Integer STATUS_STUDENT_HIRING_ROOM = 1;
    public final static Integer STATUS_STUDENT_REFUND_ROOM = -1;

    /*-----------------------------------------------------*/

    /*----------------------------------------------------*/
    /**
     * Constant Department
     * */
    public static final Integer DEPARTMENT_ACTIVE_STATUS = 1;
    public static final Integer DEPARTMENT_UN_ACTIVE_STATUS = -1;
    /*----------------------------------------------------*/

    /**
     * Constant Batches Registration
     * */
    public static final Integer STATUS_BATCHES_REGISTRATION_NOT_YET_OPEN = -1;
    public static final Integer STATUS_BATCHES_REGISTRATION_OPENING = 1;
    public static final Integer STATUS_BATCHES_REGISTRATION_CLOSED = 2;
    /*----------------------------------------------------*/

    /**
     * Constant Batches Year Group Registration
     * */
    public static final Integer STATUS_BATCHES_YEAR_GROUP_REGISTRATION_IN_ACTIVE = -1;
    public static final Integer STATUS_BATCHES_YEAR_GROUP_REGISTRATION_ACTIVE = 1;
    /*----------------------------------------------------*/
    /**
     * Constant Batches Registration Schedule
     * */
    public static final Integer STATUS_BATCHES_REGISTRATION_SCHEDULE_IN_ACTIVE = -1;
    public static final Integer STATUS_BATCHES_REGISTRATION_SCHEDULE_ACTIVE = 1;
    /*----------------------------------------------------*/
    /**
     * Constant Batches Registration Room
     * */
    public static final Integer STATUS_BATCHES_REGISTRATION_ROOM_IN_ACTIVE = -1;
    public static final Integer STATUS_BATCHES_REGISTRATION_ROOM_ACTIVE = 1;
    /*----------------------------------------------------*/
    /**
     * Constant Orders
     * */
    public static final Integer STATUS_ORDER_CANCEL = -1;
    public static final Integer STATUS_ORDER_INIT = 1;
    public static final Integer STATUS_ORDER_CONFIRM_PAYMENT = 2;
    public static final Integer STATUS_ORDER_COMPLETE_PAYMENT = 3;
    public static final Integer STATUS_ORDER_PAYMENT_FALSE = 4;
    public static final Integer TYPE_ORDER_HIRED_ROOM = 1;
    /*----------------------------------------------------*/
    /**
     * Constant Orders
     * */
    public static final Integer QUANTITY_DEFAULT_ORDER_ITEM = 1;
    public static final Integer STATUS_ORDER_ITEM_ACTIVE = 1;
    public static final Integer STATUS_ORDER_ITEM_IN_ACTIVE = -1;
    /*----------------------------------------------------*/

    /**
     * Constant TRANSACTION PAYMENT
     * */
    public static final Integer STATUS_INIT_TRANSACTION_PAYMENT = 1;
    public static final Integer STATUS_COMPLETE_TRANSACTION_PAYMENT = 2;
    public static final Integer STATUS_COMPLETE_TRANSACTION_CANCEL = 3;
    public static final Integer STATUS_COMPLETE_TRANSACTION_FALSE = 4;
    public static final Integer TYPE_REQ_TRANSACTION_PAYMENT = 1;
    public static final Integer TYPE_RES_TRANSACTION_PAYMENT = 2;
    /*----------------------------------------------------*/
    /**
     * Constant ORDER_SESSIONS
     * */
    public static final Integer ORDER_SESSION_RETRY = 1;
    public static final Integer ORDER_SESSION_STATUS_ACTIVE = 1;
    public static final Integer ORDER_SESSION_STATUS_IN_ACTIVE = -1;
    /*----------------------------------------------------*/
    /**
     * Constant TYPE_PAYMENT
     * */
    public static final String TYPE_PAYMENT_VIET_QR = "VIETQR";
    public static final String TYPE_PAYMENT_INTERNATIONAL = "INTERNATIONAL";
    public static final String TYPE_PAYMENT_DOMESTIC = "DOMESTIC";
    /*----------------------------------------------------*/



}
