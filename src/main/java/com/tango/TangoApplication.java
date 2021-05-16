package com.tango;

import com.github.javafaker.Faker;
import com.tango.models.chat.attachment.Attachment;
import com.tango.models.chat.attachment.AttachmentRepository;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageRepository;
import com.tango.models.chat.room.ChatRoom;
import com.tango.models.chat.room.ChatRoomRepository;
import com.tango.models.chat.user.ChatUser;
import com.tango.models.chat.user.ChatUserRepository;
import com.tango.models.chat.user.ChatUserRights;
import com.tango.models.films.comment.Comment;
import com.tango.models.films.comment.CommentRepository;
import com.tango.models.films.film.Film;
import com.tango.models.films.film.FilmRepository;
import com.tango.models.films.genre.FilmGenre;
import com.tango.models.films.genre.FilmGenreRepository;
import com.tango.models.films.genre.Genre;
import com.tango.models.films.genre.GenreRepository;
import com.tango.models.films.tag.Tag;
import com.tango.models.films.tag.TagRepository;
import com.tango.models.roles.Role;
import com.tango.models.roles.RoleRepository;
import com.tango.models.user.User;
import com.tango.models.user.UserRepository;
import com.tango.security.roles.AuthRole;
import com.tango.security.roles.AuthRoleRepository;
import com.tango.security.roles.ERole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class TangoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TangoApplication.class, args);
    }

    @Transactional
    @Bean
    CommandLineRunner commandLineRunner(
            FilmRepository filmRepository,
            UserRepository userRepository,
            CommentRepository commentRepository,
            AuthRoleRepository authRoleRepository,
            PasswordEncoder passwordEncoder,
            TagRepository tagRepository,
            GenreRepository genreRepository,
            RoleRepository roleRepository,
            ChatRoomRepository chatRoomRepository,
            ChatUserRepository chatUserRepository,
            MessageRepository messageRepository,
            AttachmentRepository attachmentRepository,
            FilmGenreRepository filmGenreRepository) {
        return args -> {
            ////////////////////////////////// Сохраняем роли аутентификации
            authRoleRepository.save(new AuthRole(ERole.ROLE_USER));
            authRoleRepository.save(new AuthRole(ERole.ROLE_ADMIN));
            AuthRole userRole = authRoleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            AuthRole adminRole = authRoleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
            ////////////////////////////////// Сохраняем несколько ролей
            roleRepository.save(new Role("Producer"));
            roleRepository.save(new Role("Actor"));
            roleRepository.save(new Role("Operator"));
            roleRepository.save(new Role("Composer"));
            roleRepository.save(new Role("Sound engineer"));
            roleRepository.save(new Role("Extras"));
            ////////////////////////////////// Сохраняем несколько тэгов
            tagRepository.save(new Tag("cyberpunk"));
            tagRepository.save(new Tag("steampunk"));
            tagRepository.save(new Tag("superhero"));
            tagRepository.save(new Tag("magic"));
            ////////////////////////////////// Сохраняем несколько жанров
            genreRepository.save(new Genre("SCI-FI"));
            genreRepository.save(new Genre("Drama"));
            genreRepository.save(new Genre("Comedy"));
            genreRepository.save(new Genre("Detective"));
            genreRepository.save(new Genre("Hentai"));

            Genre SCI_FI = genreRepository.findByGenreName("SCI-FI").orElseThrow(() -> new RuntimeException("genre not found"));
            Genre Drama = genreRepository.findByGenreName("Drama").orElseThrow(() -> new RuntimeException("genre not found"));
            Genre Comedy = genreRepository.findByGenreName("Comedy").orElseThrow(() -> new RuntimeException("genre not found"));
            Genre Detective = genreRepository.findByGenreName("Detective").orElseThrow(() -> new RuntimeException("genre not found"));

            var faker = new Faker();

            ////////////////////////////////// Сохраняем несколько фильмов
            Film film1 = filmRepository.save(new Film(
                    "SCI_FI future",
                    faker.number().randomDouble(1, 0, 10),
                    faker.avatar().image(),
                    faker.avatar().image(),
                    "Some film",
                    "https://vimeo.com/547868060"));
//            film1.addGenre(SCI_FI);

            filmGenreRepository.save(new FilmGenre(film1, SCI_FI));
            filmGenreRepository.save(new FilmGenre(film1, Comedy));
            filmGenreRepository.save(new FilmGenre(film1, Drama));

            filmRepository.save(film1);

            Film film3 = filmRepository.save(new Film(
                    "SCI_FI Drama",
                    faker.number().randomDouble(1, 0, 10),
                    faker.avatar().image(),
                    faker.avatar().image(),
                    "Some film",
                    "https://vimeo.com/547868060"));

            filmGenreRepository.save(new FilmGenre(film3, SCI_FI));

            filmRepository.save(new Film(
                    "Gravity falls 3 season",
                    faker.number().randomDouble(1, 0, 10),
                    faker.avatar().image(),
                    faker.avatar().image(),
                    "Some film",
                    "https://vimeo.com/547868060"));


            var film = new Film(
                    faker.name().title(),
                    faker.number().randomDouble(1, 0, 10),
                    faker.avatar().image(),
                    faker.avatar().image(),
                    faker.lorem().characters(20, 500),
                    "https://vimeo.com/547868060");

            User user = new User(
                    "username@gmail.com",
                    "username",
                    passwordEncoder.encode("password"),
                    LocalDate.now().minusYears(20),
                    LocalDate.now().plusMonths(1),
                    Set.of(userRole));

            User admin = new User(
                    "admin@gmail.com",
                    "admin",
                    passwordEncoder.encode("password"),
                    LocalDate.now().minusYears(20),
                    LocalDate.now().plusMonths(1),
                    Set.of(adminRole));

            var comment1 = new Comment(film, user, faker.lorem().characters(), true);
            var comment2 = new Comment(film, admin, faker.lorem().characters(), true);

            System.out.println("\n---------------------------------------------\n");
            userRepository.save(user);
            userRepository.save(admin);
            System.out.println();
            System.out.println();

            filmRepository.save(film);
            film.addComment(comment1);
            film.addComment(comment2);
            commentRepository.save(comment1);
            commentRepository.save(comment2);
            Film film2 = filmRepository.save(film);
            User user1 = userRepository.save(user);
            user1.addFavorite(film2);
            userRepository.save(user1);
            System.out.println(userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new Exception("А теперь всё вдвойне хуже")));
            var savedUser = userRepository
                    .findByUsernameFetch(user.getUsername());

            System.out.println(userRepository.findByUsernameFetch(user.getUsername()));

            var savedFilm = filmRepository.getOne(film.getFilmId());

            //            try {
//                User savedUser = userRepository.findUserWithFavoriteFilms(user.getId()).get(0);
//                savedUser.addFavorite(film);
//                savedUser.getFavoriteFilms().add(film);
//                userRepository.save(savedUser);
//                System.out.println(savedUser.getFavoriteFilms());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//                savedUser.addFavorite(savedFilm);
//                System.out.println("AND WHAT");
//                userRepository.save(savedUser);
//                filmRepository.save(film);

            for (int i = 0; i < 30; i++)
                System.out.print(" ○ ");
            System.out.println("OK #1\n");

            ////////////////////////////// Логика с чатом
            ChatRoom chatRoom = new ChatRoom("Новый чатик");
            chatRoomRepository.save(chatRoom);
            ChatUser chatUser = new ChatUser(ChatUserRights.GOD, admin, chatRoom);
            chatUserRepository.save(chatUser);

            Message message1 = new Message("Текстовое сообщение", chatUser, chatRoom);
            Message message2 = new Message(chatUser, chatRoom);
            Message message3 = new Message("Сообщение с вложениями", chatUser, chatRoom);
            messageRepository.save(message1);
            messageRepository.save(message2);
            messageRepository.save(message3);

            message2.addAttachment(message2.createAttachment("picture", "video", "music", "map", chatRoom));
            message2.addAttachment(message2.createAttachment("picture", "video", "music", "map", chatRoom));
            message2.addAttachment(message2.createAttachment("picture", "video", "music", "map", chatRoom));
            messageRepository.save(message2);

            for (int i = 0; i < 30; i++)
                System.out.print(" ○ ");
            System.out.println("OK #2\n");
        };
    }
}