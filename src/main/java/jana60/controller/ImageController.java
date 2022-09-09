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

import jana60.model.Events;
import jana60.model.Image;
import jana60.model.ImageForm;
import jana60.repository.EventsRepository;
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
	@Autowired
	private EventsRepository eventRepo;
	
	//__RICHIAMA IMMAGINI DA DATABASE A MODEL PER ID EVENTO__
	@GetMapping("/{eventId}")
	public String imgEvent(@PathVariable("eventId") Integer eventId, Model model)
	{
		List<Image> listImage = service.getImageByeventId(eventId);
		model.addAttribute("listImage",listImage);

		List<Image> poster = service.getPosterByeventId(eventId);
		model.addAttribute("poster", poster);

		ImageForm imageform = service.createImageForm(eventId);
		model.addAttribute("imageForm",imageform);
		
		
		
		return "/images/images";
	}
	
	//__AGGIUNGI IMMAGINI A DATABASE__
	@PostMapping("/save")
	public String SaveImage(@ModelAttribute("imageForm") ImageForm imageForm)
	{
		try
		{
			Image saveImage = service.imageSerial(imageForm);
			//Se non ci sono poster attuali, imposta la prima immagine come poster
			if(imageRepo.countByPosterTrue()==0)
			{
				saveImage.setPoster(true);
				imageRepo.save(saveImage);
				
			}
			return "redirect:/images/" + saveImage.getImageEvent().getId();
		}
		catch(IOException e) 
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//__RIMUOVI IMMAGINI DA DATABASE__
	@GetMapping("/{eventId}/{imageId}/remove")
	public String delete (@PathVariable("eventId")Integer eventId, @PathVariable("imageId") Integer imageId, RedirectAttributes ra)
	{
		Optional<Image> result = imageRepo.findById(imageId);
		if (result.isPresent())
		{
			imageRepo.deleteById(imageId);
			ra.addFlashAttribute("successMessage", "L'immagine è stata rimossa con successo");
			
			//Se, rimuovendo l'immagine, non ci sono più poster, imposta la prima della lista
			if(imageRepo.countByPosterTrue()==0)
			{
				Events curEvent = eventRepo.findById(eventId).get();
				List<Image> curImageList = imageRepo.findByimageEvent(curEvent);
				Image firstImageOfCurImageList = curImageList.get(0);
				firstImageOfCurImageList.setPoster(true);
				imageRepo.save(firstImageOfCurImageList);
			}
			
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
			Image imgToPick = result.get();
			model.addAttribute("image", imgToPick);
			
			//Se l'immagine selezionata è già settata come Poster
			if (imgToPick.isPoster()==true)
			{
				return "redirect:/images/" + eventId;
			}
			
			//Se esistono già immagini settate come Poster, le resetta tutte a false e imposta quella selezionata come nuovo poster
			if (imageRepo.countByPosterTrue() != 0)
			{
				Events curEvent = eventRepo.findById(eventId).get();
				List<Image> curPosterList = imageRepo.findByPosterAndImageEvent(true, curEvent);
				curPosterList.get(0).setPoster(false);
			}
			
			//Se non esistono immagini settate come Poster
			imgToPick.setPoster(true);
			imageRepo.save(imgToPick);
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
