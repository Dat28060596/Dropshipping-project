package com.onlinestore.api;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/analytics/export-pdf")
public class AnalyticsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/pdf");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition", "attachment; filename=\"report.pdf\"");

        try {
            // Read request body
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JsonObject requestData = gson.fromJson(sb.toString(), JsonObject.class);
            String reportType = requestData.get("reportType").getAsString();

            // Generate appropriate report
            generateReport(response, reportType);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error generating report: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        try {
            String reportType = request.getParameter("type");
            JsonObject reportData = getAnalyticsData(reportType);
            response.getWriter().write(gson.toJson(reportData));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("message", "Failed to fetch analytics data");
            response.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private void generateReport(HttpServletResponse response, String reportType) throws IOException {
        // In production, use a PDF library like iText or Apache PDFBox
        // For now, return a simple PDF header with HTML content
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().write("PDF Report Content".getBytes());
    }

    private JsonObject getAnalyticsData(String reportType) {
        JsonObject data = new JsonObject();

        switch (reportType) {
            case "sales":
                data = getSalesData();
                break;
            case "inventory":
                data = getInventoryData();
                break;
            case "customer":
                data = getCustomerData();
                break;
            case "profitability":
                data = getProfitabilityData();
                break;
            default:
                data.addProperty("error", "Unknown report type");
        }

        return data;
    }

    private JsonObject getSalesData() {
        JsonObject data = new JsonObject();
        data.addProperty("totalSales", 125500000);
        data.addProperty("totalOrders", 1542);
        data.addProperty("avgOrderValue", 81400);
        data.addProperty("conversionRate", 3.45);

        JsonArray monthlySales = new JsonArray();
        monthlySales.add(createMonthData("September", 45200000, 523));
        monthlySales.add(createMonthData("October", 58300000, 612));
        monthlySales.add(createMonthData("November", 125500000, 1542));
        data.add("monthlySales", monthlySales);

        return data;
    }

    private JsonObject getInventoryData() {
        JsonObject data = new JsonObject();
        
        JsonArray products = new JsonArray();
        products.add(createProductData("Wireless Earbuds Pro", 245, 1230, 150, "In Stock"));
        products.add(createProductData("USB-C Hub", 89, 456, 100, "Low Stock"));
        products.add(createProductData("Phone Case", 2100, 3450, 500, "In Stock"));
        products.add(createProductData("Screen Protector", 45, 892, 200, "Critical"));
        data.add("products", products);

        return data;
    }

    private JsonObject getCustomerData() {
        JsonObject data = new JsonObject();
        
        JsonArray segments = new JsonArray();
        segments.add(createSegmentData("Premium Customers", 234, 450000, 75, 3150000));
        segments.add(createSegmentData("Regular Customers", 1025, 120000, 45, 540000));
        segments.add(createSegmentData("New Customers", 1456, 85000, 12, 102000));
        segments.add(createSegmentData("At Risk", 189, 95000, 5, 57000));
        data.add("segments", segments);

        return data;
    }

    private JsonObject getProfitabilityData() {
        JsonObject data = new JsonObject();
        
        data.addProperty("totalRevenue", 125500000);
        data.addProperty("costOfGoods", 45200000);
        data.addProperty("operatingExpenses", 18750000);
        data.addProperty("netProfit", 61550000);
        data.addProperty("profitMargin", 49.0);

        JsonArray monthly = new JsonArray();
        monthly.add(createFinancialMonth("This Month", 125500000, 45200000, 18750000, 61550000));
        monthly.add(createFinancialMonth("Last Month", 58300000, 22500000, 8900000, 26900000));
        data.add("monthly", monthly);

        return data;
    }

    private JsonObject createMonthData(String month, double sales, int orders) {
        JsonObject obj = new JsonObject();
        obj.addProperty("month", month);
        obj.addProperty("sales", sales);
        obj.addProperty("orders", orders);
        return obj;
    }

    private JsonObject createProductData(String name, int stock, int sold, int reorderLevel, String status) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("stock", stock);
        obj.addProperty("sold", sold);
        obj.addProperty("reorderLevel", reorderLevel);
        obj.addProperty("status", status);
        return obj;
    }

    private JsonObject createSegmentData(String name, int count, double avgSpend, double repeatRate, double ltv) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("count", count);
        obj.addProperty("avgSpend", avgSpend);
        obj.addProperty("repeatRate", repeatRate);
        obj.addProperty("ltv", ltv);
        return obj;
    }

    private JsonObject createFinancialMonth(String month, double revenue, double cogs, double expenses, double profit) {
        JsonObject obj = new JsonObject();
        obj.addProperty("month", month);
        obj.addProperty("revenue", revenue);
        obj.addProperty("cogs", cogs);
        obj.addProperty("expenses", expenses);
        obj.addProperty("profit", profit);
        return obj;
    }
}
