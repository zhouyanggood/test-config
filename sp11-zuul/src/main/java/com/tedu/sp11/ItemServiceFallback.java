package com.tedu.sp11;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.tedu.web.util.JsonResult;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class ItemServiceFallback implements FallbackProvider {

	@Override
	public String getRoute() {
		 //当执行item-service失败，
	    //应用当前这个降级类
		return "item-service";
		//星号和null都表示所有微服务失败都应用当前降级类
		//"*"; //null;
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		// 降级响应,降级响应的数据,封装成一个Respose对象
		
		return response();
	}

	private ClientHttpResponse response() {
		
		return new ClientHttpResponse() {

			@Override
			public InputStream getBody() throws IOException {
				log.info("fallback body");
            	String s = JsonResult.err().msg("后台服务错误").toString();
                return new ByteArrayInputStream(s.getBytes("UTF-8"));
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
			}

			@Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }
            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }
            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

}
