package teamit.hust.ktxcdshustbe.response.studentRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticStudentRegisterResponse {
    @JsonProperty("total_student_register")
    private Integer totalStudentRegister;
    @JsonProperty("total_student_register_not_accept")
    private Integer totalStudentRegisterNotAccept;
    @JsonProperty("total_student_register_accept")
    private Integer totalStudentRegisterAccept;
    @JsonProperty("total_student_register_hold_room")
    private Integer totalStudentRegisterHoldRoom;
    @JsonProperty("total_student_register_confirm_order")
    private Integer totalStudentRegisterConfirmOrder;
    @JsonProperty("total_student_register_paid")
    private Integer totalStudentRegisterPaid;
    @JsonProperty("total_student_register_cancel_paid")
    private Integer totalStudentRegisterCancelPaid;
    @JsonProperty("total_student_register_false_paid")
    private Integer totalStudentRegisterFalsePaid;
    @JsonProperty("total_student_register_expires")
    private Integer totalStudentRegisterExpires;
    @JsonProperty("total_student_register_transfer")
    private Integer totalStudentRegisterTransfer;
    @JsonProperty("total_student_register_pending_payment")
    private Integer totalStudentRegisterPendingPayment;
    @JsonProperty("total_student_register_remove_room")
    private Integer totalStudentRegisterRemoveRoom;
}
