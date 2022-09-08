package jana60.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "image")
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Lob
	private byte[] content;
	
	@Column(columnDefinition = "boolean default false")
	private boolean poster;
	
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Events imageEvent;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Events getImageEvent() {
		return imageEvent;
	}

	public void setImageEvent(Events imageEvent) {
		this.imageEvent = imageEvent;
	}

	public boolean isPoster() {
		return poster;
	}

	public void setPoster(boolean poster) {
		this.poster = poster;
	}

	
	
		
	

}
