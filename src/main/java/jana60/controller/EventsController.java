package jana60.controller;

import java.util.List;
import java.util.Optional;

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
	public String eventInfo(@PathVariable("id") Integer eventId , Model model) {
		Optional<Events> result = repo.findById(eventId);
		model.addAttribute("eventInfo", result.get());		
		return "evenstInfo";

}
	}
