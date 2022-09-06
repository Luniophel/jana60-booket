package jana60.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Category;
import jana60.repository.CategoryRepository;

@Controller
@RequestMapping("/categories")
public class CategoriesController 
{
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	//Carico nuova categoria fittizia in attesa di compilazione, oltre al model
	@GetMapping
	public String CategoriesList (Model model)
	{
		model.addAttribute("categoriesList", categoryRepo.findAll());
		model.addAttribute("category", new Category());
		return "/categories/categories";
	}
	
	@PostMapping("/addCategory")
	public String addCategory(@Valid @ModelAttribute("category") Category categoryForm, BindingResult br, Model model)
	{
		boolean hasErrors= br.hasErrors();
		boolean validateName = true;
		if(categoryForm.getId() != null) 
		{
			Category categoryTemp = categoryRepo.findById(categoryForm.getId()).get();
			if (categoryForm.getName().equals(categoryForm.getName()));
			{
				validateName = false;
			}
		}
		if(validateName && categoryRepo.countByName(categoryForm.getName()) > 0)
		{
			br.addError(new FieldError("category", "name", "There cannot be two categories with the same name"));
			hasErrors = true;
		}
		if(hasErrors) {
			return "/categories/categories";
		} else {
			
			try {
				categoryRepo.save(categoryForm);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Unable to save the category");
				return "/categories/categories";
			}
			return "redirect:/categories";
		}
	}
	
	
}
