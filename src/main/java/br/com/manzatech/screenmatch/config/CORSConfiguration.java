package br.com.manzatech.screenmatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(new String[] {
                    "http://localhost:5500",
                    "http://127.0.0.1:5500",
                })
                .allowedMethods(new String[]{
                    HttpMethod.GET.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name(),
                    HttpMethod.PATCH.name(),
                    HttpMethod.DELETE.name(),
                    HttpMethod.OPTIONS.name(),
                    HttpMethod.HEAD.name(),
                    HttpMethod.TRACE.name()
                });
    }
}
