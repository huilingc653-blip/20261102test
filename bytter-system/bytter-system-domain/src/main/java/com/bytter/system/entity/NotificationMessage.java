package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
/**
 * 消息国际化存储表
 *
 * @author zhoul
 * @since 2019-11-28
 */
@Entity
@Table(name = "SYS_NOTIFICATION_MESSAGE")
public class NotificationMessage implements Serializable {
    @Id
    private String id;
    @Id
    @Column(name = "N_LOCALE")
    private String locale;
    @Column(name = "N_MESSAGE")
    private String message;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationMessage that = (NotificationMessage) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(locale, that.locale) &&
                Objects.equals(message, that.message);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, locale, message);
    }
}
