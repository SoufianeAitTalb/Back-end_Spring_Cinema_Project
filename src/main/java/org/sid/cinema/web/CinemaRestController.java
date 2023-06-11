package org.sid.cinema.web;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaRestController {
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    TicketRepository ticketRepository;
   @GetMapping(path = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id") Long id) throws Exception{
       Film f = filmRepository.findById(id).get();
       String photoName = f.getPhoto();
       File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
       Path path = Paths.get(file.toURI());
       return Files.readAllBytes(path);
   }
   @PostMapping(path="/payerTickets")
   @Transactional
    public List<Ticket> payerTickets (@RequestBody TicketForm ticketForm){
       List<Ticket> listTicket = new ArrayList<Ticket>();
       ticketForm.getTickets().forEach(idTicket -> {
           Ticket ticket = ticketRepository.findById(idTicket).get();
           ticket.setNomClient(ticketForm.getNomClient());
           ticket.setCodePayement(ticketForm.getCodePayment());
           ticket.setReserve(true);
           ticketRepository.save(ticket);
           listTicket.add(ticket);


       });
       return listTicket;
   }
}
@Data
class TicketForm {
    private String nomClient;
    private int codePayment;
    private List<Long> tickets = new ArrayList<Long>();
}
