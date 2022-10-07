package com.demo.springdatabug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringDataBugApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringDataBugApplication.class, args);
	}
}

@Service
class UserService {
	public final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostConstruct
	public void init() {
		User user = new User(1L, "Test");

		// success: Response -> id == 1, DB -> id == 1
		userRepository.save(user);

		// success (no error): Response -> id == 1, DB -> id == [auto-gen] new ObjectId()
		userRepository.insert(user);
	}
}

@Repository
interface UserRepository extends MongoRepository<User, Long> {}

@Document
class User {
	@MongoId
	public Long id;

	@Field
	public String name;

	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
