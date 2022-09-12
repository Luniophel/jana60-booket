package jana60.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	//__AGGIUNGI CATEGORIA__
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
	
	//__MODIFICA CATEGORIA__
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer categoryId, Model model)
	{
		Optional<Category> result = categoryRepo.findById(categoryId);
		if(result.isPresent()) {
			model.addAttribute("category", result.get());
			return "/categories/categories";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " don't exist");
		}
	}
	
	//__RIMUOVI CATEGORIA__
	@GetMapping("/delete/{id}")
	public String editCategory(@PathVariable("id") Integer categoryId, RedirectAttributes ra)
	{
		Optional<Category> result = categoryRepo.findById(categoryId);
		if(result.isPresent()) 
		{
			categoryRepo.delete(result.get());
			ra.addFlashAttribute("successMessage", "Category " + result.get().getName() + " successfully deleted");
			return "redirect:/categories";	
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id " + categoryId + " don't exist");
		}
	}
}
