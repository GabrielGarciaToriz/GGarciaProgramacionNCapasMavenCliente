package com.digis01.GGarciaProgramacionNCapasMavenCliente.Security;

import com.digis01.GGarciaProgramacionNCapasMavenCliente.Config.ServicesProperties;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Result;
import com.digis01.GGarciaProgramacionNCapasMavenCliente.ML.Usuario;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BackendAuthService {

    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");

    private final ServicesProperties servicesProperties;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BackendAuthService(ServicesProperties servicesProperties) {
        this.servicesProperties = servicesProperties;
        this.restTemplate = new RestTemplate();
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public AppUserPrincipal authenticate(String username, String password) {
        Usuario usuario = authenticateAgainstBackend(username, password);

        if (usuario == null) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        if (!isPasswordValid(password, usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        String roleName = normalizeRole(usuario.getRol() != null ? usuario.getRol().getNombre() : "USER");
        String displayName = buildDisplayName(usuario, username);
        String basicToken = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

        return new AppUserPrincipal(
                username,
                displayName,
                "Basic " + basicToken,
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }

    private Usuario authenticateAgainstBackend(String username, String password) {
        String endpoint = servicesProperties.getEndpoints().getAuthLogin();
        String loginUrl = servicesProperties.getBaseUrl() + endpoint;

        Usuario credentials = new Usuario();
        credentials.setUserName(username);
        credentials.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(credentials, headers);

        try {
            ResponseEntity<Usuario> response = restTemplate.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Usuario.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception ignored) {
            // Fallback: algunos servicios devuelven Result<Usuario> en lugar de Usuario directo.
        }

        try {
            ResponseEntity<Result<Usuario>> response = restTemplate.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful()
                    && response.getBody() != null
                    && response.getBody().object != null) {
                return response.getBody().object;
            }
        } catch (Exception ignored) {
            // Si no coincide el contrato, se considera autenticacion fallida.
            return null;
        }

        return null;
    }

    private String buildDisplayName(Usuario usuario, String fallbackUsername) {
        StringBuilder builder = new StringBuilder();
        if (usuario.getNombre() != null && !usuario.getNombre().isBlank()) {
            builder.append(usuario.getNombre()).append(" ");
        }
        if (usuario.getApellidoPaterno() != null && !usuario.getApellidoPaterno().isBlank()) {
            builder.append(usuario.getApellidoPaterno());
        }

        String displayName = builder.toString().trim();
        return displayName.isBlank() ? fallbackUsername : displayName;
    }

    private String normalizeRole(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return "USER";
        }

        String normalized = Normalizer.normalize(roleName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_+|_+$", "");

        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }

        if ("ADMINISTRADOR".equals(normalized)) {
            return "ADMIN";
        }
        if ("USUARIO".equals(normalized)) {
            return "USER";
        }

        return normalized.isBlank() ? "USER" : normalized;
    }

    private boolean isPasswordValid(String rawPassword, String storedPassword) {
        if (rawPassword == null || rawPassword.isBlank() || storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        String normalizedStoredPassword = storedPassword.trim();
        if (BCRYPT_PATTERN.matcher(normalizedStoredPassword).matches()) {
            return bCryptPasswordEncoder.matches(rawPassword, normalizedStoredPassword);
        }

        // Fallback por compatibilidad si existen usuarios legacy con password en texto plano.
        return Objects.equals(rawPassword, normalizedStoredPassword);
    }
}

