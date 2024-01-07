package com.mwaibanda.graphql_multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform