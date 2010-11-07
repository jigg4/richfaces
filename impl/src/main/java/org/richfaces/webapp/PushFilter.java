/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.webapp;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.cpr.AtmosphereServlet;
import org.richfaces.application.push.PushContext;
import org.richfaces.application.push.impl.AtmosphereHandlerProvider;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 * 
 */
//TODO override broadcaster
public class PushFilter implements Filter {

    private static final String PUSH_HUB_MAPPING = "/*";

    private static final long serialVersionUID = 7616370505508715222L;

    /**
     * @author Nick Belaevski
     * 
     */
    private final class ServletConfigFacade implements ServletConfig {
        /**
         * 
         */
        private final FilterConfig filterConfig;

        /**
         * @param filterConfig
         */
        private ServletConfigFacade(FilterConfig filterConfig) {
            this.filterConfig = filterConfig;
        }

        public String getServletName() {
            return filterConfig.getFilterName();
        }

        public ServletContext getServletContext() {
            return filterConfig.getServletContext();
        }

        public String getInitParameter(String name) {
            String result = filterConfig.getInitParameter(name);

            if (result == null) {
                result = filterConfig.getServletContext().getInitParameter(name);
            }
            
            return result;
        }

        @SuppressWarnings("unchecked")
        public Enumeration<String> getInitParameterNames() {
            Set<String> result = Sets.newLinkedHashSet();
            
            result.addAll(Collections.list(filterConfig.getInitParameterNames()));
            result.addAll(Collections.list(filterConfig.getServletContext().getInitParameterNames()));
            
            return Iterators.asEnumeration(result.iterator());
        }
    }

    private AtmosphereServlet atmosphereServlet;

    public void init(FilterConfig filterConfig) throws ServletException {
        AtmosphereHandlerProvider handlerProvider = (AtmosphereHandlerProvider) filterConfig.getServletContext().getAttribute(PushContext.INSTANCE_KEY_NAME);

        if (handlerProvider == null) {
            return;
        }
        
        atmosphereServlet = new AtmosphereServlet() {

            private static final long serialVersionUID = -8719394110408476331L;

            protected boolean detectSupportedFramework(ServletConfig sc) throws ClassNotFoundException, IllegalAccessException, InstantiationException ,NoSuchMethodException ,java.lang.reflect.InvocationTargetException {
                return false;
            };
        };
        ServletConfigFacade servletConfig = new ServletConfigFacade(filterConfig);
        atmosphereServlet.init(servletConfig);

        atmosphereServlet.addAtmosphereHandler(PUSH_HUB_MAPPING, handlerProvider.getHandler());
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {
        
        if (atmosphereServlet != null && request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            HttpServletResponse httpResp = (HttpServletResponse) response;
            
            if ("GET".equals(httpReq.getMethod()) && httpReq.getQueryString() != null && httpReq.getQueryString().contains("__richfacesPushAsync")) {
                atmosphereServlet.doGet(httpReq, httpResp);
                return;
            }
        }
        
        // TODO Auto-generated method stub
        chain.doFilter(request, response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        if (atmosphereServlet != null) {
            atmosphereServlet.removeAtmosphereHandler(PUSH_HUB_MAPPING);
            atmosphereServlet.destroy();
            atmosphereServlet = null;
        }
        // TODO Auto-generated method stub
    }

}