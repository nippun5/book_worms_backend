# Book Worms — Backend (Spring Boot)

An optional REST API backend for the Book Worms Flutter app — the Flutter app runs
standalone on a local SQLite database by default, so you only need this if you want a
shared server multiple devices can talk to.

## A note on how this was built

This was written in a sandboxed environment with no access to Maven Central, so
`mvn compile` could not be run here to produce a verified build. What I *could* do:
install a JDK and run `javac` across all 47 files directly. That surfaced **zero syntax
errors** — every error was purely "package/symbol not found," which is exactly what you'd
expect when the Spring Boot/Jakarta jars aren't on the classpath. I also manually
cross-checked every repository method referenced from a service, and every service method
referenced from a controller, against their actual declarations. Please run a real
`mvn spring-boot:run` on your machine (with internet access to Maven Central) as the first
real test — let me know what comes up if anything doesn't compile.

## Stack

- Java 21, Spring Boot 3.3 (Web, Data JPA, Validation, Security)
- H2 in-memory database by default — **zero setup**, resets every restart
- MySQL-ready — see `database/schema.sql` and the commented block in
  `application.properties`

## Run it

```bash
cd book_worms_backend
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. The H2 console (if you want to poke at the
data directly) is at `http://localhost:8080/h2-console` — JDBC URL `jdbc:h2:mem:bookworms`,
user `sa`, no password. There's no seed data here (unlike the Flutter app's local demo
mode) — register a user via `/api/auth/register` to get started.

### Switching to MySQL

1. Create a database: `CREATE DATABASE book_worms;`
2. Run `database/schema.sql` against it (or let Hibernate create it — see
   `spring.jpa.hibernate.ddl-auto` in `application.properties`).
3. In `application.properties`, comment out the H2 block and uncomment the MySQL block,
   filling in your credentials.

## API reference

All endpoints are under `/api`. Request/response bodies are JSON.

| Method | Path                              | Purpose                                   |
|--------|-----------------------------------|--------------------------------------------|
| POST   | `/auth/register`                  | Create an account                         |
| POST   | `/auth/login`                     | Log in                                    |
| GET    | `/users`                          | List users (admin)                        |
| GET    | `/users/{id}`                     | Get a user                                |
| PATCH  | `/users/{id}/profile`             | Update name/bio                           |
| PATCH  | `/users/{id}/ban`                 | Suspend/unsuspend (admin)                 |
| PATCH  | `/users/{id}/role`                | Promote/demote (admin)                    |
| GET    | `/books?q=`                       | List or search the catalog                |
| GET    | `/books/{id}`                     | Get a book                                |
| POST   | `/books`                          | Add a book (reuses a title+author match)  |
| DELETE | `/books/{id}`                     | Remove a book + everything tied to it (admin) |
| GET    | `/reviews?tab=trending\|recent`   | Dashboard feed                            |
| GET    | `/books/{bookId}/reviews`         | Reviews for one book                      |
| GET    | `/books/{bookId}/rating`          | Average rating + count                    |
| GET    | `/users/{userId}/reviews`         | A user's reviews                          |
| GET    | `/users/{userId}/liked-review-ids`| Which review IDs this user has liked      |
| POST   | `/reviews`                        | Post a review                             |
| POST   | `/reviews/{id}/like?userId=`      | Toggle a like                             |
| DELETE | `/reviews/{id}`                   | Remove a review (admin)                   |
| GET    | `/listings?bookId=\|ownerUserId=` | Browse/filter the marketplace             |
| GET    | `/listings/{id}`                  | Get a listing                             |
| POST   | `/listings`                       | Post a listing (sell or loan)             |
| PATCH  | `/listings/{id}/status`           | Mark available/unavailable                |
| DELETE | `/listings/{id}`                  | Remove a listing                          |
| GET    | `/loans/borrowed/{userId}`        | Books this user borrowed                  |
| GET    | `/loans/lent/{userId}`            | Books this user lent out                  |
| POST   | `/loans`                          | Request to borrow                         |
| PATCH  | `/loans/{id}/approve`             | Lender accepts (auto-declines rivals)     |
| PATCH  | `/loans/{id}/decline`             | Lender declines                           |
| PATCH  | `/loans/{id}/return`              | Mark returned, listing re-opens           |
| GET    | `/admin/stats`                    | Dashboard counts                          |

## What's intentionally left as a starter

- **Auth is checked at the application layer** (`UserService.login`), not enforced per
  request with a token. `SecurityConfig` currently `permitAll()`s everything so the app
  is easy to call during development. Before shipping: add JWT (or session) auth and
  restrict `/api/admin/**` and the ban/role endpoints to admins only.
- **Passwords** are hashed with BCrypt here (`PasswordConfig`) — note this is a different
  hash than the Flutter app's local-only SQLite mode (which uses SHA-256 client-side for
  its own, separate database). The two aren't interchangeable; a user registered in one
  won't exist in the other until you wire the Flutter repositories to call this API
  instead of the local database.
- No pagination on list endpoints — fine for a demo/small dataset, worth adding
  (`Pageable`) before this sees real traffic.
