package com.digis01.GGarciaProgramacionNCapasMavenCliente.Controller;

import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Colonia;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Direccion;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Estado;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Municipio;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Pais;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Result;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Rol;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static String rutaBase = "http://localhost:8081";

    @GetMapping()
    public String Index(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> usuarios = restTemplate.exchange(rutaBase + "/api/usuario",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });
        if (usuarios.getStatusCode().value() == 200) {
            Result result = usuarios.getBody();
            model.addAttribute("usuarios", result.objects);
            model.addAttribute("usuarioBusqueda", new Usuario());
        }
        return "Usuario";
    }

    @GetMapping("/form")
    public String Form(Model model) {
        Usuario usuario = new Usuario();
        usuario.setRol(new Rol());
        Direccion direccion = new Direccion();
        Colonia colonia = new Colonia();
        Municipio municipio = new Municipio();
        Estado estado = new Estado();
        Pais pais = new Pais();

        estado.setPais(pais);
        municipio.setEstado(estado);
        colonia.setMunicipio(municipio);
        direccion.setColonia(colonia);

        usuario.setDirecciones(new ArrayList<>());
        usuario.getDirecciones().add(direccion);

        LocalDate fechaMaxima = LocalDate.now().minusYears(-18);
        model.addAttribute("fechaMaxima", fechaMaxima);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Pais>> paises = restTemplate.exchange(rutaBase + "/api/pais",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Pais>>() {
        });

        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(rutaBase + "/api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
        });

        if (paises.getStatusCode().value() == 200 && roles.getStatusCode().value() == 200) {
            Result<Pais> resultPaises = paises.getBody();
            Result<Rol> resultRoles = roles.getBody();
            if (resultPaises != null && resultRoles != null) {
                model.addAttribute("paises", resultPaises.objects);
                model.addAttribute("roles", resultRoles.objects);
            }

        }
        model.addAttribute("usuario", usuario);

        return "UsuarioForm";
    }

    @GetMapping("/detail/{idUsuario}")
    public String DetilUsuario(@PathVariable("idUsuario") int idUsuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> usuarioBusqueda = restTemplate.exchange(rutaBase + "/api/detail/" + idUsuario,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });
        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(rutaBase + "/api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
        });
        if (usuarioBusqueda.getStatusCode().value() == 200) {
            Result result = usuarioBusqueda.getBody();
            model.addAttribute("usuario", result.objects.get(0));
        }
        return "UsuarioDetail";

    }
}
