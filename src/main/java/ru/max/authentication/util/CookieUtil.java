package ru.max.authentication.util;

import jakarta.servlet.http.Cookie;
import lombok.ToString;

@ToString
public class CookieUtil {
	public static Cookie generateRefreshCookie(String refreshToken, int maxAgeSeconds) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		// cookie.setAttribute("refreshToken", refreshToken);
		// cookie.setDomain("");
		// cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeSeconds);
		return cookie;
	}
}
