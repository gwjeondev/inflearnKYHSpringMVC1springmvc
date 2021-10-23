package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    // request.getParameter 형태
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={} age={}", username, age);

        response.getWriter().write("ok");
    }

    //@RequestParam 형태.
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String userName,
            @RequestParam("age") int userAge) {

        log.info("username={} age={}", userName, userAge);
        return "ok";
    }

    //@RequestParam name 생략 형태 parameter name이 변수 이름이 됨.
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={} age={}", username, age);
        return "ok";
    }

    /* @RequestParam annotation 자체를 생략하는 형태 parameter value가 primitive 타입인 경우 생략 가능하며
    parameter name이 변수 이름이 됨. */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {

        log.info("username={} age={}", username, age);
        return "ok";
    }

    /* @RequestParam parameter required 지정. default는 true이며 true일시 무조건 값이 있어야 동작함.
    빈문자의 경우 username= 과 같은 경우 null이 아닌 공백("")이 적용됨. */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {

        log.info("username={} age={}", username, age);
        return "ok";
    }

    /* @RequestParam parameter default value 지정, parameter가 없을경우 지정해놓은 defaultValue로 대체됨.
    빈 문자의 경우 username= 과 같은 경우에도 defaultValue가 적용됨.*/
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(defaultValue = "guest") String username,
            @RequestParam(defaultValue = "-1") int age) {

        log.info("username={} age={}", username, age);
        return "ok";
    }

    //@RequestParam 요청 parameter를 map으로 조회. Map or MultiValueMap 사용 가능.
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){

        log.info("username={} age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    //@RequestParam 요청 parameter를 MultiValueMap 조회. Map or MultiValueMap 사용 가능.
    @ResponseBody
    @RequestMapping("/request-param-multivaluemap")
    public String requestParamMap(@RequestParam MultiValueMap<String, Object> paramMap){
        //MultiValueMap={key=[value1, value2, ...], key=[value1, value2, ...]}
        log.info("MultiValueMap={}", paramMap);
        return "ok";
    }
}
