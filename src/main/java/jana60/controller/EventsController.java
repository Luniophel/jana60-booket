package jana60.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		Iterator<Events> iter = listEvents.iterator();
		while(iter.hasNext())
		{
			Events curEvent = iter.next();
			LocalDateTime today = LocalDateTime.now();
			if(today.isAfter(curEvent.getEndDate()))
					{
						curEvent.setVisible(false);
						repo.save(curEvent);
					}
		}
		model.addAttribute("listEvents", listEvents);
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
	
	@GetMapping("/booket/{id}")
	public String saveEventInfo(@PathVariable("id") Integer eventId, Model model)
	{
		Optional<Events> result = repo.findById(eventId);
		if (result.isPresent())
		{
		Booking booking = new Booking();
		booking.setEventBooket(result.get());
		model.addAttribute("booking", booking);
		}
		
		//model.addAttribute("listCapacity", formEvent.getEventLocation().getCapacity());
		return "/event/booket";	
	}
	@PostMapping("/booket")
	public String saveEventInfo(@Valid @ModelAttribute("booking") Booking formBooking, BindingResult br, RedirectAttributes ra )
	{	
		ra.addFlashAttribute("successMessage", "You have successfully booked " + formBooking.getNumberBooket() + " ticket(s)");

		if (formBooking.getEventBooket().getEventLocation().getBooketAvailable() - formBooking.getNumberBooket() < 0)
		{
			br.addError(new FieldError("booking", "quantity", formBooking.getNumberBooket(), false, null, null, "Impossible to book: there are just " + formBooking.getEventBooket().getEventLocation().getBooketAvailable() + " tickets available."));
		}
		else {
				formBooking.getEventBooket().getEventLocation().setBooketAvailable(formBooking.getEventBooket().getEventLocation().getBooketAvailable() - formBooking.getNumberBooket());
				if(formBooking.getEventBooket().getEventLocation().getBooketAvailable() < 0 ) {
					br.addError(new FieldError("booking", "quantity", formBooking.getNumberBooket(), false, null, null, "Impossible to booking " + formBooking.getNumberBooket() + "you can book up to  " + formBooking.getEventBooket().getEventLocation().getBooketAvailable()));
				}
			 }
		if(formBooking.getEmail().isEmpty())
		{
			br.addError(new FieldError("booking", "email", formBooking.getEmail(), false, null, null, "email necessaria per prenotare"));
		}
		//SIIIIIIIIIIIIIIUUUUUUMMMMMM
		if (br.hasErrors()) 
		{
			return "/event/booket";
		}
		else {
			repoBooket.save(formBooking);
		}
		return "redirect:/ourEvents";
	}
	
	@GetMapping("/addEvent")
	public String eventForm(Model model) 
	{
		model.addAttribute("event", new Events());
		model.addAttribute("categoriesList", repoCategory.findAll());
		model.addAttribute("listLocation", repoLoc.findAll());	
		return "/event/addEvent";
	}
	
//___INIZIO METODO ADD___
	@PostMapping("/addEvent")
	public String save(@Valid @ModelAttribute("event") Events formEvent, BindingResult br, Model model) 
	{	
		//SE NON CI SONO EVENTI
		if(repo.findAll().iterator().hasNext()==false)
		{
			formEvent.setVisible(false);
			formEvent.setPublishedStatus(true);
			repo.save(formEvent);
			return "redirect:/events";
		}
		formEvent.getEventLocation().getCapacity();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH/mm");
		formEvent.getStartDate().format(formatter);
		formEvent.getEndDate().format(formatter);
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime pastDate = LocalDateTime.from(formEvent.getStartDate());
		
		
		//Controllo per date
		boolean isAfter = today.isAfter(pastDate);
		if(isAfter) {
			
			br.addError(new FieldError("event", "startDate", formEvent.getStartDate(), false, null, null, "the date must be following the " + formEvent.getStartDate().format(formatter) ));
		}
		LocalDateTime endDate = LocalDateTime.from(formEvent.getEndDate());
		boolean isBefore = endDate.isBefore(pastDate);
		if(isBefore) 
		{
			
			br.addError(new FieldError("event", "endDate", formEvent.getStartDate(), false, null, null, "the date must be following the " + formEvent.getStartDate().format(formatter) ));
		}

		List<Events> listEventLocation = repo.findAllByEventLocation(formEvent.getEventLocation());
//INIZIO NUOVO CODICE
		boolean dateValid = true;
		Iterator<Events> iter = listEventLocation.iterator();
		if(iter.hasNext()==false)
		{
			formEvent.setVisible(false);
			formEvent.setPublishedStatus(true);
			repo.save(formEvent);
			return "redirect:/events";
		}
		while(iter.hasNext())
		{
			Events curEvent = iter.next();
			if(formEvent.getId() != curEvent.getId())
			{
				//CASO 1. LE DATE DEL FORMEVENT SONO PRECEDENTI ALL'EVENTO CORRENTE DELLA LISTA
				if	(formEvent.getEndDate().isBefore(curEvent.getStartDate()))
					dateValid = true;
					
				//CASO 2. LE DATE DEL FORMEVENT SONO SUCCESSIVE ALL'EVENTO CORRENTE DELLA LISTA
				else
				if	(formEvent.getStartDate().isAfter(curEvent.getEndDate()))
					dateValid = true;

				//CASO 3. LE DATE DEL FORMEVENT SI INTERSECANO CON QUELLE DELL'EVENTO CORRENTE DELLA LISTA
				else
				{
					dateValid = false;
					br.addError(new FieldError("event", "startDate", formEvent.getStartDate(), false, null, null, "la data e la location sono stati gi?? prenotati" ));
				}
			}
			else 
			{
				//CASO ID IDENTICO. GLI ID DELL'EVENTO CORRENTE DELLA LISTA E' IDENTICO A QUELLO DEL FORMEVENT,
				if(repo.findById(formEvent.getId()).get().getVisible()==true)
					formEvent.setModificed(true);
			}
		}
//FINE NUOVO CODICE
//INIZIO VECCHIO CODICE
//		for (int i = 0; i < listEventLocation.size(); i++) {
//			if (listEventLocation.get(i).getStartDate().isEqual(formEvent.getStartDate()) || listEventLocation.get(i).getEndDate().isEqual(formEvent.getEndDate())) {
//				br.addError(new FieldError("event", "startDate", formEvent.getStartDate(), false, null, null, "la data e la location sono stati gi?? prenotati" ));
//			}
//			}
//FINE VECCHIO CODICE
		
		if (br.hasErrors() || dateValid==false) 
		{
			model.addAttribute("listLocation", repoLoc.findAll());
			model.addAttribute("categoriesList", repoCategory.findAll());
			return "/event/addEvent";
		} 
		else 
		{
			formEvent.setVisible(false);
			formEvent.setPublishedStatus(true);
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
//___FINE METODO ADD___
	
	
	
	@GetMapping("/editEvent/{id}")
	public String editForm(@PathVariable("id") Integer eventId , Model model) 
	{
		Optional<Events> result = repo.findById(eventId);
		if(result.isPresent())
		{
			model.addAttribute("event", result.get());
			model.addAttribute("categoriesList", repoCategory.findAll());
			model.addAttribute("listLocation", repoLoc.findAll());
			return "/event/addEvent";
		}
			else			
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza con ID" + eventId + "non presente.");
		
	}
	
	@PostMapping("/editEvent/{id}")
	public String edit(@Valid @ModelAttribute("event") Events formEvent,@PathVariable("id") Integer eventId,  BindingResult br) 
	{
		Image img = repoImg.findByPosterAndImageEvent(true, formEvent).get(formEvent.getId());
		if( !(img.isPoster()) && formEvent.getEventLocation().getId()<= 1) {
			formEvent.setVisible(false);
			br.addError(new FieldError("event", "location", formEvent.getStartDate(), false, null, null, "to make the event visible, you must insert the location and the image" ));
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
	
	@GetMapping("/cancel/{id}")
	public String cancelEvent(@PathVariable("id") Integer eventId, RedirectAttributes ra)
	{
		Events curEvent = repo.findById(eventId).get();
		if (curEvent.getVisible()==false)
		{
			repo.deleteById(eventId);
			ra.addFlashAttribute("successMessage", "You successfully deleted your event");
			return "redirect:/events";
		}
		curEvent.setPublishedStatus(false);
		repo.save(curEvent);
		ra.addFlashAttribute("successMessage", "You have canceled your event, you can't book anymore tickets");
		return "redirect:/events";
	}
	
	@GetMapping("/publish/{id}")
	public String publishEvent(@PathVariable("id") Integer eventId, RedirectAttributes ra)
	{
		Events curEvent = repo.findById(eventId).get();
		//SE MANCA IMMAGINE O LOCATION NON E' SPECIFICATA, RESTITUISCE ERRORE E NON PUBBLICA
		if(curEvent.getEventImage().isEmpty()||curEvent.getEventLocation().getId()==1)
		{
			ra.addFlashAttribute("failMessage", "Failed to publish your event; you need to set at least a Poster and a Location");
			return "redirect:/events";
		}
		curEvent.setVisible(true);
		ra.addFlashAttribute("successMessage", "You have successfully published your event");
		repo.save(curEvent);
		return "redirect:/events";
	}
	
	
}
	
