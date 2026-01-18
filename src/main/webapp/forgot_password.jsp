<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // --- CHỐNG CACHE TRÌNH DUYỆT (FIX LỖI BACK SAU KHI LOGOUT) ---
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Khôi phục mật khẩu - TradeSim VN</title>
    <jsp:include page="components/head_css.jsp" />
</head>
<body class="min-h-screen flex items-center justify-center p-4 bg-dark-900 bg-[url('https://images.unsplash.com/photo-1555421689-491a97ff2040?q=80&w=2070&auto=format&fit=crop')] bg-cover bg-center bg-no-repeat bg-blend-overlay">

<div class="glass-effect w-full max-w-4xl rounded-2xl shadow-2xl overflow-hidden flex flex-col md:flex-row animate-fade-in-up">

    <div class="w-full md:w-5/12 bg-dark-800 relative hidden md:block overflow-hidden">
        <img src="https://images.unsplash.com/photo-1563013544-824ae1b704d3?q=80&w=2070&auto=format&fit=crop"
             class="absolute inset-0 w-full h-full object-cover opacity-50" alt="Security Lock">
        <div class="absolute inset-0 bg-gradient-to-t from-dark-900 via-transparent to-transparent"></div>
        <div class="absolute bottom-0 left-0 p-8 z-10">
            <h2 class="text-2xl font-bold text-white mb-2">Bảo mật tài khoản</h2>
            <p class="text-gray-400 text-sm">Sử dụng mã bảo mật (Security Code) bạn đã tạo khi đăng ký để thiết lập lại mật khẩu.</p>
        </div>
    </div>

    <div class="w-full md:w-7/12 bg-dark-800 p-8 md:p-12 flex flex-col justify-center">

        <div class="mb-6">
            <h1 class="text-2xl font-bold text-white mb-2">Quên mật khẩu?</h1>
            <p class="text-gray-400 text-sm">Đừng lo, hãy điền thông tin để lấy lại quyền truy cập.</p>
        </div>

        <div class="min-h-[20px] mb-4">
            <c:if test="${not empty error}">
                <div role="alert" class="flex items-center bg-red-500/10 border border-red-500 text-red-500 px-4 py-2 rounded text-sm animate-pulse">
                    <i class="fa-solid fa-triangle-exclamation mr-2"></i>
                    <span>${error}</span>
                </div>
            </c:if>
        </div>

        <form action="reset-password" method="post" class="space-y-4">

            <div>
                <label class="block text-gray-400 text-xs uppercase font-bold mb-1">Tên đăng nhập</label>
                <input type="text" name="user_name" required placeholder="Nhập tên đăng nhập của bạn"
                       value="${oldUserName}"
                       class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary transition">
            </div>

            <div>
                <label class="block text-gray-400 text-xs uppercase font-bold mb-1">Mã bảo mật (Security Code)</label>
                <div class="relative">
                     <span class="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-500">
                        <i class="fa-solid fa-shield-cat"></i>
                    </span>
                    <input type="password" name="security_code" required placeholder="Nhập mã bảo mật..."
                           value="${oldSecurityCode}"
                           class="w-full bg-dark-900 border border-dark-700 rounded-lg pl-10 pr-4 py-3 text-white focus:outline-none focus:border-primary transition">
                </div>
            </div>

            <div class="grid grid-cols-1 gap-4 pt-2 border-t border-dark-700 mt-2">
                <div>
                    <label class="block text-gray-400 text-xs uppercase font-bold mb-1">Mật khẩu mới</label>
                    <div class="relative">
                        <input type="password" id="passInput" name="new_password" required placeholder="Mật khẩu mới..." onkeyup="checkMatchPassword()"
                               class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary transition pr-10">
                        <button type="button" onclick="togglePassword('passInput', 'eyeNewPass')"
                                class="absolute inset-y-0 right-0 px-3 text-gray-400 hover:text-white cursor-pointer">
                            <i id="eyeNewPass" class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                </div>

                <div>
                    <label class="block text-gray-400 text-xs uppercase font-bold mb-1">Nhập lại mật khẩu mới</label>
                    <div class="relative">
                        <input type="password" id="confirmPassInput" name="confirm_password" required placeholder="Nhập lại..." onkeyup="checkMatchPassword()"
                               class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary transition pr-10">
                        <button type="button" onclick="togglePassword('confirmPassInput', 'eyeConfirmNew')"
                                class="absolute inset-y-0 right-0 px-3 text-gray-400 hover:text-white cursor-pointer">
                            <i id="eyeConfirmNew" class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                    <div id="matchMessage" class="min-h-[20px] text-xs mt-1"></div>
                </div>
            </div>

            <button type="submit" class="w-full bg-primary hover:bg-primary-hover text-white font-bold py-3 rounded-lg shadow-lg shadow-purple-900/40 transition duration-300 transform hover:-translate-y-0.5 mt-2">
                Đổi mật khẩu
            </button>
        </form>

        <div class="mt-6 text-center text-sm">
            <a href="login.jsp" class="text-gray-400 hover:text-white transition flex items-center justify-center gap-2">
                <i class="fa-solid fa-arrow-left"></i> Quay lại đăng nhập
            </a>
        </div>
    </div>
</div>

<jsp:include page="components/scripts.jsp" />

<script>
    function checkMatchPassword() {
        var password = document.getElementById("passInput").value;
        var confirmPassword = document.getElementById("confirmPassInput").value;
        var message = document.getElementById("matchMessage");

        if (password && confirmPassword) {
            if (password === confirmPassword) {
                message.style.color = '#4ade80'; // Màu xanh
                message.innerHTML = '<i class="fa-solid fa-check mr-1"></i> Mật khẩu khớp';
            } else {
                message.style.color = '#f87171'; // Màu đỏ
                message.innerHTML = '<i class="fa-solid fa-xmark mr-1"></i> Mật khẩu chưa khớp';
            }
        } else {
            message.innerHTML = "";
        }
    }
</script>

</body>
</html>