const checkoutStepElements = document.querySelectorAll(".checkoutStep");
const postStepElements = document.querySelectorAll(".postStep");
let checkoutId = "http://localhost:8080/master/checkout/" +document.getElementById("id").value+"/delete";

//주문상태와 배송상태에 따라 색깔 다르게 적용
checkoutStepElements.forEach((checkoutStepElement, index) => {
    const checkoutStep = checkoutStepElement.textContent;
    const postStep = postStepElements[index].textContent;

    if(checkoutStep =="주문완료"){
        checkoutStepElement.style.color ="blue";
    }
    else if(checkoutStep =="주문보류"){
        checkoutStepElement.style.color ="green";
    }
    else{
        checkoutStepElement.style.color ="red";
    }

    if(postStep =="배송완료"){
        postStepElements[index].style.color = "blue";
    }
    else if(postStep =="반품"){
        postStepElements[index].style.color = "red";
    }
});


// 스크롤을 내리면 버튼을 보여주기
window.onscroll = function () {
    let topButton = document.getElementById("topBtn");
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        topButton.style.display = "block";  // 스크롤이 50px 이상 내려가면 버튼 표시
    } else {
        topButton.style.display = "none";   // 50px 이하로 올라가면 버튼 숨김
    }
};

// 버튼 클릭 시 페이지 맨 위로 스크롤
document.getElementById("topBtn").onclick = function () {
    window.scrollTo({ top: 0, behavior: 'smooth' });  // 부드러운 스크롤로 맨 위로 이동
};


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

let deleteBtn = document.getElementById("deleteBtn");

deleteBtn.addEventListener("click", () => {
    var answer = confirm("주문삭제를 하시면, 복구가 어려운 점을 참고하시기 바랍니다. 정말 삭제를 원하시나요?");

    if (answer == true) {
        location = checkoutId;
    }
})

const priceElements = document.querySelectorAll(".price");
const totalPriceElements = document.querySelectorAll(".totalPrice");

function formatPrice(p){
  return p.toLocaleString();
}

priceElements.forEach((priceElement,index)=>{
  const price = parseFloat(priceElement.textContent);
  const totalPrice = parseFloat(totalPriceElements[index].textContent);

  priceElement.textContent = formatPrice(price)+" 원"
  totalPriceElements[index].textContent = formatPrice(totalPrice) + " 원";

});