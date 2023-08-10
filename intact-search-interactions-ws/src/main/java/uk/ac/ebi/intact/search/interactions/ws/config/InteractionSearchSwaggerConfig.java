package uk.ac.ebi.intact.search.interactions.ws.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.lang.Nullable;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class InteractionSearchSwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .paths(Predicates.not(PathSelectors.regex("/")))
                .build()
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "IntAct Interaction Search REST API",
                "This API allow searching interactor information in the IntAct database.",
                "API 1.0 BETA",
                "https://www.ebi.ac.uk/about/terms-of-use",
                 new Contact("IntAct", "https://www.ebi.ac.uk/intact", "intact@helpdesk.ebi.ac.uk"),
                "License of API", "https://creativecommons.org/licenses/by/4.0/", Collections.emptyList());
    }

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer customizer() {
        return pageableResolver -> {
            pageableResolver.setPageParameterName("page");
            pageableResolver.setSizeParameterName("pageSize");
        };
    }


    private static class SwaggerPageable {
        @ApiParam(value = "Results page you want to retrieve (0..N)", example = "0")
        private Integer page;
        @ApiParam(value = "Number of records per page", example = "10")
        private Integer pageSize;

        @Nullable
        public Integer getPage() {
            return page;
        }
        @Nullable
        public Integer getPageSize() {
            return pageSize;
        }
    }
}