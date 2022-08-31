package jana60.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Events;
import jana60.repository.RepoEvents;

@Controller
@RequestMapping("/events")
public class EventsController {
	@Autowired
	private RepoEvents repo;

	@GetMapping("/{id}")
	public String pizze(@PathVariable("id") Iterable<Integer> eventId , Model model) {
		//List<Events> eventInfo = (List<Events>) repo.findById(eventId);
		List <Events> eventInfo = (List<Events>) repo.findAllById(eventId);
		model.addAttribute("eventInfo", eventInfo);		
		return "evenstInfo";

}
	}
