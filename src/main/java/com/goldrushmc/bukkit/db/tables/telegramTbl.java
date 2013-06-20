package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Lex
 * Date: 20/06/13
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "telegrams_tbl")
public class TelegramTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "SENDER_NAME")
    @NotEmpty
    private String sender_name;
    @Column(name = "RECIPIENT_NAME")
    @NotEmpty
    private String recipient_name;
    @Column(name = "MESSAGE")
    @NotEmpty
    private String message;
    @Column(name = "DATE_SENT")
    @NotEmpty
    private Date date_sent ;
    @Column(name = "received")
    @NotEmpty
    private Boolean received ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(Date date_sent) {
        this.date_sent = date_sent;
    }

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }
}
