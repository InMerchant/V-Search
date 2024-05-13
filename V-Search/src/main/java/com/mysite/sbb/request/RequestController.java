package com.mysite.sbb.request;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class RequestController {

    @GetMapping(value = "/api/wait", produces = MediaType.TEXT_PLAIN_VALUE)
    public DeferredResult<String> waitForResponse() {
        // DeferredResult를 사용하여 비동기 응답을 처리
        DeferredResult<String> deferredResult = new DeferredResult<>();
        
        // 비동기적으로 응답을 반환
        new Thread(() -> {
            // 비동기 작업 수행 (예: 10초간 대기)
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 작업이 완료되면 DeferredResult에 결과 설정
            deferredResult.setResult("파이썬에서 답을 보내주세요!");
        }).start();
        
        return deferredResult;
    }
}
