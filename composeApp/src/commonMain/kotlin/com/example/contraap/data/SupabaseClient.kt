package com.example.contraap.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://spixodmhwmtqisytorhd.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNwaXhvZG1od210cWlzeXRvcmhkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njg4NTQ2ODMsImV4cCI6MjA4NDQzMDY4M30.ta-RT8w_UhRmXMpqkHbAY7AcpJD1xJZhSaNZWWZcPJI"
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
        install(Storage)
    }
}