package teamit.hust.ktxcdshustbe.utility;

public class MessageUtil {

    // notifycation
    public static final String EMAIL_LOSTPASSWORD_SUBJECT_VI = "email.subject.forgetPassword";
    public static final String EMAIL_LOSTPASSWORD_CONTENT_VI = "email.content.forgetPassword";
    public static final String PASS_NULL_01 = "pass01";
    public static final String EMAIL_ADDRESS_IS_NOT_ENTERED = "Bạn chưa nhập địa chỉ email";
    public static final String EMAIL_IS_NOT_FORMAT = "Địa chỉ email không đúng định đạng";
    public static final String EMAIL_DOES_NOT_EXIST = "email.doesnotexist";
    public static final String WAREHOUSE_GET_ALL = "warehouse.getall";
    public static final String SUBMITTED_SUCCESSFULLY = "Gửi email thành công";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Đổi mật khẩu thành công";
    public static final String ACCEPT_OTP = "otp.acceptOTP";
    public static final String OTP_FAIL = "otp.incorrect";
    public static final String OTP_NULL = "otp.null";
    public static final String INCORRECT_PASSWORD = "Mật khẩu không chính xác";
    public static final String PASSWORD_NULL = "Bạn chưa nhập vào mật khẩu";
    public static final String PASSWORD_FALSE = "incorrect.password";
    public static final String PASSWORD_DISSIMILARITY = "Mật khẩu mới không khớp, bạn vui lòng nhập lại";
    public static final String EMAIL_ALREADY_EXIST = "Địa chỉ email đã tồn tại";

    // common
    public static final String GET_ALL_PROVINCE = "Lấy danh sách tỉnh/thành phố thành công";
    public static final String GET_DISTRICT_BY_PROVINCE = "Lấy danh sách quận/huyện thành công";
    public static final String GET_COMMUNE_BY_DISTRICT = "Lấy danh sách phường/xã thành công";

    // account
    public static final String FULL_NAME_HAS_MAX_LENGTH = "Độ dài tối đa của họ và tên là 50 ký tự";
    public static final String FULL_NAME_NULL = "full.name.null";
    public static final String PHONE_NULL = "Bạn cần nhập vào số điện thoại";

    // shop
    public static final String GET_ID_SHOP = "get.id.shop";
    public static final String GET_ALL_SHOP = "get.all.shop";

    public static final String GET_FULL_SHOP = "Lấy danh sách shop thành công";
    public static final String NAME_SHOP_NULL = "Tên shop là trường bắt buộc";
    public static final String NAME_HAS_MAX_LENGTH = "Độ dài tối đa của tên shop là 50 ký tự";
    public static final String PHONE_HAS_MAX_LENGTH = "Độ dài tối đa của số điện thoại là 11 ký tự";
    public static final String EMAIL_HAS_MAX_LENGTH = "Độ dài tối đa của email là 50 ký tự";
    public static final String EMAIL_INCORRECT_FORMAT = "Email không đúng định dạng";
    public static final String PHONE_INCORRECT_FORMAT = "Số điện thoại không đúng định dạng";
    public static final String FACEBOOK_HAS_MAX_LENGTH = "Độ dài tối đa của facebook là 200 ký tự";
    public static final String ADDRESS_HAS_MAX_LENGTH = "Độ dài tối đa của địa chỉ là 100 ký tự";
    public static final String CREATE_SHOP_SUCCESS = "Tạo shop mới thành công";
    public static final String DELETE_SHOP = "Xóa shop thành công";
    public static final String SHOP_DOES_NOT_EXIST = "Shop không tồn tại";
    public static final String ACCOUNT_DOES_NOT_EXIST = "Tài khoản quản lý shop không tồn tại";
    public static final String SHOP_HAS_BEEN_DELETED = "Tài khoản shop đã bị khóa";
    public static final String SAVE_SHOP = "Lưu shop thành công";

    // product
    public static final String SAVE_PRODUCT = "Lưu sản phẩm thành công";
    public static final String NAME_PRODUCT_NULL = "Bạn chưa điền tên sản phẩm";
    public static final String NAME_PRODUCT_HAS_MAX_LENGTH = "Tên sản phẩm không dài quá 200 ký tự";
    public static final String CODE_PRODUCT_HAS_MAX_LENGTH = "Mã sản phẩm không dài quá 15 ký tự";
    public static final String OBJECT_PRODUCT_NULL = "Bạn vui lòng chọn đối tượng sử dụng";
    public static final String CATEGORY_PRODUCT_NULL = "Bạn vui lòng chọn loại sản phẩm";
    public static final String CODE_PRODUCT_DUPLICATE = "Mã sản phẩm đã tồn tại";
    public static final String PRODUCT_DOES_NOT_EXIST = "Sản phẩm không tồn tại";
    public static final String ACCOUNT_NOT_MANY_SHOPS = "Một tài khoản không thể quản lý nhiều shop";
    public static final String DELETE_PRODUCT = "Xóa sản phẩm thành công";
    public static final String PRODUCT_HAS_BEEN_DELETED = "Sản phẩm đã bị khóa";
    public static final String PRODUCT_GET_ALL = "Lấy danh sách sản phẩm thành công";

    // order
    public static final String ORDER_GET_ALL = "Lấy danh sách đơn hàng thành công";
    public static final String GET_ID_ORDER = "Lấy thông tin đơn hàng thành công";
    public static final String SAVE_ORDER = "Lưu đơn hàng thành công";

    // wholesale
    public static final String SAVE_WHOLESALE = "Xuất hàng thành công";
    public static final String GET_ID_WHOLESALE = "Lấy thông tin đơn hàng thành công";
    public static final String CANCEL_SHIPPING = "Hủy đơn hàng thành công";
    public static final String PRODUCT_GET_WHOLESALE = "Lấy danh sách đơn hàng thành công";

    // language
    public static final String SELECT_LANGUAGE = "Chọn ngôn ngữ thành công";

    //EduQuestion Category
    public static final String QUESTION_CATEGORY_CAT_ID_OVER="Độ dài tối đa của catId là 100 ký tự ";
    public static final String QUESTION_CATEGORY_CAT_NAME_OVER="Độ dài tối đa của tên shop là 100 ký tự ";
    public static final String QUESTION_CATEGORY_CAT_CODE_OVER="Độ dài tối đa của tên shop là 100 ký tự ";
    public static final String QUESTION_CATEGORY_OWNER_OVER="Độ dài tối đa của tên shop là 100 ký tự ";
    public static final String QUESTION_CATEGORY_PARENT_ID_OVER="Độ dài tối đa của tên shop là 100 ký tự ";
    public static final String QUESTION_CATEGORY_STATUS_OVER="Trang thai chi co 0 va 1";
    //question
    public static final String QUESTION_QUESTION_ID_OVER="Độ dài tối đa của câu hỏi là 100 ký tự ";
    public static final String QUESTION_CONTENT_OVER="Độ dài tối đa của câu hỏi là 4000 ký tự ";

}
