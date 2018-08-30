package mans.abdullah.abdullah_mansour.library;

public class DataClass {
    String book_title,author_name;

    public DataClass(String book_title, String author_name) {
        this.book_title = book_title;
        this.author_name = author_name;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
}
