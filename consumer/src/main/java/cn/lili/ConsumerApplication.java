package cn.lili;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * 消费者
 *
 * @author Chopper
 * @since 2020/11/16 10:03 下午
 */
@SpringBootApplication
public class ConsumerApplication {


    public final static Logger log = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) throws Exception{
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        ConfigurableApplicationContext run = SpringApplication.run(ConsumerApplication.class, args);
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