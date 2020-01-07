package com.example.demo;

import brave.handler.FinishedSpanHandler;
import brave.handler.MutableSpan;
import brave.propagation.TraceContext;
import org.springframework.stereotype.Component;

@Component
public class CustomFinishedSpanHandler extends FinishedSpanHandler {
    @Override
    public boolean handle(TraceContext context, MutableSpan span) {
        if (span.name() == "async") {//过滤掉名字为async的span，这样就不会发送到zipkin
            return false;
        }
        return true;
    }
}
