window.addEventListener('DOMContentLoaded', (event) => {
    openNav();
});

function openNav() {

    document.getElementById("mySidenav").style.width = "250px";
    document.getElementById("main").style.marginLeft = "250px";
    document.querySelector(".SlideNavOpenDiv").classList.remove("SlideShow");
}


function closeNav() {

    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
    setTimeout(delay, 300);
}

function delay() {
    document.querySelector(".SlideNavOpenDiv").classList.add("SlideShow");
}