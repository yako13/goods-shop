
$("#hiddenName").mouseover(function () {
    $("#hiddenNameArea").css("display", "block");
  })

  $("#hiddenName").mouseout(function () {
    $("#hiddenNameArea").css("display", "none");
  })

  const name_check= /^[가-힣0-9]{2,10}|[a-zA-Z0-9]{2,10}$/;

  //기본결제카드 중복 여부 확인용
  let check_bol = 0;

  window.addEventListener("load", function () {

    let inputs = document.querySelectorAll(".HanSignUpInput");

    inputs.forEach(function (input) {
      input.addEventListener("focusout", function () {
        let parentLi = input.closest(".HanSignUpLi"); // 부모 li 요소 찾기
        let warningText = parentLi.querySelector(".HanSignUpInfo"); // 경고 문구 찾기

        if (input.value.trim() === "") {
          input.classList.add("HanRed"); // 빨간 테두리 추가
          if (warningText) warningText.style.display = "block"; // 경고 문구 표시
        } else {
          input.classList.remove("HanRed"); // 빨간 테두리 제거
          if (warningText) warningText.style.display = "none"; // 경고 문구 숨김
        }
      });
    });


    $("#HanCheckbox").click(function () {
      if ($("#HanCheckbox").is(":checked")) {

        var params = {
          memberId: $("#sessionId").val()
        }

        // ajax 통신
        $.ajax({
          type: "POST",            // HTTP method type(GET, POST) 형식
          url: "/check/defaultCard",      // 컨트롤러에서 대기중인 URL 주소
          data: params,            // Json 형식의 데이터
          success: function (res) { // 비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터
            if (res == "false") {
              alert("이미 기본 결제 카드가 존재합니다.");
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

    const form = document.querySelector("#HanSignUpForm"); // 폼 선택 (폼 ID가 있는 경우)
    const submitButton = document.querySelector("#HanSignUpSubmit"); // submit 버튼 선택

    submitButton.addEventListener("click", function (event) {


      let isValid = true; // 모든 필드가 유효한지 확인하는 변수
      event.preventDefault(); // 폼 제출 방지


      inputs.forEach(function (input) {
        let parentLi = input.closest(".HanSignUpLi"); // 부모 li 요소 찾기
        let warningText = parentLi.querySelector(".HanSignUpInfo"); // 경고 문구 찾기

        if (input.value.trim() === "") {
          input.classList.add("HanRed"); // 빨간 테두리 추가
          if (warningText) warningText.style.display = "block"; // 경고 문구 표시
          isValid = false; // 하나라도 비어 있으면 폼 제출 방지
        } else {
          input.classList.remove("HanRed"); // 빨간 테두리 제거
          if (warningText) warningText.style.display = "none"; // 경고 문구 숨김
        }
      });

      if(!name_check.test($("#HanCardNewLiTitleInput").val())){
        $("#HanCardNewLiTitleInput").addClass("HanRed");
        $("#hiddenNameArea").css("display", "block");
        isValid = false;
      }
      else{
        $("#HanCardNewLiTitleInput").removeClass("HanRed");
        $("#hiddenNameArea").css("display", "none");
      }

      if ($("#cardNum1").val().length !== 4) {
        $("#cardNum1").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#cardNum1").removeClass("HanRed");
      }

      if ($("#cardNum2").val().length !== 4) {
        $("#cardNum2").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#cardNum2").removeClass("HanRed");
      }

      if ($("#cardNum3").val().length !== 4) {
        $("#cardNum3").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#cardNum3").removeClass("HanRed");
      }

      if ($("#cardNum4").val().length !== 4) {
        $("#cardNum4").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#cardNum2").removeClass("HanRed");
      }

      if ($("#expPeriod1").val().length !== 2) {
        $("#expPeriod1").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#expPeriod1").removeClass("HanRed");
      }

      if ($("#expPeriod2").val().length !== 2) {
        $("#expPeriod2").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#expPeriod2").removeClass("HanRed");
      }

      if ($("#HanSignUpPw3").val().length !== 3) {
        $("#HanSignUpPw3").addClass("HanRed");
        isValid = false;
      }
      else {
        $("#HanSignUpPw3").removeClass("HanRed");
      }

      if (!$("#HanCheckbox").is(":checked")) {

        var params = {
          memberId: $("#sessionId").val()
        }

        // ajax 통신
        $.ajax({
          type: "POST",            // HTTP method type(GET, POST) 형식
          url: "/check/defaultCard",      // 컨트롤러에서 대기중인 URL 주소
          data: params,            // Json 형식의 데이터
          success: function (res) { // 비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터
            if (res == "false") {
              isValid=true;
              check_bol=1;
            }
            else {
              isValid = false;
              alert("기본 결제 카드는 반드시 하나는 필요합니다.");
              check_bol = 1;
              isValid = true;
              $("#HanCheckbox").prop("checked", true);
              $("#HanCheckbox").val(true);
            }
          },
          error: function (XMLHttpRequest, textStatus, errorThrown) { // 비동기 통신이 실패할경우 error 콜백으로 들어옴
            alert("통신 실패.")
          }
        })
      }

      if (isValid && check_bol == 1) {

        document.getElementById("HanSignUpForm").submit();
      }

    });


  });

  

