package template.framework.load;

import lombok.extern.slf4j.Slf4j;
import template.framework.config.UserCredentials;
import template.framework.config.UserThread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Нагрузочные тесты
 */
@Slf4j
public class LoadTests {
    static UserCredentials userCredentials = new UserCredentials();
    static int timer = 120;

    public void test() throws InterruptedException {
        userCredentials.addCredentials("l.load01", "VPgrV9GFTB", true);

        long startTime = System.currentTimeMillis();
        int threadsNumber = userCredentials.getCredentials().size();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(threadsNumber);
        for (int i = 0; i < threadsNumber; i++) {
            UserThread userThread = new UserThread(userCredentials.getCredentials().get(i).getLogin(), userCredentials.getCredentials().get(i).getPassword());
            executorService.scheduleWithFixedDelay(userThread, 1, 1, TimeUnit.SECONDS);
            Thread.sleep(1000);
        }
        while ((System.currentTimeMillis() - startTime) / 1000 < timer) {
            Thread.sleep(1000);
            log.info("Осталось {} с.", timer - ((System.currentTimeMillis() - startTime) / 1000));
        }
        log.info("Пользователи закругляются");
        executorService.shutdown();
    }
}
