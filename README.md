# publisher-crud

Необходимо реализовать консольное CRUD приложение, которое имеет следующие сущности:

Writer (id, firstName, lastName, List<Post> posts)
Post (id, content, created, updated, List<Label> labels)
Label (id, name)
PostStatus (enum ACTIVE, UNDER_REVIEW, DELETED)

В качестве хранилища данных необходимо использовать json файлы:
writers.json, posts.json, labels.json
Пользователь в консоли должен иметь возможность создания, получения, редактирования и удаления данных.
Слои:
model - POJO классы
repository - классы, реализующие доступ к текстовым файлам
controller - обработка запросов от пользователя
view - все данные, необходимые для работы с консолью

Например: Writer, WriterRepository, WriterController, WriterView и т.д.
Для репозиторного слоя желательно использовать базовый интерфейс:
interface GenericRepository<T,ID>



interface WriterRepository extends GenericRepository<Writer, Long>

class JsonWriterRepositoryImpl implements WriterRepository

Результатом выполнения задания должен быть отдельный репозиторий с README.md файлом, который содержит описание задачи, проекта и инструкции запуска приложения через командную строку.
Для работы с json необходимо использовать библиотеку Gson (https://www.baeldung.com/gson-deserialization-guide). Для импорта зависимостей использовать на выбор Maven/Gradle.
