package com.example.demo;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomConcurrencyStrategy extends HystrixConcurrencyStrategy {
    private static final Logger log = LoggerFactory.getLogger(CustomConcurrencyStrategy.class);
    private HystrixConcurrencyStrategy delegate;

    public CustomConcurrencyStrategy() {
        this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
        if (this.delegate instanceof CustomConcurrencyStrategy) {
            // Welcome to singleton hell...
            return;
        }
        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();

        logCurrentStateOfHystrixPlugins(eventNotifier, metricsPublisher, propertiesStrategy);

        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
        HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
    }

    private void logCurrentStateOfHystrixPlugins(HystrixEventNotifier eventNotifier,
                                                 HystrixMetricsPublisher metricsPublisher,
                                                 HystrixPropertiesStrategy propertiesStrategy) {
        if (log.isDebugEnabled()) {
            log.debug("Current Hystrix plugins configuration is ["
                    + "concurrencyStrategy [" + this.delegate + "]," + "eventNotifier ["
                    + eventNotifier + "]," + "metricPublisher [" + metricsPublisher + "],"
                    + "propertiesStrategy [" + propertiesStrategy + "]," + "]");
            log.debug("Registering Custom Hystrix Concurrency Strategy.");
        }
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        if (callable instanceof CustomWrappedCallable) {
            return callable;
        }

        Callable<T> wrappedCallable = this.delegate != null
                ? this.delegate.wrapCallable(callable) : callable;

        if (wrappedCallable instanceof CustomWrappedCallable) {
            return wrappedCallable;
        }

        return new CustomWrappedCallable<>(RequestContextHolder.getRequestAttributes(), wrappedCallable);
    }


    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return this.delegate.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
        return this.delegate.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return this.delegate.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return this.delegate.getRequestVariable(rv);
    }

    public static class CustomWrappedCallable<T> implements Callable<T> {
        private final RequestAttributes attributes;
        private final Callable<T> callable;
        public CustomWrappedCallable(RequestAttributes attributes, Callable<T> callable) {
            this.attributes = attributes;
            this.callable = callable;
        }

        @Override
        public T call() throws Exception {
            try {
                RequestContextHolder.setRequestAttributes(attributes);
                return callable.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
