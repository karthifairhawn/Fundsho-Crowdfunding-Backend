package com.api.spring.boot.funsho.api.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
public class Files {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;
  private String name;
  private String type;
  @Lob
  private byte[] data;

  public Files(String name, String type, byte[] data) {
    this.name = name;
    this.type = type;
    this.data = data;
  }
 
}