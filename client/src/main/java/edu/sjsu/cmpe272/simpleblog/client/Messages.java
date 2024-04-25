package edu.sjsu.cmpe272.simpleblog.client;

public class Messages {}

class PostMessage{
    public String date;
    public String author;
    public String message;
    public String attachment;
    public String signature;

    public PostMessage(String date, String author, String message, String attachment, String signature) {
        this.date=date;
        this.author=author;
        this.message=message;
        this.attachment=attachment;
        this.signature=signature;

    }

}
class ListMessage{
    public Integer limit;
    public Integer next;

    public ListMessage(Integer limit,Integer next){
        this.limit=limit;
        this.next=next;
    }
}
