package com.neverpile.eureka.client.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

// FIXME: split ContentElementDto and MultipartFile
public class ContentElementDto implements MultipartFile {

  private String name;
  private String fileName;
  private String contentType;
  private byte[] payload;
  private String id;
  private long length;
  private Digest digest;

  public ContentElementDto() {
  }

  public ContentElementDto(final byte[] payload) {
    if (payload == null) {
      throw new IllegalArgumentException("Payload cannot be null.");
    }
    this.fileName = "dokument.txt";
    this.payload = payload;
    this.name = "part";
    this.contentType = "application/octet-stream";
  }

  public ContentElementDto(final String originalFileName, final String contentType, final byte[] payload) {
    if (payload == null) {
      throw new IllegalArgumentException("Payload cannot be null.");
    }
    this.fileName = originalFileName;
    this.payload = payload;
    this.name = "part";
    this.contentType = contentType;
  }

  // TODO - erneut einbinden
  public ContentElementDto(final String name, final String originalFileName, final String contentType, final byte[] payload) {
    if (payload == null) {
      throw new IllegalArgumentException("Payload cannot be null.");
    }
    this.fileName = originalFileName;
    this.payload = payload;
    this.name = name;
    this.contentType = contentType;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getOriginalFilename() {
    return fileName;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public boolean isEmpty() {
    return payload.length == 0;
  }

  @Override
  public long getSize() {
    return payload.length;
  }

  @Override
  public byte[] getBytes() {
    return payload;
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(payload);
  }

  @Override
  public void transferTo(final File dest) throws IOException, IllegalStateException {
    FileOutputStream fileOutputStream = new FileOutputStream(dest);
    fileOutputStream.write(payload);
    fileOutputStream.close();
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(final String originalFileName) {
    this.fileName = originalFileName;
  }

  public byte[] getPayload() {
    return payload;
  }

  public void setPayload(final byte[] payload) {
    this.payload = payload;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public long getLength() {
    return length;
  }

  public void setLength(final long length) {
    this.length = length;
  }

  public Digest getDigest() {
    return digest;
  }

  public void setDigest(final Digest digest) {
    this.digest = digest;
  }

}
