package org.tokio.spring.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class EmailApiApplication {



    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(EmailApiApplication.class, args);

        final LoginMail pwdMail = (LoginMail) ctx.getBean("loginMailText");
        log.info("Login email with:  "+pwdMail.showLogin());
    }

    /* start debug */
    private record LoginMail(String userMail,String pwdMail){
        public String showLogin(){
            return userMail.substring(0,5).concat("xxxx, ").concat(pwdMail.substring(0,5)).concat("xxxx");
        }
    }

    @Value("${spring.mail.password}")
    private String pwdMailLoad;

    @Value("${spring.mail.username}")
    private String userMailLoad;

    @Bean("loginMailText")
    LoginMail getPWD(){
        return new LoginMail(userMailLoad,pwdMailLoad);
    }
    /* fin debug */
}
