<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // --- CHỐNG CACHE TRÌNH DUYỆT (FIX LỖI BACK SAU KHI LOGOUT) ---
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Đăng nhập - TradeSim VN</title>
    <jsp:include page="components/head_css.jsp" />
</head>
<body class="min-h-screen flex items-center justify-center p-4 bg-dark-900 bg-[url('https://images.unsplash.com/photo-1642543492481-44e81e3914a7?q=80&w=2070&auto=format&fit=crop')] bg-cover bg-center bg-no-repeat bg-blend-overlay">

<div class="glass-effect w-full max-w-4xl rounded-2xl shadow-2xl overflow-hidden flex flex-col md:flex-row animate-fade-in-up">

    <div class="w-full md:w-5/12 bg-gradient-to-t from-dark-900 to-transparent relative hidden md:block group">
        <img src="img/Gemini_Generated_Image_kaew0bkaew0bkaew.png"
             class="absolute inset-0 w-full h-full object-cover opacity-60 transition duration-700 group-hover:scale-110" alt="Stock chart">
        <div class="absolute bottom-0 left-0 p-8 z-10 bg-gradient-to-t from-dark-900 via-dark-900/80 to-transparent w-full">
            <h2 class="text-2xl font-bold text-white mb-2">Welcome Back!</h2>
            <p class="text-gray-300 text-sm">Tiếp tục hành trình chinh phục thị trường chứng khoán.</p>
        </div>
    </div>

    <div class="w-full md:w-7/12 bg-dark-800 p-8 md:p-12 flex flex-col justify-center">

        <div class="mb-8">
            <h1 class="text-3xl font-bold text-white flex items-center gap-2">
                <span class="text-primary text-4xl">ll</span> TradeSim VN
            </h1>
            <p class="text-gray-400 mt-2 text-sm">Đăng nhập hệ thống quản lý tài sản.</p>
        </div>

        <div class="min-h-[50px]">
            <% if(request.getAttribute("error") != null) { %>
            <div role="alert" class="flex items-center bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-lg mb-4 text-sm shadow-lg shadow-red-900/20">
                <i class="fa-solid fa-circle-exclamation mr-2"></i>
                <span><%= request.getAttribute("error") %></span>
            </div>
            <% } %>
        </div>

        <form action="login-form" method="post" class="space-y-5">
            <div>
                <label class="block text-gray-400 text-sm mb-1 font-medium">Tên đăng nhập</label>
                <div class="relative">
                    <span class="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-500">
                        <i class="fa-regular fa-user"></i>
                    </span>
                    <input type="text" name="user_name" required placeholder="Nhập username..." autocomplete="off"
                           class="w-full bg-dark-900 border border-dark-700 rounded-lg pl-10 pr-4 py-3 text-white focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition placeholder-gray-600">
                </div>
            </div>

            <div>
                <div class="flex justify-between items-center mb-1">
                    <label class="block text-gray-400 text-sm font-medium">Mật khẩu</label>
                    <a href="forgot_password.jsp" class="text-xs text-primary hover:text-white transition">Quên mật khẩu?</a>
                </div>
                <div class="relative">
                    <span class="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-500">
                        <i class="fa-solid fa-lock"></i>
                    </span>
                    <input type="password" id="loginPass" name="user_password" required placeholder="••••••••" autocomplete="new-password"
                           class="w-full bg-dark-900 border border-dark-700 rounded-lg pl-10 pr-10 py-3 text-white focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition placeholder-gray-600">

                    <button type="button" onclick="togglePassword('loginPass', 'eyeIconLogin')"
                            class="absolute inset-y-0 right-0 px-3 flex items-center text-gray-400 hover:text-white cursor-pointer z-10">
                        <i id="eyeIconLogin" class="fa-solid fa-eye"></i>
                    </button>
                </div>
            </div>

            <div class="flex items-center">
                <input type="checkbox" id="remember" class="w-4 h-4 rounded bg-dark-700 border-gray-600 text-primary focus:ring-primary focus:ring-offset-dark-800 cursor-pointer">
                <label for="remember" class="ml-2 text-sm text-gray-400 cursor-pointer select-none">Ghi nhớ đăng nhập</label>
            </div>

            <button type="submit" class="w-full bg-primary hover:bg-primary-hover text-white font-bold py-3 rounded-lg shadow-lg shadow-purple-900/50 transition duration-300 transform hover:-translate-y-0.5 active:scale-95 flex justify-center items-center gap-2">
                <span>Đăng Nhập</span>
                <i class="fa-solid fa-arrow-right-to-bracket"></i>
            </button>
        </form>

        <div class="mt-8 text-center text-gray-400 text-sm">
            Chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register.jsp" class="text-primary hover:text-white font-medium ml-1 transition underline-offset-4 hover:underline">
                Đăng ký ngay
            </a>
        </div>

        <jsp:include page="components/footer.jsp" />
    </div>
</div>

<jsp:include page="components/scripts.jsp" />
<script>
    // Khi trang Login vừa tải xong -> Xóa sạch 2 ô nhập liệu ngay lập tức
    window.onload = function() {
        // Thay 'username' và 'password' bằng id hoặc name của thẻ input bên bạn
        let userInput = document.querySelector('input[name="username"]');
        let passInput = document.querySelector('input[name="password"]');

        if(userInput) userInput.value = "";
        if(passInput) passInput.value = "";
    }
</script>
</body>
</html>