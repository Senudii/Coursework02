package Entity;

public class Book {
    private int id;
    private String title;
    private String author;
    private int categoryId;
    private String edition;
    private String description;

    // Default constructor
    public Book() {}

    // Parameterized constructor
    public Book(int id, String title, String author, int categoryId, String edition, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.categoryId = categoryId;
        this.edition = edition;
        this.description = description;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for author
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter and Setter for categoryId
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    // Getter and Setter for edition
    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}