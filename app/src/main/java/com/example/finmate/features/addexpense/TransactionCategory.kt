package com.example.finmate.features.model

enum class TransactionCategory(val label: String, val type: TransactionType?) {
    // Income
    SALARY("Salary", TransactionType.INCOME),
    FREELANCING("Freelancing", TransactionType.INCOME),
    BUSINESS("Business", TransactionType.INCOME),
    INVESTMENT("Investment", TransactionType.INCOME),
    GIFTS_AND_DONATIONS("Gifts & Donations", null),
    GIFT("Gift", TransactionType.INCOME),

    // Expense
    FOOD("Food", TransactionType.EXPENSE),
    TRANSPORT("Transport", TransactionType.EXPENSE),
    RENT("Rent", TransactionType.EXPENSE),
    UTILITIES("Utilities", TransactionType.EXPENSE),
    ENTERTAINMENT("Entertainment", TransactionType.EXPENSE),
    SHOPPING("Shopping", TransactionType.EXPENSE),
    HEALTH("Health", TransactionType.EXPENSE),
    EDUCATION("Education", TransactionType.EXPENSE),
    PERSONAL_CARE("Personal Care", TransactionType.EXPENSE),
    DONATION("Donation", TransactionType.EXPENSE),

    // Common
    OTHERS("Others", null);

    companion object {

        fun getCategoriesByType(type: TransactionType): List<TransactionCategory> {
            return values().filter { it.type == type || it.type == null }
        }

        val subCategoryMap = mapOf(
            // INCOME
            SALARY to listOf("Monthly Salary", "Annual Bonus", "Overtime"),
            FREELANCING to listOf("Upwork", "Fiverr", "Direct Clients", "Freelance Projects"),
            BUSINESS to listOf("Product Sales", "Service Revenue", "Business Investment Return"),
            INVESTMENT to listOf("Stocks", "Mutual Funds", "Dividends", "Crypto", "Real Estate"),
            GIFT to listOf("Cash Gift", "Gift Card", "Birthday Gift", "Wedding Gift", "Holiday Gift", "Inheritance"),

            // EXPENSE
            FOOD to listOf("Groceries", "Dining Out", "Snacks", "Cafes", "Takeaway"),
            TRANSPORT to listOf("Bus", "Train", "Taxi", "Fuel", "Car Maintenance", "Flight"),
            RENT to listOf("Home Rent", "Office Rent", "Storage Rent"),
            UTILITIES to listOf("Electricity", "Water", "Internet", "Gas", "Mobile Recharge"),
            ENTERTAINMENT to listOf("Movies", "Concerts", "Games", "Streaming Services", "Events"),
            SHOPPING to listOf("Clothes", "Electronics", "Accessories", "Home Decor", "Gadgets"),
            HEALTH to listOf("Medicines", "Doctor Visits", "Health Insurance", "Fitness", "Therapy"),
            EDUCATION to listOf("Tuition", "Books", "Online Courses", "Workshops", "Exam Fees"),
            PERSONAL_CARE to listOf("Salon", "Spa", "Haircut", "Skincare", "Cosmetics", "Toiletries"),
            DONATION to listOf("Charity", "Religious Offering", "NGO Contributions", "Fundraisers", "Crowdfunding", "Zakat / Tithe"),

                    // COMMON
            OTHERS to listOf("Miscellaneous", "Uncategorized")
        )
    }
}
