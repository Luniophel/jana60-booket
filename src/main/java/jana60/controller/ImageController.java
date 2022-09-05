package jana60.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jana60.model.Image;
import jana60.model.ImageForm;
import jana60.service.ImageService;

@Controller
@RequestMapping("/images")
public class ImageController 
{
	
	@Autowired
	private ImageService service;
	
	@GetMapping("/{eventId}")
	public String imgEvent(@PathVariable("eventId") Integer eventId, Model model)
	{
		List<Image> listImage = service.getImageByeventId(eventId);
		ImageForm imageform = service.createImageForm(eventId);
		
		model.addAttribute("imageForm",imageform);
		model.addAttribute("listImage",listImage); 
		return "/images/images";
	}
		
	@PostMapping("/save")
	public String SaveImage(@ModelAttribute("imageForm") ImageForm imageForm)
	{
		try
		{
			Image saveImage = service.imageSerial(imageForm);
			return "redirect:/images/" + saveImage.getImageEvent().getId();
		}
		catch(IOException e) 
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/{imageId}/content", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageContent(@PathVariable("imageId") Integer imageId) {
      // recupero il content dal database
      byte[] content = service.getImageContent(imageId);
      // preparo gli headers della response con il tipo di contenuto
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);
      // ritorno il contenuto, gli headers e lo status http
      return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
    }
}
