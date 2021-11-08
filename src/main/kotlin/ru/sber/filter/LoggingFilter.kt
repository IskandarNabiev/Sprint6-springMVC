package ru.sber.filter

import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/api/*", "/app/*"])
class LoggingFilter: HttpFilter() {

    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        this.context = filterConfig.servletContext
    }

    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        context.log("Method: ${request!!.method}\n" + "Uri: ${request.requestURI}\n")
        val parameters = request.parameterNames

        while (parameters.hasMoreElements()){
            val parameter = parameters.nextElement()
            context.log("Request parameter: $parameter: ${request.getParameter(parameter)}")
        }

        chain!!.doFilter(request, response)
    }
}