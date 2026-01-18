<%@ page pageEncoding="UTF-8" %>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<script src="https://cdn.tailwindcss.com"></script>

<script>
    tailwind.config = {
        darkMode: 'class',
        theme: {
            extend: {
                fontFamily: {
                    sans: ['Inter', 'sans-serif'],
                },
                colors: {
                    dark: {
                        900: '#151521', // Màu nền cực tối
                        800: '#1e1e2d', // Màu nền card
                        700: '#2b2b40',
                    },
                    primary: {
                        DEFAULT: '#a855f7', // Màu tím chủ đạo
                        hover: '#9333ea',
                    },
                    success: '#00c853', // Màu xanh lãi
                    danger: '#ff3d00',  // Màu đỏ lỗ
                }
            }
        }
    }
</script>

<style>
    body { background-color: #151521; color: #e0e0e0; }

    /* Hiệu ứng kính mờ */
    .glass-effect {
        background: rgba(30, 30, 45, 0.7);
        backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.1);
    }

    /* Scrollbar đẹp */
    ::-webkit-scrollbar { width: 6px; height: 6px; }
    ::-webkit-scrollbar-track { background: #151521; }
    ::-webkit-scrollbar-thumb { background: #2b2b40; border-radius: 3px; }
    ::-webkit-scrollbar-thumb:hover { background: #a855f7; }
</style>