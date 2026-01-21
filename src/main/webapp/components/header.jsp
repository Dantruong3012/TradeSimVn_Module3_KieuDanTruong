<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    HEADER COMPONENT
    - Sử dụng biến 'availableCash' trực tiếp từ Controller (Backend đã tính)
    - Sử dụng biến 'unreadCount' & 'notifications' từ Controller
--%>
<header class="h-16 bg-dark-800/80 backdrop-blur-md border-b border-dark-700 sticky top-0 z-50 flex items-center justify-between px-4 sm:px-6 transition-all duration-300">

    <div class="flex items-center gap-4">
        <button class="md:hidden text-gray-400 hover:text-white transition p-1 rounded-md hover:bg-dark-700">
            <i class="fa-solid fa-bars text-xl"></i>
        </button>
        <%-- Tiêu đề động hoặc mặc định --%>
        <h2 class="text-white font-semibold text-lg hidden sm:block tracking-wide">
            ${pageTitle != null ? pageTitle : 'TradeSim VN'}
        </h2>
    </div>

    <div class="flex items-center gap-3 sm:gap-5">

        <%-- TIỀN KHẢ DỤNG --%>
        <div class="hidden sm:flex items-center gap-3 bg-dark-900/80 border border-dark-700 px-3 py-1.5 rounded-lg shadow-sm">
            <div class="text-right mr-1">
                <div class="text-[10px] text-gray-400 font-bold uppercase tracking-wider mb-0.5">Tiền khả dụng</div>
                <div class="text-emerald-400 font-bold text-base leading-none font-mono">
                    <fmt:formatNumber value="${availableCash}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                </div>
            </div>

            <button class="flex items-center gap-2 bg-primary hover:bg-primary-hover text-white text-xs font-bold px-3 py-2 rounded transition-all duration-300 shadow-lg shadow-purple-900/20 hover:shadow-purple-900/40 hover:-translate-y-0.5">
                <i class="fa-solid fa-wallet"></i> <span>Nạp</span>
            </button>
        </div>

        <div class="h-6 w-px bg-dark-700 hidden sm:block"></div>

        <%-- NOTIFICATION BELL --%>
        <div class="relative" id="notificationContainer">
            <button onclick="toggleNotification()" class="relative p-2 text-gray-400 hover:text-white transition duration-200 group focus:outline-none rounded-full hover:bg-white/5">
                <i class="fa-regular fa-bell text-xl group-hover:scale-110 transition-transform"></i>

                <%-- [LOGIC CHẤM ĐỎ] Chỉ hiện khi unreadCount > 0 --%>
                <c:if test="${unreadCount != null && unreadCount > 0}">
                    <span class="absolute top-1.5 right-2 w-2.5 h-2.5 bg-red-500 rounded-full animate-pulse border border-dark-800"></span>
                </c:if>
            </button>

            <%-- DROPDOWN --%>
            <div id="notificationDropdown" class="hidden absolute right-0 mt-4 w-80 sm:w-96 bg-dark-800 border border-dark-700 rounded-xl shadow-2xl z-50 overflow-hidden origin-top-right animate-fade-in-up">

                <div class="p-4 border-b border-dark-700 flex justify-between items-center bg-dark-900/50">
                    <h3 class="font-bold text-white text-sm">Thông báo (${unreadCount != null ? unreadCount : 0})</h3>
                    <a href="${pageContext.request.contextPath}/read-all" class="text-[10px] text-gray-500 hover:text-primary transition uppercase font-bold tracking-wider cursor-pointer no-underline">
                        Đánh dấu đã đọc
                    </a>
                </div>

                <div class="max-h-[300px] overflow-y-auto scrollbar-thin scrollbar-thumb-dark-600">
                    <c:forEach var="n" items="${notifications}">
                        <div class="p-4 border-b border-dark-700 hover:bg-dark-700/50 transition cursor-pointer flex gap-3 group ${n.seen ? 'opacity-60' : 'bg-dark-700/20'}">
                            <div class="mt-1 flex-shrink-0">
                                <c:choose>
                                    <c:when test="${n.type == 'SUCCESS'}">
                                        <div class="w-8 h-8 rounded-full bg-emerald-500/10 flex items-center justify-center text-emerald-500 border border-emerald-500/20 group-hover:border-emerald-500/50 transition">
                                            <i class="fa-solid fa-gift text-xs"></i>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="w-8 h-8 rounded-full bg-blue-500/10 flex items-center justify-center text-blue-500 border border-blue-500/20 group-hover:border-blue-500/50 transition">
                                            <i class="fa-solid fa-circle-info text-xs"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <p class="text-sm text-gray-300 group-hover:text-white transition leading-snug mb-1">${n.message}</p>
                                <p class="text-[10px] text-gray-500 font-mono">
                                    <fmt:formatDate value="${n.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </p>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty notifications}">
                        <div class="flex flex-col items-center justify-center py-8 text-gray-500">
                            <i class="fa-regular fa-bell-slash text-2xl mb-2 opacity-50"></i>
                            <span class="text-xs">Không có thông báo mới</span>
                        </div>
                    </c:if>
                </div>
                <div class="p-2 bg-dark-900/50 text-center border-t border-dark-700">
                    <a href="#" class="text-xs text-primary hover:text-white transition">Xem tất cả</a>
                </div>
            </div>
        </div>

        <%-- USER AVATAR --%>
        <c:set var="displayName" value="${sessionScope.account.userDisplayName != null ? sessionScope.account.userDisplayName : sessionScope.account.userName}" />
        <div class="relative group cursor-pointer" title="${displayName}">
            <div class="h-9 w-9 rounded-full bg-gradient-to-tr from-primary to-blue-500 p-[2px] shadow-lg shadow-primary/20 transition-transform group-hover:scale-105">
                <img src="https://ui-avatars.com/api/?name=${displayName}&background=151521&color=fff&size=128"
                     class="h-full w-full rounded-full object-cover border-2 border-dark-800 bg-dark-800"
                     alt="${displayName}">
            </div>
            <div class="absolute bottom-0 right-0 w-2.5 h-2.5 bg-green-500 border-2 border-dark-900 rounded-full"></div>
        </div>

    </div>
</header>

<%-- TOAST CONTAINER & SCRIPTS --%>
<div id="global-toast-container" class="fixed top-20 right-5 z-[100] flex flex-col gap-3 pointer-events-none"></div>

<script>
    // 1. Toggle Dropdown
    function toggleNotification() {
        const dropdown = document.getElementById('notificationDropdown');
        dropdown.classList.toggle('hidden');
    }

    // Đóng dropdown khi click ra ngoài
    document.addEventListener('click', function(event) {
        const container = document.getElementById('notificationContainer');
        const dropdown = document.getElementById('notificationDropdown');
        if (!container.contains(event.target) && !dropdown.classList.contains('hidden')) {
            dropdown.classList.add('hidden');
        }
    });

    // 2. Global Toast Logic
    function showGlobalToast(message, type) {
        const container = document.getElementById('global-toast-container');
        const toast = document.createElement('div');
        const bgColor = type === 'SUCCESS' ? 'bg-emerald-600' : 'bg-red-600';
        const iconClass = type === 'SUCCESS' ? 'fa-circle-check' : 'fa-circle-exclamation';

        toast.className = `pointer-events-auto min-w-[320px] max-w-md \${bgColor} text-white px-4 py-3 rounded-lg shadow-2xl flex items-center gap-3 transform transition-all duration-300 translate-x-10 opacity-0 border border-white/10`;
        toast.innerHTML = `
            <i class="fa-solid \${iconClass} text-xl"></i>
            <div class="flex-1">
                <h4 class="font-bold text-sm">\${type === 'SUCCESS' ? 'Thành công' : 'Thất bại'}</h4>
                <p class="text-sm opacity-90 leading-tight">\${message}</p>
            </div>
            <button onclick="this.parentElement.remove()" class="text-white/50 hover:text-white"><i class="fa-solid fa-xmark"></i></button>
        `;
        container.appendChild(toast);
        requestAnimationFrame(() => toast.classList.remove('translate-x-10', 'opacity-0'));
        setTimeout(() => {
            toast.classList.add('translate-x-10', 'opacity-0');
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }
</script>

<%-- BẮT SESSION MESSAGE TOAST --%>
<c:if test="${not empty sessionScope.msgSuccess}">
    <script>document.addEventListener('DOMContentLoaded', () => showGlobalToast('${sessionScope.msgSuccess}', 'SUCCESS'));</script>
    <c:remove var="msgSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.msgError}">
    <script>document.addEventListener('DOMContentLoaded', () => showGlobalToast('${sessionScope.msgError}', 'ERROR'));</script>
    <c:remove var="msgError" scope="session"/>
</c:if>