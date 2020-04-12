package com.definiteplans.config;


import java.time.LocalDate;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.definiteplans.util.DateUtil;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) {
                return DateUtil.parseDate(text);
            }
            @Override
            public String print(LocalDate object, Locale locale) {
                return DateUtil.printDate(object);
            }
        };
    }

}
