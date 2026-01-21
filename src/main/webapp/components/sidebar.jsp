<%@ page pageEncoding="UTF-8" %>
<aside class="w-64 bg-dark-800 border-r border-dark-700 hidden md:flex flex-col flex-shrink-0 transition-all duration-300">

  <div class="h-16 flex items-center px-6 border-b border-dark-700">
    <a href="home.jsp" class="flex items-center gap-2">
      <span class="text-primary text-3xl font-bold">ll</span>
      <span class="text-white font-bold text-xl tracking-tight">TradeSim VN</span>
    </a>
  </div>

  <div class="flex-1 overflow-y-auto py-4">
    <nav class="px-3 space-y-1">

      <a href="${pageContext.request.contextPath}/home"
         class="flex items-center gap-3 px-3 py-2.5 rounded-lg group transition-colors ${pageContext.request.servletPath.contains('/home') || pageContext.request.servletPath.contains('home.jsp') ? 'bg-primary/10 text-primary' : 'text-gray-400 hover:text-white hover:bg-dark-700'}">
        <i class="fa-solid fa-chart-pie w-5 text-center"></i>
        <span class="font-medium text-sm">Tổng quan</span>
      </a>

      <a href="${pageContext.request.contextPath}/trading" class="flex items-center gap-3 px-3 py-2.5 text-gray-400 hover:text-white hover:bg-dark-700 rounded-lg group transition-colors">
        <i class="fa-solid fa-arrow-right-arrow-left w-5 text-center"></i>
        <span class="font-medium text-sm">Giao dịch</span>
      </a>

      <a href="#" class="flex items-center gap-3 px-3 py-2.5 text-gray-400 hover:text-white hover:bg-dark-700 rounded-lg group transition-colors">
        <i class="fa-solid fa-clock-rotate-left w-5 text-center"></i>
        <span class="font-medium text-sm">Lịch sử lệnh</span>
      </a>

      <div class="pt-4 pb-2">
        <p class="px-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">Thị trường</p>
      </div>

      <a href="${pageContext.request.contextPath}/order-book" class="flex items-center gap-3 px-3 py-2.5 text-gray-400 hover:text-white hover:bg-dark-700 rounded-lg group transition-colors ${pageContext.request.servletPath.contains('order-book') ? 'bg-primary/10 text-primary' : ''}">
        <i class="fa-solid fa-list-check w-5 text-center"></i>
        <span class="font-medium text-sm">Sổ lệnh</span>
      </a>

      <a href="#" class="flex items-center gap-3 px-3 py-2.5 text-gray-400 hover:text-white hover:bg-dark-700 rounded-lg group transition-colors">
        <i class="fa-solid fa-newspaper w-5 text-center"></i>
        <span class="font-medium text-sm">Tin tức</span>
      </a>
    </nav>
  </div>

  <div class="p-4 border-t border-dark-700">
    <a href="${pageContext.request.contextPath}/logout" class="flex items-center gap-3 px-3 py-2 text-gray-400 hover:text-red-400 transition-colors">
      <i class="fa-solid fa-right-from-bracket w-5 text-center"></i>
      <span class="font-medium text-sm">Đăng xuất</span>
    </a>
  </div>
</aside>