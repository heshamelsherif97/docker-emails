package CommandPattern;

import java.sql.Timestamp;

public class Email{

    private Integer id;
    private String timestamp;
//    private User sender;
    private String subject;
    private String body;
    private String type;
    private String folder;
    private String thread_id;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp.toString();
    }

//    public void setSender(User sender) {
//        this.sender = sender;
//    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }
}