<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <title>Sổ lệnh - TradeSim VN</title>
  <jsp:include page="components/head_css.jsp" />
</head>
<body class="bg-dark-900 text-gray-300 font-sans overflow-hidden">

<div class="flex h-screen">
  <%-- SIDEBAR --%>
  <jsp:include page="components/sidebar.jsp" />

  <%-- MAIN CONTENT --%>
  <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
    <%-- HEADER --%>
    <jsp:include page="components/header.jsp" />

    <%-- SCROLLABLE AREA --%>
    <main class="flex-1 overflow-y-auto p-4 sm:p-6 scrollbar-thin scrollbar-thumb-dark-700">

      <div class="max-w-6xl mx-auto">
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
          <div>
            <h1 class="text-2xl font-bold text-white">Sổ lệnh</h1>
            <p class="text-sm text-gray-500 mt-1">Quản lý các lệnh đặt mua/bán của bạn</p>
          </div>

          <%-- Backend Filter Form --%>
          <form action="order-book" method="GET" class="flex bg-dark-800 p-1 rounded-lg border border-dark-700">
            <button type="submit" name="status" value="ALL" class="px-4 py-1.5 text-xs font-bold rounded-md transition ${param.status == null || param.status == 'ALL' ? 'bg-primary text-white shadow' : 'text-gray-400 hover:text-white'}">Tất cả</button>
            <button type="submit" name="status" value="PENDING" class="px-4 py-1.5 text-xs font-bold rounded-md transition ${param.status == 'PENDING' ? 'bg-primary text-white shadow' : 'text-gray-400 hover:text-white'}">Chờ khớp</button>
            <button type="submit" name="status" value="MATCHED" class="px-4 py-1.5 text-xs font-bold rounded-md transition ${param.status == 'MATCHED' ? 'bg-primary text-white shadow' : 'text-gray-400 hover:text-white'}">Đã khớp</button>
          </form>
        </div>

        <%-- TABLE CONTAINER --%>
        <div class="bg-dark-800 border border-dark-700 rounded-xl shadow-xl overflow-hidden">
          <div class="overflow-x-auto">
            <table class="w-full text-left border-collapse">
              <thead>
              <tr class="bg-dark-900/50 text-gray-400 text-xs uppercase tracking-wider border-b border-dark-700">
                <th class="px-6 py-4 font-semibold">Mã CK</th>
                <th class="px-6 py-4 font-semibold text-center">Loại</th>
                <th class="px-6 py-4 font-semibold text-right">Giá đặt</th>
                <th class="px-6 py-4 font-semibold text-right">KL đặt</th>
                <th class="px-6 py-4 font-semibold text-right">Đã khớp</th>
                <th class="px-6 py-4 font-semibold text-center">Trạng thái</th>
                <th class="px-6 py-4 font-semibold text-right">Thời gian</th>
                <th class="px-6 py-4 font-semibold text-center">Hành động</th>
              </tr>
              </thead>
              <tbody class="divide-y divide-dark-700 text-sm">

              <%-- Dữ liệu mẫu giả lập cho Backend dễ hình dung --%>
              <%-- Trong thực tế: <c:forEach var="o" items="${orderList}"> --%>
              <c:forEach var="o" items="${orderList}">
                <tr class="hover:bg-dark-700/30 transition duration-150 group">
                  <td class="px-6 py-4 font-bold text-white">${o.symbol}</td>

                  <td class="px-6 py-4 text-center">
                    <span class="px-2 py-1 rounded text-[10px] font-bold border ${o.side == 'BUY' ? 'bg-success/10 text-success border-success/20' : 'bg-danger/10 text-danger border-danger/20'}">
                        ${o.side == 'BUY' ? 'MUA' : 'BÁN'}
                    </span>
                  </td>

                  <td class="px-6 py-4 text-right font-mono text-gray-300">
                    <c:choose>
                      <c:when test="${o.orderType == 'MP'}">MP</c:when>
                      <c:otherwise><fmt:formatNumber value="${o.price}"/></c:otherwise>
                    </c:choose>
                  </td>

                  <td class="px-6 py-4 text-right font-mono text-white"><fmt:formatNumber value="${o.quantity}"/></td>

                  <td class="px-6 py-4 text-right font-mono text-gray-400"><fmt:formatNumber value="${o.matchedQty}"/></td>

                  <td class="px-6 py-4 text-center">
                    <c:choose>
                      <c:when test="${o.status == 'PENDING'}">
                        <span class="text-yellow-500 font-bold text-xs"><i class="fa-regular fa-clock mr-1"></i>Chờ khớp</span>
                      </c:when>
                      <c:when test="${o.status == 'MATCHED'}">
                        <span class="text-success font-bold text-xs"><i class="fa-solid fa-check mr-1"></i>Đã khớp</span>
                      </c:when>
                      <c:when test="${o.status == 'CANCELLED'}">
                        <span class="text-gray-500 font-bold text-xs"><i class="fa-solid fa-ban mr-1"></i>Đã hủy</span>
                      </c:when>
                      <c:otherwise>
                        <span class="text-gray-400 font-bold text-xs uppercase">${o.status}</span>
                      </c:otherwise>
                    </c:choose>
                  </td>

                  <td class="px-6 py-4 text-right text-xs text-gray-500">
                      ${o.createdTime}
                  </td>

                  <td class="px-6 py-4 text-center">
                    <c:if test="${o.status == 'PENDING'}">
                      <div class="flex items-center justify-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">

                          <%-- [CẬP NHẬT] Thêm tham số orderType vào hàm openEditModal để JS xử lý --%>
                        <button onclick="openEditModal('${o.id}', '${o.symbol}', '${o.price}', '${o.quantity}', '${o.orderType}')"
                                class="w-8 h-8 rounded bg-dark-700 hover:bg-blue-600 hover:text-white text-blue-500 transition flex items-center justify-center" title="Sửa lệnh">
                          <i class="fa-solid fa-pen text-xs"></i>
                        </button>

                        <button onclick="openCancelModal('${o.id}', '${o.symbol}')"
                                class="w-8 h-8 rounded bg-dark-700 hover:bg-red-600 hover:text-white text-red-500 transition flex items-center justify-center" title="Hủy lệnh">
                          <i class="fa-solid fa-trash text-xs"></i>
                        </button>
                      </div>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>

              <c:if test="${empty orderList}">
                <tr>
                  <td colspan="8" class="px-6 py-12 text-center text-gray-500">
                    <i class="fa-solid fa-box-open text-4xl mb-3 opacity-30"></i>
                    <p>Chưa có lệnh nào trong danh sách</p>
                  </td>
                </tr>
              </c:if>
              </tbody>
            </table>
          </div>

          <div class="px-6 py-4 border-t border-dark-700 flex justify-between items-center bg-dark-900/30">
            <span class="text-xs text-gray-500">Hiển thị 1-10 trên tổng số 50 lệnh</span>
            <div class="flex gap-1">
              <button class="px-3 py-1 bg-dark-700 text-gray-400 rounded hover:bg-dark-600 text-xs">Trước</button>
              <button class="px-3 py-1 bg-primary text-white rounded text-xs">1</button>
              <button class="px-3 py-1 bg-dark-700 text-gray-400 rounded hover:bg-dark-600 text-xs">2</button>
              <button class="px-3 py-1 bg-dark-700 text-gray-400 rounded hover:bg-dark-600 text-xs">Sau</button>
            </div>
          </div>
        </div>
      </div>

      <jsp:include page="components/footer.jsp" />
    </main>
  </div>
</div>

<%-- ================= MODAL SỬA LỆNH ================= --%>
<div id="editModal" class="hidden fixed inset-0 z-[60] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
  <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
    <div class="fixed inset-0 bg-black/80 transition-opacity" onclick="closeModal('editModal')"></div>
    <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
    <div class="inline-block align-bottom bg-dark-800 rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg w-full border border-dark-700">

      <form action="edit-order" method="POST">
        <input type="hidden" name="orderId" id="editOrderId">
        <input type="hidden" name="symbol" id="editSymbolInput">

        <%-- Input ẩn để lưu loại lệnh, phục vụ logic backend nếu cần --%>
        <input type="hidden" name="orderType" id="editOrderTypeInput">

        <div class="bg-dark-800 px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
          <div class="sm:flex sm:items-start">
            <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-blue-900/30 sm:mx-0 sm:h-10 sm:w-10">
              <i class="fa-solid fa-pen text-blue-500"></i>
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
              <h3 class="text-lg leading-6 font-medium text-white" id="modal-title">
                Sửa lệnh <span id="editSymbolDisplay" class="font-bold text-primary"></span>
                <span id="editTypeDisplay" class="ml-2 text-xs font-bold px-2 py-0.5 rounded bg-gray-700 text-gray-300"></span>
              </h3>

              <div class="mt-4 space-y-4">
                <div>
                  <label class="block text-xs font-bold text-gray-400 mb-1 uppercase">Giá đặt mới (x1000)</label>
                  <%-- Thêm id để JS can thiệp --%>
                  <input type="number" step="0.05" name="newPrice" id="editPriceInput" class="w-full bg-dark-900 border border-dark-600 rounded p-2 text-white focus:border-primary outline-none transition-colors">
                  <p id="mpNote" class="hidden text-[10px] text-yellow-500 mt-1 italic">* Lệnh MP sử dụng giá thị trường, không thể sửa giá.</p>
                </div>
                <div>
                  <label class="block text-xs font-bold text-gray-400 mb-1 uppercase">Khối lượng mới</label>
                  <input type="number" step="100" name="newQty" id="editQtyInput" class="w-full bg-dark-900 border border-dark-600 rounded p-2 text-white focus:border-primary outline-none">
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bg-dark-900/50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse border-t border-dark-700">
          <button type="submit" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none sm:ml-3 sm:w-auto sm:text-sm">
            Cập nhật
          </button>
          <button type="button" onclick="closeModal('editModal')" class="mt-3 w-full inline-flex justify-center rounded-md border border-dark-600 shadow-sm px-4 py-2 bg-dark-700 text-base font-medium text-gray-300 hover:bg-dark-600 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
            Đóng
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<%-- ================= MODAL HỦY LỆNH ================= --%>
<div id="cancelModal" class="hidden fixed inset-0 z-[60] overflow-y-auto">
  <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
    <div class="fixed inset-0 bg-black/80 transition-opacity" onclick="closeModal('cancelModal')"></div>
    <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
    <div class="inline-block align-bottom bg-dark-800 rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg w-full border border-dark-700">
      <form action="cancel-order" method="POST">
        <input type="hidden" name="orderId" id="cancelOrderId">
        <input type="hidden" name="symbol" id="cancelSymbolInput">

        <div class="bg-dark-800 px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
          <div class="sm:flex sm:items-start">
            <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-900/30 sm:mx-0 sm:h-10 sm:w-10">
              <i class="fa-solid fa-triangle-exclamation text-red-500"></i>
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-white">Xác nhận hủy lệnh</h3>
              <div class="mt-2">
                <p class="text-sm text-gray-400">
                  Bạn có chắc chắn muốn hủy lệnh <span id="cancelSymbolDisplay" class="font-bold text-white"></span> không?
                  Hành động này không thể hoàn tác.
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="bg-dark-900/50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse border-t border-dark-700">
          <button type="submit" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none sm:ml-3 sm:w-auto sm:text-sm">
            Hủy lệnh ngay
          </button>
          <button type="button" onclick="closeModal('cancelModal')" class="mt-3 w-full inline-flex justify-center rounded-md border border-dark-600 shadow-sm px-4 py-2 bg-dark-700 text-base font-medium text-gray-300 hover:bg-dark-600 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
            Quay lại
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<jsp:include page="components/scripts.jsp" />

<div id="toast-container" class="fixed top-5 right-5 z-[100] flex flex-col gap-2"></div>

<script>
  function showToast(message, type) {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `min-w-[300px] p-4 rounded-lg shadow-2xl text-white transform transition-all duration-300 translate-x-full flex items-center gap-3 \${type === 'SUCCESS' ? 'bg-green-600' : 'bg-red-600'}`;
    const icon = type === 'SUCCESS' ? '<i class="fa-solid fa-circle-check text-xl"></i>' : '<i class="fa-solid fa-circle-exclamation text-xl"></i>';
    toast.innerHTML = `\${icon}<div class="font-medium">\${message}</div>`;
    container.appendChild(toast);
    requestAnimationFrame(() => { toast.classList.remove('translate-x-full'); });
    setTimeout(() => {
      toast.classList.add('opacity-0', 'translate-x-full');
      setTimeout(() => { toast.remove(); }, 300);
    }, 4000);
  }

  <c:if test="${not empty sessionScope.msgSuccess}">
  showToast('${sessionScope.msgSuccess}', 'SUCCESS');
  <c:remove var="msgSuccess" scope="session"/>
  </c:if>

  <c:if test="${not empty sessionScope.msgError}">
  showToast('${sessionScope.msgError}', 'ERROR');
  <c:remove var="msgError" scope="session"/>
  </c:if>
</script>

<%-- LOGIC JS CHO MODAL --%>
<script>
  // [CẬP NHẬT] Thêm tham số orderType vào hàm
  function openEditModal(id, symbol, price, qty, orderType) {
    document.getElementById('editOrderId').value = id;
    document.getElementById('editSymbolInput').value = symbol;
    document.getElementById('editOrderTypeInput').value = orderType; // Lưu loại lệnh vào input ẩn

    document.getElementById('editSymbolDisplay').innerText = symbol;
    document.getElementById('editTypeDisplay').innerText = orderType; // Hiển thị loại lệnh (LO/MP)

    document.getElementById('editQtyInput').value = qty;

    // Xử lý logic cho từng loại lệnh
    const priceInput = document.getElementById('editPriceInput');
    const mpNote = document.getElementById('mpNote');

    if (orderType === 'MP') {
      // Nếu là lệnh MP: Khóa ô nhập giá, hiện ghi chú
      priceInput.value = ''; // Hoặc để 0 tuỳ logic backend
      priceInput.placeholder = 'Giá thị trường (MP)';
      priceInput.readOnly = true; // Không cho sửa
      priceInput.classList.add('bg-dark-700', 'text-gray-500', 'cursor-not-allowed'); // Style disable
      priceInput.classList.remove('bg-dark-900', 'text-white', 'focus:border-primary');
      mpNote.classList.remove('hidden'); // Hiện dòng chú thích
    } else {
      // Nếu là lệnh LO: Cho phép sửa bình thường
      priceInput.value = price;
      priceInput.placeholder = '';
      priceInput.readOnly = false;
      priceInput.classList.remove('bg-dark-700', 'text-gray-500', 'cursor-not-allowed');
      priceInput.classList.add('bg-dark-900', 'text-white', 'focus:border-primary');
      mpNote.classList.add('hidden');
    }

    document.getElementById('editModal').classList.remove('hidden');
  }

  function openCancelModal(id, symbol) {
    document.getElementById('cancelOrderId').value = id;
    document.getElementById('cancelSymbolInput').value = symbol;
    document.getElementById('cancelSymbolDisplay').innerText = symbol;
    document.getElementById('cancelModal').classList.remove('hidden');
  }

  function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
  }
</script>

</body>
</html>