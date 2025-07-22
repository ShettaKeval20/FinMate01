package com.example.finmate.features.model

fun suggestCategoryFromText(input: String): TransactionCategory {
    val lowercaseInput = input.lowercase()

    return when {
        // FOOD
        listOf("zomato", "swiggy", "restaurant", "dinner", "lunch", "snack", "groceries", "takeaway", "cafe").any { it in lowercaseInput } ->
            TransactionCategory.FOOD

        // ENTERTAINMENT
        listOf("netflix", "prime", "spotify", "hotstar", "movie", "concert", "game", "event", "streaming").any { it in lowercaseInput } ->
            TransactionCategory.ENTERTAINMENT

        // TRANSPORT
        listOf("uber", "ola", "bus", "train", "taxi", "cab", "transport", "fuel", "flight", "car maintenance").any { it in lowercaseInput } ->
            TransactionCategory.TRANSPORT

        // RENT
        listOf("home rent", "office rent", "storage rent").any { it in lowercaseInput } ->
            TransactionCategory.RENT

        // UTILITIES
        listOf("rent", "electricity", "water", "internet", "wifi", "gas", "bills", "recharge").any { it in lowercaseInput } ->
            TransactionCategory.UTILITIES

        // SHOPPING
        listOf("shopping", "clothes", "electronics", "accessories", "gadgets", "home decor").any { it in lowercaseInput } ->
            TransactionCategory.SHOPPING

        // HEALTH
        listOf("health", "doctor", "medicine", "fitness", "therapy", "hospital", "insurance").any { it in lowercaseInput } ->
            TransactionCategory.HEALTH

        // EDUCATION
        listOf("education", "books", "tuition", "course", "exam", "fees", "learning", "workshop").any { it in lowercaseInput } ->
            TransactionCategory.EDUCATION

        // FREELANCING
        listOf("upwork", "fiverr", "freelance", "client", "gig", "side hustle").any { it in lowercaseInput } ->
            TransactionCategory.FREELANCING

        // SALARY
        listOf("salary", "paycheck", "income", "bonus", "overtime").any { it in lowercaseInput } ->
            TransactionCategory.SALARY

        // BUSINESS
        listOf("business", "sales", "revenue", "client payment", "startup").any { it in lowercaseInput } ->
            TransactionCategory.BUSINESS

        // INVESTMENT
        listOf("investment", "mutual fund", "stock", "crypto", "dividend", "real estate").any { it in lowercaseInput } ->
            TransactionCategory.INVESTMENT

//        Personal Care
        listOf("salon", "spa", "haircut", "cosmetics", "makeup", "skincare", "personal care", "toiletries").any { it in lowercaseInput } ->
            TransactionCategory.PERSONAL_CARE




        else -> TransactionCategory.OTHERS
    }
}
