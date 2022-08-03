package cn.lili;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetAddress;

/**
 * 运营后台 API
 *
 * @author Chopper
 * @since 2020/11/16 10:03 下午
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class ManagerApiApplication {

    public final static Logger log = LoggerFactory.getLogger(ManagerApiApplication.class);
    @Primary
    @Bean
    public TaskExecutor primaryTask() {
        return new ThreadPoolTaskExecutor();
    }

    public static void main(String[] args) throws Exception{
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        System.setProperty("rocketmq.client.logUseSlf4j","true");
        ConfigurableApplicationContext run = SpringApplication.run(ManagerApiApplication.class, args);
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
