
### a) Identify a couple of examples that use AssertJ expressive methods chaining.

No ficheiro `B_EmployeeService_UnitTest.java`:

```java
assertThat(result).isPresent()
                  .get()
                  .extracting(Employee::getName)
                  .isEqualTo("Ana");
```

Outro exemplo:

```java
assertThat(result.get().getId()).isNotNull();
```

---

### b) Take note of transitive annotations included in `@DataJpaTest`.

A anotação `@DataJpaTest` inclui de forma transitiva várias outras funcionalidades:

- Configuração automática de Spring Data JPA
- Transações rollback após cada teste
- H2 in-memory database (caso não haja outro configurado)
- Anotações incluídas:
    - `@Transactional`
    - `@AutoConfigureTestDatabase`
    - `@TestPropertySource` (pode ser usada para configuração específica)

---

### c) Identify an example in which you mock the behavior of the repository (and avoid involving a database).

O ficheiro `B_EmployeeService_UnitTest.java` é um exemplo claro:

```java
@Mock
private EmployeeRepository employeeRepository;

@BeforeEach
void setUp() {
    service = new EmployeeServiceImpl(employeeRepository);
}
```

Neste teste, o repositório é simulado com Mockito       , evitando acesso real à base de dados.

---

### d) What is the difference between standard `@Mock` and `@MockBean`?

- `@Mock` (Mockito):
    - Usado em **testes unitários**
    - Cria um mock standalone do objeto
    - Deve ser passado manualmente às classes dependentes

- `@MockBean` (Spring Boot):
    - Usado em **testes de integração**
    - Regista o mock no ApplicationContext
    - Substitui o bean real por um mock automaticamente

---

### e) What is the role of the file `application-integrationtest.properties`? In which conditions will it be used?

Este ficheiro define propriedades específicas para testes de integração, como:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
```

Será usado quando os testes são anotados com `@TestPropertySource` ou quando é carregado pelo contexto de teste (ex: no `D_EmployeeRestControllerIT.java` ou `E_EmployeeRestControllerTemplateIT.java`).

---

### f) The sample project demonstrates three test strategies to assess an API (C, D and E) developed with SpringBoot. Which are the main/key differences?

- **C_EmployeeController_WithMockServiceTest.java**
    - **Testes unitários**
    - Usa `@WebMvcTest` com `@MockBean` para simular o `EmployeeService`
    - Foca-se apenas no **controller**, sem carregar o contexto completo

- **D_EmployeeRestControllerIT.java**
    - **Testes de integração**
    - Usa `@SpringBootTest` e `TestRestTemplate`
    - Integra a aplicação completa, incluindo os beans reais e contexto web

- **E_EmployeeRestControllerTemplateIT.java**
    - Também usa `TestRestTemplate`
    - Demonstra **uma abordagem alternativa à anterior**, mas com menos mocks e maior foco no comportamento end-to-end

---

```
