package teamit.hust.ktxcdshustbe.utility;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import teamit.hust.ktxcdshustbe.dto.registerRoom.AcceptStudentRegisterRoomDto;
import teamit.hust.ktxcdshustbe.dto.registerRoom.PaymentRegisterRoomSuccessDto;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import java.util.Queue;


public class EmailUtil implements Runnable {

    private static Logger log = LoggerFactory.getLogger(EmailUtil.class);

    private static EmailUtil INSTANCE = null;

    private static SmtpAuthenticator smtpAuthenticator;
    private Queue<MailDto> mailDtoQueue;

    public static EmailUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EmailUtil();
            new Thread(INSTANCE).start();
        }
        return INSTANCE;
    }

    public EmailUtil() {
        String email = PropertiesUtil.getEmailProperty("mail.user");
        String password = PropertiesUtil.getEmailProperty("mail.password");
        smtpAuthenticator = new SmtpAuthenticator(email, password);
        mailDtoQueue = new LinkedList<>();
    }

    public boolean sendLostPasswordEmail(MessageSource messageSource, Integer language, String emailTo, String code, String fullName) {
        String subject = messageSource.getMessage(MessageUtil.EMAIL_LOSTPASSWORD_SUBJECT_VI, null, Locale.forLanguageTag("vi"));
        String content = messageSource.getMessage(MessageFormat.format(MessageUtil.EMAIL_LOSTPASSWORD_CONTENT_VI, fullName, code), null, Locale.forLanguageTag("vi"))
                .replace("{USER_NAME}", fullName)
                .replace("{CODE}", code);
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendMailRegister(String emailTo, Integer otp) {
        String subject = OptUtils.SUBJECT_REGISTER_ACCOUNT;
        String content = OptUtils.CONTENT_VERIFY_ACCOUNT;
        content = content.replace("{{USER-NAME-HERE}}", emailTo).replace("{{OTP-HERE}}", String.valueOf(otp.intValue()));
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendApprovedRoom(AcceptStudentRegisterRoomDto registerRoomDto){
        String subject = null;
        String content = null;
        if (registerRoomDto.getStatusAccept().equals(Constants.STUDENT_REGISTER_ROOM_STATUS_ACCEPT)) {
            subject = OptUtils.SUBJECT_REGISTER_ROOM;
            content = OptUtils.CONTENT_REGISTER_SUCCESS_ROOM;
            content = content.replace("{{USER_NAME}}", registerRoomDto.getUserName())
                             .replace("{{TITLE_DEPARTMENT}}", registerRoomDto.getTitleDepartment())
                             .replace("{{TITLE_ROOM}}", registerRoomDto.getTitleRoom())
                             .replace("{{PRICE}}", registerRoomDto.getPrice())
                             .replace("{{TIME_HIRED}}", registerRoomDto.getTimeHired());
        } else {
            subject = OptUtils.SUBJECT_REGISTER_ROOM;
        }
        return mailDtoQueue.add(new MailDto(registerRoomDto.getUserName(), subject, content));
    }

    public boolean sendMailPaymentSuccess(PaymentRegisterRoomSuccessDto paymentRegisterRoomSuccessDto){
//        String subject = OptUtils.SUBJECT_REGISTER_ROOM;
//        String content = OptUtils.CONTENT_REGISTER_SUCCESS_ROOM;
//        content = content.replace("{{USER_NAME}}", registerRoomDto.getUserName())
//                .replace("{{TITLE_DEPARTMENT}}", registerRoomDto.getTitleDepartment())
//                .replace("{{TITLE_ROOM}}", registerRoomDto.getTitleRoom())
//                .replace("{{PRICE}}", registerRoomDto.getPrice())
//                .replace("{{TIME_HIRED}}", registerRoomDto.getTimeHired());
//        return mailDtoQueue.add(new MailDto(registerRoomDto.getUserName(), subject, content));
        return false;
    }

    private static boolean send(MailDto mailDto) {
        try {
            Properties emailProps = new Properties();
            emailProps.load(PropertiesUtil.class.getResourceAsStream("/email.properties"));
            // Get the default Session object.
            Session session = Session.getDefaultInstance(emailProps, smtpAuthenticator);
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(PropertiesUtil.getEmailProperty("mail.user")));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDto.getEmailTo()));
            // Set Subject: header field
            message.setSubject(mailDto.getSubject(), "UTF-8");

            // Send the actual HTML message, as big as you like
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
//            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setContent(mailDto.getContent(), "text/html; charset=UTF-8");
            // Send message
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

                MailDto mailDto = mailDtoQueue.poll();
                if (mailDto != null) {
                    String rs = send(mailDto) ? "success" : "fail";
                    log.info("Send mail is " + rs + " (" + mailDto + ")");
                }
            } catch (Exception e) {
                log.error("Lỗi", e);
            }
        }
    }
}
