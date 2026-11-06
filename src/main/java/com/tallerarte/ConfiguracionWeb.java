package com.tallerarte;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    // El idioma por defecto es espanol y se guarda en la sesion
    @Bean
    public LocaleResolver localeResolver() {
        var resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("es"));
        return resolver;
    }

    // Permite cambiar el idioma con ?lang=es o ?lang=en
    @Bean
    public LocaleChangeInterceptor cambioIdioma() {
        var interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(cambioIdioma());
    }

    // Archivos de mensajes: messages.properties y messages_en.properties
    @Bean("messageSource")
    public MessageSource messageSource() {
        var fuente = new ResourceBundleMessageSource();
        fuente.setBasenames("messages", "messages_reservas");
        fuente.setDefaultEncoding("UTF-8");
        // Si falta el archivo de un idioma, usa messages.properties y no el idioma de Windows
        fuente.setFallbackToSystemLocale(false);
        return fuente;
    }
}