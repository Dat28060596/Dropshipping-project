<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Order List</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <style>
        /* General Body and Layout */
        body {
            background-color: #f4f7f6;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #495057;
            font-size: 0.9rem;
        }

        .container {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
            padding: 30px;
            margin-top: 30px;
            margin-bottom: 30px;
            min-height: 80vh;
        }

        /* Navbar Customization - CHANGED TO WHITE THEME */
        .navbar {
            border-radius: 6px;
            background: #ffffff !important; /* Changed to White */
            box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05); /* Softer, darker shadow */
            border: 1px solid #e9ecef; /* Subtle border for definition */
            padding: 0.8rem 1.5rem;
            margin-bottom: 2rem;
        }

        .navbar-brand {
            font-weight: 700;
            letter-spacing: 1px;
            color: #005c97 !important; /* Changed text to Blue for contrast */
        }

        .navbar-nav .nav-link {
            color: #555 !important; /* Changed text to Dark Grey */
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .navbar-nav .nav-link:hover {
            color: #005c97 !important; /* Blue on hover */
            transform: translateY(-1px);
        }

        /* Heading Styling */
        h3 {
            color: #333; /* Darker grey for heading */
            font-weight: 700;
            margin-top: 10px;
            margin-bottom: 25px;
            position: relative;
            padding-bottom: 10px;
        }

        h3::after {
            content: '';
            position: absolute;
            left: 0;
            bottom: 0;
            width: 60px;
            height: 3px;
            background-color: #005c97; /* Blue accent line */
            border-radius: 2px;
        }

        /* Table Styling */
        .table-responsive {
            border-radius: 8px;
            overflow: hidden;
        }

        .table {
            margin-bottom: 0;
            border: 1px solid #e9ecef;
        }

        .table thead th {
            background-color: #f8f9fa;
            color: #343a40;
            font-weight: 600;
            border-bottom: 2px solid #dee2e6;
            border-top: none;
            vertical-align: middle;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
        }

        .table tbody tr {
            transition: background-color 0.2s ease;
        }

        .table tbody tr:hover {
            background-color: #f8fbff; /* Very light blue hover */
        }

        .table td {
            vertical-align: middle;
            padding: 15px 12px;
            color: #555;
        }

        /* Specific Column Styling */
        .table td:nth-child(2) { /* Amount Column */
            font-weight: 600;
            color: #28a745;
        }

        /* Status Badges */
        .pay-unsuccess, .pay-success {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 50px;
            font-size: 0.75rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            white-space: nowrap;
        }

        .pay-unsuccess {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }

        .pay-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        /* Links in Table */
        .table td a {
            color: #005c97; /* Blue link */
            font-weight: 600;
            text-decoration: none;
            border: 1px solid #005c97;
            padding: 4px 10px;
            border-radius: 4px;
            font-size: 0.85rem;
            transition: all 0.2s;
            display: inline-block;
        }

        .table td a:hover {
            background-color: #005c97;
            color: white;
        }

        /* Pager / Pagination */
        .pager {
            margin-top: 20px;
            margin-bottom: 20px;
            text-align: right;
        }

        .pager span, .pager a {
            display: inline-block;
            padding: 8px 14px;
            margin-left: 5px;
            border-radius: 4px;
            border: 1px solid #dee2e6;
            text-decoration: none;
            color: #005c97;
            background-color: white;
            font-weight: 500;
            transition: all 0.2s;
        }

        .pager a:hover {
            background-color: #e9ecef;
            border-color: #dee2e6;
        }

        .pager .current {
            background-color: #005c97; /* Blue active state */
            color: white;
            border-color: #005c97;
        }

        .pager .disabled {
            color: #adb5bd;
            pointer-events: none;
            background-color: #f8f9fa;
        }

        /* Footer */
        .footer {
            border-top: 1px solid #e9ecef;
            margin-top: 40px;
            padding-top: 20px;
            text-align: center;
            color: #868e96;
            font-size: 0.9rem;
        }
    </style>
</head>

<body>
    <div class="container">
        <div class="clearfix">
            <nav class="navbar navbar-expand-lg navbar-light">
                <a class="navbar-brand" href="https://sandbox.vnpayment.vn/tryitnow/">VNPAY DEMO</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="https://sandbox.vnpayment.vn/tryitnow/">List</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="https://sandbox.vnpayment.vn/tryitnow/Home/CreateOrder">Create New</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>

        <h3>Order List</h3>
        
        <div class="table-responsive">
            <div class="pager">
                <span class="disabled">«</span><span class="current">1</span><a href="?page=2">2</a><a href="?page=3">3</a><a href="?page=4">4</a><a href="?page=5">5</a><a href="?page=2">»</a>
            </div>
            
            <table class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Amount</th>
                        <th>Description</th>
                        <th>Created Date</th>
                        <th>Status</th>
                        <th>IP Address</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>285212</td>
                        <td>100,000</td>
                        <td>Order payment time: 2025-11-29 15:19:02</td>
                        <td class="center">2025-11-29 15:19:12</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>1.55.48.201</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285211</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 15:09:12</td>
                        <td class="center">2025-11-29 15:09:14</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>14.191.232.74</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285210</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 15:06:50</td>
                        <td class="center">2025-11-29 15:06:57</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>171.243.63.181</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285209</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 15:05:43</td>
                        <td class="center">2025-11-29 15:05:50</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>14.191.232.74</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285208</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 15:01:13</td>
                        <td class="center">2025-11-29 15:01:36</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>14.191.232.74</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285207</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 15:01:13</td>
                        <td class="center">2025-11-29 15:01:18</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>14.191.232.74</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285206</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 15:01:13</td>
                        <td class="center">2025-11-29 15:01:15</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>14.191.232.74</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285205</td>
                        <td>100,000</td>
                        <td>Order payment time: 2025-11-29 14:50:17</td>
                        <td class="center">2025-11-29 14:50:37</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>1.55.48.201</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285204</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 14:47:12</td>
                        <td class="center">2025-11-29 14:49:19</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>116.96.44.213</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                    <tr>
                        <td>285203</td>
                        <td>10,000</td>
                        <td>Order payment time: 2025-11-29 14:47:12</td>
                        <td class="center">2025-11-29 14:48:03</td>
                        <td class="center"><span class="pay-unsuccess">Unpaid</span></td>
                        <td>116.96.44.213</td>
                        <td><a href="#">Details</a></td>
                    </tr>
                </tbody>
            </table>
            
            <div class="pager">
                <span class="disabled">«</span><span class="current">1</span><a href="?page=2">2</a><a href="?page=3">3</a><a href="?page=4">4</a><a href="?page=5">5</a><a href="?page=2">»</a>
            </div>
        </div>

        <footer class="footer">
            <p>© VNPAY 2025</p>
        </footer>
    </div> <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>