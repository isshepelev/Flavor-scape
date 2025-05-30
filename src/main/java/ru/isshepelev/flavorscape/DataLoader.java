package ru.isshepelev.flavorscape;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.*;
import ru.isshepelev.flavorscape.infrastructure.persistance.entity.enums.GeneralImpression;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.PlaceRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.ReviewRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.RoleRepository;
import ru.isshepelev.flavorscape.infrastructure.persistance.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public void run(String... args) throws Exception {
        Role role1 = new Role();
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");
        roleRepository.save(role1);
        roleRepository.save(role2);

        User user1 = new User();
        user1.setUsername("1");
        user1.setPassword(passwordEncoder.encode("1"));
        user1.setRoles(Set.of(role1));

        User user2 = new User();
        user2.setUsername("2");
        user2.setPassword(passwordEncoder.encode("2"));
        user2.setRoles(Set.of(role2));

        User user3 = new User();
        user3.setUsername("3");
        user3.setPassword(passwordEncoder.encode("3"));
        user3.setRoles(Set.of(role1));

        User user4 = new User();
        user4.setUsername("4");
        user4.setPassword(passwordEncoder.encode("4"));
        user4.setRoles(Set.of(role1));

        User user5 = new User();
        user5.setUsername("5");
        user5.setPassword(passwordEncoder.encode("5"));
        user5.setRoles(Set.of(role1));

        User user6 = new User();
        user6.setUsername("6");
        user6.setPassword(passwordEncoder.encode("6"));
        user6.setRoles(Set.of(role1));

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);



//        Place place1 = new Place();
//        place1.setId(70000001089307106L);
//        place1.setName("Chicko-вкус Кореи");
//        place1.setAddressName("площадь Ленина, 6");
//        place1.setAddressComment("1 этаж");
//
//        Place place2 = new Place();
//        place2.setId(70000001096845567L);
//        place2.setName("Vысота, пастерия");
//        place2.setAddressName("проспект Революции, 34а");
//        place2.setAddressComment("1 этаж; серая входная группа");
//
//        Place place3 = new Place();
//        place3.setId(70000001033821598L);
//        place3.setName("Винзавод, пространство");
//        place3.setAddressName("Кольцовская улица, 24/3");
//
//        Place place4 = new Place();
//        place4.setId(4363390419997137L);
//        place4.setName("Вкусно — и точка, предприятие быстрого питания");
//        place4.setAddressName("проспект Революции, 32а");
//
//        Place place5 = new Place();
//        place5.setId(70000001066143993L);
//        place5.setName("Культурно Коротко, сосисочная");
//        place5.setAddressName("проспект Революции, 47");
//        place5.setAddressComment("1 этаж");
//
//        Place place6 = new Place();
//        place6.setId(70000001028320803L);
//        place6.setName("Кинолента, кафе-пекарня");
//        place6.setAddressName("Петровская набережная, 11/5");
//
//        Place place7 = new Place();
//        place7.setId(70000001100798592L);
//        place7.setName("Токки");
//        place7.setAddressName("Никитинская, 42");
//        place7.setAddressComment("1 этаж");
//
//        Place place8 = new Place();
//        place8.setId(70000001080263341L);
//        place8.setName("Культурно Коротко, чебуречная");
//        place8.setAddressName("площадь Ленина, 6");
//
//        Place place9 = new Place();
//        place9.setId(4363390419997139L);
//        place9.setName("Вкусно — и точка, предприятие быстрого питания");
//        place9.setAddressName("Генерала Лизюкова, 4г");
//
//        Place place10 = new Place();
//        place10.setId(70000001055471881L);
//        place10.setName("Культурно Коротко, чебуречная");
//        place10.setAddressName("Пушкинская, 1");
//        place10.setAddressComment("1 этаж");
//
//
//        placeRepository.save(place1);
//        placeRepository.save(place2);
//        placeRepository.save(place3);
//        placeRepository.save(place4);
//        placeRepository.save(place5);
//        placeRepository.save(place6);
//        placeRepository.save(place7);
//        placeRepository.save(place8);
//        placeRepository.save(place9);
//        placeRepository.save(place10);
//
//
//
//        // Создание отзывов для Chicko-вкус Кореи
//        List<Review> reviews = new ArrayList<>();
//
//        // Отзыв 1
//        Critique critique1 = new Critique();
//        critique1.setMusic((short)5);
//        critique1.setPoliteness((short)5);
//        critique1.setPurity((short)4);
//        critique1.setTasteOfFood((short)5);
//        critique1.setPrice((short)4);
//        Review review1 = new Review();
//        review1.setCreatedAt(LocalDateTime.now());
//        review1.setGeneralImpression(GeneralImpression.LIKED);
//        review1.setCritique(critique1);
//        review1.setGeneralRating(4.8);
//        review1.setContent("Отличное место с аутентичной корейской кухней! Особенно понравились куриные крылышки в соусе янъян.");
//        review1.setAuthor(user1);
//        review1.setPlace(place1);
//        reviews.add(review1);
//
//        // Отзыв 2
//        Critique critique2 = new Critique();
//        critique2.setMusic((short)3);
//        critique2.setPoliteness((short)4);
//        critique2.setPurity((short)4);
//        critique2.setTasteOfFood((short)3);
//        critique2.setPrice((short)4);
//        Review review2 = new Review();
//        review2.setCreatedAt(LocalDateTime.now());
//        review2.setGeneralImpression(GeneralImpression.LIKED);
//        review2.setCritique(critique2);
//        review2.setGeneralRating(3.5);
//        review2.setContent("Неплохо, но ожидал большего. Рамен показался мне слишком простым, хотя бульон был вкусный.");
//        review2.setAuthor(user2);
//        review2.setPlace(place1);
//        reviews.add(review2);
//
//        // Отзыв 3
//        Critique critique3 = new Critique();
//        critique3.setMusic((short)5);
//        critique3.setPoliteness((short)5);
//        critique3.setPurity((short)5);
//        critique3.setTasteOfFood((short)5);
//        critique3.setPrice((short)5);
//        Review review3 = new Review();
//        review3.setCreatedAt(LocalDateTime.now());
//        review3.setGeneralImpression(GeneralImpression.LIKED);
//        review3.setCritique(critique3);
//        review3.setGeneralRating(5.0);
//        review3.setContent("Обожаю это место! Лучший кимчи в городе. Персонал всегда приветливый, цены адекватные.");
//        review3.setAuthor(user3);
//        review3.setPlace(place1);
//        reviews.add(review3);
//
//        // Отзыв 4
//        Critique critique4 = new Critique();
//        critique4.setMusic((short)2);
//        critique4.setPoliteness((short)2);
//        critique4.setPurity((short)3);
//        critique4.setTasteOfFood((short)2);
//        critique4.setPrice((short)2);
//        Review review4 = new Review();
//        review4.setCreatedAt(LocalDateTime.now());
//        review4.setGeneralImpression(GeneralImpression.LIKED);
//        review4.setCritique(critique4);
//        review4.setGeneralRating(2.2);
//        review4.setContent("Заказал ттокпокки - оказалось очень остро, хотя я просил сделать менее острым. Персонал невнимательный.");
//        review4.setAuthor(user4);
//        review4.setPlace(place1);
//        reviews.add(review4);
//
//        // Отзыв 5
//        Critique critique5 = new Critique();
//        critique5.setMusic((short) 4);
//        critique5.setPoliteness((short)4);
//        critique5.setPurity((short)5);
//        critique5.setTasteOfFood((short)4);
//        critique5.setPrice((short)5);
//        Review review5 = new Review();
//        review5.setCreatedAt(LocalDateTime.now());
//        review5.setGeneralImpression(GeneralImpression.LIKED);
//        review5.setCritique(critique5);
//        review5.setGeneralRating(4.4);
//        review5.setContent("Хорошее соотношение цены и качества. Интерьер приятный, музыка ненавязчивая. Буду возвращаться!");
//        review5.setAuthor(user5);
//        review5.setPlace(place1);
//        reviews.add(review5);
//
//        // Сохранение всех отзывов
//        reviewRepository.saveAll(reviews);
//
//        // Обновляем место с отзывами
//        place1.setReviews(reviews);
//        placeRepository.save(place1);
    }
}
