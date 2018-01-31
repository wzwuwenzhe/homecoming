package com.deady.demo.utils;

import com.deady.demo.entity.Operator;
import com.deady.demo.entity.Student;
import net.shunpay.util.ConfigUtil;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wzwuw on 2018/1/31.
 */
public class OperatorSessionInfo {

    public static final String OPERATOR_SESSION_ID = "DEADY_OPERATOR";
    public static final String STUDENT_SESSION_ID = "DEADY_STUDENT";

    public static final PropertiesConfiguration cacheConfig = ConfigUtil
            .getProperties("memcache");
    public static final String COOKIE_USER_NAME = "DEADY_NAME";
    public static final String COOKIE_USER_PWD = "DEADY_PWD";

    /**
     *
     * @param request
     * @param key
     * @param object
     * @param time
     *            时间 单位分钟
     */
    public static void save(HttpServletRequest request, String key,
                            Object object, int time) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(time * 60);
        session.setAttribute(key, object);
    }

    public static void saveCookie(HttpServletRequest request,
                                  HttpServletResponse response) {
        if (null != request.getParameter("remember")
                && request.getParameter("remember").equals("1")) {// 记住我
            Cookie nameCookie = new Cookie(COOKIE_USER_NAME,
                    request.getParameter("username"));
            // 设置Cookie的有效期为3天
            nameCookie.setMaxAge(60 * 60 * 24 * cacheConfig
                    .getInt("memcache.cookieTimeOut"));
            Cookie pwdCookie = new Cookie(COOKIE_USER_PWD,
                    request.getParameter("password"));
            pwdCookie.setMaxAge(60 * 60 * 24 * cacheConfig
                    .getInt("memcache.cookieTimeOut"));
            response.addCookie(nameCookie);
            response.addCookie(pwdCookie);
        } else {// 删除cookie
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_USER_NAME)
                        || cookie.getName().equals(COOKIE_USER_PWD)) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

    }

    public static void remove(HttpServletRequest request, String key) {
        HttpSession session = request.getSession();
        session.removeAttribute(key);
    }

    private static Object get(HttpServletRequest request, String key) {
        return request.getSession().getAttribute(key);
    }

    public static boolean isOperatorLogined(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute(OPERATOR_SESSION_ID) != null;
    }

    public static Operator getOperator(HttpServletRequest request) {
        return (Operator) get(request, OPERATOR_SESSION_ID);
    }

    public static Student getStudent(HttpServletRequest request) {
        return (Student) get(request, STUDENT_SESSION_ID);
    }

}
