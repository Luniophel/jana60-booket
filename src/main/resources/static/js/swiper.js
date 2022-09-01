let menu = document.querySelector('#menu-bars')
let navbar = document.querySelector('.navbar');

menu.onclick = () =>{
  menu.classList.toggle('fa-times');
  navbar.classList.toggle('active');
}
window.onscroll = () =>{
    menu.classList.remove('fa-times');
    navbar.classList.remove('active');
}
<<<<<<< HEAD:src/main/resources/static/js/script.js
var swiper = new Swiper(".home-slider", {
=======
const swiper = new Swiper('.home-slider', {
>>>>>>> dev:src/main/resources/static/js/swiper.js
    effect: "coverflow",
    grabCursor: true,
    centeredSlides: true,
    slidesPerView: "auto",
    coverflowEffect: {
      rotate: 0,
      stretch: 0,
      depth: 100,
      modifier: 2,
      slideShadows: true,
    },
   loop:true,
   autoplay:{
    delay:3000,
    disableOnInteraction:false,
    }
  });
