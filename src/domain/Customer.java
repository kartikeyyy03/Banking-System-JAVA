package domain;

public class Customer {

    private String Id;
    private String name;
    private String email;

    public Customer(String Id, String name, String email) {
        this.Id = Id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
