# Конслоьное приложение для работы с FTP сервером 
### [Автотесты для данного проекта](https://github.com/DyukovNA/ftpTests)

На сервере расположен файл с информацией о студентах кафедры в виде JSON подобной структуры:
<p align="center">
  <img width="300" alt="image" src="https://github.com/user-attachments/assets/1da39357-2335-4bfa-b25a-7f9b401899f4">
</p>

В соответствии с заданием, при написании программы не использовались внешние библиотеки.<p></p>
Перед началом работы необходимо убедиться, что сервер активен и на нём находится файл с названием students.json. Название файла, 
который должен находиться на сервере при необходимости можно изменить в коде (поле jsonName класса Main). После изменения требуется пересобрать проект.
После успешного запуска приложение попросит пользователя ввести данные для подключения к серверу: имя пользователя, пароль и IP-адрес.
Затем будет необходимо выбрать одно из пяти действий:<p></p>

1. Вывести список студентов (по алфавиту)
2. Вся информация о студенте
3. Добавить студента в файл
4. Удалить студента из файла
5. Завершение работы с приложением

Вывод информации и студенте или его удаление происходит при предоставлении пользователем ID студента. Для добавления студента необходимо предоставить информацию о нём в
следующем виде:

Аргумент1 значение1, агрумент2 значение2, …

Необходимо, чтобы присутствовал хотя бы один аргумент. При добавлении, запись помещается в конец списка. ID генерируется автоматически.
