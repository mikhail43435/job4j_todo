## Todo

### О проекте
Приложение для ведения списка задач. Позволяет пользователям совместо вести список заданий.

### Функционал
* CRUD операции с Hibernate и PostgreSQL
* Авторизация/регистрация. Доступ только у авторизованных пользователей (реализовано через фильтр).
  Авторизованный пользователь сохраняется в сессию.

### Используемые технологии
```
- Spring boot
- JDBC (PostgreSQL)
- Bootstrap
- Thymeleaf
- Logback
- Slf4j
- Junit
- Liquidbase
- h2
- Assertj
- Mockito
- Travis CI
```

`java` `spring boot` `thymeleaf` `postgresql` `liquidbase` `h2` `logback` `slf4j` `junit` `assertj` `mockito` `travis CI`

###Pages (templates):

####tasks
<sub>Displays a list of all tasks.</sub>
```
fields:
- #
- name
- date of creation
- status
buttons: 
- Show all 
- Show new
- Show finished
```
#####showTask

<sub>tasks details of selected task.</sub>
```
fields (all view only):
 - #
 - date of creation
 - name
 - description
 - status
buttons:
 - Mark as done - go back to task's page
 - Edit - available only task has 'news' status
 - Delete
```
#####createTask
<sub>Page for creating new task.</sub>
```
fields:
 - name
 - description
buttons:
 - Save
 - Discard (back to task's list)
```
#####editTask
<sub>Page for editing selected task.</sub>
```fields:
 - #
 - date of creation (view only)
 - status(view only)
 - name (editable)
 - description (editable)
buttons:
 - Save
 - Discard (back to task's list)
 ```
#####loginUser
<sub>Page for user login action.</sub>
```
fields:
 - name
 - login
 - password
buttons:
 - Login
 - Register
 ```
#####registrationUser
<sub>Page for user registration action.</sub>
```
fields:
  - name  
  - login
  - password
buttons:
  - Register
```
#####users
<sub>Displays a list of all users.</sub>
```
fields:
  - name  
  - login
  - password
```
#####Constrains
```
task.name - length range 1-255, not blank string
task.description - length range 0-1000
user.name - length range 1-100, not blank string
user.login - length range 1-100, not blank string
user.password - length range 6-100, not blank string, only digits or letters
```