let bol_all = 0;
let bol_id = 0;
let bol_password = 0;
let bol_name = 0;
let bol_phone = 0;

let userPassword = document.getElementById("userPassword");
let userPasswordCheck = document.getElementById("passwordCheck");
let passwordError = document.getElementById("passwordError");
let eye = document.getElementById("eye");

const password_check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/; //대소문자, 숫자, 특수문자 조합으로 8~15자
const name_check = /^[가-힣]{2,5}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/; //한글 2~5자 또는 영문 이름 2~10자 이내 : 띄어쓰기(\s)가 들어가며 First, Last Name 형식
const phone_check = /^(010|011|016|017|018|019)[0-9]{7,8}$/;

let button = document.getElementById("button");
let userName = document.getElementById("userName");
let phone = document.getElementById("phoneNumber");

let provider = document.getElementById("blue");


button.addEventListener("click", () => {
    //SNS 이용자의 경우
    if(provider!=null){

        if (name_check.test(userName.value)) {
            bol_name = 1;
        }
        else {
            bol_name = 0;
            alert("이름은 한글 2~5자 또는 성과 이름을 구분하여 영문 2~10자리씩이여야 합니다.");
        }
    
        if (phone_check.test(phone.value)) {
            bol_phone = 1;
        }
        else {
            bol_phone = 0;
            alert("휴대전화번호는 010, 011, 016, 017, 018, 019로 시작하며, 숫자 10~11자리여야 합니다.");
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
            alert("비밀번호는 대소문자, 숫자, 특수문자 조합으로 8~15자리여야합니다.");
        }
        if (name_check.test(userName.value)) {
            bol_name = 1;
        }
        else {
            bol_name = 0;
            alert("이름은 한글 2~5자 또는 성과 이름을 구분하여 영문 2~10자리씩이여야 합니다.");
        }
    
        if (phone_check.test(phone.value)) {
            bol_phone = 1;
        }
        else {
            bol_phone = 0;
            alert("휴대전화번호는 010, 011, 016, 017, 018, 019로 시작하며, 숫자 10~11자리여야 합니다.");
        }
    
    
        bol_all =  bol_password + bol_name + bol_phone ;
    
        if (bol_all === 3) {
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