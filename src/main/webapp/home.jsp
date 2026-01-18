<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // --- CHỐNG CACHE TRÌNH DUYỆT (FIX LỖI BACK SAU KHI LOGOUT) ---
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- ... Các code khác giữ nguyên ... --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${sessionScope.account == null}">
    <c:redirect url="login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Tổng quan tài sản - TradeSim VN</title>

    <%-- 1. LOAD FONT & CSS --%>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            darkMode: 'class',
            theme: {
                extend: {
                    fontFamily: { sans: ['Inter', 'sans-serif'] }, // Áp dụng font Inter toàn web
                    colors: {
                        dark: { 900: '#151521', 800: '#1e1e2d', 700: '#2b2b40' },
                        primary: { DEFAULT: '#a855f7', hover: '#9333ea' },
                        success: '#00c853', danger: '#ff3d00',
                    }
                }
            }
        }
    </script>

    <%-- 2. NHẬN BIẾN TỪ CONTROLLER --%>
    <c:set var="chartNav" value="${totalNav != null ? totalNav : 0}" />
    <c:set var="chartStock" value="${stockValue != null ? stockValue : 0}" />
    <c:set var="chartCash" value="${cashValue != null ? cashValue : 0}" />
    <c:set var="chartInitial" value="${initialCapital != null ? initialCapital : 0}" />

    <c:set var="valStockPercent" value="${stockPercent != null ? stockPercent : 0}" />
    <c:set var="valCashPercent" value="${cashPercent != null ? cashPercent : 100}" />

    <style>
        body { background-color: #151521; color: #e0e0e0; font-family: 'Inter', sans-serif; }

        /* Hiệu ứng biểu đồ */
        .chart-group:hover .chart-segment { opacity: 0.3; transition: opacity 0.3s ease; }
        .chart-group .chart-segment:hover { opacity: 1; stroke-width: 14; filter: drop-shadow(0 0 8px rgba(255, 255, 255, 0.3)); cursor: pointer; }

        /* TOOLTIP ĐẸP & CHUẨN */
        #chart-tooltip {
            position: fixed;
            background: rgba(255, 255, 255, 0.98); /* Nền trắng đục */
            border: 1px solid #e5e7eb;
            color: #1f2937; /* Chữ xám đậm */
            padding: 12px 16px;
            border-radius: 10px;
            font-size: 13px;
            pointer-events: none;
            z-index: 9999;
            opacity: 0;
            transition: opacity 0.15s ease-out;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
            min-width: 160px;
            backdrop-filter: blur(4px);
        }
        #chart-tooltip .label-title {
            display: block; font-weight: 800; font-size: 15px; margin-bottom: 6px; border-bottom: 2px solid #f3f4f6; padding-bottom: 4px;
        }
        #chart-tooltip .label-row { display: flex; justify-content: space-between; align-items: center; margin-top: 4px; }
        #chart-tooltip .label-key { font-weight: 500; color: #6b7280; font-size: 12px; }
        #chart-tooltip .label-val { font-weight: 700; font-family: 'Inter', monospace; font-size: 13px; }
    </style>
</head>

<body class="bg-dark-900 text-gray-300 font-sans overflow-hidden">

<div class="flex h-screen">
    <jsp:include page="components/sidebar.jsp" />

    <div class="flex-1 flex flex-col min-w-0 overflow-hidden relative">
        <jsp:include page="components/header.jsp" />

        <main class="flex-1 overflow-y-auto p-4 sm:p-6 lg:p-8 scroll-smooth">

            <%-- SECTION 1: 3 THẺ CHỈ SỐ --%>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <div class="bg-dark-800 p-6 rounded-xl border border-dark-700 relative overflow-hidden group">
                    <div class="absolute right-0 top-0 p-4 opacity-5"><i class="fa-solid fa-vault text-6xl text-white"></i></div>
                    <p class="text-gray-400 text-sm font-medium mb-1">Tổng tài sản ròng</p>
                    <h3 class="text-3xl font-bold text-white mb-2 tracking-tight">
                        <fmt:formatNumber value="${chartNav}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </h3>
                    <div class="inline-flex items-center text-xs font-bold ${totalPerformance >= 0 ? 'text-success bg-success/10' : 'text-danger bg-danger/10'} px-2 py-1 rounded">
                        <span><c:if test="${totalPerformance > 0}">+</c:if><fmt:formatNumber value="${totalPerformance}" maxFractionDigits="2"/>% hiệu quả</span>
                    </div>
                </div>

                <div class="bg-dark-800 p-6 rounded-xl border border-dark-700 relative overflow-hidden">
                    <div class="absolute right-0 top-0 p-4 opacity-5"><i class="fa-solid fa-chart-line text-6xl ${totalGainLoss >= 0 ? 'text-success' : 'text-danger'}"></i></div>
                    <p class="text-gray-400 text-sm font-medium mb-1">Tổng Lãi/Lỗ tạm tính</p>
                    <h3 class="text-3xl font-bold ${totalGainLoss >= 0 ? 'text-success' : 'text-danger'} mb-2 tracking-tight">
                        <c:if test="${totalGainLoss > 0}">+</c:if>
                        <fmt:formatNumber value="${totalGainLoss}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </h3>
                    <div class="text-xs text-gray-400">
                        Vốn gốc: <span class="text-white font-bold"><fmt:formatNumber value="${chartInitial}" type="currency" currencySymbol="" maxFractionDigits="0"/></span>
                    </div>
                </div>

                <div class="bg-dark-800 p-6 rounded-xl border border-dark-700 relative overflow-hidden">
                    <div class="absolute right-0 top-0 p-4 opacity-5"><i class="fa-solid fa-bolt text-6xl text-primary"></i></div>
                    <p class="text-gray-400 text-sm font-medium mb-1">Sức mua tối đa</p>
                    <h3 class="text-3xl font-bold text-white mb-2 tracking-tight">
                        <fmt:formatNumber value="${buyingPower}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </h3>
                    <div class="text-xs text-primary font-medium">Margin tỷ lệ: <fmt:formatNumber value="${marginRatio}" maxFractionDigits="0"/>%</div>
                </div>
            </div>

                <%-- SECTION 2: BẢNG + BIỂU ĐỒ (CẤU TRÚC FULL) --%>
                <div class="grid grid-cols-1 xl:grid-cols-3 gap-6 mb-8 items-start">

                    <div class="xl:col-span-2 bg-dark-800 rounded-xl border border-dark-700 flex flex-col h-full overflow-hidden">
                        <div class="p-5 border-b border-dark-700 flex justify-between items-center">
                            <h3 class="text-white font-bold flex items-center gap-2 text-base">
                                <span class="w-1.5 h-6 bg-primary rounded-full"></span> Danh mục nắm giữ
                            </h3>
                            <button class="text-xs bg-dark-700 hover:bg-dark-600 text-white px-3 py-1.5 rounded-lg transition font-medium">
                                <i class="fa-solid fa-filter mr-1"></i> Lọc
                            </button>
                        </div>

                        <div class="overflow-x-auto">
                            <table class="w-full text-sm text-left">
                                <thead class="text-xs text-gray-400 uppercase bg-dark-900/50">
                                <tr>
                                    <th class="px-6 py-3 font-semibold whitespace-nowrap">Mã CK</th>
                                    <th class="px-6 py-3 text-right font-semibold whitespace-nowrap">KL</th>
                                    <th class="px-6 py-3 text-right font-semibold whitespace-nowrap">Giá vốn</th>
                                    <th class="px-6 py-3 text-right font-semibold whitespace-nowrap">Thị trường</th>
                                    <th class="px-6 py-3 text-right font-semibold whitespace-nowrap">Lãi/Lỗ</th>
                                </tr>
                                </thead>
                                <tbody class="divide-y divide-dark-700">
                                <c:forEach var="item" items="${portfolioList}">
                                    <tr class="hover:bg-dark-700/30 transition group cursor-pointer">
                                        <td class="px-6 py-4">
                                            <div class="font-bold text-white group-hover:text-primary transition text-base">${item.symbol}</div>
                                            <div class="text-[11px] text-gray-500 truncate max-w-[150px] font-medium">
                                                    ${item.companyName != null ? item.companyName : item.symbol}
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 text-right text-white font-medium">
                                            <fmt:formatNumber value="${item.quantity}" />
                                        </td>
                                        <td class="px-6 py-4 text-right text-gray-400">
                                            <fmt:formatNumber value="${item.avgPrice}" />
                                        </td>
                                        <td class="px-6 py-4 text-right font-bold ${item.currentPrice > item.avgPrice ? 'text-success' : (item.currentPrice < item.avgPrice ? 'text-danger' : 'text-yellow-500')}">
                                            <fmt:formatNumber value="${item.currentPrice}" />
                                        </td>
                                        <td class="px-6 py-4 text-right">
                                            <span class="font-bold ${item.gainLoss >= 0 ? 'text-success' : 'text-danger'} block">
                                                <c:if test="${item.gainLoss > 0}">+</c:if><fmt:formatNumber value="${item.gainLoss}" maxFractionDigits="0"/>
                                            </span>
                                            <span class="inline-block text-[10px] font-bold mt-1 px-1.5 py-0.5 rounded ${item.gainlossPercent >= 0 ? 'text-success bg-success/10' : 'text-danger bg-danger/10'}">
                                                <c:if test="${item.gainlossPercent > 0}">+</c:if><fmt:formatNumber value="${item.gainlossPercent}" maxFractionDigits="2"/>%
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty portfolioList}">
                                    <tr>
                                        <td colspan="5" class="text-center py-16 text-gray-500">
                                            <i class="fa-solid fa-box-open text-3xl mb-3 block opacity-40"></i>
                                            <span class="text-sm block">Bạn chưa nắm giữ cổ phiếu nào.</span>
                                        </td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="xl:col-span-1 bg-dark-800 rounded-xl border border-dark-700 p-5 flex flex-col items-center relative h-full">

                        <h3 class="text-white font-bold w-full mb-2 text-base">Phân bổ tài sản</h3>

                        <c:set var="C" value="251.2" />
                        <c:set var="cashStroke" value="${(valCashPercent / 100) * C}" />
                        <c:set var="stockStroke" value="${(valStockPercent / 100) * C}" />

                        <div class="relative w-56 h-56 group mt-4">
                            <svg width="224" height="224" viewBox="0 0 100 100" class="transform -rotate-90 donut-svg">
                                <circle cx="50" cy="50" r="40" stroke="#2b2b40" stroke-width="10" fill="transparent" />
                                <g class="chart-group">
                                    <circle cx="50" cy="50" r="40" stroke="#10b981" stroke-width="10" fill="transparent"
                                            stroke-dasharray="${cashStroke} ${C}" stroke-dashoffset="0"
                                            class="chart-segment transition-all duration-500 ease-out"
                                            data-name="Tiền mặt"
                                            data-display-amount="<fmt:formatNumber value='${chartCash}' type='currency' currencySymbol='₫' maxFractionDigits='0'/>"
                                            data-display-percent="<fmt:formatNumber value='${valCashPercent}' maxFractionDigits='1'/>%"
                                            data-color="text-emerald-500" />

                                    <circle cx="50" cy="50" r="40" stroke="#a855f7" stroke-width="10" fill="transparent"
                                            stroke-dasharray="${stockStroke} ${C}" stroke-dashoffset="-${cashStroke}"
                                            class="chart-segment transition-all duration-500 ease-out"
                                            data-name="Cổ phiếu"
                                            data-display-amount="<fmt:formatNumber value='${chartStock}' type='currency' currencySymbol='₫' maxFractionDigits='0'/>"
                                            data-display-percent="<fmt:formatNumber value='${valStockPercent}' maxFractionDigits='1'/>%"
                                            data-color="text-primary" />
                                </g>
                            </svg>
                            <div class="absolute inset-0 m-auto w-36 h-36 bg-dark-800 rounded-full flex flex-col items-center justify-center pointer-events-none">
                                <span class="text-gray-400 text-xs font-medium mb-1">NAV</span>
                                <span class="text-xl font-extrabold text-white tracking-tight">
                                <fmt:formatNumber value="${chartNav}" type="currency" currencySymbol="" maxFractionDigits="0"/>
                            </span>
                                <span class="text-[10px] text-gray-500 mt-1 font-bold">VND</span>
                            </div>
                        </div>

                        <div class="w-full space-y-4 px-4 mt-8">
                            <div class="flex justify-between items-center text-sm border-b border-dark-700 pb-2">
                                <div class="flex items-center gap-3">
                                    <span class="w-3 h-3 rounded-full bg-emerald-500 shadow-[0_0_10px_rgba(16,185,129,0.4)]"></span>
                                    <span class="text-gray-300 font-medium">Tiền mặt</span>
                                </div>
                                <div class="text-right flex flex-col items-end">
                                <span class="text-white font-bold text-sm tracking-wide">
                                    <fmt:formatNumber value="${chartCash}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </span>
                                    <span class="text-emerald-500 text-xs font-bold bg-emerald-500/10 px-1.5 rounded mt-0.5">
                                    <fmt:formatNumber value="${valCashPercent}" maxFractionDigits="1"/>%
                                </span>
                                </div>
                            </div>

                            <div class="flex justify-between items-center text-sm pt-1">
                                <div class="flex items-center gap-3">
                                    <span class="w-3 h-3 rounded-full bg-primary shadow-[0_0_10px_rgba(168,85,247,0.4)]"></span>
                                    <span class="text-gray-300 font-medium">Cổ phiếu</span>
                                </div>
                                <div class="text-right flex flex-col items-end">
                                <span class="text-white font-bold text-sm tracking-wide">
                                    <fmt:formatNumber value="${chartStock}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </span>
                                    <span class="text-primary text-xs font-bold bg-primary/10 px-1.5 rounded mt-0.5">
                                    <fmt:formatNumber value="${valStockPercent}" maxFractionDigits="1"/>%
                                </span>
                                </div>
                            </div>
                        </div>

                        <div id="chart-tooltip">
                            <span class="label-title" id="tt-name"></span>
                            <div class="label-row">
                                <span class="label-key">Giá trị:</span>
                                <span class="label-val" id="tt-amount"></span>
                            </div>
                            <div class="label-row">
                                <span class="label-key">Tỷ trọng:</span>
                                <span class="label-val" id="tt-percent"></span>
                            </div>
                        </div>
                    </div>

                </div>

            <div class="mt-auto"><jsp:include page="components/footer.jsp" /></div>
        </main>
    </div>
</div>

<jsp:include page="components/scripts.jsp" />

<%-- SCRIPT TOOLTIP (Cực đơn giản - Không tính toán JS nữa) --%>
<script>
    const segments = document.querySelectorAll('.chart-segment');
    const tooltip = document.getElementById('chart-tooltip');
    const ttName = document.getElementById('tt-name');
    const ttAmount = document.getElementById('tt-amount');
    const ttPercent = document.getElementById('tt-percent');

    segments.forEach(segment => {
        segment.addEventListener('mousemove', (e) => {
            // Lấy dữ liệu đã format sẵn từ JSP
            const name = segment.getAttribute('data-name');
            const displayAmount = segment.getAttribute('data-display-amount');
            const displayPercent = segment.getAttribute('data-display-percent');
            const colorClass = segment.getAttribute('data-color');

            ttName.textContent = name;
            ttName.className = `label-title ${colorClass}`;
            ttAmount.textContent = displayAmount;
            ttPercent.textContent = displayPercent;

            tooltip.style.opacity = '1';
            tooltip.style.left = (e.clientX + 15) + 'px';
            tooltip.style.top = (e.clientY - 20) + 'px';
        });

        segment.addEventListener('mouseleave', () => {
            tooltip.style.opacity = '0';
        });
    });
</script>

</body>
</html>