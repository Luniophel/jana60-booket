package jana60.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Events;
import jana60.repository.EventsRepository;

@Controller
@RequestMapping("/")
public class HomeController{
	
	
	@Autowired
	private EventsRepository repo;
	
	@GetMapping
	public String home(Model model)
	{	
		List<Events> listEvents = (List<Events>) repo.findAll();
		model.addAttribute("listEvents", listEvents);
		return "/home";
	}
}
	