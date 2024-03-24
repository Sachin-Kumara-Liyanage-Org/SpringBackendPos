package com.techbirdssolutions.springpos;

import com.techbirdssolutions.springpos.config.DefaultDataLoad;
import com.techbirdssolutions.springpos.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class WelcomeMessageRunner implements CommandLineRunner {

    @Autowired
    private DefaultDataLoad defaultDataLoad;

    @Autowired
    private CommonConstant commonConstant;

    private void printWelcomeMessage() {
        String separator = "==========================================================================================================";
        String posStr="\u001B[36m" +
                "██████████████████████████████████████████████████████████████████\n" +
                "█─▄─▄─█▄─▄▄─█─▄▄▄─█─█─███▄─▄─▀█▄─▄█▄─▄▄▀█▄─▄▄▀███▄─▄▄─█─▄▄─█─▄▄▄▄█\n" +
                "███─████─▄█▀█─███▀█─▄─████─▄─▀██─███─▄─▄██─██─████─▄▄▄█─██─█▄▄▄▄─█\n" +
                "▀▀▄▄▄▀▀▄▄▄▄▄▀▄▄▄▄▄▀▄▀▄▀▀▀▄▄▄▄▀▀▄▄▄▀▄▄▀▄▄▀▄▄▄▄▀▀▀▀▄▄▄▀▀▀▄▄▄▄▀▄▄▄▄▄▀" ;
        String welcomeMessage = String.format(
                "%n\u001B[35m%s\u001B[0m%n" +
                        "%s%n" +
                        "\u001B[34m                  \uD83D\uDE0AWelcome to %s!\uD83D\uDE0A\u001B[0m%n"+
                        "\u001B[32m ✔ \u001B[0m Your current profile is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current server Host is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current server port is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current database Host is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current database Port is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current database Name is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current database Url is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current hibernate ddl-auto is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current show sql is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current format sql is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Your current swagger-ui Url is: \u001B[36m%s\u001B[0m%n" +
                        "\u001B[32m ✔ \u001B[0m Application Started Time: \u001B[33m%s\u001B[0m%n" +
                        "\u001B[32m ▶▶Application Started Successfully!▶▶\u001B[0m%n" +
                        "\u001B[35m%s\u001B[0m%n",
                separator,
                posStr,
                commonConstant.getApplicationName(),
                commonConstant.getProfile(),
                commonConstant.getServerHost(),
                commonConstant.getServerPort(),
                commonConstant.getDatabaseHost(),
                commonConstant.getDatabasePort(),
                commonConstant.getDatabaseName(),
                commonConstant.getDatabaseUrl(),
                commonConstant.getHibernateType(),
                commonConstant.getShowSql(),
                commonConstant.getFormatSql(),
                "http://"+commonConstant.getServerHost()+":"+commonConstant.getServerPort()+"/swagger-ui/index.html",
                getCurrentTime(),
                separator
        );
        log.info("Welcome Message: {}", welcomeMessage);
    }


    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        return sdf.format(new Date());
    }

    @Override
    public void run(String... args) throws Exception {
        defaultDataLoad.runDefaultDataLoad();
        printWelcomeMessage();
    }
}
