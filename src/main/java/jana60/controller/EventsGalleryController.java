package jana60.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jana60.model.Events;
import jana60.repository.BooketRepository;
import jana60.repository.CategoryRepository;
import jana60.repository.EventsRepository;
import jana60.repository.ImageRepository;
import jana60.repository.LocationRepository;

@Controller
@RequestMapping("/ourEvents")
public class EventsGalleryController 
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
	
	//FUNZIONE DI RICERCA
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
		return "/eventsGallery";
	}
	
	@GetMapping
	public String event(Model model) 
	{
		List<Events> listEvents = (List<Events>) repo.findAll();
		model.addAttribute("listEvents", listEvents);
		return "/eventsGallery";
	}
	
}
