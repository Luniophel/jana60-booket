package jana60.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jana60.model.Events;
import jana60.model.Image;
import jana60.model.ImageForm;
import jana60.repository.EventsRepository;
import jana60.repository.ImageRepository;

@Service
public class ImageService {
	
	
	@Autowired
	private ImageRepository imagesRepo;
	@Autowired
	private EventsRepository eventsRepo;

	//Creazione Lista di IMG per Evento con ID SPECIFICO
	public List<Image> getImageByeventId(Integer eventId)
	{
		Events event = eventsRepo.findById(eventId).get();
		return imagesRepo.findByimageEvent(event);		
	}
	
	//Creazione del Form con l'immagine salvata in byte[]
	public ImageForm createImageForm(Integer eventId)
	{
		Events event = eventsRepo.findById(eventId).get();
		ImageForm imageForm = new ImageForm();
		imageForm.setImageEvent(event);
		return imageForm;
	}
	
	//Serializzazione dell'immagine in byte[]
	public Image imageSerial (ImageForm imageForm) throws IOException 
	{
		Image imagetoSave = new Image();
		imagetoSave.setImageEvent(imageForm.getImageEvent());
		
		if(imageForm.getContentMultipart() != null) 
		{
			byte[] contentSerial = imageForm.getContentMultipart().getBytes();
			imagetoSave.setContent(contentSerial);
		}
		return imagesRepo.save(imagetoSave);
		
	}
	
	//Chiama il content dell'immagine
	public byte[] getImageContent(Integer Id) {
		Image image = imagesRepo.findById(Id).get();
		return image.getContent();		
	}
	
	
}
