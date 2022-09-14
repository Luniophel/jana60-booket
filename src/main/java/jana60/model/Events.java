package jana60.model;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "events")
public class Events {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@NotEmpty(message = "Name required!")
	@Column(nullable = false)
	private String name;
	
	@NotEmpty(message = "Description required!")
	@Column(nullable = false)
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(nullable = false)
	private LocalDateTime startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(nullable = false)
	private LocalDateTime endDate;
	
	@Column(nullable = false)
	private Boolean publishedStatus;
	
	@Column(nullable = false)
	private Boolean visible;
	
	@ManyToMany
	List<Category> categories;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location eventLocation;
	
	@OneToMany
	@JoinColumn(name = "event_id")
	List <Image> eventImage;
	
	@OneToMany
	@JoinColumn(name = "event_booket")
	List<Booking> eventBooket;

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean getPublishedStatus() {
		return publishedStatus;
	}

	public void setPublishedStatus(Boolean publishedStatus) {
		this.publishedStatus = publishedStatus;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Location getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(Location eventLocation) {
		this.eventLocation = eventLocation;
	}

	public List<Image> getEventImage() {
		return eventImage;
	}

	public void setEventImage(List<Image> eventImage) {
		this.eventImage = eventImage;
		
		
	}

	public List<Booking> getEventBooket() {
		return eventBooket;
	}

	public void setEventBooket(List<Booking> eventBooket) {
		this.eventBooket = eventBooket;
	}
	

	public Image getPosterImg() {
		List<Image> imgList = getEventImage();
		Iterator<Image> iter = imgList.iterator();
		while(iter.hasNext())
		{
			Image curImg = iter.next();
			if(curImg.isPoster())
				return curImg;
		}
		return imgList.get(0);	
	}

	
	
	}
			
	

