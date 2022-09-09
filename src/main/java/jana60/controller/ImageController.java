package jana60.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jana60.model.Image;
import jana60.model.ImageForm;
import jana60.repository.ImageRepository;
import jana60.service.ImageService;

@Controller
@RequestMapping("/images")
public class ImageController 
{
	
	@Autowired
	private ImageService service;
	@Autowired
	private ImageRepository imageRepo;
	
	//Richiesta per caricare sul Model le immagini dal Database
	@GetMapping("/{eventId}")
	public String imgEvent(@PathVariable("eventId") Integer eventId, Model model)
	{
		List<Image> listImage = service.getImageByeventId(eventId);
		List<Image> poster = service.getPosterByeventId(eventId);
		ImageForm imageform = service.createImageForm(eventId);
		
		model.addAttribute("listImage",listImage);
		model.addAttribute("imageForm",imageform);
		model.addAttribute("poster", poster);
		return "/images/images";
	}
	
	//Richiesta per aggiungere immagini al Database
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
	
	//Richiesta per rimuovere immagini dal Database
	@GetMapping("/{eventId}/{imageId}/remove")
	public String delete (@PathVariable("eventId")Integer eventId, @PathVariable("imageId") Integer imageId, RedirectAttributes ra)
	{
		Optional<Image> result = imageRepo.findById(imageId);
		if (result.isPresent())
		{
			imageRepo.deleteById(imageId);
			ra.addFlashAttribute("successMessage", "L'immagine è stata rimossa con successo");
			return "redirect:/images/" + eventId;
		}
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'immagine con ID: " + imageId + " non è presente./n Se l'errore persiste contattare l'assistenza.");
		
	}
	
	//Richiesta per rendere poster un'immagine dal Database
	@GetMapping("/{eventId}/{imageId}/setPoster")
	public String selectPoster 
	(
		@PathVariable("eventId")Integer eventId, 
		@PathVariable("imageId") Integer imageId,
		RedirectAttributes ra, 
		Model model
	)
	{
		//Richiesta dell'immagine per verificarne la presenza senza errori
		Optional<Image> result = imageRepo.findById(imageId);
		if (result.isPresent())
		{
			Image updatedImg = result.get();
			model.addAttribute("image", updatedImg);
			
			//Se l'immagine selezionata è già settata come Poster
			if (updatedImg.isPoster()==true)
			{
				return "redirect:/images/" + eventId;
			}
			
			//Se esistono già immagini settate come Poster, le resetta tutte a false e imposta quella selezionata come nuovo poster
			if (imageRepo.countByPosterTrue() != 0)
			{
//				List<Image> imgToSetFalse = imageRepo.findByPosterAndImageEvent(true, eventId);
//				Iterator<Image> iter = imgToSetFalse.iterator();
//				while(iter.hasNext())
//				{
//					Image curImage = iter.next();
//					curImage.setPoster(false);
//					imageRepo.save(curImage);
//				}				
			}
			
			//Se non esistono immagini settate come Poster
			updatedImg.setPoster(true);
			imageRepo.save(updatedImg);
			ra.addFlashAttribute("successMessage", "L'immagine è stata inserita come poster");
			return "redirect:/images/" + eventId;
		}
		//Se la richiesta fallisce
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image with id " + imageId + " doesn't exist");
		}
		
	}
	
	//Richiesta per decodificare immagini in arrivo dal Database (da byte[] a jpg)
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
