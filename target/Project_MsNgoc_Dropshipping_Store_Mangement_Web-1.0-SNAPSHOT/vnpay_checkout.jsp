<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VNPay Payment Gateway</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f5f5;
            color: #333;
            line-height: 1.6;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .payment-header {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .payment-header h1 {
            color: #005BAA;
            font-size: 24px;
            font-weight: 600;
        }

        .payment-content {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 20px;
        }

        .payment-methods {
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .order-summary {
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            height: fit-content;
        }

        h2 {
            color: #005BAA;
            font-size: 20px;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #005BAA;
        }

        .method-section {
            margin-bottom: 30px;
        }

        .method-section h3 {
            font-size: 16px;
            color: #333;
            margin-bottom: 15px;
            font-weight: 600;
        }

        .payment-option {
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
        }

        .payment-option:hover {
            border-color: #005BAA;
            background: #f8f9fa;
        }

        .payment-option input[type="radio"] {
            margin-right: 15px;
            width: 20px;
            height: 20px;
            cursor: pointer;
        }

        .payment-option label {
            cursor: pointer;
            flex: 1;
            font-size: 15px;
        }

        .bank-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 15px;
            margin-top: 15px;
        }

        .bank-item {
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            padding: 15px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            background: #fff;
        }

        .bank-item:hover {
            border-color: #005BAA;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .bank-item input[type="radio"] {
            display: none;
        }

        .bank-item input[type="radio"]:checked + .bank-label {
            border-color: #005BAA;
            background: #e3f2fd;
        }

        .bank-label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            color: #333;
            padding: 10px;
            border-radius: 6px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .summary-row.total {
            font-size: 18px;
            font-weight: 700;
            color: #005BAA;
            border-bottom: none;
            margin-top: 10px;
        }

        .summary-label {
            color: #666;
        }

        .summary-value {
            font-weight: 600;
            color: #333;
        }

        .payment-button {
            width: 100%;
            padding: 15px;
            background: #4FBA69;
            color: #fff;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 20px;
            transition: all 0.3s;
        }

        .payment-button:hover {
            background: #45a85d;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(79, 186, 105, 0.3);
        }

        .payment-button:active {
            transform: translateY(0);
        }

        .cancel-button {
            width: 100%;
            padding: 15px;
            background: #fff;
            color: #666;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 10px;
            transition: all 0.3s;
        }

        .cancel-button:hover {
            border-color: #999;
            background: #f5f5f5;
        }

        .security-notice {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
            font-size: 13px;
            color: #666;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .security-icon {
            font-size: 20px;
        }

        @media (max-width: 968px) {
            .payment-content {
                grid-template-columns: 1fr;
            }

            .bank-grid {
                grid-template-columns: repeat(2, 1fr);
            }
        }

        @media (max-width: 480px) {
            .bank-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="payment-header">
            <h1> VNPay Payment Gateway</h1>
        </div>

        <!-- Main Content -->
        <div class="payment-content">
            <!-- Payment Methods -->
            <div class="payment-methods">
                <h2>Select Payment Method</h2>

                <!-- ATM Card / Internet Banking -->
                <div class="method-section">
                    <h3>ATM Card / Internet Banking</h3>
                    
                    <div class="payment-option">
                        <input type="radio" id="atm" name="paymentMethod" value="atm" checked>
                        <label for="atm">Pay with ATM Card / Internet Banking</label>
                    </div>

                    <div class="bank-grid" id="bankList">
                        <div class="bank-item">
                            <input type="radio" id="vcb" name="bank" value="VCB">
                            <label for="vcb" class="bank-label">Vietcombank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="bidv" name="bank" value="BIDV">
                            <label for="bidv" class="bank-label">BIDV</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="vietinbank" name="bank" value="ICB">
                            <label for="vietinbank" class="bank-label">VietinBank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="agribank" name="bank" value="AGB">
                            <label for="agribank" class="bank-label">Agribank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="acb" name="bank" value="ACB">
                            <label for="acb" class="bank-label">ACB</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="techcombank" name="bank" value="TCB">
                            <label for="techcombank" class="bank-label">Techcombank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="mb" name="bank" value="MB">
                            <label for="mb" class="bank-label">MB Bank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="ncb" name="bank" value="NCB">
                            <label for="ncb" class="bank-label">NCB Bank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="vib" name="bank" value="VIB">
                            <label for="vib" class="bank-label">VIB</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="shb" name="bank" value="SHB">
                            <label for="shb" class="bank-label">SHB</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="tpb" name="bank" value="TPB">
                            <label for="tpb" class="bank-label">TPBank</label>
                        </div>
                        <div class="bank-item">
                            <input type="radio" id="vpb" name="bank" value="VPB">
                            <label for="vpb" class="bank-label">VPBank</label>
                        </div>
                    </div>
                </div>

                <!-- Credit Card -->
                <div class="method-section">
                    <h3>International Card</h3>
                    <div class="payment-option">
                        <input type="radio" id="credit" name="paymentMethod" value="credit">
                        <label for="credit">Pay with Visa / Mastercard / JCB</label>
                    </div>
                </div>

                <!-- QR Code -->
                <div class="method-section">
                    <h3>QR Code Payment</h3>
                    <div class="payment-option">
                        <input type="radio" id="qr" name="paymentMethod" value="qr">
                        <label for="qr">Pay with VNPay QR Code</label>
                    </div>
                </div>

                <!-- E-Wallet -->
                <div class="method-section">
                    <h3>E-Wallet</h3>
                    <div class="payment-option">
                        <input type="radio" id="wallet" name="paymentMethod" value="wallet">
                        <label for="wallet">Pay with Mobile Wallet</label>
                    </div>
                </div>
            </div>

            <!-- Order Summary -->
            <div class="order-summary">
                <h2>Order Summary</h2>
                
                <div class="summary-row">
                    <span class="summary-label">Order ID:</span>
                    <span class="summary-value" id="orderId">ORD-2025-001</span>
                </div>

                <div class="summary-row">
                    <span class="summary-label">Description:</span>
                    <span class="summary-value" id="orderDesc">Payment for order</span>
                </div>

                <div class="summary-row">
                    <span class="summary-label">Subtotal:</span>
                    <span class="summary-value" id="subtotal">100,000 VND</span>
                </div>

                <div class="summary-row">
                    <span class="summary-label">Transaction Fee:</span>
                    <span class="summary-value" id="fee">0 VND</span>
                </div>

                <div class="summary-row total">
                    <span class="summary-label">Total Amount:</span>
                    <span class="summary-value" id="total">100,000 VND</span>
                </div>

                <button class="payment-button" onclick="processPayment()">
                    Continue to Payment
                </button>

                <button class="cancel-button" onclick="cancelPayment()">
                    Cancel
                </button>

                <div class="security-notice">
                    <span class="security-icon">?</span>
                    <span>Your transaction is secured and encrypted with 256-bit SSL</span>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Get URL parameters
        function getUrlParameter(name) {
            name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
            var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
            var results = regex.exec(location.search);
            return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
        }

        // Update order info from URL parameters
        window.onload = function() {
            const amount = getUrlParameter('amount');
            const orderInfo = getUrlParameter('orderInfo');
            
            if (amount) {
                const formattedAmount = new Intl.NumberFormat('vi-VN').format(amount) + ' VND';
                document.getElementById('subtotal').textContent = formattedAmount;
                document.getElementById('total').textContent = formattedAmount;
            }
            
            if (orderInfo) {
                document.getElementById('orderDesc').textContent = orderInfo;
            }
        };

        // Handle bank selection
        document.querySelectorAll('.bank-item').forEach(item => {
            item.addEventListener('click', function() {
                const radio = this.querySelector('input[type="radio"]');
                radio.checked = true;
                
                // Highlight selected
                document.querySelectorAll('.bank-label').forEach(label => {
                    label.style.borderColor = '#e0e0e0';
                    label.style.background = '#fff';
                });
                
                const label = this.querySelector('.bank-label');
                label.style.borderColor = '#005BAA';
                label.style.background = '#e3f2fd';
            });
        });

        // Process payment
        function processPayment() {
            const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
            
            if (!paymentMethod) {
                alert('Please select a payment method');
                return;
            }

            if (paymentMethod.value === 'atm') {
                const bank = document.querySelector('input[name="bank"]:checked');
                if (!bank) {
                    alert('Please select a bank');
                    return;
                }
                console.log('Selected bank:', bank.value);
            }

            // Redirect to payment processing
            alert('Redirecting to payment gateway...');
            // In real implementation, submit form to your servlet
            // window.location.href = 'api/payment/process?method=' + paymentMethod.value;
        }

        // Cancel payment
        function cancelPayment() {
            if (confirm('Are you sure you want to cancel this payment?')) {
                window.history.back();
            }
        }
    </script>
</body>
</html>
