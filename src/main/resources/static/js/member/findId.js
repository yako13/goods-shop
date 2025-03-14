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




