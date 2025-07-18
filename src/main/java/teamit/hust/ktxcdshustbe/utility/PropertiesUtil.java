package teamit.hust.ktxcdshustbe.utility;

import java.util.Properties;

public class PropertiesUtil {
    private static Properties props;
    private static Properties evnProps;
    private static Properties emailProps;

    public static String getProperty(String name) {
        try {
            if (props == null) {
                props = new Properties();
                props.load(PropertiesUtil.class.getResourceAsStream("/application.properties"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props.getProperty(name);
    }

    public static String getPropertyEnvironment(String name) {
        try {
            if (evnProps == null) {
                evnProps = new Properties();
                evnProps.load(PropertiesUtil.class.getResourceAsStream("/environment.properties"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return evnProps.getProperty(name);
    }

    public static String getEmailProperty(String name) {
        try {
            if (emailProps == null) {
                emailProps = new Properties();
                emailProps.load(PropertiesUtil.class.getResourceAsStream("/email.properties"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailProps.getProperty(name);
    }
}
