package vn.sapo.controller;

import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import vn.sapo.config.SapoProperties;
import vn.sapo.domain.Authorize;

import java.nio.charset.Charset;
import java.util.Collections;

@Controller
@RequestMapping("/")
public class SapoConnectController {

    private final SapoProperties sapoProperties;

    public SapoConnectController(SapoProperties sapoProperties) {
        this.sapoProperties = sapoProperties;
    }

    @GetMapping
    public String hello(Model model) {
        model.addAttribute("authorize", new Authorize());
        return "hello";
    }

    @GetMapping("connected")
    public String connected() {
        return "connected";
    }

    @GetMapping("connectFail")
    public String connectFail() {
        return "connectFail";
    }

    @PostMapping("authorize")
    public String authorize(@ModelAttribute Authorize authorize) {
        String url = "https://" + authorize.getStore() + ".bizwebvietnam.net/admin/oauth/authorize?client_id=" + authorize.getApiKey() + "&scope=" + authorize.getScopes() + "&redirect_uri=" + authorize.getRedirectUri();
        return "redirect:" + url;
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
