
$(".XBtn").click(function (event) {

    if (confirm("정말 배송지를 삭제하시겠습니까?")) {
        var params = event.target.id;
    
        $.ajax({
            type: "GET",            // HTTP method type(GET, POST) 형식
            url: "/member/delivery/delete/" + params,      // 컨트롤러에서 대기중인 URL 주소
            success: function (res) { // 비동기통신의 성공일경우 success콜백으로 들어옴. 'res'는 응답받은 데이터
                if (res == 1000) {
                    alert("등록된 배송지가 삭제되었습니다.");
                    $("#deliveryBody" + params).remove();
                }
                else {
                    alert("배송지를 삭제 할 수 없습니다.");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) { // 비동기 통신이 실패할경우 error 콜백으로 들어옴
                alert("통신 실패.")
            }
        })
    }
    })