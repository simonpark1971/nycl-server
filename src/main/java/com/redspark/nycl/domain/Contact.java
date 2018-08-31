package com.redspark.nycl.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by simonpark on 03/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Contact implements Serializable {

  public Contact() {}

  public static enum ContactType {
    MAIN_CONTACT,
    CLUB_CONTACT,
    FIXTURES_CONTACT,
    FIRST_CONTACT,
    SECOND_CONTACT,
  }

  private String contactName;
  private String contactPosition;
  private String contactAddress;
  private String contactPostcode;
  private String contactHomePhone;
  private String contactMobilePhone;
  private String contactEmail;
  private ContactType contactType;
  private String username;

  public String getContactName() {
    return contactName;
  }

  public String getContactPosition() {
    return contactPosition;
  }

  public String getContactAddress() {
    return contactAddress;
  }

  public String getContactPostcode() {
    return contactPostcode;
  }

  public String getContactHomePhone() {
    return contactHomePhone;
  }

  public String getContactMobilePhone() {
    return contactMobilePhone;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public ContactType getContactType() {
    return contactType;
  }

  public Contact setContactName(String contactName) {
    this.contactName = contactName;
    return this;
  }

  public Contact setContactPosition(String contactPosition) {
    this.contactPosition = contactPosition;
    return this;
  }

  public Contact setContactAddress(String contactAddress) {
    this.contactAddress = contactAddress;
    return this;
  }

  public Contact setContactPostcode(String contactPostcode) {
    this.contactPostcode = contactPostcode;
    return this;
  }

  public Contact setContactHomePhone(String contactHomePhone) {
    this.contactHomePhone = contactHomePhone;
    return this;
  }

  public Contact setContactMobilePhone(String contactMobilePhone) {
    this.contactMobilePhone = contactMobilePhone;
    return this;
  }

  public Contact setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
    return this;
  }

  public Contact setContactType(ContactType contactType) {
    this.contactType = contactType;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Contact setUsername(String username) {
    this.username = username;
    return this;
  }


  public static Contact createContact(String name,
                                      String position,
                                      String addr,
                                      String postcode,
                                      String hPhone,
                                      String mPhone,
                                      String email,
                                      ContactType type) {

    return new Contact().setContactAddress(addr)
      .setContactEmail(email)
      .setContactHomePhone(hPhone)
      .setContactMobilePhone(mPhone)
      .setContactName(name)
      .setContactPosition(position)
      .setContactPostcode(postcode)
      .setContactType(type)
      .setUsername(name.replaceAll("\\s+","."));
  }
}
