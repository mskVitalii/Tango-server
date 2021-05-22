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

            Genre SCI_FI = genreRepository.findByGenreName("SCI-FI").orElseThrow(() -> new RuntimeException("genre not found"));
            Genre Drama = genreRepository.findByGenreName("Drama").orElseThrow(() -> new RuntimeException("genre not found"));
            Genre Comedy = genreRepository.findByGenreName("Comedy").orElseThrow(() -> new RuntimeException("genre not found"));
            Genre Detective = genreRepository.findByGenreName("Detective").orElseThrow(() -> new RuntimeException("genre not found"));

            var faker = new Faker();

            ////////////////////////////////// Сохраняем несколько фильмов
            Film film1 = filmRepository.save(new Film(
                    "Satantango",
                    8.0,
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/0c32806d-9e09-4aac-a53a-8cb5ee6aa9f8/960x960",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/0c32806d-9e09-4aac-a53a-8cb5ee6aa9f8/960x960",
                    "Действие фильма разворачивается на территории фермы, доживающей свои последние дни. " +
                            "Несколько ее жителей решают уйти, похитив деньги, вырученные всеми участниками коммуны перед ее закрытием. " +
                            "Однако их планы нарушают слухи о появлении красноречивого и харизматичного Иримиаша, " +
                            "пропавшего полтора года назад и считавшегося погибшим.",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"));

            filmGenreRepository.save(new FilmGenre(film1, SCI_FI));
            filmGenreRepository.save(new FilmGenre(film1, Comedy));
            filmGenreRepository.save(new FilmGenre(film1, Drama));
            filmGenreRepository.save(new FilmGenre(film1, Detective));

            filmRepository.save(film1);

            Film film3 = filmRepository.save(new Film(
                    "Догвилль",
                    7.9,
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/157fa777-376a-40be-8c1d-336d34413902/960x960",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/157fa777-376a-40be-8c1d-336d34413902/960x960",
                    "Юная Грейс, сбежав от банды гангстеров, " +
                            "находит спасение в маленьком городке Догвилль где-то в Скалистых горах. " +
                            "Местные жители – один прекраснее другого – готовы ее укрыть. " +
                            "А взамен им совсем ничего не надо, ну, разве что помочь по дому или присмотреть за детьми. " +
                            "Но постепенно милый Догвилль превращается для девушки в тюрьму.",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"));

            filmGenreRepository.save(new FilmGenre(film3, Detective));
            filmRepository.save(film3);

            Film film4 = filmRepository.save(new Film(
                    "Мандерлей",
                    7.1,
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1773646/639d1108-7006-4a61-8307-bc56f9537418/800x800",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1773646/639d1108-7006-4a61-8307-bc56f9537418/800x800",
                    "Через два месяца после описанных в «Догвилле» событий Грейс оказывается на плантации в Алабаме, " +
                            "где рабочие не знают, что рабство было отменено 70 лет назад...",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"));

            filmGenreRepository.save(new FilmGenre(film4, Drama));
            filmGenreRepository.save(new FilmGenre(film4, Detective));
            filmRepository.save(film4);

            Film film5 = filmRepository.save(new Film(
                    "Рассекая волны",
                    7.9,
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1600647/9d776b40-6bbb-481d-91c3-a03942f6b042/960x960",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1600647/9d776b40-6bbb-481d-91c3-a03942f6b042/960x960",
                    "Бог дает каждому что-то, чтобы он стал лучше. " +
                            "Молодая девушка Бесс из отдаленной общины на северо-западе Шотландии влюбилась в хорошего парня " +
                            "- Яна, работающего на буровой установке в море. Несмотря на противостояние родственников, они женятся. " +
                            "Бесс просит Бога, чтобы Ян всегда был рядом с ней, и Ян возвращается к ней искалеченным после несчастного случая на буровой." +
                            " Что может сделать молодая женщина, чтобы ее любимый остался жив? На какую жертву может пойти?",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"));

            filmGenreRepository.save(new FilmGenre(film5, Drama));
            filmRepository.save(film5);


            Film film6 = filmRepository.save(new Film(
                    "Реконструкция",
                    7.5,
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/0abaa12f-01ff-46db-9653-efd31961fd33/960x960",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/0abaa12f-01ff-46db-9653-efd31961fd33/960x960",
                    "Двое — мужчина и женщина — встречаются в Копенгагене, проводят вместе восхитительную ночь и затем отчаянно пытаются освободиться от рутины будней и рискуют всем ради возможности быть вместе...",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"));

            filmGenreRepository.save(new FilmGenre(film6, SCI_FI));
            filmGenreRepository.save(new FilmGenre(film6, Detective));
            filmRepository.save(film6);



            var film = new Film(
                    "Меланхолия",
                    7.0,
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1773646/52191db9-ffe5-4280-9e8a-3ec334b1dcc5/800x800",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1773646/52191db9-ffe5-4280-9e8a-3ec334b1dcc5/800x800",
                    "События фильма разворачиваются в дни, которые предшествуют катастрофе. " +
                            "Первая часть посвящена свадьбе Жюстин, которая быстро охладевает к торжеству, " +
                            "чем вызывает непонимание близких и гостей. " +
                            "Героиней второй части является Клэр, сестра Жюстин. " +
                            "Вначале Клэр ухаживает за впавшей в клиническую депрессию Жюстин " +
                            "и одновременно страшится сообщений о приближении таинственной планеты Меланхолия. " +
                            "Постепенно, по мере приближения планеты, Жюстин и Клэр меняются ролями. " +
                            "Теперь паникующая Клэр нуждается в заботе. В отчаянии она с сестрой и сыном готовится принять неизбежное.",
                    "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");




            User user = new User(
                    "Gleb@gmail.com",
                    "Gleb",
                    passwordEncoder.encode("password"),
                    LocalDate.now().minusYears(20),
                    LocalDate.now().plusMonths(1),
                    Set.of(userRole, adminRole));
            user.setAvatar("https://sun9-13.userapi.com/impf/c851420/v851420370/17fde8/h8rAbGB6ARU.jpg?size=1280x851&quality=96&sign=d679fde48469fd7b3223f322b6a95190&type=album");
            User admin = new User(
                    "Vitaly@gmail.com",
                    "Vitaly",
                    passwordEncoder.encode("password"),
                    LocalDate.now().minusYears(20),
                    LocalDate.now().plusMonths(1),
                    Set.of(adminRole));
            admin.setAvatar("https://sun9-72.userapi.com/impg/iIilCYpm_t5LShkqB4be6oJYIW_vfxzQu8m37Q/xSVgMhi9ccE.jpg?size=810x1080&quality=96&sign=2124c94bab72cf2719b8fdf3ec1edd3c&type=album");
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
            filmGenreRepository.save(new FilmGenre(film2, SCI_FI));
            filmRepository.save(film2);

            User user1 = userRepository.save(user);
            user1.addFavorite(film2);
            userRepository.save(user1);
            System.out.println(userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new Exception("А теперь всё вдвойне хуже")));
            userRepository.findByUsernameFetch(user.getUsername());

            System.out.println(userRepository.findByUsernameFetch(user.getUsername()));

            filmRepository.getOne(film.getFilmId());

            for (int i = 0; i < 30; i++)
                System.out.print(" ○ ");
            System.out.println("OK #1\n");

            ////////////////////////////// Логика с чатом
            ChatRoom chatRoom = new ChatRoom("Новый чатик");
            chatRoomRepository.save(chatRoom);
            ChatUser Vitaly = new ChatUser(ChatUserRights.GOD, true, admin, chatRoom);
            ChatUser Gleb = new ChatUser(ChatUserRights.GOD, true, user, chatRoom);

            chatUserRepository.save(Vitaly);
            chatUserRepository.save(Gleb);

            Message message1 = new Message("------------------------------", Vitaly, chatRoom);
            Message message2 = new Message("\tСервер запустился",Vitaly, chatRoom);
            Message message3 = new Message("------------------------------", Vitaly, chatRoom);
            messageRepository.save(message1);
            messageRepository.save(message2);
            messageRepository.save(message3);

            message2.addAttachment(message2.createAttachment("picture", "video", "music", "map"));
            message2.addAttachment(message2.createAttachment("picture", "video", "music", "map"));
            message2.addAttachment(message2.createAttachment("picture", "video", "music", "map"));
            messageRepository.save(message2);

            for (int i = 0; i < 30; i++)
                System.out.print(" ○ ");
            System.out.println("OK #2\n");
        };
    }
}