package io.csie.chris.demo.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;

class ObjectIdSerializerTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    private ObjectIdSerializer objectIdSerializer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        objectIdSerializer = new ObjectIdSerializer();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void serialize() throws IOException {
        ObjectId objectId = new ObjectId("669ff1757c82331d2b30b978");

        objectIdSerializer.serialize(objectId, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeString("669ff1757c82331d2b30b978");
    }
}