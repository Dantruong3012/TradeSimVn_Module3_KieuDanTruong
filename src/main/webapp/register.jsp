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
  <title>Đăng ký - TradeSim VN</title>
  <jsp:include page="components/head_css.jsp" />
</head>
<body class="min-h-screen flex items-center justify-center p-4 bg-dark-900 bg-[url('https://images.unsplash.com/photo-1611974765270-ca1258634369?q=80&w=2070&auto=format&fit=crop')] bg-cover bg-center bg-no-repeat bg-blend-overlay">

<div class="glass-effect w-full max-w-5xl rounded-2xl shadow-2xl overflow-hidden flex flex-col md:flex-row">

  <div class="w-full md:w-1/2 bg-gradient-to-br from-purple-900/90 to-dark-900/95 p-10 flex flex-col justify-center relative overflow-hidden">
    <div class="absolute top-0 right-0 -mr-20 -mt-20 w-64 h-64 bg-primary rounded-full blur-[100px] opacity-20 pointer-events-none"></div>

    <div class="relative z-10">
      <h1 class="text-3xl font-bold flex items-center gap-2 mb-8">
        <span class="text-primary">ll</span> TradeSim VN
      </h1>

      <h2 class="text-4xl font-bold mb-6 leading-tight text-white">
        Kiến tạo tương lai <br/>
        <span class="text-transparent bg-clip-text bg-gradient-to-r from-primary to-pink-500">Tài chính số</span>
      </h2>

      <ul class="space-y-4 mb-8 text-gray-300">
        <li class="flex items-center gap-3">
          <div class="w-8 h-8 rounded-full bg-primary/20 flex items-center justify-center text-primary"><i class="fa-solid fa-chart-line"></i></div>
          <span>Dữ liệu thị trường Real-time</span>
        </li>
        <li class="flex items-center gap-3">
          <div class="w-8 h-8 rounded-full bg-success/20 flex items-center justify-center text-success"><i class="fa-solid fa-shield-halved"></i></div>
          <span>An toàn & Bảo mật tuyệt đối</span>
        </li>
        <li class="flex items-center gap-3">
          <div class="w-8 h-8 rounded-full bg-blue-500/20 flex items-center justify-center text-blue-400"><i class="fa-solid fa-graduation-cap"></i></div>
          <span>Môi trường học tập chuyên nghiệp</span>
        </li>
      </ul>
    </div>
  </div>

  <div class="w-full md:w-1/2 bg-dark-800 p-8 md:p-10">
    <h3 class="text-2xl font-bold text-white mb-2">Tạo tài khoản mới</h3>
    <p class="text-gray-400 mb-6 text-sm">Điền thông tin bên dưới để bắt đầu.</p>

    <% if(request.getAttribute("error") != null) { %>
    <div role="alert" class="flex items-start bg-red-500/10 border border-red-500 text-red-500 px-4 py-3 rounded-lg mb-6 text-sm shadow-md animate-pulse-soft">
      <i class="fa-solid fa-triangle-exclamation mt-1 mr-2"></i>
      <div>
        <span class="font-bold">Đăng ký thất bại:</span>
        <p><%= request.getAttribute("error") %></p>
      </div>
    </div>
    <% } %>
    <form action="register" method="post" class="space-y-4">
      <div>
        <label class="block text-gray-400 text-xs uppercase font-bold mb-1 tracking-wider">Tên đăng nhập</label>
        <input type="text" name="user_name" required
               class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary focus:ring-1 focus:ring-primary transition"
               value="<%= request.getAttribute("oldName") != null ? request.getAttribute("oldName") : "" %>">
      </div>

      <div>
        <label class="block text-gray-400 text-xs uppercase font-bold mb-1 tracking-wider">Tên hiển thị</label>
        <input type="text" name="display_name" required
               class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary transition">
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-gray-400 text-xs uppercase font-bold mb-1 tracking-wider">Mật khẩu</label>
          <div class="relative">
            <input type="password" id="regPass" name="user_password" required onkeyup="checkMatchPassword()"
                   class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary transition pr-10">
            <button type="button" onclick="togglePassword('regPass', 'eye1')" class="absolute inset-y-0 right-0 px-3 text-gray-400 hover:text-white">
              <i id="eye1" class="fa-solid fa-eye"></i>
            </button>
          </div>
        </div>
        <div>
          <label class="block text-gray-400 text-xs uppercase font-bold mb-1 tracking-wider">Nhập lại</label>
          <div class="relative">
            <input type="password" id="regConfirm" name="confirm_password" required onkeyup="checkMatchPassword()"
                   class="w-full bg-dark-900 border border-dark-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:border-primary transition pr-10">
            <button type="button" onclick="togglePassword('regConfirm', 'eye2')" class="absolute inset-y-0 right-0 px-3 text-gray-400 hover:text-white">
              <i id="eye2" class="fa-solid fa-eye"></i>
            </button>
          </div>
          <div id="matchMessage" class="min-h-[20px]"></div>
        </div>
      </div>

      <div>
        <label class="block text-gray-400 text-xs uppercase font-bold mb-1 tracking-wider flex justify-between">
          <span>Mã bảo mật</span>
          <span class="text-xs text-gray-500 normal-case font-normal">(Dùng để lấy lại mật khẩu)</span>
        </label>
        <div class="relative">
          <span class="absolute inset-y-0 left-0 pl-3 flex items-center text-gray-500"><i class="fa-solid fa-shield-cat"></i></span>
          <input type="text" name="security_code" required placeholder="Nhập mã bí mật..."
                 class="w-full bg-dark-900 border border-dark-700 rounded-lg pl-10 pr-4 py-3 text-white focus:outline-none focus:border-primary transition">
        </div>
      </div>

      <button type="submit" class="w-full mt-2 bg-primary hover:bg-primary-hover text-white font-bold py-3.5 rounded-lg shadow-lg shadow-purple-900/40 transition duration-300 transform hover:-translate-y-0.5">
        Đăng Ký Tài Khoản
      </button>
    </form>

    <div class="mt-6 text-center text-gray-400 text-sm border-t border-dark-700 pt-4">
      Đã là thành viên?
      <a href="login.jsp" class="text-primary hover:text-white font-bold transition">Đăng nhập tại đây</a>
    </div>
  </div>
</div>

<jsp:include page="components/scripts.jsp" />
</body>
</html>