


// 약관 동의 시작
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
// 약관동의 끝





let dupId = 0;
let bol_all = 0;
let bol_id = 0;
let bol_password = 0;
let bol_name = 0;
let bol_phone = 0;
let bol_agree = 0;

let userPassword = document.getElementById("userPassword");
let userPasswordCheck = document.getElementById("passwordCheck");
let passwordError = document.getElementById("passwordError");
let eye = document.getElementById("eye");


const id_check = /^[a-z]{1}[a-z0-9]{5,10}$/; //첫문자는 영소문자, 영소문자와 숫자 포함 총 6~11자
const password_check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/; //대소문자, 숫자, 특수문자 조합으로 8~15자
const name_check = /^[가-힣]{2,5}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/; //한글 2~5자 또는 영문 이름 2~10자 이내 : 띄어쓰기(\s)가 들어가며 First, Last Name 형식
const phone_check = /^(010|011|016|017|018|019)[0-9]{7,8}$/;

let userId = document.getElementById("userId");
let button = document.getElementById("button");
let userName = document.getElementById("userName");
let phone = document.getElementById("phoneNumber");

let hiddenIdArea = document.getElementById("hiddenIdArea");
let hiddenPasswordArea = document.getElementById("hiddenPasswordArea");
let hiddenNameArea = document.getElementById("hiddenNameArea");
let hiddenPhoneArea = document.getElementById("hiddenPhoneArea");

let hiddenId = document.getElementById("hiddenId");
let hiddenPassword = document.getElementById("hiddenPassword");
let hiddenName = document.getElementById("hiddenName");
let hiddenPhone = document.getElementById("hiddenPhone");



//정규식 적용 알림
hiddenId.addEventListener("mouseover",()=>{
    hiddenIdArea.style.display="block";
})
hiddenId.addEventListener("mouseout",()=>{
    hiddenIdArea.style.display="none";
})

hiddenPassword.addEventListener("mouseover",()=>{
    hiddenPasswordArea.style.display="block";
})
hiddenPassword.addEventListener("mouseout",()=>{
    hiddenPasswordArea.style.display="none";
})

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

userId.addEventListener("focus",()=>{
    hiddenIdArea.style.display="none";
})

userPassword.addEventListener("focus",()=>{
    hiddenPasswordArea.style.display="none";
})

userName.addEventListener("focus",()=>{
    hiddenNameArea.style.display="none";
})

phone.addEventListener("focus",()=>{
    hiddenPhoneArea.style.display="none";
})

//Ajax
// 'duplicateId'라는 id를 가진 버튼 클릭 시 실행
$("#duplicateId").click(function () {

    //중복 체크 전 정규식부터 확인
    if (id_check.test(userId.value)) {
        bol_id = 1;


        // json 형식으로 데이터 set
        var params = {
            userId: $("#userId").val()
        }

        // ajax 통신
        $.ajax({
            type: "POST",            // HTTP method type(GET, POST) 형식
            url: "/check/id",      // 컨트롤러에서 대기중인 URL 주소
            data: params,            // Json 형식의 데이터
            success: function (res) { // 비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터
                if (res == "") {
                    dupId = 0;
                    alert("중복된 아이디입니다.");
                }
                else {
                    dupId = 1;
                    alert("사용가능 아이디입니다.");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) { // 비동기 통신이 실패할경우 error 콜백으로 들어옴
                alert("통신 실패.")
            }
        });

    }
    else {
        bol_id = 0;
        hiddenIdArea.style.display="block";
    }

});




//
button.addEventListener("click", () => {

    if (dupId == 0) {
        alert("아이디 중복 체크를 하셔야합니다.");
    }

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

    if (privacyAgreement.value === "true" && termsAgreement.value === "true") {
        bol_agree = 1;
    }
    else {
        bol_agree = 0;
        alert("약관에 모두 동의하셔야 합니다.");
    }

    bol_all = dupId + bol_id + bol_password + bol_name + bol_phone + bol_agree + bol_check_password;

    if (bol_all === 7) {
        button.type = "submit";
        button.onclick;
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

bol_check_password=0;

//비밀번호 일치 여부
function checkPassword() {
    if (userPassword.value !== userPasswordCheck.value) {
        passwordError.style.display = "block";
        bol_check_password=0;
    }
    else {
        passwordError.style.display = "none";
        bol_check_password=1;
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




