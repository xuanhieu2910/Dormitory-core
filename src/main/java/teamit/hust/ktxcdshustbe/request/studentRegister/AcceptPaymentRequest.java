package teamit.hust.ktxcdshustbe.request.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // Dùng @Data cho gọn, nó bao gồm cả @Getter, @Setter, @ToString...

import java.math.BigDecimal;

@Data // Thay thế @Getter, @Setter, @NoArgsConstructor bằng @Data cho ngắn gọn
public class AcceptPaymentRequest {

    /**
     * ID của đơn đăng ký phòng cần xác nhận thanh toán.
     */
    @NotNull(message = "ID đơn đăng ký không được để trống")
    @JsonProperty("studentRegisterRoomId")
    private Integer studentRegisterRoomId;

    /**
     * Mã giao dịch từ cổng thanh toán hoặc ngân hàng.
     */
    @NotBlank(message = "Mã giao dịch không được để trống")
    @JsonProperty("transactionId")
    private String transactionId;

    /**
     * Số tiền thực tế đã thanh toán để hệ thống đối soát.
     */
    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Số tiền phải lớn hơn 0")
    @JsonProperty("amount")
    private BigDecimal amount;
}