package teamit.hust.ktxcdshustbe.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private String emailTo;
    private String subject;
    private String content;

    @Override
    public String toString() {
        return "To: " + emailTo + ", Subject: " + subject + ", Content: " + content;
    }
}
