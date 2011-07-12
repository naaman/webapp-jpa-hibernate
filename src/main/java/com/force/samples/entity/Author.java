package com.force.samples.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Author {
	
	@Id
	@GeneratedValue
	private long id;
	
	private String firstName;
	
	private String lastName;

    @OneToMany //(cascade = CascadeType.PERSIST)
    private List<Book> books;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

}
