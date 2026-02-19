package com.example.contraap.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.contraap.ui.theme.*
import com.example.contraap.ui.components.PrimaryButton
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import kotlinx.datetime.*
import kotlinx.datetime.format.*

data class ServiceCategory(val id: Int, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceScreen(
    onBackClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<ServiceCategory?>(null) }
    var description by remember { mutableStateOf("") }
    var hasResources by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("Seleccionar fecha") }
    var selectedTime by remember { mutableStateOf("Seleccionar hora") }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val categories = remember {
        listOf(
            ServiceCategory(1, "Servicio de limpieza"),
            ServiceCategory(2, "Jardinería"),
            ServiceCategory(3, "Electricista"),
            ServiceCategory(4, "Plomería"),
            ServiceCategory(5, "Pintor"),
            ServiceCategory(6, "Albañilería")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Request Service",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Categoría de Servicio
            Text(
                text = "Categoría de Servicio",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { showCategoryDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedCategory != null) PrimaryBlue.copy(alpha = 0.1f) else BackgroundWhite,
                    contentColor = TextPrimary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (selectedCategory != null) PrimaryBlue else DividerGray
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategory?.name ?: "Seleccionar categoría",
                        fontSize = 15.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ¿Qué necesita arreglar?
            Text(
                text = "¿Qué necesita arreglar?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text(
                        "Describe el problema detalladamente. Por ejemplo:\nEl fregadero de la cocina gotea debajo del armario...",
                        color = TextSecondary.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = DividerGray,
                    focusedContainerColor = Color(0xFFFFF9E6),
                    unfocusedContainerColor = Color(0xFFFFF9E6)
                ),
                maxLines = 5
            )

            Spacer(Modifier.height(24.dp))

            // Tengo los recursos necesarios
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { hasResources = !hasResources }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasResources,
                    onCheckedChange = { hasResources = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryBlue,
                        uncheckedColor = TextSecondary
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Tengo los recursos necesarios",
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            }

            Spacer(Modifier.height(24.dp))

            // Add Photos
            Text(
                text = "Add Photos",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Optional",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            Spacer(Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { /* Agregar foto */ },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF9E6)
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar foto",
                                    tint = AccentYellow,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Add Photo",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Photos help contractors bring the right tools.",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(Modifier.height(24.dp))

            // Preferred Schedule
            Text(
                text = "Preferred Schedule",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = TextPrimary
            )

            Spacer(Modifier.height(12.dp))

            // Date
            ScheduleButton(
                icon = Icons.Default.CalendarMonth,
                label = "DATE",
                value = selectedDate,
                onClick = { showDatePicker = true }
            )

            Spacer(Modifier.height(12.dp))

            // Time
            ScheduleButton(
                icon = Icons.Default.Schedule,
                label = "TIME",
                value = selectedTime,
                onClick = { showTimePicker = true }
            )

            Spacer(Modifier.height(32.dp))

            // Botón Send Request
            PrimaryButton(
                text = "Send Request  ➤",
                onClick = onSubmit
            )

            Spacer(Modifier.height(24.dp))
        }
    }

    // Diálogo de categorías
    if (showCategoryDialog) {
        CategorySelectionDialog(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = {
                selectedCategory = it
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false }
        )
    }

    // Date Picker
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // Time Picker
    if (showTimePicker) {
        TimePickerDialog(
            onTimeSelected = { time ->
                selectedTime = time
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
fun ScheduleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = AccentYellow.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AccentYellow,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = TextSecondary
            )
        }
    }
}

@Composable
fun CategorySelectionDialog(
    categories: List<ServiceCategory>,
    selectedCategory: ServiceCategory?,
    onCategorySelected: (ServiceCategory) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Seleccionar Categoría",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                categories.forEach { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onCategorySelected(category) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCategory?.id == category.id)
                                PrimaryBlue.copy(alpha = 0.1f)
                            else
                                BackgroundWhite
                        ),
                        border = if (selectedCategory?.id == category.id)
                            androidx.compose.foundation.BorderStroke(2.dp, PrimaryBlue)
                        else
                            null
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = PrimaryBlue)
            }
        },
        containerColor = BackgroundWhite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

                        val monthNames = listOf(
                            "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
                        )
                        val formattedDate = "${monthNames[localDate.monthNumber - 1]} ${localDate.dayOfMonth}, ${localDate.year}"

                        onDateSelected(formattedDate)
                    }
                }
            ) {
                Text("OK", color = PrimaryBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = BackgroundWhite
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = AccentYellow,
                todayContentColor = PrimaryBlue,
                todayDateBorderColor = PrimaryBlue
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val hour = if (timePickerState.hour == 0) {
                        12
                    } else if (timePickerState.hour > 12) {
                        timePickerState.hour - 12
                    } else {
                        timePickerState.hour
                    }
                    val amPm = if (timePickerState.hour >= 12) "PM" else "AM"
                    val time = "${hour.toString().padStart(2, '0')}:${timePickerState.minute.toString().padStart(2, '0')} $amPm"
                    onTimeSelected(time)
                }
            ) {
                Text("OK", color = PrimaryBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = PrimaryBlue.copy(alpha = 0.1f),
                    selectorColor = AccentYellow,
                    timeSelectorSelectedContainerColor = AccentYellow,
                    timeSelectorSelectedContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundWhite
    )
}