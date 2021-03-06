package com.neverpile.eureka.client.impl.feign;


import java.io.UnsupportedEncodingException;

import com.neverpile.eureka.client.model.Metadata;
import com.neverpile.eureka.client.model.MetadataElement;

public class AbstractMetadataElementBuilderImpl<P, I> {

  protected final P parent;
  
  protected final MetadataElement element;

  public AbstractMetadataElementBuilderImpl(final P parent, final String schema, final Metadata metadata) {
    this.parent = parent;

    element = new MetadataElement();
    element.setSchema(schema);
    
    metadata.get().put(schema, element);
  }

  @SuppressWarnings("unchecked")
  public I content(final String s) {
    try {
      element.setContent(s.getBytes("utf-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return (I) this;
  }

  @SuppressWarnings("unchecked")
  public I content(final byte[] content) {
    element.setContent(content);
    return (I) this;
  }

  public P attach() {
    return parent;
  }

}