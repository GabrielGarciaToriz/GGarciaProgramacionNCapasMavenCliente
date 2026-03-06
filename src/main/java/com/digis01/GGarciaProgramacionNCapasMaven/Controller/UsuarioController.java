package com.digis01.GGarciaProgramacionNCapasMaven.Controller;

import com.digis01.GGarciaProgramacionNCapasMaven.ML.Result;
import com.digis01.GGarciaProgramacionNCapasMaven.ML.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static final String rutsaBase = "http://localhost:8081";

    @GetMapping
    public String Index(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(rutsaBase + "/api/usuario", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Usuario>>() {
        });
        if(responseEntity.getStatusCode().value() == 200){
            Result result = responseEntity.getBody();
            model.addAttribute("usuarios", result.objects);
        }
        return "Usuario";

    }
}
