package models;

import interfaces.Manageable;
public abstract class User implements Manageable {

    private String id;
    private String name;
    private String email;
    private int    age;

    public User(String id, String name, String email, int age) {
        this.id    = id;
        this.name  = name;
        this.email = email;
        this.age   = age;
    }

    public String getId()    { return id; }

    public String getName()  { return name; }

    public String getEmail() { return email; }

    public int    getAge()   { return age;   }

    public void setName(String name)   { this.name  = name;  }

    public void setEmail(String email) { this.email = email; }

    public void setAge(int age)        { this.age   = age;   }

    public abstract String getRole();

    @Override
    public String getSummary() {
        return "[" + getId() + "] " + getName() + " | Role: " + getRole();
    }

    @Override
    public void displayDetails() {
        System.out.println("  ID    : " + id);
        System.out.println("  Name  : " + name);
        System.out.println("  Email : " + email);
        System.out.println("  Age   : " + age);
        System.out.println("  Role  : " + getRole());
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
