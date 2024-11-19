document.addEventListener('DOMContentLoaded', function() {
    loadContent('/views/admin/dashboard.html', 'Dashboard'); // Tải nội dung ban đầu và cập nhật URL
});

function loadContent(url, title) {
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Có lỗi xảy ra');
            }
            return response.text();
        })
        .then(data => {
            // Chèn nội dung của tệp HTML vào #contentAdmin
            document.getElementById('contentAdmin').innerHTML = data;

            // Cập nhật URL trong thanh địa chỉ mà không tải lại trang
            window.history.pushState({ path: url }, title, url);
        })
        .catch(error => {
            console.error('Có lỗi xảy ra:', error);
        });
}

// Xử lý sự kiện quay lại (Back) trên trình duyệt
window.onpopstate = function(event) {
    if (event.state) {
        loadContent(event.state.path); // Tải lại nội dung khi quay lại
    }
};

