package nhlgameupdatelambda.ddb;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.playbyplay.Play;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.DocumentAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.ImmutableTableSchema;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.UncheckedIOException;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Slf4j
public final class LinkedHashSetPlayConverter implements AttributeConverter<LinkedHashSet<Play>> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ImmutableTableSchema<Play> PLAY_TABLE_SCHEMA = ImmutableTableSchema.create(Play.class);
    private static final AttributeConverter<Play> playConverter = DocumentAttributeConverter.create(PLAY_TABLE_SCHEMA,
            EnhancedType.of(Play.class));
    private final DefaultAttributeConverterProvider defaultAttributeConverterProvider = DefaultAttributeConverterProvider.create();
    private final AttributeConverter<String> stringAttributeConverter = defaultAttributeConverterProvider
            .converterFor(EnhancedType.of(String.class));
    private final AttributeConverter mapAttributeConverter = defaultAttributeConverterProvider
            .converterFor(EnhancedType.mapOf(String.class, String.class));

    @Override
    public AttributeValue transformFrom(LinkedHashSet<Play> input) throws UncheckedIOException{
        // Convert LinkedHashSet to a list of attributes for storing in DynamoDB
        if (input == null) {
            return AttributeValue.fromNul(true);
        }
        return AttributeValue.fromL(input.stream()
                .map(playConverter::transformFrom)
                .collect(Collectors.toList()));
    }

    @Override
    public LinkedHashSet<Play> transformTo(AttributeValue input) {
        if (input == null) {
            return null;
        }

        return input.l()
                .stream()
                .map(playConverter::transformTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public EnhancedType<LinkedHashSet<Play>> type() {
        final LinkedHashSet<Play> linkedHashSet = new LinkedHashSet<>();
        return (EnhancedType<LinkedHashSet<Play>>) EnhancedType.of(linkedHashSet.getClass());
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.L;
    }
}
