package jana60.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import antlr.debug.Event;
import jana60.model.Booking;
import jana60.model.Category;
import jana60.model.Events;
import jana60.model.Image;
import jana60.repository.BooketRepository;
import jana60.repository.CategoryRepository;
import jana60.repository.EventsRepository;
import jana60.repository.ImageRepository;
import jana60.repository.LocationRepository;
@Controller
@RequestMapping("/")
public class EventsController 
{
	
	@Autowired
	private EventsRepository repo;
	@Autowired
	private CategoryRepository repoCategory;
	@Autowired
	private LocationRepository repoLoc;
	@Autowired
	private ImageRepository repoImg;
	@Autowired
	private BooketRepository repoBooket;
	
	 @GetMapping("/advanced_search")
	  public String advancedSearch() {
		  return "/event/search";
	  }
	
	@GetMapping("/search")
	public String search(@RequestParam(name = "queryName") String queryName, 
			@RequestParam(name = "queryLocation", required = false) String queryLocation, 
			@RequestParam(name = "queryCategory", required = false) String queryCategory, 
			Model model) {
		if(queryName != null && queryName.isEmpty()) {
			queryName = null;
		}
		if(queryLocation != null && queryLocation.isEmpty()) {
			queryLocation = null;
		}
		if(queryCategory != null && queryCategory.isEmpty()) {
			queryCategory = null;
		}
		List<Events> listEvents = repo.findByNameContainingOrEventLocationNameContainingOrCategoriesNameContainingIgnoreCase(queryName, queryLocation, queryCategory);
		model.addAttribute("listEvents", listEvents);
		return "/event/events";
	}
	
	@GetMapping("/events")
	public String event(Model model) 
	{
		List<Events> listEvents = (List<Events>) repo.findAll();
		model.addAttribute("listEvents", listEvents);
		Image poster = null;
		if(listEvents.isEmpty()) {
			return "/event/events";
		} 
		for(int i = 0; i <= listEvents.size(); i++) {
			poster = listEvents.get(i).showPoster();
			model.addAttribute("poster", poster);
		}
		return "/event/events";
	}

	@GetMapping("/events/{id}")
	public String eventInfo(@PathVariable("id") Integer eventId , Model model) 
	{
		Optional<Events> result = repo.findById(eventId);
		List<Category> listCategories = (List<Category>) repoCategory.findAll();
		model.addAttribute("eventInfo", result.get());	
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listBooket", repoBooket.findAll());
		return "/event/evenstInfo";
	}
	
	@PostMapping("/events/{id}")
	public String saveEventInfo(@Valid @ModelAttribute("booking") Booking formBooking, @Valid @ModelAttribute("event") Events formEvent, BindingResult br)
	{
		if ((formBooking.getNumberBooket() - formEvent.getEventLocation().getCapacity()) <= 0)
		{
			br.addError(new FieldError("booking", "quantity", formBooking.getNumberBooket(), false, null, null, "Posti per " + formEvent.getEventLocation().getName() + " finiti"));
		}
		else 
		{	
			List<Booking> listBooking = repoBooket.findAllByid(formEvent.getEventLocation().getId());
			for (int i = 0, booketNumber = 0; i < listBooking.size(); i++) {
				booketNumber += listBooking.get(i).getNumberBooket();
				formBooking.setBooketAvailable(booketNumber);
			}
			repoBooket.save(formBooking);
		}
		return "/event/evenstInfo";
	}
	
	@GetMapping("/addEvent")
	public String eventForm(Model model) 
	{
		model.addAttribute("event", new Events());
		model.addAttribute("categoriesList", repoCategory.findAll());
		model.addAttribute("listLocation", repoLoc.findAll());	
		return "/event/addEvent";
	}
	
	@PostMapping("/addEvent")
	public String save(@Valid @ModelAttribute("event") Events formEvent, BindingResult br, Model model) 
	{	
		formEvent.getEventLocation().getCapacity();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH/mm");
		formEvent.getStartDate().format(formatter);
		formEvent.getEndDate().format(formatter);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime pastDate = LocalDateTime.from(formEvent.getStartDate());
		boolean isAfter = today.isAfter(pastDate);
		if(isAfter) {
			
			br.addError(new FieldError("event", "startDate", formEvent.getStartDate(), false, null, null, "la data deve essere futura a " + formEvent.getStartDate().format(formatter) ));
		}
		LocalDateTime endDate = LocalDateTime.from(formEvent.getEndDate());
		boolean isBefore = endDate.isBefore(pastDate);
		if(isBefore) 
		{
			
			br.addError(new FieldError("event", "endDate", formEvent.getStartDate(), false, null, null, "la data deve essere futura a " + formEvent.getStartDate().format(formatter) ));
		}
		
		List<Events> listEventLocation = repo.findAllByEventLocation(formEvent.getEventLocation());
		for (int i = 0; i < listEventLocation.size(); i++) {
			if (listEventLocation.get(i).getStartDate().isEqual(formEvent.getStartDate()) || listEventLocation.get(i).getEndDate().isEqual(formEvent.getEndDate())) {
				br.addError(new FieldError("event", "startDate", formEvent.getStartDate(), false, null, null, "la data e la location sono stati giá prenotati" ));
			}
			}
				
		if (br.hasErrors()) 
		{
			model.addAttribute("listLocation", repoLoc.findAll());
			model.addAttribute("categoriesList", repoCategory.findAll());
			return "/event/addEvent";
		} 
		else 
		{
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
	
	@GetMapping("/editEvent/{id}")
	public String editForm(@PathVariable("id") Integer eventId , Model model) 
	{
		Optional<Events> result = repo.findById(eventId);
		model.addAttribute("event", result.get());
		model.addAttribute("categoriesList", repoCategory.findAll());
		model.addAttribute("listLocation", repoLoc.findAll());
		return "/event/editEvent";
		
	}
	
	@PostMapping("/editEvent/{id}")
	public String edit(@Valid @ModelAttribute("event") Events formEvent,@PathVariable("id") Integer eventId,  BindingResult br) 
	{
		Image img = repoImg.findByPosterAndImageEvent(true, formEvent).get(formEvent.getId());
		if( !(img.isPoster()) && formEvent.getEventLocation().getId()<= 1) {
			formEvent.setVisible(false);
			br.addError(new FieldError("event", "location", formEvent.getStartDate(), false, null, null, "per rendere l'evento visibile devi poter mettere la location e l immagine" ));
		}
		if (br.hasErrors()) 
		{
			return "/";
		} 
		else 
		{
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
	
}
	
