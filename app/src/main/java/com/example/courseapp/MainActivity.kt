package com.example.courseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseapp.ui.theme.CourseAppTheme
import com.example.courseapp.model.Course
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseAppTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}



@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            CourseList(courses = sampleCourses())
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun CourseList(
    modifier: Modifier = Modifier,
    courses: List<Course>
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(courses) { course ->
            CourseCard(course = course)
        }
    }
}

@Composable
private fun CourseCard(course: Course, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 32.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "CourseCardPadding"
    )

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = extraPadding.coerceAtLeast(0.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Code: ${course.code}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Credit: ${course.credit}", style = MaterialTheme.typography.bodyMedium)

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description: ${course.description}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Prerequisites: ${course.prerequisites}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

fun sampleCourses(): List<Course> {
    return listOf(
        Course("Mobile App Development", "CS301", 3, "Introduction to Android apps with Compose.", "CS201"),
        Course("computer graphics", "CS202", 4, "Covers fundamental computer graphics.", "CS101"),
        Course("Operating Systems", "CS303", 3, "Process management, memory, file systems.", "CS202"),
        Course("cyber security", "CS304", 4, "Design and implementation of database systems, SQL, and data modeling.", "CS202"),
        Course("Artificial Intelligence", "CS401", 3, "Introduction to AI concepts, machine learning, and neural networks.", "CS202, CS301")
    )
}

@Preview(showBackground = true)
@Composable
fun CourseListLightPreview() {
    CourseAppTheme(darkTheme = false) {
        CourseList(courses = sampleCourses())
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CourseListDarkPreview() {
    CourseAppTheme(darkTheme = true) {
        CourseList(courses = sampleCourses())
    }
}



