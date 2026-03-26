package com.digis01.GGarciaProgramacionNCapasMavenCliente.Controller;

import com.digis01.GGarciaProgramacionNCapasMavenCliente.Config.ServicesProperties;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Colonia;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Direccion;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Estado;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Municipio;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Pais;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Result;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Rol;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Usuario;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.Security.AppUserPrincipal;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ServicesProperties servicesProperties;

    public UsuarioController(ServicesProperties servicesProperties) {
        this.servicesProperties = servicesProperties;
    }

    private String buildUrl(String endpoint) {
        return servicesProperties.getBaseUrl() + endpoint;
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AppUserPrincipal principal) {
            headers.set(HttpHeaders.AUTHORIZATION, principal.getApiAuthorizationHeader());
        }
        return headers;
    }

    private HttpEntity<Void> authorizedEmptyEntity() {
        return new HttpEntity<>(authHeaders());
    }

    private <T> HttpEntity<T> authorizedEntity(T body) {
        return new HttpEntity<>(body, authHeaders());
    }

    @ModelAttribute
    public void injectServiceConfig(Model model) {
        model.addAttribute("servicesBaseUrl", servicesProperties.getBaseUrl());
        model.addAttribute("usuarioEndpoint", servicesProperties.getEndpoints().getUsuario());
        model.addAttribute("estadoEndpoint", servicesProperties.getEndpoints().getEstado());
        model.addAttribute("municipioEndpoint", servicesProperties.getEndpoints().getMunicipio());
        model.addAttribute("coloniaEndpoint", servicesProperties.getEndpoints().getColonia());
    }

// <editor-fold defaultstate="collapsed" desc="--- GET MAPPINGS / LECTURA ---">
    @GetMapping()
    public String Index(Model model) {
        ResponseEntity<Result<Usuario>> usuarios = restTemplate.exchange(buildUrl(servicesProperties.getEndpoints().getUsuario()),
                HttpMethod.GET,
                authorizedEmptyEntity(),
                new ParameterizedTypeReference<Result<Usuario>>() {
        });
        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(buildUrl(servicesProperties.getEndpoints().getRol()),
                HttpMethod.GET,
                authorizedEmptyEntity(),
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

        LocalDate fechaMaxima = LocalDate.now().minusYears(18);
        model.addAttribute("fechaMaxima", fechaMaxima);

        ResponseEntity<Result<Pais>> paises = restTemplate.exchange(buildUrl(servicesProperties.getEndpoints().getPais()),
                HttpMethod.GET,
                authorizedEmptyEntity(),
                new ParameterizedTypeReference<Result<Pais>>() {
        });

        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(buildUrl(servicesProperties.getEndpoints().getRol()),
                HttpMethod.GET,
                authorizedEmptyEntity(),
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
        ResponseEntity<Usuario> usuarioBusqueda = restTemplate.exchange(
                buildUrl(servicesProperties.getEndpoints().getUsuario()) + "/" + idUsuario,
                HttpMethod.GET,
                authorizedEmptyEntity(),
                Usuario.class);
        if (usuarioBusqueda.getStatusCode().value() == 200) {
            model.addAttribute("usuario", usuarioBusqueda.getBody());
        }

        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(
                buildUrl(servicesProperties.getEndpoints().getRol()),
                HttpMethod.GET,
                authorizedEmptyEntity(),
                new ParameterizedTypeReference<Result<Rol>>() {
        });

        if (roles.getStatusCode().value() == 200) {
            model.addAttribute("roles", roles.getBody().objects);
        }

        ResponseEntity<Result<Pais>> paises = restTemplate.exchange(
                buildUrl(servicesProperties.getEndpoints().getPais()),
                HttpMethod.GET,
                authorizedEmptyEntity(),
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
    @PostMapping("/addDirection/{idUsuario}")
    public String AddDirection(@ModelAttribute("nuevaDireccion") Direccion nuevaDireccion, @PathVariable("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) {
        String urlServicio = buildUrl(servicesProperties.getEndpoints().getDireccion()) + "/" + idUsuario;
        HttpEntity<Direccion> requesBody = authorizedEntity(nuevaDireccion);
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
        return "redirect:/usuario/detail/" + idUsuario;
    }

    @PostMapping("/add")
    public String AddUsuarioDireccion(@ModelAttribute("usuario") Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        String urlServicio = buildUrl(servicesProperties.getEndpoints().getUsuario());
        HttpEntity<Usuario> requesBody = authorizedEntity(usuario);
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlServicio,
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

    @PostMapping("/buscar")
    public String Buscar(@ModelAttribute("usuarioBusqueda") Usuario usuarioBusqueda, Model model, RedirectAttributes redirectAttributes) {
        String urlServicio = buildUrl(servicesProperties.getEndpoints().getUsuario()) + "/buscar";
        HttpEntity<Usuario> requesBody = authorizedEntity(usuarioBusqueda);
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlServicio,
                    HttpMethod.POST,
                    requesBody,
                    Result.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("usuarioBusqueda", usuarioBusqueda);
                ResponseEntity<Result<Rol>> roles = restTemplate.exchange(buildUrl(servicesProperties.getEndpoints().getRol()),
                        HttpMethod.GET,
                        authorizedEmptyEntity(),
                        new ParameterizedTypeReference<Result<Rol>>() {
                });
                if (roles.getStatusCode().is2xxSuccessful()) {
                    model.addAttribute("roles", roles.getBody().objects);
                }
                model.addAttribute("usuarios", response.getBody().objects);
            }
        } catch (Exception e) {
        }
        return "Usuario";
    }
    // </editor-fold>

}
