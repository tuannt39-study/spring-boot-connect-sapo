package vn.sapo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import vn.sapo.config.SapoProperties;

import java.nio.charset.Charset;
import java.util.*;

@Controller
@RequestMapping("/")
public class SapoConnectController {

    @Value("sapo.client.clientId")
    private String clientId;
    @Value("sapo.client.clientSecret")
    private String clientSecret;

    private final SapoProperties sapoProperties;

    public SapoConnectController(SapoProperties sapoProperties) {
        this.sapoProperties = sapoProperties;
    }

    @GetMapping
    public String hello() {
        return "hello";
    }

    @GetMapping("/connected")
    public String connected() {
        return "connected";
    }

    @GetMapping("/connectFail")
    public String connectFail() {
        return "connectFail";
    }

    @GetMapping("auth")
    public String auth(@RequestParam String code, @RequestParam String hmac, @RequestParam String store) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders header = new HttpHeaders();
        header.setAccept(Collections.singletonList(MediaType.ALL));
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(header);

        ResponseEntity<Object> result;
        try {
            String url = "https://" + store + "/admin/oauth/access_token" + "?client_id=" + sapoProperties.getClient().getClientId() + "&client_secret=" + sapoProperties.getClient().getClientSecret() + "&code=" + code;
            result = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
            if (result.getStatusCode().equals(HttpStatus.OK)) {
                System.out.println(result);
                return "redirect:/connected";
            } else {
                return "redirect:/connectFail";
            }
        } catch (HttpStatusCodeException e) {
            return "redirect:/connectFail";
        }
    }

}
