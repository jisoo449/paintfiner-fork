package com.paintfinder.global.interceptor;

import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.global.handler.JwtTokenHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 실행순서
 * preHandle
 * api 실행
 * postHandle
 * afterCompletion
 */
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenHandler jwtTokenHandler;

    /**
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        //헤더에서 토큰 인가 요청 여부 확인
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // JWT 토큰이 맞는지 확인
        if(authorization != null && authorization.startsWith("Bearer ")){

            String jwtToken = authorization.substring(7);

            try{
                // 토큰이 올바르면 userIdx 전달.
                Claims claims = jwtTokenHandler.parseClaims(jwtToken);

                // accessToken일 때 userIdx 를 요청값에 넣어서 사용자 식별이 가능하게 한다.

                if(claims.containsKey("memberIdx")){
                    int memberIdx = Integer.parseInt(String.valueOf(claims.get("memberIdx")));

                    request.setAttribute("memberIdx", memberIdx);
                }

                // refreshToken일 땐 토큰에 userIdx 가 없고, refreshToken으로 userIdx를 찾아야 하므로 토큰을 넘겨준다.
                else {

                    // 토큰 재사용에 대해서는 현 시점에서 고려하지 않는다.
                    // refreshToken 이 만료되지 않았으나, 새로운 토큰이 발급된 상태에서 이전 refresh토큰을 전송했을 때 에러 발생
//                    if(jwtTokenHandler.findMemberIdxByRefresh(jwtToken) == null){
//                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired");
//                        throw new GlobalException(ErrorCode.INVALID_TOKEN);
//                        return false;
//                    }
                    int memberIdx = Integer.parseInt(String.valueOf(claims.get("refreshMemIdx")));
                    request.setAttribute("refreshMemIdx", memberIdx);
                    request.setAttribute("refreshToken", jwtToken);
                }

                return true;

            } catch (IllegalArgumentException e){ // 토큰 형식이 올바르지 않을 때

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token format.");

                return false;

            } catch (ExpiredJwtException e){ // 토큰이 만료 되었을 때

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired.");

                return false;

            } catch (UnsupportedJwtException e){ // 토큰이 지원하지 않는 형식일 때

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported JWT Token");

                return false;
            }
        } else { // 토큰이 손상되었을 때

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or malformed.");

            return false;
        }

    }

    /**
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     * (can also be {@code null})
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }


    /**
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param ex any exception thrown on handler execution, if any; this does not
     * include exceptions that have been handled through an exception resolver
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception{

    }
}
