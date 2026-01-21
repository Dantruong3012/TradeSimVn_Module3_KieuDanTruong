<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // --- CHỐNG CACHE TRÌNH DUYỆT ---
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${sessionScope.account == null}">
    <c:redirect url="login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Bảng giá trực tuyến - TradeSim VN</title>
    <jsp:include page="components/head_css.jsp" />
    <script src="https://unpkg.com/lightweight-charts/dist/lightweight-charts.standalone.production.js"></script>

    <style>
        /* SCROLLBAR */
        ::-webkit-scrollbar { width: 4px; height: 4px; }
        ::-webkit-scrollbar-track { background: #151521; }
        ::-webkit-scrollbar-thumb { background: #3f3f50; border-radius: 2px; }

        /* MÀU SẮC CHỨNG KHOÁN */
        .color-ceil { color: #d946ef; } .color-floor { color: #06b6d4; }
        .color-ref { color: #eab308; } .color-up { color: #00c853; } .color-down { color: #ff3d00; }

        /* BẢNG GIÁ */
        .market-table th { position: sticky; top: 0; background-color: #1e1e2d; z-index: 10; font-size: 11px; padding: 10px 4px; border-bottom: 1px solid #2b2b40; border-right: 1px solid #2b2b40; color: #9ca3af; font-weight: 600; }
        .market-table td { padding: 8px 4px; border-bottom: 1px solid #2b2b40; border-right: 1px solid #2b2b40; font-family: 'Roboto Mono', monospace; font-size: 13px; font-weight: 500; }
        .market-table tr:hover td { background-color: #2b2b40; cursor: pointer; transition: background-color 0.1s; }

        /* UTILS */
        .modal-overlay { background-color: rgba(0, 0, 0, 0.85); backdrop-filter: blur(5px); }
        input[type=number]::-webkit-inner-spin-button, input[type=number]::-webkit-outer-spin-button { -webkit-appearance: none; margin: 0; }
        .input-error { border-color: #ff3d00 !important; color: #ff3d00 !important; }
        .text-error { color: #ff3d00; font-size: 10px; margin-top: 4px; display: block; font-weight: bold; }
        @keyframes marquee { 0% { transform: translateX(100%); } 100% { transform: translateX(-150%); } }
        .animate-marquee { animation: marquee 15s linear infinite; white-space: nowrap; }

        /* --- STYLE CHO NÚT LOẠI LỆNH --- */
        .btn-selected {
            border: 1px solid #00c853 !important;
            color: #00c853 !important;
            background-color: rgba(0, 200, 83, 0.15) !important;
            box-shadow: 0 0 8px rgba(0, 200, 83, 0.3);
        }
        .btn-unselected {
            border: 1px solid #374151;
            color: #9ca3af;
            background-color: #1f2937;
        }
    </style>
</head>

<body class="bg-dark-900 text-gray-300 font-sans overflow-hidden flex flex-col h-screen">

<%-- 1. HEADER --%>
<header class="h-14 bg-dark-800 border-b border-dark-700 flex items-center justify-between px-4 shrink-0">
    <div class="flex items-center gap-6">
        <a href="${pageContext.request.contextPath}/home" class="flex items-center gap-2">
            <span class="text-primary text-2xl font-bold">ll</span>
            <span class="text-white font-bold text-lg tracking-tight">TradeSim VN</span>
        </a>
        <nav class="hidden md:flex items-center gap-1">
            <a href="${pageContext.request.contextPath}/home" class="px-3 py-1.5 text-sm text-gray-400 hover:text-white hover:bg-dark-700 rounded transition">Tổng quan</a>
            <a href="${pageContext.request.contextPath}/trading" class="px-3 py-1.5 text-sm text-white bg-primary/20 font-bold rounded transition">Bảng giá</a>
        </nav>
    </div>
    <div class="flex items-center gap-4">
        <div class="text-right hidden sm:block">
            <div class="text-[10px] text-gray-400 uppercase font-bold">Sức mua</div>
            <div class="text-emerald-400 font-bold text-sm font-mono">
                <fmt:formatNumber value="${sessionScope.account.balance - sessionScope.account.lockedBalance}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="text-gray-400 hover:text-red-500 transition"><i class="fa-solid fa-power-off"></i></a>
    </div>
</header>

<%-- 2. TOOLBAR --%>
<div class="h-10 bg-dark-900 border-b border-dark-700 flex items-center px-4 gap-4 shrink-0">
    <button class="text-xs font-bold text-primary border-b-2 border-primary px-2 h-full">HOSE</button>
    <div class="w-px h-4 bg-dark-700 mx-2"></div>
    <div class="flex-1 overflow-hidden relative h-full flex items-center mask-image-linear">
        <div class="animate-marquee text-xs font-mono font-bold text-emerald-400/80">
            Hello <span class="text-white">${sessionScope.account.userDisplayName != null ? sessionScope.account.userDisplayName : sessionScope.account.userName}</span>! Are you ready for today? 🚀
        </div>
    </div>
    <div class="flex items-center gap-2 bg-dark-800 border border-dark-700 px-3 py-1 rounded">
        <i class="fa-regular fa-clock text-[10px] text-gray-500"></i>
        <span id="liveClock" class="text-xs font-mono font-bold text-white w-16 text-center">--:--:--</span>
    </div>
    <input type="text" id="stockSearch" class="bg-dark-800 border border-dark-700 text-white text-xs rounded pl-2 pr-2 py-1 outline-none uppercase w-40 focus:w-64 transition-all focus:border-primary" placeholder="Nhập mã CK...">
</div>

<%-- 3. TOAST NOTIFICATION --%>
<c:if test="${not empty sessionScope.msgError}">
    <div id="toast-error" class="fixed top-20 right-5 bg-red-600/90 text-white px-6 py-4 rounded-lg shadow-xl z-50 flex items-center gap-3 border border-red-500 animate-fade-in-down backdrop-blur-sm">
        <i class="fa-solid fa-circle-exclamation text-xl"></i>
        <div><h4 class="font-bold text-sm uppercase">Thông báo lỗi</h4><p class="text-sm">${sessionScope.msgError}</p></div>
        <c:remove var="msgError" scope="session"/>
    </div>
</c:if>
<c:if test="${not empty sessionScope.msgSuccess}">
    <div id="toast-success" class="fixed top-20 right-5 bg-emerald-600/90 text-white px-6 py-4 rounded-lg shadow-xl z-50 flex items-center gap-3 border border-emerald-500 animate-fade-in-down backdrop-blur-sm">
        <i class="fa-solid fa-circle-check text-xl"></i>
        <div><h4 class="font-bold text-sm uppercase">Thành công</h4><p class="text-sm">${sessionScope.msgSuccess}</p></div>
        <c:remove var="msgSuccess" scope="session"/>
    </div>
</c:if>

<%-- 4. MARKET TABLE --%>
<div class="flex-1 overflow-auto bg-dark-900 relative">
    <table class="w-full text-right border-collapse market-table">
        <thead>
        <tr>
            <th class="text-left sticky left-0 z-20 bg-dark-800 w-16">Mã</th>
            <th class="text-center color-ceil w-14">Trần</th>
            <th class="text-center color-floor w-14">Sàn</th>
            <th class="text-center color-ref w-14 border-r border-dark-700">TC</th>
            <th colspan="6" class="text-center text-gray-300 bg-dark-800/50 border-b border-dark-700">Bên Mua</th>
            <th colspan="3" class="text-center text-primary bg-dark-800/50 border-b border-dark-700">Khớp Lệnh</th>
            <th colspan="6" class="text-center text-gray-300 bg-dark-800/50 border-b border-dark-700">Bên Bán</th>
            <th class="text-center w-24">Tổng KL</th>
        </tr>
        <tr class="text-[10px]">
            <th class="sticky left-0 z-20 bg-dark-800"></th> <th colspan="3" class="border-r border-dark-700"></th>
            <th>Giá 3</th> <th>KL 3</th> <th>Giá 2</th> <th>KL 2</th> <th>Giá 1</th> <th>KL 1</th>
            <th class="text-white bg-dark-700/30">Giá</th> <th class="text-white bg-dark-700/30">KL</th> <th class="text-white bg-dark-700/30">+/-</th>
            <th>Giá 1</th> <th>KL 1</th> <th>Giá 2</th> <th>KL 2</th> <th>Giá 3</th> <th>KL 3</th>
            <th></th>
        </tr>
        </thead>
        <tbody class="text-white text-sm">
        <c:forEach var="s" items="${marketList}">
            <c:set var="colorClass" value="color-ref" />
            <c:choose>
                <c:when test="${s.currentPrice == s.ceilingPrice}"><c:set var="colorClass" value="color-ceil" /></c:when>
                <c:when test="${s.currentPrice == s.floorPrice}"><c:set var="colorClass" value="color-floor" /></c:when>
                <c:when test="${s.currentPrice > s.refPrice}"><c:set var="colorClass" value="color-up" /></c:when>
                <c:when test="${s.currentPrice < s.refPrice}"><c:set var="colorClass" value="color-down" /></c:when>
            </c:choose>

            <tr onclick="openStockModal('${s.symbol}', ${s.ceilingPrice}, ${s.floorPrice}, ${s.refPrice}, ${s.currentPrice}, ${s.changePercent}, ${s.volume != null ? s.volume : 0})">
                <td class="text-left font-bold ${colorClass} sticky left-0 bg-dark-900 border-r border-dark-700 pl-2">${s.symbol}</td>
                <td class="color-ceil text-center"><fmt:formatNumber value="${s.ceilingPrice}" minFractionDigits="2"/></td>
                <td class="color-floor text-center"><fmt:formatNumber value="${s.floorPrice}" minFractionDigits="2"/></td>
                <td class="color-ref border-r border-dark-700 text-center"><fmt:formatNumber value="${s.refPrice}" minFractionDigits="2"/></td>

                    <%-- [FIXED] XÓA * 10 Ở CỘT KHỐI LƯỢNG --%>
                <td class="color-ref text-center"><c:if test="${s.topBuy[2].price != null}"><fmt:formatNumber value="${s.topBuy[2].price}" minFractionDigits="2"/></c:if><c:if test="${s.topBuy[2].price == null}">-</c:if></td>
                <td class="text-gray-400 text-center"><c:if test="${s.topBuy[2].totalVolume > 0}"><fmt:formatNumber value="${s.topBuy[2].totalVolume}"/></c:if></td>
                <td class="color-ref text-center"><c:if test="${s.topBuy[1].price != null}"><fmt:formatNumber value="${s.topBuy[1].price}" minFractionDigits="2"/></c:if><c:if test="${s.topBuy[1].price == null}">-</c:if></td>
                <td class="text-gray-400 text-center"><c:if test="${s.topBuy[1].totalVolume > 0}"><fmt:formatNumber value="${s.topBuy[1].totalVolume}"/></c:if></td>
                <td class="color-ref font-bold text-center border-r border-dark-700"><c:if test="${s.topBuy[0].price != null}"><fmt:formatNumber value="${s.topBuy[0].price}" minFractionDigits="2"/></c:if><c:if test="${s.topBuy[0].price == null}">-</c:if></td>
                <td class="text-white font-bold border-r border-dark-700 text-center"><c:if test="${s.topBuy[0].totalVolume > 0}"><fmt:formatNumber value="${s.topBuy[0].totalVolume}"/></c:if></td>

                <td class="${colorClass} font-bold bg-dark-800/50 text-center"><fmt:formatNumber value="${s.currentPrice}" minFractionDigits="2"/></td>
                <td class="${colorClass} font-bold bg-dark-800/50 text-center"><fmt:formatNumber value="${s.volume != null ? s.volume : 0}" /></td>
                <td class="${colorClass} bg-dark-800/50 border-r border-dark-700 text-center"><c:if test="${s.changePercent > 0}">+</c:if><fmt:formatNumber value="${s.changePercent}" maxFractionDigits="2"/>%</td>

                    <%-- [FIXED] XÓA * 10 Ở CỘT KHỐI LƯỢNG --%>
                <td class="color-down font-bold text-center"><c:if test="${s.topSell[0].price != null}"><fmt:formatNumber value="${s.topSell[0].price}" minFractionDigits="2"/></c:if><c:if test="${s.topSell[0].price == null}">-</c:if></td>
                <td class="text-white font-bold text-center border-r border-dark-700"><c:if test="${s.topSell[0].totalVolume > 0}"><fmt:formatNumber value="${s.topSell[0].totalVolume}"/></c:if></td>
                <td class="color-down text-center"><c:if test="${s.topSell[1].price != null}"><fmt:formatNumber value="${s.topSell[1].price}" minFractionDigits="2"/></c:if><c:if test="${s.topSell[1].price == null}">-</c:if></td>
                <td class="text-gray-400 text-center border-r border-dark-700"><c:if test="${s.topSell[1].totalVolume > 0}"><fmt:formatNumber value="${s.topSell[1].totalVolume}"/></c:if></td>
                <td class="color-down text-center"><c:if test="${s.topSell[2].price != null}"><fmt:formatNumber value="${s.topSell[2].price}" minFractionDigits="2"/></c:if><c:if test="${s.topSell[2].price == null}">-</c:if></td>
                <td class="text-gray-400 text-center"><c:if test="${s.topSell[2].totalVolume > 0}"><fmt:formatNumber value="${s.topSell[2].totalVolume}"/></c:if></td>

                <td class="text-center pr-2"><fmt:formatNumber value="${s.volume != null ? s.volume : 0}" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%-- 5. MODAL CHI TIẾT (FORM ĐẶT LỆNH) --%>
<div id="stockModal" class="hidden fixed inset-0 z-50 flex items-center justify-center modal-overlay p-4">
    <div class="bg-dark-900 w-full max-w-[1400px] h-[90vh] rounded-lg border border-dark-700 flex flex-col modal-content overflow-hidden animate-fade-in-up">

        <%-- HEADER MODAL --%>
        <div class="h-14 border-b border-dark-700 flex justify-between items-center px-5 bg-dark-800 shrink-0">
            <div class="flex items-center gap-8">
                <div class="flex items-center gap-3">
                    <span class="text-3xl font-extrabold text-white tracking-tight" id="mSymbol">HPG</span>
                    <div class="flex flex-col">
                        <div class="flex items-center gap-2">
                            <span class="text-2xl font-bold color-up font-mono leading-none" id="mPrice">26.15</span>
                            <span class="text-sm font-bold color-up bg-success/10 px-1.5 rounded" id="mChange">+0.15 (2.1%)</span>
                        </div>
                    </div>
                </div>
                <div class="hidden md:flex gap-4 text-xs font-mono font-bold ml-auto bg-dark-900 p-2 rounded border border-dark-700">
                    <span class="color-ceil">Trần: <span id="mCeil">27.85</span></span> |
                    <span class="color-ref">TC: <span id="mRef">26.00</span></span> |
                    <span class="color-floor">Sàn: <span id="mFloor">24.15</span></span>
                </div>
            </div>
            <button onclick="closeStockModal()" class="text-gray-400 hover:text-white w-8 h-8 flex items-center justify-center hover:bg-dark-700 rounded-full"><i class="fa-solid fa-xmark text-xl"></i></button>
        </div>

        <div class="flex-1 grid grid-cols-12 overflow-hidden bg-dark-900">
            <%-- CHART --%>
            <div class="col-span-12 lg:col-span-7 flex flex-col border-r border-dark-700 relative">
                <div class="h-9 flex items-center gap-1 px-2 border-b border-dark-700 bg-dark-800/50">
                    <button class="text-[11px] font-bold text-white bg-dark-700 px-3 py-1 rounded">5M</button>
                    <span class="text-[11px] text-gray-500 ml-auto"><i class="fa-solid fa-chart-line mr-1"></i>Area Chart</span>
                </div>
                <div id="tv-chart-container" class="flex-1 w-full h-full relative"></div>
            </div>

            <%-- SỔ LỆNH --%>
            <div class="col-span-12 lg:col-span-2 flex flex-col border-r border-dark-700 bg-dark-800/30">
                <div class="h-9 flex items-center justify-center border-b border-dark-700 text-xs font-bold text-gray-400 uppercase bg-dark-800">Sổ lệnh</div>
                <div class="flex-1 overflow-y-auto font-mono text-[11px]">
                    <div class="grid grid-cols-3 text-center py-1.5 text-gray-500 border-b border-dark-700 bg-dark-900"><div class="text-left pl-2">MUA</div> <div>GIÁ</div> <div class="text-right pr-2">BÁN</div></div>
                    <%-- Dữ liệu giả lập sổ lệnh --%>
                    <div class="space-y-0.5 pt-1">
                        <div class="flex justify-between items-center px-2 py-1 relative group hover:bg-dark-700/50 cursor-pointer"><span class="w-1/3"></span><span class="w-1/3 text-center color-down font-bold">26.30</span><span class="w-1/3 text-right text-gray-300">5,000</span><div class="absolute right-0 top-0 bottom-0 bg-red-500/15 w-[15%]"></div></div>
                        <div class="flex justify-between items-center px-2 py-1 relative group hover:bg-dark-700/50 cursor-pointer"><span class="w-1/3"></span><span class="w-1/3 text-center color-down font-bold">26.25</span><span class="w-1/3 text-right text-gray-300">22,100</span><div class="absolute right-0 top-0 bottom-0 bg-red-500/15 w-[60%]"></div></div>
                    </div>
                    <div class="my-1 py-2 bg-dark-800 border-y border-dark-700 flex justify-between items-center px-3"><span class="color-up font-bold text-lg" id="matchPriceDisplay">26.15</span><span class="text-xs text-gray-400">KL: 500</span></div>
                    <div class="space-y-0.5 pb-1">
                        <div class="flex justify-between items-center px-2 py-1 relative group hover:bg-dark-700/50 cursor-pointer"><span class="w-1/3 text-left text-white">89,000</span><span class="w-1/3 text-center color-up font-bold">26.10</span><span class="w-1/3"></span><div class="absolute left-0 top-0 bottom-0 bg-green-500/25 w-[85%]"></div></div>
                    </div>
                </div>
            </div>

            <%-- Cột Phải: FORM ĐẶT LỆNH --%>
            <div class="col-span-12 lg:col-span-3 bg-dark-800 flex flex-col relative">
                <div class="grid grid-cols-2 p-1 bg-dark-900 m-3 rounded-lg border border-dark-700">
                    <button id="tabBuy" class="py-2 text-sm font-bold rounded-md bg-success text-white shadow-md transition-all">MUA</button>
                    <button id="tabSell" class="py-2 text-sm font-bold rounded-md text-gray-400 hover:text-white transition-all">BÁN</button>
                </div>

                <form id="orderForm" action="order-place" method="POST" class="px-4 flex flex-col gap-4">

                    <%-- [BACKEND] CÁC BIẾN QUAN TRỌNG GỬI ĐI --%>
                    <input type="hidden" id="hiddenSymbol" name="symbol" value="">

                    <input type="hidden" id="hiddenOrderType" name="orderType" value="LO">

                    <input type="hidden" id="hiddenSide" name="side" value="BUY">

                    <div class="flex justify-between items-center text-xs text-gray-400 border-b border-dark-700 pb-2">
                        <span>Sức mua:</span>
                        <span class="text-white font-mono font-bold"><fmt:formatNumber value="${sessionScope.account.balance - sessionScope.account.lockedBalance}" /> ₫</span>
                    </div>

                    <%-- CHỌN LOẠI LỆNH (LO/MP) --%>
                    <div class="grid grid-cols-2 gap-2">
                        <button type="button" id="btnLO" onclick="selectOrderType('LO')" class="text-xs font-bold py-2 rounded transition btn-selected">LO</button>
                        <button type="button" id="btnMP" onclick="selectOrderType('MP')" class="text-xs font-bold py-2 rounded transition btn-unselected">MP</button>
                    </div>

                    <div>
                        <label class="block text-[10px] text-gray-400 font-bold uppercase mb-1">Khối lượng</label>
                        <input type="number" id="inputVol" name="quantity" min="100" step="100" class="w-full bg-dark-900 border border-dark-600 rounded-lg p-3 text-white font-mono font-bold text-sm focus:border-primary outline-none transition" placeholder="Bội số 100" oninput="validateInput()">
                        <span id="errVol" class="text-error hidden">KL phải là bội số của 100</span>
                    </div>

                    <div>
                        <label class="block text-[10px] text-gray-400 font-bold uppercase mb-1">Giá đặt (x1000)</label>
                        <div class="flex items-stretch border border-dark-600 rounded-lg overflow-hidden focus-within:border-primary">
                            <button type="button" onclick="adjustPrice(-0.05)" class="w-10 bg-dark-900 hover:bg-dark-700 text-white border-r border-dark-600 font-bold">-</button>

                            <input type="number" id="inputPrice" name="price" step="0.05" class="flex-1 bg-transparent text-center text-white font-mono font-bold text-sm focus:border-primary outline-none p-2" value="0.00" oninput="validateInput()">

                            <button type="button" onclick="adjustPrice(0.05)" class="w-10 bg-dark-900 hover:bg-dark-700 text-white border-l border-dark-600 font-bold">+</button>
                        </div>
                        <span id="errPrice" class="text-error hidden">Giá ngoài biên độ!</span>
                    </div>

                    <div class="flex justify-between items-center bg-dark-900 p-3 rounded-lg border border-dark-700/50 mt-auto">
                        <span class="text-xs text-gray-500 font-bold uppercase">Tổng giá trị</span>
                        <span class="text-success font-mono font-bold text-base" id="totalValue">0 ₫</span>
                    </div>

                    <button type="button" id="btnSubmitOrder" onclick="submitOrder()" class="w-full bg-success hover:bg-green-600 text-white font-bold py-3.5 rounded-lg shadow-lg shadow-green-900/40 transition transform active:scale-95 text-sm uppercase tracking-wider mt-2 mb-4">
                        Xác nhận Mua
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="components/scripts.jsp" />
<script>
    let chart, areaSeries;
    let currentCeil, currentFloor, currentRef, currentMatch;

    // --- CLOCK ---
    function updateClock() { document.getElementById('liveClock').innerText = new Date().toLocaleTimeString('vi-VN'); }
    setInterval(updateClock, 1000); updateClock();

    // --- ALERT AUTO HIDE ---
    setTimeout(function() {
        const err = document.getElementById('toast-error');
        const succ = document.getElementById('toast-success');
        if(err) { err.style.opacity = '0'; setTimeout(()=>err.remove(), 500); }
        if(succ) { succ.style.opacity = '0'; setTimeout(()=>succ.remove(), 500); }
    }, 4000);

    // --- GLOBAL VARS ---
    const tabBuy = document.getElementById('tabBuy');
    const tabSell = document.getElementById('tabSell');
    const btnSubmit = document.getElementById('btnSubmitOrder');
    const hiddenSide = document.getElementById('hiddenSide');
    const hiddenOrderType = document.getElementById('hiddenOrderType');
    const btnLO = document.getElementById('btnLO');
    const btnMP = document.getElementById('btnMP');
    const inputPrice = document.getElementById('inputPrice');

    // --- LOGIC TAB MUA / BÁN (SỬA LẠI: Cập nhật giá MP khi chuyển Tab) ---
    tabBuy.addEventListener('click', e => { e.preventDefault(); setTab('BUY'); });
    tabSell.addEventListener('click', e => { e.preventDefault(); setTab('SELL'); });

    function setTab(side) {
        hiddenSide.value = side;

        // UI Đổi màu nút
        if (side === 'BUY') {
            tabBuy.className = "py-2 text-sm font-bold rounded-md bg-success text-white shadow-md transition-all w-full";
            tabSell.className = "py-2 text-sm font-bold rounded-md text-gray-400 hover:text-white transition-all w-full";
            btnSubmit.className = "w-full bg-success hover:bg-green-600 text-white font-bold py-3.5 rounded-lg shadow-lg shadow-green-900/40 transition transform active:scale-95 text-sm uppercase tracking-wider mt-2 mb-4";
            btnSubmit.innerText = "XÁC NHẬN MUA";
        } else {
            tabSell.className = "py-2 text-sm font-bold rounded-md bg-red-600 text-white shadow-md transition-all w-full";
            tabBuy.className = "py-2 text-sm font-bold rounded-md text-gray-400 hover:text-white transition-all w-full";
            btnSubmit.className = "w-full bg-red-600 hover:bg-red-700 text-white font-bold py-3.5 rounded-lg shadow-lg shadow-red-900/40 transition transform active:scale-95 text-sm uppercase tracking-wider mt-2 mb-4";
            btnSubmit.innerText = "XÁC NHẬN BÁN";
        }

        // [LOGIC MỚI] Nếu đang ở chế độ MP, tự động cập nhật giá (Mua -> Trần, Bán -> Sàn)
        if (hiddenOrderType.value === 'MP') {
            if (side === 'BUY') {
                inputPrice.value = currentCeil.toFixed(2);
            } else {
                inputPrice.value = currentFloor.toFixed(2);
            }
            validateInput(); // Tính lại tổng tiền
        }
    }

    // --- LOGIC CHỌN LOẠI LỆNH (SỬA LẠI: Set giá Trần/Sàn khi chọn MP) ---
    function selectOrderType(type) {
        hiddenOrderType.value = type;

        if (type === 'LO') {
            // Style Active cho LO
            btnLO.classList.remove('btn-unselected'); btnLO.classList.add('btn-selected');
            btnMP.classList.remove('btn-selected'); btnMP.classList.add('btn-unselected');

            // Mở khóa nhập giá, set về giá khớp lệnh hiện tại để dễ sửa
            inputPrice.disabled = false;
            inputPrice.classList.remove('opacity-50', 'cursor-not-allowed');
            if(currentMatch) inputPrice.value = currentMatch.toFixed(2);

        } else {
            // Style Active cho MP
            btnMP.classList.remove('btn-unselected'); btnMP.classList.add('btn-selected');
            btnLO.classList.remove('btn-selected'); btnLO.classList.add('btn-unselected');

            // Khóa nhập giá
            inputPrice.disabled = true;
            inputPrice.classList.add('opacity-50', 'cursor-not-allowed');

            // [LOGIC MỚI] Tự động điền giá Trần/Sàn tùy theo Mua/Bán
            if (hiddenSide.value === 'BUY') {
                inputPrice.value = currentCeil.toFixed(2); // Mua MP -> Lấy giá Trần
            } else {
                inputPrice.value = currentFloor.toFixed(2); // Bán MP -> Lấy giá Sàn
            }
        }
        validateInput();
    }

    // --- VALIDATION ---
    function validateInput() {
        const volInput = document.getElementById('inputVol');
        const errVol = document.getElementById('errVol');
        const errPrice = document.getElementById('errPrice');
        const totalDisplay = document.getElementById('totalValue');

        let vol = parseInt(volInput.value) || 0;
        let price = parseFloat(inputPrice.value) || 0;
        let type = hiddenOrderType.value;
        let isValid = true;

        if ((vol <= 0 || vol % 100 !== 0) && volInput.value !== "") {
            volInput.classList.add('input-error'); errVol.classList.remove('hidden'); isValid = false;
        } else {
            volInput.classList.remove('input-error'); errVol.classList.add('hidden');
        }

        if (type === 'LO') {
            if (price < currentFloor || price > currentCeil) {
                inputPrice.parentElement.classList.add('input-error'); errPrice.classList.remove('hidden'); isValid = false;
            } else {
                inputPrice.parentElement.classList.remove('input-error'); errPrice.classList.add('hidden');
            }
        } else {
            inputPrice.parentElement.classList.remove('input-error'); errPrice.classList.add('hidden');
        }

        if (isValid && vol > 0 && price > 0) {
            let total = vol * price * 1000;
            totalDisplay.innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(total);
        } else {
            totalDisplay.innerText = "0 ₫";
        }
        return isValid;
    }

    function adjustPrice(step) {
        if(inputPrice.disabled) return;
        let val = parseFloat(inputPrice.value) || 0;
        inputPrice.value = Math.round((val + step) * 100) / 100;
        validateInput();
    }

    function submitOrder() {
        if (validateInput()) {
            document.getElementById('orderForm').submit();
        } else {
            alert("Vui lòng kiểm tra lại thông tin!");
        }
    }

    // --- CHART & MODAL ---
    function initChart() { /* ... Giữ nguyên ... */
        const container = document.getElementById('tv-chart-container'); if (!container) return; container.innerHTML = '';
        chart = LightweightCharts.createChart(container, { width: container.clientWidth, height: container.clientHeight, layout: { background: { color: '#151521' }, textColor: '#9ca3af' }, grid: { vertLines: { color: '#2b2b40' }, horzLines: { color: '#2b2b40' } }, timeScale: { timeVisible: true, borderColor: '#2b2b40' }, rightPriceScale: { borderColor: '#2b2b40' } });
        areaSeries = chart.addAreaSeries({ topColor: 'rgba(0, 200, 83, 0.56)', bottomColor: 'rgba(0, 200, 83, 0.04)', lineColor: 'rgba(0, 200, 83, 1)', lineWidth: 2 });
        const data = []; let p = currentMatch; let time = Math.floor(Date.now() / 1000) - (100 * 60);
        for (let i = 0; i < 100; i++) { p = p + (Math.random() - 0.5) * 0.1; data.push({ time: time + i * 60, value: p }); }
        areaSeries.setData(data);
        new ResizeObserver(entries => { if (entries.length && entries[0].target === container) chart.applyOptions({ height: entries[0].contentRect.height, width: entries[0].contentRect.width }); }).observe(container);
    }

    function openStockModal(symbol, ceil, floor, ref, price, change, vol) {
        currentCeil = ceil; currentFloor = floor; currentRef = ref; currentMatch = price;
        document.getElementById('mSymbol').innerText = symbol; document.getElementById('mCeil').innerText = ceil.toFixed(2); document.getElementById('mFloor').innerText = floor.toFixed(2); document.getElementById('mRef').innerText = ref.toFixed(2); document.getElementById('mPrice').innerText = price.toFixed(2); document.getElementById('mChange').innerText = (change > 0 ? "+" : "") + change + "%"; document.getElementById('matchPriceDisplay').innerText = price.toFixed(2);

        // Reset Form
        document.getElementById('hiddenSymbol').value = symbol;
        document.getElementById('inputVol').value = "";
        document.getElementById('totalValue').innerText = "0 ₫";

        // Reset Mặc định
        setTab('BUY');
        selectOrderType('LO');

        document.getElementById('stockModal').classList.remove('hidden'); requestAnimationFrame(() => { initChart(); });
    }

    function closeStockModal() { document.getElementById('stockModal').classList.add('hidden'); if(chart) { chart.remove(); chart = null; } }
    document.getElementById('stockModal').addEventListener('click', function(e) { if (e.target === this) closeStockModal(); });
</script>
</body>
</html>