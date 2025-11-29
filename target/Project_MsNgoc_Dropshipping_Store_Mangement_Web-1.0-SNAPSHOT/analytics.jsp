<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="config.jsp" %>
<%@page import="com.security.model.User"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(LINK_LOGIN);
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New Analytics Dashboard - Dropshipping Store Management</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script> <!-- Updated to latest Chart.js -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"> <!-- Updated Font Awesome -->
    <style>
        /* Modernized Styles with Improved Layout and Responsiveness */
        :root {
            --primary: #4f46e5;
            --secondary: #7c3aed;
            --success: #22c55e;
            --danger: #ef4444;
            --warning: #eab308;
            --info: #3b82f6;
            --bg-dark: #111827;
            --bg-card: #1f2937;
            --input-bg: #111827;
            --border: #374151;
            --text-main: #f3f4f6;
            --text-sub: #9ca3af;
            --shadow-lg: 0 10px 30px rgba(0,0,0,0.25);
            --shadow-md: 0 4px 10px rgba(0,0,0,0.15);
        }
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Inter', sans-serif; /* Modern font */
            background: linear-gradient(145deg, #111827 0%, #1f2937 100%);
            color: var(--text-main);
            min-height: 100vh;
            line-height: 1.5;
        }
        .container {
            max-width: 1440px;
            margin: 0 auto;
            padding: 2rem 1rem;
        }
        .header {
            text-align: center;
            margin-bottom: 2.5rem;
        }
        .header h1 {
            font-size: 2.5rem;
            background: linear-gradient(90deg, var(--primary), var(--secondary));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .header p {
            color: var(--text-sub);
            font-size: 1.1rem;
        }
        .export-buttons {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: center;
            margin-bottom: 3rem;
        }
        .export-btn {
            padding: 0.75rem 1.5rem;
            background: linear-gradient(90deg, var(--success), #16a34a);
            color: white;
            border: none;
            border-radius: 0.5rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
            box-shadow: var(--shadow-md);
        }
        .export-btn:hover {
            transform: scale(1.05);
            box-shadow: var(--shadow-lg);
        }
        .metrics-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            margin-bottom: 3rem;
        }
        .metric {
            background: var(--bg-card);
            padding: 1.5rem;
            border-radius: 0.75rem;
            box-shadow: var(--shadow-md);
            text-align: center;
            transition: transform 0.2s;
        }
        .metric:hover {
            transform: translateY(-5px);
        }
        .metric h3 {
            font-size: 0.9rem;
            color: var(--text-sub);
            margin-bottom: 0.5rem;
        }
        .metric-value {
            font-size: 1.8rem;
            font-weight: bold;
            color: var(--primary);
        }
        .metric-trend {
            font-size: 0.8rem;
            margin-top: 0.25rem;
        }
        .trend-up { color: var(--success); }
        .trend-down { color: var(--danger); }
        .report {
            background: var(--bg-card);
            padding: 2rem;
            border-radius: 1rem;
            margin-bottom: 2.5rem;
            box-shadow: var(--shadow-md);
        }
        .report h2 {
            font-size: 1.5rem;
            margin-bottom: 1.5rem;
            border-bottom: 1px solid var(--border);
            padding-bottom: 0.75rem;
        }
        .report-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 2rem;
        }
        .chart-wrap {
            background: rgba(79, 70, 229, 0.05);
            padding: 1rem;
            border-radius: 0.5rem;
        }
        .details h3 {
            font-size: 1.1rem;
            margin-bottom: 1rem;
        }
        .table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0 0.5rem;
        }
        .table th {
            text-align: left;
            padding: 0.75rem;
            font-size: 0.8rem;
            color: var(--text-sub);
            text-transform: uppercase;
        }
        .table td {
            padding: 0.75rem;
            background: rgba(0,0,0,0.2);
            border-radius: 0.25rem;
        }
        .badge {
            padding: 0.25rem 0.75rem;
            border-radius: 999px;
            font-size: 0.75rem;
            font-weight: 500;
        }
        .badge-green { background: rgba(34,197,94,0.2); color: var(--success); }
        .badge-yellow { background: rgba(234,179,8,0.2); color: var(--warning); }
        .badge-red { background: rgba(239,68,68,0.2); color: var(--danger); }
        @media (max-width: 1024px) {
            .report-grid { grid-template-columns: 1fr; }
        }
        @media print {
            body { background: white; color: black; }
            .export-buttons { display: none; }
            .report { box-shadow: none; background: white; }
            .metric { background: white; border: 1px solid #ccc; }
            .chart-wrap { background: white; }
            .table td { background: #f9f9f9; }
        }
    </style>
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>Analytics Dashboard</h1>
            <p>Real-time insights into your dropshipping performance</p>
        </header>

        <div class="export-buttons">
            <button class="export-btn" onclick="exportPDF('sales')">Export Sales PDF</button>
            <button class="export-btn" onclick="exportPDF('inventory')">Export Inventory PDF</button>
            <button class="export-btn" onclick="exportPDF('customers')">Export Customers PDF</button>
            <button class="export-btn" onclick="exportPDF('profits')">Export Profits PDF</button>
        </div>

        <section class="metrics-grid">
            <div class="metric">
                <h3>Total Revenue</h3>
                <div class="metric-value">₫125M</div>
                <span class="metric-trend trend-up">+15% MoM</span>
            </div>
            <div class="metric">
                <h3>Orders</h3>
                <div class="metric-value">1,542</div>
                <span class="metric-trend trend-up">+12% MoM</span>
            </div>
            <div class="metric">
                <h3>Avg. Order Value</h3>
                <div class="metric-value">₫81K</div>
                <span class="metric-trend trend-up">+9% MoM</span>
            </div>
            <div class="metric">
                <h3>Conversion Rate</h3>
                <div class="metric-value">3.5%</div>
                <span class="metric-trend trend-up">+0.5% MoM</span>
            </div>
        </section>

        <section class="report" id="sales">
            <h2>Sales Overview</h2>
            <div class="report-grid">
                <div class="chart-wrap"><canvas id="sales-chart"></canvas></div>
                <div class="details">
                    <h3>Monthly Breakdown</h3>
                    <table class="table">
                        <thead><tr><th>Month</th><th>Sales</th><th>Orders</th><th>Growth</th></tr></thead>
                        <tbody>
                            <tr><td>Sep</td><td>₫45M</td><td>523</td><td>+13%</td></tr>
                            <tr><td>Oct</td><td>₫58M</td><td>612</td><td>+29%</td></tr>
                            <tr><td>Nov</td><td>₫125M</td><td>1,542</td><td>+115%</td></tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

        <section class="report" id="inventory">
            <h2>Inventory Insights</h2>
            <div class="report-grid">
                <div class="chart-wrap"><canvas id="inventory-chart"></canvas></div>
                <div class="details">
                    <h3>Stock Levels</h3>
                    <table class="table">
                        <thead><tr><th>Product</th><th>Stock</th><th>Sold</th><th>Reorder</th><th>Status</th></tr></thead>
                        <tbody>
                            <tr><td>Earbuds</td><td>245</td><td>1,230</td><td>150</td><td><span class="badge badge-green">Good</span></td></tr>
                            <tr><td>Hub</td><td>89</td><td>456</td><td>100</td><td><span class="badge badge-yellow">Low</span></td></tr>
                            <tr><td>Case</td><td>2,100</td><td>3,450</td><td>500</td><td><span class="badge badge-green">Good</span></td></tr>
                            <tr><td>Protector</td><td>45</td><td>892</td><td>200</td><td><span class="badge badge-red">Critical</span></td></tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

        <section class="report" id="customers">
            <h2>Customer Analytics</h2>
            <div class="report-grid">
                <div class="chart-wrap"><canvas id="customers-chart"></canvas></div>
                <div class="details">
                    <h3>Segments</h3>
                    <table class="table">
                        <thead><tr><th>Segment</th><th>Count</th><th>Avg Spend</th><th>Repeat %</th><th>LTV</th></tr></thead>
                        <tbody>
                            <tr><td>Premium</td><td>234</td><td>₫450K</td><td>75%</td><td>₫3.15M</td></tr>
                            <tr><td>Regular</td><td>1,025</td><td>₫120K</td><td>45%</td><td>₫540K</td></tr>
                            <tr><td>New</td><td>1,456</td><td>₫85K</td><td>12%</td><td>₫102K</td></tr>
                            <tr><td>At Risk</td><td>189</td><td>₫95K</td><td>5%</td><td>₫57K</td></tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

        <section class="report" id="profits">
            <h2>Profit Metrics</h2>
            <div class="report-grid">
                <div class="chart-wrap"><canvas id="profits-chart"></canvas></div>
                <div class="details">
                    <h3>Financials</h3>
                    <table class="table">
                        <thead><tr><th>Metric</th><th>This Month</th><th>Last Month</th><th>Change</th></tr></thead>
                        <tbody>
                            <tr><td>Revenue</td><td>₫125M</td><td>₫58M</td><td>+115%</td></tr>
                            <tr><td>COGS</td><td>₫45M</td><td>₫23M</td><td>+101%</td></tr>
                            <tr><td>Expenses</td><td>₫19M</td><td>₫9M</td><td>+111%</td></tr>
                            <tr><td>Profit</td><td>₫62M</td><td>₫27M</td><td>+129%</td></tr>
                            <tr><td>Margin</td><td>49%</td><td>46%</td><td>+3%</td></tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>

    <script>
        const options = {
            responsive: true,
            plugins: { legend: { position: 'top' } }
        };

        new Chart('sales-chart', {
            type: 'line',
            data: {
                labels: ['Sep', 'Oct', 'Nov'],
                datasets: [{ label: 'Sales', data: [45, 58, 125], borderColor: 'rgb(79,70,229)', fill: true }]
            },
            options
        });

        new Chart('inventory-chart', {
            type: 'bar',
            data: {
                labels: ['Earbuds', 'Hub', 'Case', 'Protector'],
                datasets: [
                    { label: 'Stock', data: [245, 89, 2100, 45], backgroundColor: 'rgb(124,58,237)' },
                    { label: 'Reorder', data: [150, 100, 500, 200], backgroundColor: 'rgb(239,68,68)' }
                ]
            },
            options
        });

        new Chart('customers-chart', {
            type: 'doughnut',
            data: {
                labels: ['Premium', 'Regular', 'New', 'Risk'],
                datasets: [{ data: [234, 1025, 1456, 189], backgroundColor: ['#4f46e5', '#7c3aed', '#ec4899', '#f97316'] }]
            },
            options
        });

        new Chart('profits-chart', {
            type: 'bar',
            data: {
                labels: ['Revenue', 'COGS', 'Expenses', 'Profit'],
                datasets: [
                    { label: 'This Month', data: [125, 45, 19, 62], backgroundColor: 'rgb(34,197,94)' },
                    { label: 'Last Month', data: [58, 23, 9, 27], backgroundColor: 'rgb(6,182,212)' }
                ]
            },
            options
        });

        function exportPDF(id) {
            const el = document.getElementById(id);
            if (!el) return alert('Section not found');
            html2pdf(el, {
                margin: 0.5,
                filename: `${id}.pdf`,
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2 },
                jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
            });
        }
    </script>
</body>
</html>