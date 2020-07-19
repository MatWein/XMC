package org.xmc.common;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;
import org.xmc.common.annotations.ConditionalOnMissingResource;

import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class OnMissingResourceCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var attributes = metadata.getAllAnnotationAttributes(ConditionalOnMissingResource.class.getName(), true);

        ResourceLoader loader = context.getResourceLoader();
        List<String> locations = new ArrayList<>();
        collectValues(locations, attributes.get("resources"));

        Assert.isTrue(!locations.isEmpty(), "@" + ConditionalOnMissingResource.class.getSimpleName() + " annotations must specify at least one resource location");

        List<String> existing = new ArrayList<>();
        for (String location : locations) {
            String resource = context.getEnvironment().resolvePlaceholders(location);
            if (loader.getResource(resource).exists()) {
                existing.add(location);
            }
        }

        if (existing.isEmpty()) {
            return ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnMissingResource.class)
                    .didNotFind("location", "locations").items(locations));
        } else {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnMissingResource.class)
                    .found("resource", "resources").items(Style.QUOTE, existing));
        }
    }

    private void collectValues(List<String> names, List<Object> values) {
        for (Object value : values) {
            for (Object item : (Object[]) value) {
                names.add((String) item);
            }
        }
    }
}
