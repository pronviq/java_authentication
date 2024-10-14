package ru.max.authentication.util;

import org.springframework.http.ResponseCookie;
import lombok.ToString;

@ToString
public class CookieUtil {

  public static ResponseCookie generateRefreshCookie(String refreshToken, int maxAgeSeconds) {
    return ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			// .secure(true) 
			.path("/")
			.maxAge(maxAgeSeconds)
			.build();
  }
}
