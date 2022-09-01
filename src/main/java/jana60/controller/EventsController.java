package jana60.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Category;
import jana60.model.Events;
import jana60.repository.CategoryRepository;
import jana60.repository.RepoEvents;

@Controller
@RequestMapping("/")
public class EventsController {
	@Autowired
	private RepoEvents repo;
	@Autowired
	private CategoryRepository repoCategory;
	
	@GetMapping("/events")
	public String event(Model model) {
		List<Events> listEvents = (List<Events>) repo.findAll();
		model.addAttribute("listEvents", listEvents);
		return "event/events";
	}

	@GetMapping("/events/{id}")
	public String eventInfo(@PathVariable("id") Integer eventId , Model model) {
		Optional<Events> result = repo.findById(eventId);
		List<Category> listCategories = (List<Category>) repoCategory.findAll();
		model.addAttribute("eventInfo", result.get());	
		model.addAttribute("listCategories", listCategories);
		return "event/evenstInfo";

}
	@GetMapping("/addEvent")
	public String eventForm(Model model) {
		model.addAttribute("event", new Events());
		return "event/addEvent";
	}
	
	@PostMapping("/addEvent")
	public String save(@Valid @ModelAttribute("event") Events formEvent, BindingResult br) {
		if (br.hasErrors()) {
			return "/";
		} else {
			formEvent.setVisible(false);
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
	
	@GetMapping("/editEvent/{id}")
	public String editForm(@PathVariable("id") Integer eventId , Model model) {
		Optional<Events> result = repo.findById(eventId);
		model.addAttribute("event", result.get());
		return "event/editEvent";
		
	}
	
	@PostMapping("/editEvent/{id}")
	public String edit(@Valid @ModelAttribute("event") Events formEvent, BindingResult br) {
		if (br.hasErrors()) {
			return "/";
		} else {
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
	}
	
