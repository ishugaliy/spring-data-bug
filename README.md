### WHAT
**Spring Data Mongo** repositories API -> `insert(...)` ignores `@MongoId` annotation with variable type and always auto-generates new `ObjectId` for the provided entity.

### VERSION
**org.springframework.boot**: 2.7.4
**io.spring.dependency-management**: 1.0.14.RELEASE

### EXPECTED BEHAVIOUR

`repository.insert(...)` - should create an entity in Mongo DB according to the configuration & variable type of provided `@MongoId` annotation inputs. In case of the existance of the entity - throw `org.springframework.dao.DuplicateKeyException` exception.

Issue **not** reproduced for: **io.spring.dependency-management**: 1.0.13.RELEASE


### CODE SAMPLE
```kotlin
@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) { runApplication<DemoApplication>(*args) }

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @PostConstruct
    fun init() {
        val user = User(1, "Test")

        // success: Response -> id == 1, DB -> id == 1
        userRepository.save(user) 

        // success (no error): Response -> id == 1, DB -> id == [auto-gen] new ObjectId()
        userRepository.insert(user) 
    }
}

@Repository
interface UserRepository: MongoRepository<User, Long>

@Document
class User(
    @MongoId val id: Long,
    @Field val name: String
)
```

### RESULTED MONGO DATA
**Mongo version**: 4.4.13

![image](https://user-images.githubusercontent.com/7562034/194579648-e7b91435-4658-4d75-bb5b-a18a89955a57.png)
