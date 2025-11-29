<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Create Order - VNPay Payment</title>
    
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    
    <!-- Custom styles for this template -->
    <link href="${pageContext.request.contextPath}/css/jumbotron-narrow.css" rel="stylesheet">
    
    <style>
        :root {
            --primary-green: #4fba69;
            --greyscale-0: #1A1C1E;
            --greyscale-90: #E2E2E5;
            --greyscale-100: #FFFFFF;
        }

        .btn-custom-primary {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 24px;
            background: var(--primary-green);
            color: var(--greyscale-100);
            border: none;
            border-radius: 8px;
            font-family: "Segoe UI", system-ui, sans-serif;
            font-size: 16px;
            font-weight: 600;
            line-height: 20px;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
        }

        .btn-custom-primary:hover {
            background: #45a85d;
            color: var(--greyscale-100);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(79, 186, 105, 0.3);
            text-decoration: none;
        }

        .btn-custom-primary:active {
            background: #3d9651;
            transform: translateY(0);
        }

        .btn-custom-primary:focus {
            outline: 2px solid var(--primary-green);
            outline-offset: 2px;
        }

        .btn-custom-primary svg {
            flex-shrink: 0;
        }

        .form-actions {
            display: flex;
            gap: 12px;
            margin-top: 24px;
        }

        .btn-secondary-custom {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 24px;
            background: var(--greyscale-90);
            color: var(--greyscale-0);
            border: none;
            border-radius: 8px;
            font-family: "Segoe UI", system-ui, sans-serif;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
        }

        .btn-secondary-custom:hover {
            background: #D6D6D6;
            color: var(--greyscale-0);
            text-decoration: none;
        }
    </style>
    
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</head>

<body style="font-size: 0.9rem;">
<div class="container">
    <div class="clearfix" style="padding-bottom: 1rem;">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard.jsp">VNPAY</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavDropdown">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="transaction_list.jsp">Order List</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/payment.jsp">
                            Create New <span class="sr-only">(current)</span>
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
    
    <h3>Create New Order</h3>
    <div class="table-responsive">
        <form id="frmCreateOrder">
            
            <div class="form-group">
                <label for="ordertype">Product Type</label>
                <select name="ordertype" id="ordertype" class="form-control">
                    <option value="topup">Mobile Top-up</option>
                    <option value="billpayment">Bill Payment</option>
                    <option value="fashion">Fashion</option>
                    <option value="other">Other</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="Amount">Amount (VND)</label>
                <input class="form-control" 
                       data-val="true" 
                       data-val-number="The field Amount must be a number." 
                       data-val-required="The Amount field is required." 
                       id="Amount" 
                       name="amount" 
                       type="number" 
                       value="100000" 
                       min="10000"
                       required />
            </div>
            
            <div class="form-group">
                <label for="OrderDescription">Payment Description</label>
                <textarea class="form-control" 
                          cols="20" 
                          id="OrderDescription" 
                          name="orderInfo" 
                          rows="2" 
                          required>Payment for order - <%= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %></textarea>
            </div>
            
            <div class="form-group">
                <label for="bankcode">Bank</label>
                <select name="bankCode" id="bankcode" class="form-control">
                    <option value="">No Selection (All Banks)</option>
                    <option value="QRONLY">QR Code Only</option>
                    <option value="MBAPP">Mobile Banking App</option>
                    <option value="VNPAYQR">VNPAY QR</option>
                    <option value="VNBANK">Local Bank</option>
                    <option value="IB">Internet Banking</option>
                    <option value="ATM">ATM Card</option>
                    <option value="INTCARD">International Card</option>
                    <option value="VISA">VISA</option>
                    <option value="MASTERCARD">MASTERCARD</option>
                    <option value="JCB">JCB</option>
                    <option value="NCB">NCB Bank</option>
                    <option value="SACOMBANK">Sacombank</option>
                    <option value="EXIMBANK">Eximbank</option>
                    <option value="VIETINBANK">Vietinbank</option>
                    <option value="VIETCOMBANK">Vietcombank</option>
                    <option value="HDBANK">HDBank</option>
                    <option value="TPBANK">TPBank</option>
                    <option value="BIDV">BIDV</option>
                    <option value="TECHCOMBANK">Techcombank</option>
                    <option value="VPBANK">VPBank</option>
                    <option value="AGRIBANK">Agribank</option>
                    <option value="MBBANK">MBBank</option>
                    <option value="ACB">ACB</option>
                    <option value="OCB">OCB</option>
                    <option value="APPLEPAY">Apple Pay</option>
                    <option value="GOOGLEPAY">Google Pay</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="language">Language</label>
                <select name="language" id="language" class="form-control">
                    <option value="vn">Vietnamese</option>
                    <option value="en">English</option>
                </select>
            </div>
            
            <div class="form-actions">
                <button type="button" class="btn-secondary-custom" onclick="window.location.href='${pageContext.request.contextPath}/dashboard.jsp'">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M15 10H5M5 10L10 5M5 10L10 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                    <span>Cancel</span>
                </button>
                
                <button type="button" class="btn-custom-primary" onclick="goToCheckout()">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <rect x="2" y="4" width="16" height="12" rx="2" stroke="currentColor" stroke-width="2" fill="none"></rect>
                        <path d="M3 6h14M5 10h10M7 14h6" stroke="currentColor" stroke-width="2" stroke-linecap="round"></path>
                    </svg>
                    <span>Pay with Redirect</span>
                </button>
            </div>
            
            <!-- Hidden fields -->
            <input type="hidden" name="orderId" id="orderId" value="ORDER<%= System.currentTimeMillis() %>">
        </form>
    </div>
    
    <p>&nbsp;</p>

    <footer class="footer">
        <p>&copy; VNPAY <%= java.time.Year.now().getValue() %></p>
    </footer>
</div>

<!-- VNPay Popup Integration (Optional) -->
<link href="https://pay.vnpay.vn/lib/vnpay/vnpay.css" rel="stylesheet" />
<script src="https://pay.vnpay.vn/lib/vnpay/vnpay.js"></script>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
    function goToCheckout() {
        // Validate form
        const form = document.getElementById('frmCreateOrder');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        // Get form values
        const amount = document.getElementById('Amount').value;
        const orderInfo = document.getElementById('OrderDescription').value;
        const bankCode = document.getElementById('bankcode').value;
        const language = document.getElementById('language').value;
        const orderType = document.getElementById('ordertype').value;
        const orderId = document.getElementById('orderId').value;
        
        // Build URL parameters
        const params = new URLSearchParams({
            amount: amount,
            orderInfo: orderInfo,
            bankCode: bankCode,
            language: language,
            orderType: orderType,
            orderId: orderId
        });
        
        // Redirect to checkout.jsp (FIXED!)
        window.location.href = '${pageContext.request.contextPath}/vnpay_checkout.jsp?' + params.toString();
    }

    // Optional: Popup payment (if needed in the future)
    $("#btnPopup").click(function (e) {
        e.preventDefault();
        var postData = $("#frmCreateOrder").serialize();
        var submitUrl = "${pageContext.request.contextPath}/api/payment/process";
        
        $.ajax({
            type: "POST",
            url: submitUrl,
            data: postData,
            dataType: 'JSON',
            success: function (response) {
                if (response.code === '00') {
                    if (window.vnpay) {
                        vnpay.open({ 
                            width: 768, 
                            height: 600, 
                            url: response.data 
                        });
                    } else {
                        window.location = response.data;
                    }
                } else {
                    alert("Error: " + response.message);
                }
            },
            error: function(xhr, status, error) {
                alert("Payment request failed: " + error);
            }
        });
        return false;
    });
</script>
</body>
</html>
