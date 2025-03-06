let userPassword = document.getElementById("userPassword");
let userPasswordCheck = document.getElementById("passwordCheck");
let passwordError = document.getElementById("passwordError");
let eye = document.getElementById("eye");

checkEye = true;

function clickEye() {
    checkEye = !checkEye;
}

//눈 클릭 했을 경우
eye.addEventListener("click", () => {
    if (checkEye) {
        eye.className = "fa-solid fa-eye-slash";
        userPassword.type = "text";
        userPasswordCheck.type = "text";
        clickEye();
    }
    else {
        eye.className = "fa-solid fa-eye"
        userPassword.type = "password";
        userPasswordCheck.type = "password";
        clickEye();
    }
})

//비밀번호 일치 여부
function checkPassword() {
    if (userPassword.value !== userPasswordCheck.value) {
        passwordError.style.display = "block";
    }
    else {
        passwordError.style.display = "none";
    }
}

userPassword.addEventListener("keydown", () => {
    checkPassword();
})

userPassword.addEventListener("keyup", () => {
    checkPassword();
})

userPasswordCheck.addEventListener("keydown", () => {
    checkPassword();
})

userPasswordCheck.addEventListener("keyup", () => {
    checkPassword();
})




// 약관 동의
let bol = true;
let check01 = document.getElementById("check01");
let check02 = document.getElementById("check02");
let check03 = document.getElementById("check03");

let privacyAgreement = document.getElementById("privacyAgreement");
let termsAgreement = document.getElementById("termsAgreement");

function check() {
    bol = !bol;
}


check01.addEventListener("click", () => {
    if (bol) {
        check01.className = "fa-solid fa-circle-check";
        check02.className = "fa-solid fa-circle-check";
        check03.className = "fa-solid fa-circle-check";
        check();
        termsAgreement.value = true;
        privacyAgreement.value = true;
    }
    else {
        check01.className = "fa-regular fa-circle-check";
        check02.className = "fa-regular fa-circle-check";
        check03.className = "fa-regular fa-circle-check";
        check();
        termsAgreement.value = false;
        privacyAgreement.value = false;
    }


})

let bol2 = true;
function bol_2() {
    bol2 = !bol2;
}

let bol3 = true;
function bol_3() {
    bol3 = !bol3;
}

check02.addEventListener("click", () => {
    if (bol2) {
        check02.className = "fa-solid fa-circle-check";
        bol_2();
        termsAgreement.value = true;
    }
    else {
        check02.className = "fa-regular fa-circle-check";
        bol_2();
        termsAgreement.value = false;
    }
})

check03.addEventListener("click", () => {
    if (bol3) {
        check03.className = "fa-solid fa-circle-check";
        bol_3();
        privacyAgreement.value = true;
    }
    else {
        check03.className = "fa-regular fa-circle-check";
        bol_3();
        privacyAgreement.value = false;
    }
})



