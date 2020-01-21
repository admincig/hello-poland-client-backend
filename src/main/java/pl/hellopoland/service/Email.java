package pl.hellopoland.service;

public class Email {

  public String recipientEmail;
  public String subject;
  public String msg;

  public Email(String recipientEmail, String subject, String msg) {
    this.recipientEmail = recipientEmail;
    this.subject = subject;
    this.msg = msg;
  }
}
