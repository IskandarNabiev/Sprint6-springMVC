package ru.sber.filter

import java.time.Instant
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/api/*", "/app/*"])
class AuthFilter: HttpFilter() {

    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        this.context = filterConfig.servletContext
        this.context.log("Filter initialized")
    }


    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val cookies = request!!.cookies

        if (cookies == null) {
            this.context.log("Cookies not found")
            response!!.sendRedirect("/login")
        }
        else {
            val currentTime = Instant.now().toString()
            for (cookie in cookies) {
                when {
                    cookie.name != "auth" -> {
                        response!!.sendRedirect("/login")
                    }
                    cookie.value >= currentTime -> {
                        response!!.sendRedirect("/login")
                    }
                    else -> {
                        chain!!.doFilter(request, response)
                        return
                    }
                }
            }
        }


    }
}