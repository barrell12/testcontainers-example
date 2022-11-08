package com.dan.testcontainers.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pet {

  @Id
  private String id;
  private String name;
  private int age;

  public Pet() {
  }

  public Pet(final String id, final String name, final int age) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
  }

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge(final int age) {
    this.age = age;
  }
}
