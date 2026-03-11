package com.digis01.GGarciaProgramacionNCapasMavenCliente.Controller;

import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Colonia;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Direccion;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Estado;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Municipio;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Pais;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Result;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Rol;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Usuario;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.UsuarioVista;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static String rutaBase = "http://localhost:8081";

// <editor-fold defaultstate="collapsed" desc="--- GET MAPPINGS / LECTURA ---">
    @GetMapping()
    public String Index(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Usuario>> usuarios = restTemplate.exchange(rutaBase + "/api/usuario",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });
        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(rutaBase + "/api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
        });
        if (usuarios.getStatusCode().is2xxSuccessful() && roles.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("usuarios", usuarios.getBody().objects);
            model.addAttribute("usuarioBusqueda", new Usuario());
            model.addAttribute("roles", roles.getBody().objects);
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
    public String DetailUsuario(@PathVariable("idUsuario") int idUsuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Usuario> usuarioBusqueda = restTemplate.exchange(
                rutaBase + "/api/usuario/" + idUsuario,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Usuario.class);
        if (usuarioBusqueda.getStatusCode().value() == 200) {
            model.addAttribute("usuario", usuarioBusqueda.getBody());
        }

        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(
                rutaBase + "/api/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
        });

        if (roles.getStatusCode().value() == 200) {
            model.addAttribute("roles", roles.getBody().objects);
        }

        ResponseEntity<Result<Pais>> paises = restTemplate.exchange(
                rutaBase + "/api/pais",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Pais>>() {
        }
        );

        if (paises.getStatusCode().value() == 200) {
            model.addAttribute("paises", paises.getBody().objects);
        }
        model.addAttribute("nuevaDireccion", new Direccion());
        return "UsuarioDetail";

    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="--- POST MAPPINGS (ESCRITURA / PROCESAMIENTO) ---">
    @PostMapping("/addDirection")
    public String AddDirection(@ModelAttribute("nuevaDireccion") Direccion nuevaDireccion, @RequestParam("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        String urlServicio = rutaBase + "/api/direccion" + idUsuario;
        HttpEntity<Direccion> requesBody = new HttpEntity<>(nuevaDireccion);
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlServicio,
                    HttpMethod.POST,
                    requesBody,
                    Result.class);
            if (response.getStatusCode().value() == 200) {
                redirectAttributes.addFlashAttribute("mensajeExito", "La direccion se agrego correctamnte al perfil");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "La direccion no pudo ser insertada debido a -> " + e.getLocalizedMessage());
        }
        return "redirect:/usuario/detail" + idUsuario;
    }

    @PostMapping("/add")
    public String AddUsuarioDireccion(@ModelAttribute("usuario") Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        String urlServicio = rutaBase + "/api/usuario";
        HttpEntity<Usuario> requesBody = new HttpEntity<>(usuario);
        try {
            ResponseEntity<Result> response = restTemplate.exchange(urlServicio,
                    HttpMethod.POST,
                    requesBody,
                    Result.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Usuario agregado con exito");
                return "redirect:/usuario";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "El usuario no pudo ser agregado -> " + e.getLocalizedMessage());
        }
        return "redirect:/usuario/form";
    }
    // </editor-fold>

}
