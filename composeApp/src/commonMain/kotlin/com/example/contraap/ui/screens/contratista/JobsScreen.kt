package com.example.contraap.ui.screens.contractor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// 🎨 Colores
private val LightBackground = Color(0xFFF5F7FA)
private val CardBlue = Color(0xFF4FC3F7)
private val ButtonYellow = Color(0xFFFFCA28)
private val TextDark = Color(0xFF2D3142)
private val TextGray = Color(0xFF9098B1)
private val GreenSuccess = Color(0xFF4CAF50)
private val RedError = Color(0xFFE53935)

// 📦 Modelo
data class Job(
    val id: String,
    val title: String,
    val client: String,
    val date: String,
    val time: String,
    val price: String,
    val status: JobStatus,
    val distance: String
)

enum class JobStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(navController: NavController) {

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Todos", "Pendientes", "En Progreso", "Completados")

    val jobs = remember {
        listOf(
            Job("1", "Reparación de Plomería", "Juan Pérez", "Hoy", "02:00 PM", "$85.00", JobStatus.IN_PROGRESS, "3.2 km"),
            Job("2", "Instalación Eléctrica", "María García", "Mañana", "10:00 AM", "$150.00", JobStatus.PENDING, "5.1 km"),
            Job("3", "Pintura de Casa", "Carlos López", "23 Oct", "09:00 AM", "$300.00", JobStatus.COMPLETED, "2.8 km")
        )
    }

    Scaffold(
        containerColor = LightBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Trabajos",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = CardBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // 🔹 Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = LightBackground,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.BottomStart)
                                .offset(x = tabPositions[selectedTab].left)
                                .width(tabPositions[selectedTab].width)
                                .height(3.dp)
                                .background(ButtonYellow)
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index)
                                    FontWeight.Bold
                                else
                                    FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        },
                        selectedContentColor = TextDark,
                        unselectedContentColor = TextGray
                    )
                }
            }

            // 🔹 Lista de trabajos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                val filteredJobs = jobs.filter {
                    when (selectedTab) {
                        1 -> it.status == JobStatus.PENDING
                        2 -> it.status == JobStatus.IN_PROGRESS
                        3 -> it.status == JobStatus.COMPLETED
                        else -> true
                    }
                }

                items(filteredJobs) { job ->
                    JobCard(job, navController)
                }
            }
        }
    }
}

@Composable
fun JobCard(job: Job, navController: NavController) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            navController.navigate("job_detail/${job.id}")
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    job.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                StatusChip(job.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(job.client, fontSize = 14.sp, color = TextGray)

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(job.distance, fontSize = 14.sp, color = TextGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = CardBlue,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "${job.date} • ${job.time}",
                        fontSize = 13.sp,
                        color = TextDark,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    job.price,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CardBlue
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: JobStatus) {

    val (backgroundColor, textColor, text) = when (status) {
        JobStatus.PENDING ->
            Triple(ButtonYellow.copy(alpha = 0.2f), ButtonYellow, "Pendiente")

        JobStatus.IN_PROGRESS ->
            Triple(CardBlue.copy(alpha = 0.2f), CardBlue, "En Progreso")

        JobStatus.COMPLETED ->
            Triple(GreenSuccess.copy(alpha = 0.2f), GreenSuccess, "Completado")

        JobStatus.CANCELLED ->
            Triple(RedError.copy(alpha = 0.2f), RedError, "Cancelado")
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}