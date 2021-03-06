package com.neverpile.eureka.client.impl.feign.metadata;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neverpile.eureka.client.impl.feign.AbstractMetadataElementBuilderImpl;
import com.neverpile.eureka.client.metadata.JsonMetadataElementBuilder;
import com.neverpile.eureka.client.model.Metadata;

public class JsonMetadataElementBuilderImpl<P>
    extends
      AbstractMetadataElementBuilderImpl<P, JsonMetadataElementBuilderImpl<P>>
    implements
      JsonMetadataElementBuilder<P> {

  public JsonMetadataElementBuilderImpl(final P parent, final String schema, final Metadata metadata) {
    super(parent, schema, metadata);
    
    element.setContentType(MediaType.APPLICATION_JSON_VALUE);
  }

  @Override
  public JsonMetadataElementBuilder<P> content(final ObjectNode node) {
    try {
      element.setContent(new ObjectMapper().writeValueAsBytes(node));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  @Override
  public JsonMetadataElementBuilder<P> content(final Object jsonSerializableObject) {
    try {
      element.setContent(new ObjectMapper().writeValueAsBytes(jsonSerializableObject));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

}
