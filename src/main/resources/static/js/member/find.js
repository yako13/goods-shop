$("#findIdBtn").click(function () {


    var params = {
        name: $("#userName").val(),
        phoneNumber: $("#phoneNumber").val()
    }

    $.ajax({
        type: "POST",            // HTTP method type(GET, POST) 형식
        url: "/find/id",      // 컨트롤러에서 대기중인 URL 주소
        data: params,            // Json 형식의 데이터
        success: function (res) { // 비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터
            if (res == "") {
                alert("아이디를 찾을 수 없습니다.");
            }
            else {
                $("#findId").css("display","none");
                $("#found").css("display","block");
                $("#foundId").text(res);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) { // 비동기 통신이 실패할경우 error 콜백으로 들어옴
            alert("통신 실패.")
        }
    });



});



let info = document.getElementById("info");
let bol_password =0;
let hiddenArea = document.getElementById("hiddenPasswordArea");

let userPassword = document.getElementById("userPassword");
let userPasswordCheck = document.getElementById("passwordCheck");
let passwordError = document.getElementById("passwordError");
let eye = document.getElementById("eye");
let sub = document.getElementById("sub");

const password_check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/; //대소문자, 숫자, 특수문자 조합으로 8~15자
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

bol_check_password = 0;

//비밀번호 일치 여부
function checkPassword() {
    if (userPassword.value !== userPasswordCheck.value) {
        passwordError.style.display = "block";
        bol_check_password = 0;
    }
    else {
        passwordError.style.display = "none";
        bol_check_password = 1;
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

info.addEventListener("mouseover", () => {
    hiddenArea.style.display = "block";
})
info.addEventListener("mouseout", () => {
    hiddenArea.style.display = "none";
})


sub.addEventListener("click", function (event) {
    if (password_check.test(userPassword.value)) {
        bol_password = 1;
    }
    else {
        bol_password = 0;
        hiddenPasswordArea.style.display = "block";
    }

    if(bol_check_password==1 && bol_password==1){

    }
    else{
        event.preventDefault();
    }

})

$("#findPasswordBtn").click(function () {

    var params = {
        userId: $("#userId").val(),
        phoneNumber: $("#phoneNumber").val()
    }

    $.ajax({
        type: "POST",
        url: "/find/password",
        data: params,
        success: function (res) {
            if (res == "") {
                alert("비밀번호를 찾을 수 없습니다.");
            }
            else {
                $("#findPassword").css("display","none");
                $("#found").css("display","block");
                $("#userIdInput").val(res);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("통신 실패.")
        }
    });



    });




