let menu = document.querySelector('#menu-bars');
let navbar = document.querySelector('.navbar-swiper');

menu.onclick = () =>{
  menu.classList.toggle('fa-times');
  navbar.classList.toggle('active');
}
let themeToggler = document.querySelector('.theme-toggler');
let toggleBtn = document.querySelector('.toggle-btn');

toggleBtn.onclick = () =>{
  themeToggler.classList.toggle('active');
}
window.onscroll = () =>{
    menu.classList.remove('fa-times');
    navbar.classList.remove('active');
    themeToggler.classList.remove('active');
}
document.querySelectorAll('.theme-toggler .theme-btn').forEach(btn =>{
	btn.onclick = () =>{
	let color = btn.style.background;
	document.querySelector(':root').style.setProperty('--main-color',color);	
	}
});

var swiper = new Swiper(".home-slider", {
    effect: "coverflow",
    grabCursor: true,
    centeredSlides: true,
    slidesPerView: "auto",
    coverflowEffect: {
      rotate: 0,
      stretch: 0,
      depth: 100,
      modifier: 2,
      slideShadows: false,
    },
   loop:true,
   autoplay:{
   delay:3000,
   disableOnInteraction:false,
    }
  });
