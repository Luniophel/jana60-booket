package jana60.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jana60.model.Category;
import jana60.model.Events;
import jana60.model.Image;
import jana60.model.ImageForm;
import jana60.repository.CategoryRepository;
import jana60.repository.EventsRepository;
import jana60.repository.ImageRepository;
import jana60.repository.LocationRepository;
import jana60.service.ImageService;

@Controller
@RequestMapping("/")
public class EventsController {
	@Autowired
	private EventsRepository repo;
	@Autowired
	private CategoryRepository repoCategory;
	@Autowired
	private ImageRepository repoImg;
	@Autowired
	private LocationRepository repoLoc;
	@Autowired
	private ImageService service;
	
	@GetMapping("/events")
	public String event(Model model) {
		List<Events> listEvents = (List<Events>) repo.findAll();
		model.addAttribute("listEvents", listEvents);
		return "/event/events";
	}

	@GetMapping("/events/{id}")
	public String eventInfo(@PathVariable("id") Integer eventId , Model model) {
		Optional<Events> result = repo.findById(eventId);
		List<Category> listCategories = (List<Category>) repoCategory.findAll();
		model.addAttribute("eventInfo", result.get());	
		model.addAttribute("listCategories", listCategories);
		return "/event/evenstInfo";

}
	@GetMapping("/addEvent")
	public String eventForm(Model model) {
		model.addAttribute("event", new Events());
		model.addAttribute("categoriesList", repoCategory.findAll());
		model.addAttribute("listLocation", repoLoc.findAll());		
		return "/event/addEvent";
	}
	
	@PostMapping("/addEvent")
	public String save(@Valid @ModelAttribute("event") Events formEvent, BindingResult br) {
		if (br.hasErrors()) {
			return "/";
		} else {
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
	
	@GetMapping("/editEvent/{id}")
	public String editForm(@PathVariable("id") Integer eventId , Model model) {
		Optional<Events> result = repo.findById(eventId);
		model.addAttribute("event", result.get());
		model.addAttribute("categoriesList", repoCategory.findAll());
		model.addAttribute("listLocation", repoLoc.findAll());
		// Image event
				List<Image> listImage = service.getImageByeventId(eventId);
				ImageForm imageform = service.creatImageForm(eventId);
				model.addAttribute("imageForm",imageform);
				model.addAttribute("listImage",listImage);
		return "/event/editEvent";
		
	}
	
	@PostMapping("/editEvent/{id}")
	public String edit(@Valid @ModelAttribute("event") Events formEvent,@PathVariable("id") Integer eventId,  BindingResult br) {
		if (br.hasErrors()) {
			return "/";
		} else {
			repo.save(formEvent);
			return "redirect:/events";
		}
	}
	@PostMapping("/{id}/image/save")
	public String SaveImage(@ModelAttribute("imageForm") ImageForm imageForm, @ModelAttribute("event") Events formEvent,@PathVariable("id") Integer eventId){
		try {
			Image saveImage = service.imageSerial(imageForm);
			return "/redirect:/editEvent/" + eventId;
		}
		catch(IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/{imageId}/content", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageContent(@PathVariable("immagineId") Integer immagineId) {
      // recupero il content dal database
      byte[] content = service.getImageContent(immagineId);
      // preparo gli headers della response con il tipo di contenuto
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);
      // ritorno il contenuto, gli headers e lo status http
      return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
    }
}
	
