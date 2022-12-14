package jana60.model;



import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {

	private Integer id;
	
	@NotNull
	@Column(nullable = false)
	private MultipartFile contentMultipart;
	
	private Events imageEvent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MultipartFile getContentMultipart() {
		return contentMultipart;
	}

	public void setContentMultipart(MultipartFile contentMultipart) {
		this.contentMultipart = contentMultipart;
	}

	public Events getImageEvent() {
		return imageEvent;
	}

	public void setImageEvent(Events imageEvent) {
		this.imageEvent = imageEvent;
	}
	
	
}
