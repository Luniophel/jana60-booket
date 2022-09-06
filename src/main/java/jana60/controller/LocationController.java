package jana60.controller;

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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jana60.model.Location;
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
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("location", new Location());
		return "/location/new";
	}
	
	@PostMapping("/add")
	public String saveLocation(@Valid @ModelAttribute("location") Location formLocation, BindingResult br, Model model) {
		boolean hasErrors= br.hasErrors();
		boolean validateName = true;
		if(formLocation.getId() != null) {
			Location locationBeforeUpdate = locationRepo.findById(formLocation.getId()).get();
			if(formLocation.getName().equals(formLocation.getName())) {
				validateName = false;
			}
		}
		if(validateName && locationRepo.countByName(formLocation.getName()) > 0) {
			br.addError(new FieldError("location", "name", "There cannot be two locations with the same name"));
			hasErrors = true;
		}
		
		if(hasErrors) {
			return "/location/new";
		} else {
			
			try {
				locationRepo.save(formLocation);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Unable to save the location");
				return "/location/new";
			}
			return "redirect:/location";
		}
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer locationId, RedirectAttributes ra) {
		Optional<Location> result = locationRepo.findById(locationId);
		if(result.isPresent()) {
			locationRepo.delete(result.get());
			ra.addFlashAttribute("successMessage", "Location " + result.get().getName() + " succesfully deleted");
			return "redirect:/location";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with id " + locationId + " don't exist");
		}
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer locationId, Model model) {
		Optional<Location> result = locationRepo.findById(locationId);
		if(result.isPresent()) {
			model.addAttribute("location", result.get());
			return "/location/new";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with id " + locationId + " don't exist");
		}
	}
}
