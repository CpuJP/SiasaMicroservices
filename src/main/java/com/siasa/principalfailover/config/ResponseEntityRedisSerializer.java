package com.siasa.principalfailover.config;

import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.io.*;

public class ResponseEntityRedisSerializer implements RedisSerializer<ResponseEntity<?>> {

    @Nullable
    @Override
    public byte[] serialize(@Nullable ResponseEntity<?> responseEntity) throws SerializationException {
        if (responseEntity == null) {
            return new byte[0];
        }

        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(responseEntity.getStatusCode());
            objectStream.writeObject(responseEntity.getHeaders());
            objectStream.writeObject(responseEntity.getBody());
            return byteStream.toByteArray();
        } catch (IOException ex) {
            throw new SerializationFailedException("Failed to serialize object", ex);
        }
    }

    @Nullable
    @Override
    public ResponseEntity<?> deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            HttpStatus statusCode = (HttpStatus) objectStream.readObject();
            HttpHeaders headers = (HttpHeaders) objectStream.readObject();
            Object body = objectStream.readObject();
            return new ResponseEntity<>(body, headers, statusCode);
        } catch (IOException | ClassNotFoundException ex) {
            throw new SerializationFailedException("Failed to deserialize object", ex);
        }
    }
}
