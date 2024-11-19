
// Hàm này sẽ tải nội dung từ file HTML vào main
function loadContent(page) {
    fetch(page)
        .then(response => response.text())
        .then(data => {
            document.getElementById('content').innerHTML = data;

        })
        .catch(error => console.error('Error loading page:', error));
}


// Thêm sản phẩm vào giỏ hàng
const addToCart = (id) => {
    console.log(`Thêm sản phẩm có ID: ${id} vào giỏ hàng`);

    // Gửi yêu cầu đến API để thêm sản phẩm vào giỏ hàng
    fetch('/home/add-cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ productId: id }), // Gửi ID sản phẩm
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Có lỗi khi thêm sản phẩm vào giỏ hàng');
            }
        })
        .then(message => {
            console.log(message);
            // window.location.href = "/views/client/content/cart.html";
            alert("Đã thêm sản phẩm vào giỏ hàng");
            displayCart();
        })
        .catch(error => {
            console.error('Có lỗi:', error);
        });
};

// Hàm hiển thị giỏ hàng
const displayCart = () => {
    const cartTable = document.getElementById('cart-table');

    cartTable.innerHTML = '';

    // Lấy giỏ hàng từ API
    fetch('/home/cart/list')
        .then(response => {
            if (!response.ok) {
                throw new Error('Lỗi khi tải giỏ hàng');
            }
            return response.json();
        })

        .then(cart => {
            // Duyệt qua các sản phẩm trong giỏ hàng
            const items = Object.values(cart);  // Chuyển giỏ hàng thành một mảng các sản phẩm

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


// Cập nhật số lượng trong giỏ hàng (nếu người dùng thay đổi số lượng)
const updateQuantity = (id, quantity) => {
    console.log(`Cập nhật số lượng của sản phẩm ID: ${id} thành ${quantity}`);

    // Gửi yêu cầu đến API để cập nhật số lượng sản phẩm trong giỏ hàng
    fetch('/home/update-cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ productId: id, quantity: quantity }), // Gửi ID và số lượng mới
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            displayCart(); // Cập nhật lại giỏ hàng sau khi thay đổi số lượng
        })
        .catch(error => {
            console.error('Có lỗi khi cập nhật số lượng:', error);
        });
};

// Xóa sản phẩm khỏi giỏ hàng
const removeFromCart = (id) => {
    console.log(`Xóa sản phẩm ID: ${id} khỏi giỏ hàng`);

    // Gửi yêu cầu đến API để xóa sản phẩm khỏi giỏ hàng
    fetch('/home/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ productId: id }), // Gửi ID sản phẩm cần xóa
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            displayCart(); // Cập nhật giỏ hàng sau khi xóa sản phẩm
        })
        .catch(error => {
            console.error('Có lỗi khi xóa sản phẩm:', error);
        });
};


const selectProduct = (id) => {
    console.log(`Sản phẩm được chọn có ID: ${id}`);
    // Xử lý khi người dùng chọn sản phẩm
};



//call Api Sản phẩm, danh mục
const loadData = async () => {
    const response = await fetch('/home/content', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (response.ok) {
        const data = await response.json();
        const { products, categories } = data;
        displayCategories(categories);
        displayProducts(products);
        displayCategories2(categories);

    } else {
        alert(`Lỗi: ${await response.text()}`);
    }
};

const displayCategories = (categories) => {
    const categoryHeader = document.getElementById('category-header');
    console.log("Categories in displayCategories:", categories);
    // Xóa nội dung cũ
    categoryHeader.innerHTML = '';

    // Lặp qua danh mục và hiển thị
    categories.forEach(({ id, name }) => {
        const categoryHtml = `
            <li>
                <a href="/categories/${id}">${name}</a>
            </li>
        `;
        categoryHeader.insertAdjacentHTML('beforeend', categoryHtml);
    });
};


const displayProducts = (products) => {
    const productHeader = document.getElementById('product-header');
    productHeader.innerHTML = ''; // Xóa nội dung cũ

    products.forEach(({ id, name }) => {
        const productHtml = `
            <li>
                <a href="#" onclick="selectProduct(${id})">
                    <span>${name}<span class="tip tip-new">New</span></span>
                </a>
            </li>
        `;
        productHeader.insertAdjacentHTML('beforeend', productHtml);
    });

    const productList = document.getElementById('product-list');
    productList.innerHTML = ''; // Xóa nội dung cũ

    products.forEach(({ id, name, image, price, details }) => {
        const productHtml = `
            <div class="product-item furniture col-6 col-md-4 col-lg-3">
                <div class="product product-4">
                    <figure class="product-media">
                        <a href="#">
                            <img src="/images/product/${image}" class="product-image" alt="${name}">
                        </a>
                    </figure>
                    <div class="product-body">
                        <h3 class="product-title"><a href="#">${name}</a></h3>
                        <h3 class="product-title">Giá: ${price} VND</h3>
                        <h3 class="product-title"><a href="#">${details}</a></h3><br>
                        <div class="product-action">
                            <button type="button" class="btn-product btn-cart" onclick="addToCart(${id})">
                                <span>Thêm vào giỏ hàng</span>
                                <i class="icon-long-arrow-right"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        productList.insertAdjacentHTML('beforeend', productHtml);
    });

    //sản phẩm ben trang category
    const PCList = document.getElementById('product-category');
    PCList.innerHTML = ''; // Xóa nội dung cũ

    products.forEach(({ id, image, price, details }) => {
        const productHtml = `
            <div class="col-6" >
                <div class="product product-7 text-center">
                    <figure class="product-media">
                        <span class="product-label label-new">New</span>
                        <a>
                            <img src="/images/product/${image}" class="product-image" alt="">
                        </a>
                        <div class="product-action">
                          
                            <button  class="btn-product btn-cart" onclick="addToCart(${id})">
                            <span>Thêm vào giỏ hàng</span></button>
                          
                        </div>
                    </figure>
    
                    <div class="product-body">
    
                        <h3 class="product-title"><a >${details}</a></h3><!-- End .product-title -->
                        <div class="product-price" >
                           ${price} VND
                        </div><!-- End .product-price -->
                        <div class="ratings-container">
                            <div class="ratings">
                                <div class="ratings-val" style="width: 20%;"></div>
                            </div>
                            <span class="ratings-text">( 2 Reviews )</span>
                        </div>
    
                    </div>
                </div>
            </div><!-- End .col-sm-6 -->
         `;

        PCList.insertAdjacentHTML('beforeend', productHtml);
    });
};



// Tải trang mặc định khi vừa load xong (ví dụ: home.html)
document.addEventListener("DOMContentLoaded", function() {
    loadContent('/views/client/content/content.html');
    loadData();
    displayCart();
});


