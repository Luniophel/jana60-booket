package jana60.controller;

import java.time.LocalDate;
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

import jana60.model.Category;
import jana60.model.Events;
import jana60.repository.CategoryRepository;
import jana60.repository.EventsRepository;
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
	
	@GetMapping("/search")
	public String search(@RequestParam(name = "queryName") String queryName, Model model) {
		List<Events> listEvents = repo.findByNameContainingIgnoreCase(queryName);
		model.addAttribute("listEvents", listEvents);
		return "/event/events";
	}
	
	@GetMapping("/events")
	public String event(Model model) 
	{
		List<Events> listEvents = (List<Events>) repo.findAll();
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
		LocalDate today = LocalDate.now();
		LocalDate pastDate = LocalDate.from(formEvent.getStartDate());
		boolean isAfter = today.isAfter(pastDate);	
		if(isAfter) 
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			br.addError(new FieldError("event", "startDate", formEvent.getStartDate(), false, null, null, "la data deve essere futura a " + formEvent.getStartDate().format(formatter) ));
		}
		LocalDate endDate = LocalDate.from(formEvent.getEndDate());
		boolean isBefore = endDate.isBefore(pastDate);
		if(isBefore) 
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
	
