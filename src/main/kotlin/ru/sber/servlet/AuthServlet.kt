package ru.sber.servlet

import java.time.Instant
import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(urlPatterns = ["/login"])
class AuthServlet: HttpServlet() {

    private val userLogin = "admin"
    private val userPassword = "adminpass"

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        servletContext.getRequestDispatcher("/login.html")?.forward(req, resp)
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val username = req?.getParameter("username")
        val password = req?.getParameter("password")

        if (username == userLogin && password == userPassword) {
            val cookie = Cookie("auth", Instant.now().toEpochMilli().toString())
            resp?.addCookie(cookie)
            resp?.sendRedirect("/app/add")
        } else {
            resp?.sendRedirect("error.html")
        }
    }
}