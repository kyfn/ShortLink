package org.lbc.shortlink.project.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 序列化器：将秒级时间戳转换为日期时间字符串
 */
public class SecondTimestampSerializer extends JsonSerializer<Long> implements ContextualSerializer {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");
    
    private String pattern = "yyyy-MM-dd HH:mm:ss";

    public SecondTimestampSerializer() {
    }

    public SecondTimestampSerializer(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // 将秒级时间戳转换为LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(value),
                ZONE_ID
        );

        // 格式化为字符串
        String formattedDate = DateTimeFormatter.ofPattern(pattern).format(dateTime);
        gen.writeString(formattedDate);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            SecondTimestampFormat annotation = property.getAnnotation(SecondTimestampFormat.class);
            if (annotation != null) {
                return new SecondTimestampSerializer(annotation.pattern());
            }
        }
        return this;
    }
}