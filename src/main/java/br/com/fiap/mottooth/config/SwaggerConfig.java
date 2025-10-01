package br.com.fiap.mottooth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        // botão estilizado que aponta para a raiz (home = "/")
        String voltarHomeBtn = """
            <p style="margin: 12px 0 4px;">
              <a href="/" target="_self"
                 style="display:inline-block;padding:10px 14px;
                        background:#0ea5e9;color:#001827;
                        border-radius:10px;text-decoration:none;
                        font-weight:700;">
                ← Voltar para Home
              </a>
            </p>
            """;

        return new OpenAPI()
                .info(new Info()
                        .title("Mottooth API")
                        .version("1.0.0")
                        .description("""
                    API para gerenciamento de motos, beacons e localizações da Mottu.
                    """ + voltarHomeBtn)
                        .contact(new Contact()
                                .name("Equipe Mottu")
                                .email("mottu@ex.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .components(new Components().addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
