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
	private EventsRepository repo;
	@Autowired
	private ImageRepository repoImg;
	
	public ImageForm creatImageForm(Integer eventId){
		Events event = repo.findById(eventId).get();
		ImageForm imageForm = new ImageForm();
		imageForm.setImageEvent(event);
		return imageForm;
	}
	
	public Image imageSerial (ImageForm imageForm) throws IOException {
		Image imagetoSave = new Image();
		imagetoSave.setImageEvent(imageForm.getImageEvent());
		if(imageForm.getContentMultipart() != null) {
			byte[] contentSerial = imageForm.getContentMultipart().getBytes();
			imagetoSave.setContent(contentSerial);
		}
		return repoImg.save(imagetoSave);
		
	}
	
	public byte[] getImageContent(Integer Id) {
		Image image = repoImg.findById(Id).get();
		return image.getContent();		
	}
	
	public List<Image> getImageByeventId(Integer eventId){
		Events event = repo.findById(eventId).get();
		return repoImg.findByimageEvent(event);
				
	}
	
}
