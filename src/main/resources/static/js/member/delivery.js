let check_bol = 0;

$("#HanCheckbox").click(function () {
  if ($("#HanCheckbox").is(":checked")) {

    var params = {
      memberId: $("#sessionId").val()
    }

    // ajax 통신
    $.ajax({
      type: "POST",            // HTTP method type(GET, POST) 형식
      url: "/check/defaultDelivery",      // 컨트롤러에서 대기중인 URL 주소
      data: params,            // Json 형식의 데이터
      success: function (res) { // 비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터
        if (res == "false") {
          alert("이미 기본 배송지가 존재합니다.");
          $("#HanCheckbox").val(false);
          $("#HanCheckbox").prop("checked", false);
          check_bol = 0;
        }
        else {
          $("#HanCheckbox").val(true);
          check_bol = 1;
          $("#HanCheckbox").prop("checked", true);
        }
      },
      error: function (XMLHttpRequest, textStatus, errorThrown) { // 비동기 통신이 실패할경우 error 콜백으로 들어옴
        alert("통신 실패.")
      }
    })
  }
}
)

const delivery_name_check = /^[가-힣0-9]{2,10}|[a-zA-Z0-9]{2,10}$/;
const name_check = /^(?:[가-힣]{2,5})$|^(?:[a-zA-Z]{2,10}\s[a-zA-Z]{2,10})$/;
const phone_check = /^(010|011|016|017|018|019)[0-9]{7,8}$/;



//필수 입력칸의 LiCheck 클래스가있는 li
const HanSignUpLiList = document.querySelectorAll('.LiCheck');

//필수 입력칸 HanSignUpInput 클래스가 있는 input
const HanSignUpInputList = document.querySelectorAll('.HanSignUpInput');

//HanSignUpSubmit 폼의 submit 버튼
const HanSignUpSubmit = document.querySelector('#HanSignUpSubmit');

// 필수 입력칸에 값이 다 있는지 모두 체크하는 함수
function HanSignUpValueCheckAll() {

  let value1 = 0;

  for (let i = 0; i < HanSignUpLiList.length; i++) {

    if (HanSignUpInputList[i].value === "") {
      //input박스를 빨간색으로 보여주고
      //경고 문자를 보여준다
      HanSignUpLiList[i].classList.add('HanRed');
      HanSignUpLiList[i].classList.remove('hanNone');
      value1 = value1 + 1;

    } else {
      //input박스의 빨간색제거
      //경고 문자 안보이게
      HanSignUpLiList[i].classList.remove('HanRed');
      HanSignUpLiList[i].classList.add('hanNone');
    }
  };

  return value1;
};

//창 로드시 필수 입력칸에 이벤트 걸어준다
window.addEventListener('load', focusEvent);


function focusEvent() {
  // 필수 입력칸에 focusout시 값이 다 있는지 체크
  for (let i = 0; i < HanSignUpLiList.length; i++) {

    HanSignUpInputList[i].addEventListener('focusout', function () {

      HanSignUpValueCheck(i);

    });


  }
}

function HanSignUpValueCheck(i) {

  if (HanSignUpInputList[i].value === "") {
    //input박스를 빨간색으로 보여주고
    //경고 문자를 보여준다
    HanSignUpLiList[i].classList.add('HanRed');
    HanSignUpLiList[i].classList.remove('hanNone');
  } else {
    //input박스의 빨간색제거
    //경고 문자 안보이게
    HanSignUpLiList[i].classList.remove('HanRed');
    HanSignUpLiList[i].classList.add('hanNone');
  }


}


$("#hiddenDeliveryName").mouseover(function () {
  $("#hiddenDeliveryNameArea").css("display", "block");
})

$("#hiddenDeliveryName").mouseout(function () {
  $("#hiddenDeliveryNameArea").css("display", "none");
})

$("#deliveryName").focus(function () {
  $("#hiddenDeliveryNameArea").css("display", "none");
})

$("#hiddenName").mouseover(function () {
  $("#hiddenNameArea").css("display", "block");
})

$("#hiddenName").mouseout(function () {
  $("#hiddenNameArea").css("display", "none");
})
$("#recipientName").focus(function () {
  $("#hiddenNameArea").css("display", "none");
})

$("#hiddenPhone").mouseover(function () {
  $("#hiddenPhoneArea").css("display", "block");
})

$("#hiddenPhone").mouseout(function () {
  $("#hiddenPhoneArea").css("display", "none");
})

$("#recipientPhoneNumber").focus(function () {
  $("#hiddenPhoneArea").css("display", "none");
})


//회원가입버튼 누를시 검사하는 메서드 호출
HanSignUpSubmit.addEventListener('click', HanSignUpSubmitCheck);

//회원가입버튼 누를시 검사하고 submit 하는 메서드


//1. 필수입력창에 값을 다 입력했는지

function HanSignUpSubmitCheck(e) {


  e.preventDefault(); // 폼 제출 방지

  if (!delivery_name_check.test($("#deliveryName").val())) {
    $("#deliveryName").addClass("HanRed");
    $("#hiddenDeliveryNameArea").css("display", "block");


  }
  else {
    $("#deliveryName").removeClass("HanRed");
  }

  if (!name_check.test($("#recipientName").val())) {
    $("#hiddenNameArea").css("display", "block");
    $("#recipientName").addClass("HanRed");

  }
  else {
    $("#recipientName").removeClass("HanRed");
  }

  if (!phone_check.test($("#recipientPhoneNumber").val())) {
    $("#hiddenPhoneArea").css("display", "block");
    $("#recipientPhoneNumber").addClass("HanRed");

  }
  else {
    $("#recipientPhoneNumber").removeClass("HanRed");
  }

  //1. 필수입력창에 값을 다 입력했는지
  let ValueCheck = HanSignUpValueCheckAll();

  if (!$("#HanCheckbox").is(":checked")) {

    var params = {
      memberId: $("#sessionId").val()
    }

    // ajax 통신
    $.ajax({
      type: "POST",
      url: "/check/defaultDelivery",
      data: params,
      success: function (res) {
        if (res == "false") {
          if (ValueCheck === 0) {
            check_bol = 1;
            document.getElementById("HanSignUpForm").submit();
          }
          else {
            alert("error");
          }
        }
        else {
          alert("기본 배송지는 반드시 하나는 필요합니다.");
          $("#HanCheckbox").trigger("click");
        }
      },
      error: function (XMLHttpRequest, textStatus, errorThrown) {
        alert("통신 실패.")
      }
    })
  };


  if (ValueCheck==0 && check_bol == 1) {

    document.getElementById("HanSignUpForm").submit();
  }
};

//HanSignUpLiList에  값을 쓸때 빨간색 테두리와 경고문자 제거하는 함수 호출
HanSignUpLiList.forEach((input, i) => {
  input.addEventListener('input', () => HanSignUpWrite(i));
});

//input에 값을 쓸때 빨간색 테두리와 경고문자 제거
function HanSignUpWrite(i) {

  HanSignUpLiList[i].classList.remove('HanRed');
  HanSignUpLiList[i].classList.add('hanNone');

}



//우편번호 및 주소찾기, 다음 API이용
function sample6_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ''; // 주소 변수
      var extraAddr = ''; // 참고항목 변수

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else { // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }

      // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
      if (data.userSelectedType === 'R') {
        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
          extraAddr += data.bname;
        }
        // 건물명이 있고, 공동주택일 경우 추가한다.
        if (data.buildingName !== '' && data.apartment === 'Y') {
          extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
        }
        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
        if (extraAddr !== '') {
          extraAddr = ' (' + extraAddr + ')';
        }
        // 조합된 참고항목을 해당 필드에 넣는다.
        //document.getElementById("sample6_extraAddress").value = extraAddr;

      } else {
        // document.getElementById("sample6_extraAddress").value = '';
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById("HanZipcodeValue").value = data.zonecode;
      document.getElementById("HanAddressValue").value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("HanDetailAddress").focus();

    }
  }).open();
}