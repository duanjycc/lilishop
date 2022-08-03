package cn.lili;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * 基础API
 *
 * @author Chopper
 * @since 2020/11/17 3:38 下午
 */
@EnableCaching
@SpringBootApplication
public class CommonApiApplication {

    public final static Logger log = LoggerFactory.getLogger(CommonApiApplication.class);

    public static void main(String[] args) throws Exception{
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        ConfigurableApplicationContext run = SpringApplication.run(CommonApiApplication.class, args);

        Environment env = run.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path") == "null" ? "" : "/";

        log.info("\n----------------------------------------------------------\n\t" +
                "Application open-pub-Boot is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "\n\t" +
                "doc.html: \thttp://localhost:" + port + path + "doc.html\n\t" +
                "----------------------------------------------------------");

    }

}
