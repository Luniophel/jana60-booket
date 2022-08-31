package jana60.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.repository.CategoryRepository;

@Controller
@RequestMapping("/categories")
public class CategoriesController 
{
	
	@Autowired
	private CategoryRepository repo;
	
	@GetMapping
	public String CategoriesList (Model model)
	{
		
		model.addAttribute("categoriesList", repo.findAll());
		return "/categories";
		
	}
	
}
