
   //창 실행시 알람창 호출
window.addEventListener('load', dataAlertFunction());

//플래시 어트리뷰트로 가져온 값을 알람창으로 보여주는 함수
function dataAlertFunction() {
  let alertElement = document.querySelector("#dataAlert");

  if (alertElement && alertElement.value.trim() !== "") { // 데이터가 존재하고 빈값 ""이 아닐경우 실행
    let message = alertElement.value;

    alert(message); // 기본 알람창 띄우기

    // FlashAttribute 값이 URL에 남지 않도록 초기화
    history.replaceState({}, document.title, window.location.pathname);

  }
};

let cancelBtn = document.querySelector(".HanMenuBarA")

cancelBtn.addEventListener("click",()=>{
  var answer = confirm("회원 탈퇴를 하시면, 복구가 어려운 점을 참고하시기 바랍니다. 정말 탈퇴를 원하시나요?");

    if(answer ==true){
      location = "/account/cancellation";
    }
})

if(document.title.includes("주문관리")){
  document.getElementById("checkoutLink").style.backgroundColor = "#fff6a6";
}

if(document.title.includes("계정관리")){
  document.getElementById("editLink").style.backgroundColor = "#fff6a6";
}

if(document.title.includes("배송지")){
  document.getElementById("deliveryLink").style.backgroundColor = "#fff6a6";
}

if(document.title.includes("결제")){
  document.getElementById("payLink").style.backgroundColor = "#fff6a6";
}
