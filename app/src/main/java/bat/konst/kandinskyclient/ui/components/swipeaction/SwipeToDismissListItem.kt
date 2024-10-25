package bat.konst.kandinskyclient.ui.components.swipeaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun SwipeToDismissListItem(
    modifier: Modifier = Modifier,
    onEndToStart: () -> Unit = {},
    onStartToEnd: () -> Unit = {},
    content: @Composable () -> Unit
) {

    // 1. State is hoisted here
    val dismissState = rememberSwipeToDismissBoxState(
        // confirmValueChange = { false },
        positionalThreshold =  { it* 0.50f } // 50% от общей ширины
    )

    // haptic эффект
    var willDismissValue: SwipeToDismissBoxValue? by remember {
        mutableStateOf(null)
    }
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(key1 = willDismissValue, block = {
        if (willDismissValue != null) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    })


    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        backgroundContent = {

            // 2. Animate the swipe by changing the color
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surfaceVariant
                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                },
                label = "swipe"
            )
            // 2.1 animate haptic
            willDismissValue = dismissState.targetValue

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color) // 3. Set the animated color here
            ) {

                // 4. Show the correct icon
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> {
                        Icon(
                            modifier = Modifier
                                .align(androidx.compose.ui.Alignment.CenterStart)
                                .padding(start = 16.dp),
                            imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                            contentDescription = "edit"
                        )
                    }

                    SwipeToDismissBoxValue.EndToStart -> {
                        Icon(
                            modifier = Modifier
                                .align(androidx.compose.ui.Alignment.CenterEnd)
                                .padding(end = 16.dp),
                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                            contentDescription = "delete"
                        )
                    }

                    SwipeToDismissBoxValue.Settled -> {
                        // Nothing to do
                    }
                }

            }
        }
    ) {
        content()
    }

    // 5. Trigger the callbacks
    when (dismissState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(dismissState.currentValue) {
                onEndToStart()

                // 6. Don't forget to reset the state value
                dismissState.snapTo(SwipeToDismissBoxValue.Settled) // or dismissState.reset()
            }

        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(dismissState.currentValue) {
                onStartToEnd()
                dismissState.snapTo(SwipeToDismissBoxValue.Settled) // or dismissState.reset()
            }
        }

        SwipeToDismissBoxValue.Settled -> {
            // Nothing to do
        }
    }
}