package jana60.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.repository.LocationRepository;

@Controller
@RequestMapping("/location")
public class LocationController {
	
	@Autowired
	private LocationRepository locationRepo;
	
	@GetMapping
	public String locationList(Model model) {
		model.addAttribute("location", locationRepo.findAll());
		return "/location/location";
	}
}
