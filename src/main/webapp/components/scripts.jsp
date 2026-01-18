<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<script>
    // 1. Chức năng Hiển thị / Ẩn mật khẩu
    function togglePassword(inputId, iconId) {
        const input = document.getElementById(inputId);
        const icon = document.getElementById(iconId);

        if (input.type === "password") {
            input.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            input.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    }

    // 2. Chức năng tự động ẩn thông báo (Alert) sau 5 giây
    document.addEventListener('DOMContentLoaded', function() {
        const alerts = document.querySelectorAll('[role="alert"]');
        if (alerts.length > 0) {
            setTimeout(function() {
                alerts.forEach(function(alert) {
                    // Hiệu ứng mờ dần
                    alert.style.transition = "opacity 0.5s ease";
                    alert.style.opacity = "0";
                    setTimeout(function() {
                        alert.remove();
                    }, 500); // Xóa khỏi DOM sau khi mờ
                });
            }, 5000); // Đợi 5 giây
        }
    });

    // 3. Client-side Validation (Kiểm tra pass nhập lại có khớp không ngay khi gõ)
    function checkMatchPassword() {
        const pass = document.getElementById('passInput').value;
        const confirm = document.getElementById('confirmPassInput').value;
        const msg = document.getElementById('matchMessage');

        if (confirm === "") {
            msg.innerText = "";
            return;
        }

        if (pass === confirm) {
            msg.innerText = "Mật khẩu khớp!";
            msg.className = "text-xs text-green-500 mt-1";
        } else {
            msg.innerText = "Mật khẩu chưa khớp!";
            msg.className = "text-xs text-red-500 mt-1";
        }
    }
</script>