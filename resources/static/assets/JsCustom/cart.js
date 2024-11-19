
// Hàm hiển thị giỏ hàng
const displayCart = () => {
    const cartTable = document.getElementById('#cart-table');

    if (!cartTable) {
        console.error("Element #cart-table không tồn tại trong DOM");
        return;
    }

    cartTable.innerHTML = '';

    // Lấy giỏ hàng từ API
    fetch('/home/cart/list')
        .then(response => {
            if (!response.ok) {
                throw new Error('Lỗi khi tải giỏ hàng');
            }
            console.log('Giỏ hàng:', cart);
            return response.json();
        })
        .then(cart => {
            // Duyệt qua các sản phẩm trong giỏ hàng
            const items = Object.values(cart);

            items.forEach(item => {
                const total = item.price * item.quantity;

                const rowHtml = `
                    <tr>
                        <td class="product-col">
                            <div class="product">
                                <figure class="product-media">
                                    <a href="#">
                                        <img src="/images/product/${item.image}" alt="${item.name}">
                                    </a>
                                </figure>
                                <h3 class="product-title">
                                    <a>${item.name}</a>
                                </h3>
                            </div>
                        </td>
                        <td class="price-col">${item.price} VND</td>
                        <td class="quantity-col">
                            <div class="cart-product-quantity">
                                <input type="number" class="form-control" value="${item.quantity}" min="1" step="1" required onchange="updateQuantity(${item.id}, this.value)">
                            </div>
                        </td>
                        <td class="total-col">${total} VND</td>
                        <td class="remove-col">
                            <button class="btn-remove" onclick="removeFromCart(${item.id})"><i class="icon-close"></i></button>
                        </td>
                    </tr>
                `;
                // Thêm hàng mới vào bảng giỏ hàng
                cartTable.insertAdjacentHTML('beforeend', rowHtml);
            });

            // Cập nhật tổng số tiền giỏ hàng
            const totalAmount = items.reduce((acc, item) => acc + (item.price * item.quantity), 0);
            document.getElementById('totalAmount').textContent = `${totalAmount} VND`;
        })

};

document.addEventListener("DOMContentLoaded", function() {

    displayCart();
});
