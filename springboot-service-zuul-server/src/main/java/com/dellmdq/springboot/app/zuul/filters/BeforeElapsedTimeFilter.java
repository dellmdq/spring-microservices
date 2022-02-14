package com.dellmdq.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class BeforeElapsedTimeFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(BeforeElapsedTimeFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {

		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();

		log.info(String.format("%s request routed to %s\n", request.getMethod(), request.getRequestURL().toString()));

		Long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;

	}

}