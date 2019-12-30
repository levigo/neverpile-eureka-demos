package com.neverpile.eureka.client.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import com.neverpile.eureka.client.DocumentService;
import com.neverpile.eureka.client.DocumentService.ContentElementResponse;
import com.neverpile.eureka.client.MockNeverpileRule;
import com.neverpile.eureka.client.metadata.MetadataFacetBuilder;
import com.neverpile.eureka.client.model.DocumentDto;
import com.neverpile.eureka.client.model.HashAlgorithm;
import com.neverpile.eureka.client.model.Metadata;

public class FeignNeverpileClientTest {
  @Rule
  public MockNeverpileRule neverpile = new MockNeverpileRule();

  @Test
  public void testThat_documentCanBeFound() throws Exception {
    // @formatter:off
    stubFor(
        get(urlEqualTo("/api/v1/documents/aTestDocument"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"documentId\": \"aTestDocument\"}")
        )
    );
    // @formatter:on

    DocumentService ds = neverpile.client.documentService();
    DocumentDto document = ds.getDocument("aTestDocument");

    assertThat(document, notNullValue());
    assertThat(document.getDocumentId(), Matchers.equalTo("aTestDocument"));
  }

  @Test
  public void testThat_documentContentCanBeRetrieved() throws Exception {
    // @formatter:off
    stubFor(
        get(urlEqualTo("/api/v1/documents/aTestDocument/content/foo"))
        .withHeader("Accept", equalTo("*/*"))
        .willReturn(
            aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "text/plain")
              .withBody("foo")
        )
    );
    // @formatter:on

    DocumentService ds = neverpile.client.documentService();
    ContentElementResponse ce = ds.getContentElement("aTestDocument", "foo");
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamUtils.copy(ce.getContent(), out);
    
    assertThat(ce.getMediaType(), Matchers.equalTo(MediaType.TEXT_PLAIN));
    assertThat(out.toByteArray(), Matchers.equalTo("foo".getBytes()));
  }

  @Test
  public void testThat_contentRetrieveHeadersAreCorrect() throws Exception {
    // @formatter:off
    stubFor(
        get(urlEqualTo("/api/v1/documents/aTestDocument/content/foo"))
        .withHeader("Accept", equalTo("*/*"))
        .willReturn(
            aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "foo/bar")
              .withHeader("ETag", "SHA-1:666f6f")
              .withBody("foo")
        )
    );
    // @formatter:on

    DocumentService ds = neverpile.client.documentService();
    ContentElementResponse ce = ds.getContentElement("aTestDocument", "foo");
    
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamUtils.copy(ce.getContent(), out);
    
    assertThat(ce.getMediaType(), Matchers.equalTo(MediaType.parseMediaType("foo/bar")));
    assertThat(ce.getDigest().getAlgorithm(), Matchers.equalTo(HashAlgorithm.SHA_1));
    assertThat(ce.getDigest().getBytes(), Matchers.equalTo("foo".getBytes()));
  }
  
  @Test
  public void testThat_documentCanBeCreated() throws Exception {
    String metadataFacetJSON = "\"metadata\" : {\"someMetadata\" : {\"schema\" : \"someMetadata\", \"contentType\" : \"application/json\", \"content\" : \"eyJmb28iOiJiYXIifQ==\"}}";

    // @formatter:off
    stubFor(
        post(urlEqualTo("/api/v1/documents"))
        .withHeader("Accept", equalTo("*/*"))
        .withMultipartRequestBody(
          aMultipart("doc")
            .withHeader("Content-Type", equalTo("application/json"))
            .withBody(equalToJson("{"+metadataFacetJSON+"}"))
        )
        .withMultipartRequestBody(
          aMultipart("part")
            .withHeader("Content-Type", equalTo("text/plain"))
            .withBody(equalTo("foo"))
        )
        .withMultipartRequestBody(
          aMultipart("part")
            .withHeader("Content-Type", equalTo("text/plain"))
            .withBody(equalTo("bar"))
        )
        .willReturn(
          aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "text/xml")
            .withBody("{\"documentId\": \"aTestDocument\", "+metadataFacetJSON+"}"
          )
        )
    );

    DocumentService ds = neverpile.client.documentService();

    DocumentDto doc = ds.newDocument() //
      .contentElement("0")
        .role("base")
        .fileName("foo.txt")
        .mediaType("text/plain")
        .content("foo".getBytes())
        .attach()
      .contentElement("1")
        .role("base")
        .fileName("bar.txt")
        .mediaType("text/plain")
        .content("bar".getBytes())
        .attach()
      .facet(MetadataFacetBuilder.metadata())
        .jsonMetadata("someMetadata")
          .content("{\"foo\":\"bar\"}")
          .attach()
      .save();
    // @formatter:on

    assertThat(doc, notNullValue());
    assertThat(doc.getDocumentId(), Matchers.equalTo("aTestDocument"));
    assertThat(doc.facets().get("metadata"), Matchers.notNullValue());
    assertThat(doc.facets().get("metadata"), Matchers.instanceOf(Metadata.class));
    assertThat(((Metadata)doc.facets().get("metadata")).get(), Matchers.hasKey("someMetadata"));
    assertThat(((Metadata)doc.facets().get("metadata")).get().get("someMetadata").getContent(), Matchers.equalTo("{\"foo\":\"bar\"}".getBytes()));
    
  }
}
