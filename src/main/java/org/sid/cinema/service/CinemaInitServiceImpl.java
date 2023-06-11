package org.sid.cinema.service;

import jakarta.transaction.Transactional;
import org.sid.cinema.dao.*;
import org.sid.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaIntitService {
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Override
    public void initVilles() {
        Stream.of("Casablanca","Marrakech","Rabat","Tanger","Ouarzazate","Berrechid","Mohammedia","Agadir").forEach(vName -> {
            Ville ville = new Ville();
            ville.setName(vName);
            villeRepository.save(ville);
        });

    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(ville ->
        {
            Stream.of("MEGARAMA","RENAISSANCE","CINEATLAS","PARADISE","DAWZIL").forEach(cName->{
                Cinema cinema = new Cinema();
                cinema.setName(cName);
                cinema.setNombreSalles(3+(int) (Math.random()*7));
                cinema.setVille(ville);

                cinemaRepository.save(cinema);
            });
        });

    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema ->{
            for(int i=0;i<cinema.getNombreSalles();i++){
                Salle salle = new Salle();
                salle.setName("Salle "+i);
                salle.setNombrePlace(15 + (int)(Math.random()*20));
                salle.setCinema(cinema);
                salleRepository.save(salle);
            }

        });


    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle -> {
            for (int i =0; i< salle.getNombrePlace();i++){
                Place place = new Place();
                place.setNumero(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });

    }

    @Override
    public void initCategories() {
        Stream.of("Histoire","Action","Fiction","Herror","Drama","Crime","Romance","Adventure").forEach(
                cat -> {
                    Categorie categorie = new Categorie();
                    categorie.setName(cat);
                    categorieRepository.save(categorie);
                }
        );

    }

    @Override
    public void initFilms() {
        List<Categorie> categories = categorieRepository.findAll();
        double [] durees = new double[]{1,1.5,2,2.5,3,3.5,4};
        Stream.of("Game Of Thrones","Breaking Bad","Prison Break","Vikings","Lost","Chernobyl","Friends","The Last Kingdom","The God Father","The Lord Of The Rings","The Green Mile").forEach( titreFilm -> {
            Film film = new Film();
            film.setTitre(titreFilm);
            film.setDuree(durees[new Random().nextInt(durees.length)]);
            film.setPhoto(titreFilm.replaceAll(" ","") + ".jpg");
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            filmRepository.save(film);
        });

    }

    @Override
    public void initSeances() {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Stream.of("12:00","16:00","18:00","20:00").forEach(
                heure ->{
                    Seance seance = new Seance();
                    try {
                        seance.setHeureDebut(df.parse(heure));
                        seanceRepository.save(seance);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

    }

    @Override
    public void initProjections() {
        double [] prices = new double[] {30,40,50,60,70,80,90,100};
        List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach( ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(
                        salle -> {
                            int index = new Random().nextInt(films.size());

                               Film film = films.get(index);
                               seanceRepository.findAll().forEach(seace -> {
                                   Projection projection = new Projection();
                                   projection.setDateProjection(new Date());
                                   projection.setFilm(film);
                                   projection.setPrix(prices[new Random().nextInt(prices.length)]);
                                   projection.setSalle(salle);
                                   projection.setSeance(seace);
                                   projectionRepository.save(projection);

                           });
                        }
                );
            });
        });

    }

    @Override
    public void initTickets() {
        projectionRepository.findAll().forEach( projection -> {
            projection.getSalle().getPlaces().forEach( place -> {
                Ticket ticket = new Ticket();
                ticket.setProjection(projection);
                ticket.setPlace(place);
                ticket.setReserve(false);
                ticket.setPrix(projection.getPrix());
                ticketRepository.save(ticket);


            });
        });

    }
}
