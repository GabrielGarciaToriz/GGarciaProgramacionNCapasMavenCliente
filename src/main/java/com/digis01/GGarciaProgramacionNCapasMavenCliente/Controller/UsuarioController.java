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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    private final String rutaBase;
    private final String usuarioEndpoint;
    private final String rolEndpoint;
    private final String paisEndpoint;
    private final String direccionEndpoint;

    public UsuarioController(
            @Value("${services.base-url}") String rutaBase,
            @Value("${services.endpoints.usuario}") String usuarioEndpoint,
            @Value("${services.endpoints.rol}") String rolEndpoint,
            @Value("${services.endpoints.pais}") String paisEndpoint,
            @Value("${services.endpoints.direccion}") String direccionEndpoint) {
        this.rutaBase = rutaBase;
        this.usuarioEndpoint = usuarioEndpoint;
        this.rolEndpoint = rolEndpoint;
        this.paisEndpoint = paisEndpoint;
        this.direccionEndpoint = direccionEndpoint;
    }

    private String buildUrl(String endpoint) {
        return rutaBase + endpoint;
    }

// <editor-fold defaultstate="collapsed" desc="--- GET MAPPINGS / LECTURA ---">
    @GetMapping()
    public String Index(Model model) {
        ResponseEntity<Result<Usuario>> usuarios = restTemplate.exchange(buildUrl(usuarioEndpoint),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });
        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(buildUrl(rolEndpoint),
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

        LocalDate fechaMaxima = LocalDate.now().minusYears(18);
        model.addAttribute("fechaMaxima", fechaMaxima);

        ResponseEntity<Result<Pais>> paises = restTemplate.exchange(buildUrl(paisEndpoint),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Pais>>() {
        });

        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(buildUrl(rolEndpoint),
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
        ResponseEntity<Usuario> usuarioBusqueda = restTemplate.exchange(
                buildUrl(usuarioEndpoint) + "/" + idUsuario,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Usuario.class);
        if (usuarioBusqueda.getStatusCode().value() == 200) {
            model.addAttribute("usuario", usuarioBusqueda.getBody());
        }

        ResponseEntity<Result<Rol>> roles = restTemplate.exchange(
                buildUrl(rolEndpoint),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
        });

        if (roles.getStatusCode().value() == 200) {
            model.addAttribute("roles", roles.getBody().objects);
        }

        ResponseEntity<Result<Pais>> paises = restTemplate.exchange(
                buildUrl(paisEndpoint),
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
    @PostMapping("/addDirection/{idUsuario}")
    public String AddDirection(@ModelAttribute("nuevaDireccion") Direccion nuevaDireccion, @PathVariable("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) {
        String urlServicio = buildUrl(direccionEndpoint) + "/" + idUsuario;
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
        return "redirect:/usuario/detail/" + idUsuario;
    }

    @PostMapping("/add")
    public String AddUsuarioDireccion(@ModelAttribute("usuario") Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        String urlServicio = buildUrl(usuarioEndpoint);
        HttpEntity<Usuario> requesBody = new HttpEntity<>(usuario);
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
        String urlServicio = buildUrl(usuarioEndpoint) + "/buscar";
        HttpEntity<Usuario> requesBody = new HttpEntity<>(usuarioBusqueda);
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                    urlServicio,
                    HttpMethod.POST,
                    requesBody,
                    Result.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("usuarioBusqueda", usuarioBusqueda);
                ResponseEntity<Result<Rol>> roles = restTemplate.exchange(buildUrl(rolEndpoint),
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
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
