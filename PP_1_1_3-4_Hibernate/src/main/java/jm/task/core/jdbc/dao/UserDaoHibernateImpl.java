package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import java.util.ArrayList;
import java.util.List;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;


public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String createTableSQL = getCreateTableSQL();
            session.createNativeQuery(createTableSQL).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица пользователей успешно создана или уже существует!");
        } catch (GenericJDBCException e) {
            e.printStackTrace();
            System.out.println("Ошибка при создании таблицы.");
        }
    }

    private String getCreateTableSQL() {
        return "CREATE TABLE IF NOT EXISTS Users ("
                + "id INT NOT NULL AUTO_INCREMENT, "
                + "name VARCHAR(40) NOT NULL, "
                + "lastname VARCHAR(40) NOT NULL, "
                + "age INT NOT NULL, "
                + "PRIMARY KEY (id))";
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // SQL для удаления таблицы
            String dropTableSQL = "DROP TABLE IF EXISTS Users";

            session.createNativeQuery(dropTableSQL).executeUpdate();
            session.getTransaction().commit();
            System.out.println("Таблица пользователей успешно удалена!");

        } catch (GenericJDBCException e) {
            e.printStackTrace();
            System.out.println("Ошибка при удалении таблицы.");
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();

        } catch (GenericJDBCException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Не удалось сохранить пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Получаем пользователя из базы данных
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user); // Удаляем пользователя
                System.out.println("Пользователь с ID " + id + " был удален."); // Сообщение об успешном удалении
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }

            transaction.commit(); // Фиксируем транзакцию
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Откат при возникновении ошибки
            }
            e.printStackTrace(); // Вывод информации об ошибке
            System.out.println("Ошибка при удалении пользователя."); // Сообщение об ошибке
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) { // Автоматическое закрытие сессии
            return session.createQuery("FROM User", User.class).list(); // Получаем всех пользователей
        } catch (Exception e) {
            // Логируем или обрабатываем исключение
            e.printStackTrace();
            return new ArrayList<>(); // Возврат пустого списка по умолчанию
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Выполнение HQL-запроса для удаления всех записей из таблицы Users
            session.createQuery("DELETE FROM User").executeUpdate();

            transaction.commit();
            System.out.println("Таблица пользователей успешно очищена!");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.println("Ошибка при очистке таблицы пользователей.");
        }
    }
}
