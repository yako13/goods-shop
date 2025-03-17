let bol_all = 0;
let bol_id = 0;
let bol_password = 0;
let bol_name = 0;
let bol_phone = 0;
let bol_password_check = 0;

let userPassword = document.getElementById("userPassword");
let userPasswordCheck = document.getElementById("passwordCheck");
let passwordError = document.getElementById("passwordError");
let eye = document.getElementById("eye");

const password_check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/; //대소문자, 숫자, 특수문자 조합으로 8~15자
const name_check =  /^[가-힣]{2,5}$|^[a-zA-Z]{1,10}\s[a-zA-Z]{1,10}$/; //한글 2~5자 또는 영문 이름 2~10자 이내 : 띄어쓰기(\s)가 들어가며 First, Last Name 형식
const phone_check = /^(010|011|016|017|018|019)[0-9]{7,8}$/;

let button = document.getElementById("button");
let userName = document.getElementById("userName");
let phone = document.getElementById("phoneNumber");

let provider = document.getElementById("blue");

let hiddenPasswordArea = document.getElementById("hiddenPasswordArea");
let hiddenNameArea = document.getElementById("hiddenNameArea");
let hiddenPhoneArea = document.getElementById("hiddenPhoneArea");

let hiddenPassword = document.getElementById("hiddenPassword");
let hiddenName = document.getElementById("hiddenName");
let hiddenPhone = document.getElementById("hiddenPhone");

//정규식 적용 알림

hiddenName.addEventListener("mouseover",()=>{
    hiddenNameArea.style.display="block";
})
hiddenName.addEventListener("mouseout",()=>{
    hiddenNameArea.style.display="none";
})

hiddenPhone.addEventListener("mouseover",()=>{
    hiddenPhoneArea.style.display="block";
})
hiddenPhone.addEventListener("mouseout",()=>{
    hiddenPhoneArea.style.display="none";
})

userName.addEventListener("focus",()=>{
    hiddenNameArea.style.display="none";
})

phone.addEventListener("focus",()=>{
    hiddenPhoneArea.style.display="none";
})



button.addEventListener("click", () => {
    //SNS 이용자의 경우
    if(provider!=null){

        if (name_check.test(userName.value)) {
            bol_name = 1;
        }
        else {
            bol_name = 0;
            hiddenNameArea.style.display="block";
        }
    
        if (phone_check.test(phone.value)) {
            bol_phone = 1;
        }
        else {
            bol_phone = 0;
            hiddenPhoneArea.style.display="block";
        }
      
        bol_all = bol_name + bol_phone;
    
        if (bol_all === 2) {
            button.type = "submit";
            button.onclick;
        }

    }
    else{
        if (password_check.test(userPassword.value)) {
            bol_password = 1;
        }
        else {
            bol_password = 0;
            hiddenPasswordArea.style.display="block";
        }
        if (name_check.test(userName.value)) {
            bol_name = 1;
        }
        else {
            bol_name = 0;
            hiddenNameArea.style.display="block";
        }
    
        if (phone_check.test(phone.value)) {
            bol_phone = 1;
        }
        else {
            bol_phone = 0;
            hiddenPhoneArea.style.display="block";
        }
    
        checkPassword();

        bol_all =  bol_password + bol_name + bol_phone + bol_password_check;
    
        if (bol_all === 4) {
            button.type = "submit";
            button.onclick;
        }
    }

   
})

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
         bol_password_check = 0 ;
    }
    else {
        passwordError.style.display = "none";
         bol_password_check = 1 ;
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

hiddenPassword.addEventListener("mouseover",()=>{
    hiddenPasswordArea.style.display="block";
})
hiddenPassword.addEventListener("mouseout",()=>{
    hiddenPasswordArea.style.display="none";
})

userPassword.addEventListener("focus",()=>{
    hiddenPasswordArea.style.display="none";
})

