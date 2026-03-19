# NERV | Persistence

A specialized Spring Data JPA persistence library providing enhanced repository features, dynamic
search specifications, and advanced projection support.

## Features

- **Advanced Projections**: Create and materialize JPA projections using `QueryProjectionUtils` and
  `JpaSpecificationExecutorWithProjection`.
- **Dynamic Search Specifications**: Build complex `Specification` objects from compact search
  strings using `QueryBuilder`.
- **Slice Repository Support**: Native support for `Slice` queries which are more efficient than
  `Page` queries when the total count is not needed.
- **Base Entities**: Standardized base classes for entities including `AuditableEntity`,
  `BusinessEntity`, and `EnableEntity`.
- **Relational Operators**: Support for a wide range of operators in search strings:
    - `:` (Equal)
    - `/` (Not Equal)
    - `>` (Greater Than)
    - `<` (Less Than)
    - `>=` (Greater Than or Equal)
    - `<=` (Less Than or Equal)
    - `*` (Like)
    - `@` (In)
    - `^` (Join)
    - `!` (Not Null)
    - `~` (Is Null)

## Prerequisites

- **Java 25**
- **Spring Boot 4.0.3**
- **Hibernate 7.1.3.Final**
- **Lombok**
- **MapStruct 1.6.3**

## Usage

### 1. Extending Repositories

Use `SliceJpaRepository` to gain access to slice-based queries and projection executors.

```java

@Repository
public interface UserRepository extends SliceJpaRepository<UserEntity, Long> {

}
```

### 2. Dynamic Query Building

You can build specifications from a simple search string format: `field:value,field2*value2,`.

```java
String searchParams = "firstName:Edward,lastName*ar";
Specification<UserEntity> spec = QueryBuilder.build(
    searchParams,
    UserSpecification.class,
    UserSearchFields.class
);

Slice<UserEntity> result = userRepository.findAllSlice(spec, Pageable.ofSize(10));
```

### 3. Projections with Specifications

Fetch projected DTOs or interfaces directly using specifications:

```java
Specification<UserEntity> spec = ...;
Page<UserDTO> projectedPage = userRepository.findAll(spec, Pageable.ofSize(10), UserDTO.class);
```

### 4. Base Entities

Inherit from specialized base classes to add common fields:

- `BaseEntity`: Basic ID-based entity.
- `AuditableEntity`: Adds `createdBy`, `createdDate`, `lastModifiedBy`, and `lastModifiedDate`.
- `BusinessEntity`: Adds a business `code`.
- `EnableEntity`: Adds an `enabled` flag.

## Installation

Add the dependency to your `pom.xml`:

```xml

<dependency>
  <groupId>com.czetsuyatech</groupId>
  <artifactId>nerv-persistence</artifactId>
  <version>${nerv-persistence.version}</version>
</dependency>
```

## Configuration

Enable the repository support by importing `NervRepositoryConfig`:

```java

@Configuration
@Import(NervRepositoryConfig.class)
public class PersistenceConfig {

}
```

## Test Resources

The project includes test resources to support integration testing and feature validation:

- `src/test/resources/sql/users.sql`: Provides a comprehensive dataset for testing various
  persistence features:
    - **Diverse Entity Data**: A set of user accounts with varied personal details (names, IDs) to
      test pagination and slice results.
    - **Search-Ready Fields**: Specific data configurations designed to validate exact matches,
      partial (like) searches, and case-sensitivity.
    - **Temporal Data**: Inclusion of date-based fields to test range queries and chronological
      sorting.
    - **Relational Mapping**: Sample data for associated entities (e.g., hobbies) to verify join
      operations and collection-based filtering.

### Reference Documentation

For further reference, please consider the following sections:

* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.3/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Data JPA Specification](https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and
`<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove
those overrides.

