

let bol = 0;
let bol_id = 0;
let bol_password = 0;

const id_check = /^[a-z]{1}[a-z0-9]{5,10}$/;
const password_check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;

let userId = document.getElementById("userId");
let button = document.getElementById("button");
let userPassword = document.getElementById("userPassword");
let passwordCheck = document.getElementById("passwordCheck");
let passwordError = document.getElementById("passwordError");

function checkPassword() {
    if (userPassword.value !== passwordCheck.value) {
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

passwordCheck.addEventListener("keydown", () => {
    checkPassword();
})

passwordCheck.addEventListener("keyup", () => {
    checkPassword();
})


button.addEventListener("click", () => {
    if (id_check.test(userId.value)) {
        bol_id = 1;

    }
    else {
        bol_id = 0;
        alert("아이디는 영소문자, 숫자조합으로 6~11자리여야 합니다.");
    }

    if (password_check.test(userPassword.value)) {
        bol_password = 1;
    }
    else {
        bol_password = 0;
        alert("비밀번호는 대소문자, 숫자, 특수문자 조합으로 8~15자리여야합니다.");
    }

    bol = bol_id + bol_password;

    if (bol === 2) {
        button.type = "submit";
        button.onclick;
    }
    else {
        alert("입력 정보가 올바르지 않습니다.");
    }
})

// 이용약관

// const pchkBoxes = document.querySelectorAll('input[name="chk"]');
// const cchkBoxes = document.querySelectorAll('input[name="chk1"]');
// function chkAll(isChecked) {


//     // 모든 체크박스를 루프 돌며 상태를 chkAll과 동일하게 설정
//     pchkBoxes.forEach((checkbox) => {
//         checkbox.checked = isChecked;
//     });
//     cchkBoxes.forEach((checkbox) => {
//         checkbox.checked = isChecked;
//     });

//     // 최소 두 개의 체크박스가 선택되었는지 확인하여 "동의" 버튼을 활성화
//     const checkedCount = Array.from(pchkBoxes).filter((checkbox) => checkbox.checked).length;
//     button.disabled = checkedCount < 2;
// }

// // chkAll 체크박스에 이벤트 리스너 추가
// document.querySelector('#chk').addEventListener('change', function () {
//     chkAll(this.checked);
// });

