package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("helloData={}", helloData);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("helloData={}", helloData);

        return "ok";
    }

    /* RequestBody는 생략 불가능
       @ModelAttribute 에서 학습한 내용을 떠올려보자.
       스프링은 @ModelAttribute , @RequestParam 해당 생략시 다음과 같은 규칙을 적용한다.
       String , int , Integer 같은 단순 타입 = @RequestParam
       나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
       따라서 이 경우 HelloData에 @RequestBody 를 생략하면 @ModelAttribute 가 적용되어버린다.
       HelloData data -> @ModelAttribute HelloData data
       따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다. */
    @ResponseBody
    @PostMapping("request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        log.info("helloData={}", helloData);
        return "ok";
    }

    //HttpEntity 사용.
    @PostMapping("request-body-json-v4")
    public HttpEntity<String> requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData helloData = httpEntity.getBody();
        log.info("helloData={}", helloData);

        return new HttpEntity<>("ok");
    }

    /* responsebody가 있으면 HttpMessageConverter가 객체도 json으로 변환해서 응답해줌, Accept Type에 따라 달라진다.
      http메세지컨버터가 messageBody의 json을 변환하여 객체가 되었다가 객체가 다시 json으로 변환되어 응답하는 형태임.
      HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type: application/json)
      @ResponseBody 적용
      - 메시지 바디 정보 직접 반환(view 조회X)
      - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용 (Accept: application/json)
    */
    @ResponseBody
    @PostMapping("request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {
        log.info("helloData={}", helloData);
        return helloData;
    }
}
