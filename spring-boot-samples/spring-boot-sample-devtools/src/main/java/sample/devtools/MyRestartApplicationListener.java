package sample.devtools;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.devtools.restart.AgentReloader;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public class MyRestartApplicationListener implements ApplicationListener<ApplicationEvent>, Ordered {

    private int order = HIGHEST_PRECEDENCE;

    private static final String ENABLED_PROPERTY = "spring.devtools.restart.enabled";

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartedEvent) {
            onApplicationStartedEvent((ApplicationStartedEvent) event);
        }
    }

    private void onApplicationStartedEvent(ApplicationStartedEvent event) {
        // It's too early to use the Spring environment but we should still allow
        // users to disable restart using a System property.
        String enabled = System.getProperty(ENABLED_PROPERTY);
        if (enabled == null || Boolean.parseBoolean(enabled)) {
            String[] args = event.getArgs();
            MyDefaultRestartInitializer initializer = new MyDefaultRestartInitializer();
            boolean restartOnInitialize = !AgentReloader.isActive();
            Restarter.initialize(args, false, initializer, restartOnInitialize);
        }
        else {
            Restarter.disable();
        }
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    /**
     * Set the order of the listener.
     * @param order the order of the listener
     */
    public void setOrder(int order) {
        this.order = order;
    }

}