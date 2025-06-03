package uk.ac.ebi.intact.search.interactions.ws.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
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
import uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@OpenAPIDefinition(servers = { @Server(url = "/intact/ws/interaction/", description = "IntAct Interaction WS") })
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

    @Bean
    List<String> interactionFields() {
        return this.getConstantsFromClass(SearchInteractionFields.class);
    }

    @Bean
    List<String> interactorFields() {
        return this.getConstantsFromClass(SearchChildInteractorFields.class);
    }

    @Bean
    List<String> advancedSearchFields() {
        return this.getConstantsFromClass(AdvancedSearchInteractionFields.AdvancedSearchFieldConstants.class);
    }

    @Bean
    List<String> solrFields(
            @Qualifier("interactionFields") List<String> interactionFields,
            @Qualifier("interactorFields") List<String> interactorFields,
            @Qualifier("advancedSearchFields") List<String> advancedSearchFields
    ) {
        return Stream.of(interactionFields, interactorFields, advancedSearchFields)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Bean
    OpenApiCustomiser fieldsCustomizer(@Qualifier("solrFields") List<String> solrFields) {
        return openApi -> {
            Schema<?> orderSchema = openApi.getComponents().getSchemas().get("Order");
            if (orderSchema != null) {
                Schema<?> fieldSchema = new StringSchema()
                        ._enum(solrFields)
                        .example("intact_miscore");
                orderSchema.addProperty("field", fieldSchema);
            }
        };
    }


    private List<String> getConstantsFromClass(Class<?> clazz) {
        List<String> values = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    Modifier.isFinal(field.getModifiers()) &&
                    field.getType().equals(String.class)) {
                try {
                    values.add((String) field.get(null));
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return values;
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