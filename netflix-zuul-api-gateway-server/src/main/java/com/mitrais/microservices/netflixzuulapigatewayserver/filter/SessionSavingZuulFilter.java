package com.mitrais.microservices.netflixzuulapigatewayserver.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionSavingZuulFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionSavingZuulFilter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();

        HttpSession httpSession = context.getRequest().getSession();
        httpSession.getId();
        Session session = sessionRepository.findById(httpSession.getId());
        if(session == null){
            session = sessionRepository.createSession();
        }

        context.addZuulRequestHeader("Cookie", "SESSION="+httpSession.getId());
        logger.info("ZuulPreFilter session proxy: {}", session.getId());
        return null;
    }
}
