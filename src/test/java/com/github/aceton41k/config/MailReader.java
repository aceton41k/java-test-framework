package com.github.aceton41k.config;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Класс для получения email во время создания пользователя, восстановления пароля
 */
@Slf4j
public class MailReader {

    private static final Properties properties;
    private static final String user;
    private static final String password;
    private static final String host;
    private static final int port;

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/mail.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user = properties.getProperty("mail.imap.user");
        password = properties.getProperty("mail.imap.password");
        host = properties.getProperty("mail.imap.host");
        port = Integer.parseInt(properties.getProperty("mail.imap.port"));
    }

    @Step("Получение логина и пароля из письма")
    public static EmailRegUserCredentials getCredentialsFromEmail(String login) {
        EmailRegUserCredentials credentials = null;
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };

        Session session = Session.getDefaultInstance(properties, auth);

        try (Store store = session.getStore()) {
            store.connect(host, port, user, password);
            try (Folder inbox = store.getFolder("INBOX")) {
                inbox.open(Folder.READ_WRITE);
                Message[] messages = null;
                int i = 0;
                while (i < 7) {
                    SearchTerm searchTerm = new SearchTerm() {
                        @Override
                        public boolean match(Message msg) {
                            try {
                                return msg.getContent().toString().contains(login);
                            } catch (MessagingException | IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
                    log.info("Поиск письма с логином: {} (попытка {})", login, i+1);

                    if (inbox.search(searchTerm).length > 0) {
                        log.info("Письмо найдено!");
                        String content;
                        try {
                            messages = inbox.search(searchTerm);
                            content = messages[0].getContent().toString();
                        } catch (IOException e) {
                            throw new RuntimeException("Не удалось получить содержимое письма");
                        }
                        credentials = new EmailRegUserCredentials();
                        Pattern patternLogin = Pattern.compile("(?<=Ваш логин: ).*?(?=\\r?\\n(?!\\s))");
                        Pattern patternPass = Pattern.compile("(?<=Ваш пароль: ).*?(?=\\r?\\n(?!\\s))");
                        Matcher matcherLogin = patternLogin.matcher(content);
                        Matcher matcherPass = patternPass.matcher(content);
                        String login1 = "error!";
                        String password = "error!";
                        if (matcherLogin.find())
                            login1 = matcherLogin.group();
                        if (matcherPass.find())
                            password = matcherPass.group();

                        credentials.setLogin(login1);
                        credentials.setPassword(password);
                        break;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                    if (i == 5)
                        throw new MessagingException(format("Не удалось найти письмо для логина: %s за %d попыток.", login, i));
                }

                assert messages != null;
                for (Message message : messages) {
                    try {
                        message.setFlag(Flags.Flag.DELETED, true);
                    } catch (MessagingException ignored) {
                    }
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return credentials;
    }
}
