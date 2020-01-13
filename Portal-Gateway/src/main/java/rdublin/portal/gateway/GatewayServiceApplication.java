package rdublin.portal.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class GatewayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    /**
     * https://www.baeldung.com/spring-boot-actuator-http
     */
    @Repository
    public class CustomTraceRepository implements HttpTraceRepository {

        AtomicReference<HttpTrace> lastTrace = new AtomicReference<>();

        @Override
        public List<HttpTrace> findAll() {
            return Collections.singletonList(lastTrace.get());
        }

        @Override
        public void add(HttpTrace trace) {
            if ("GET".equals(trace.getRequest().getMethod())
                    || "POST".equals(trace.getRequest().getMethod())) {
                lastTrace.set(trace);
            }
        }

    }

    @Component
    public class TraceRequestFilter extends HttpTraceFilter {

        public TraceRequestFilter(HttpTraceRepository repository, HttpExchangeTracer tracer) {
            super(repository, tracer);
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
            return request.getServletPath().contains("actuator");
        }
    }
}