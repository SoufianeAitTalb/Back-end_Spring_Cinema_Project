package org.sid.cinema;

import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.service.ICinemaIntitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.io.File;

@SpringBootApplication

public class CinemaApplication implements CommandLineRunner {
    @Autowired
    private ICinemaIntitService cinemaIntitService;
    @Autowired
    private RepositoryRestConfiguration restConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
            restConfiguration.exposeIdsFor(Film.class, Salle.class, Ticket.class);
//        cinemaIntitService.initVilles();
//        cinemaIntitService.initCinemas();
//        cinemaIntitService.initSalles();
//        cinemaIntitService.initPlaces();
//        cinemaIntitService.initSeances();
//        cinemaIntitService.initCategories();
//        cinemaIntitService.initFilms();
//        cinemaIntitService.initProjections();
//        cinemaIntitService.initTickets();

    }
}
