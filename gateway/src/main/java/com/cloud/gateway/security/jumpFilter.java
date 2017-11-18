package com.cloud.gateway.security;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;


public class jumpFilter extends ZuulFilter {

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
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String[] urls = request.getRequestURI().split("/");
        String jumpModule = null;
        if (urls.length > 1) {
            jumpModule = urls[1];
        }
        boolean permitted = SecurityUtils.getSubject().isPermitted(jumpModule);
        if (!permitted) {
            ctx.setResponseStatusCode(403);
            //没有模块权限。
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("无权限");
        }
        return null;
    }
}
