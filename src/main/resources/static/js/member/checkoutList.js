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