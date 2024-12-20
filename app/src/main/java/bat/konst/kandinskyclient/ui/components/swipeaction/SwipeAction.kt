package bat.konst.kandinskyclient.ui.components.swipeaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun SwipeAction(
    modifier: Modifier = Modifier,
    onEndToStart: (() -> Unit)? = null,
    onStartToEnd: (() -> Unit)? = null,
    relativeThreshold: Float = 0.50f, // перемещение для срабатывания = 50% от общей ширины
    backgroundColorSettled: Color = MaterialTheme.colorScheme.surfaceVariant,  // цвет под карточкой
    backgroundColorStartToEnd: Color = MaterialTheme.colorScheme.primaryContainer,  // цвет под карточкой при движении к концу
    backgroundColorEndToStart: Color = MaterialTheme.colorScheme.errorContainer, // цвет под карточкой при движении к началу
    iconStartToEnd: ImageVector? = androidx.compose.material.icons.Icons.Default.Edit,
    iconEndToStart: ImageVector? = androidx.compose.material.icons.Icons.Default.Delete,
    content: @Composable () -> Unit
) {

    // 1. State is hoisted here
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold =  { it * relativeThreshold } // 50% от общей ширины
    )

    // haptic effect
    var willDismissValue: SwipeToDismissBoxValue? by remember {
        mutableStateOf(null)
    }
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(key1 = willDismissValue, block = {
        if (willDismissValue != null && willDismissValue != SwipeToDismissBoxValue.Settled) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    })


    SwipeToDismissBox(
        modifier = modifier,
        enableDismissFromStartToEnd = onStartToEnd != null,
        enableDismissFromEndToStart = onEndToStart != null,
        state = dismissState,
        backgroundContent = {

            // 2. Animate the swipe by changing the color
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> backgroundColorSettled
                    SwipeToDismissBoxValue.StartToEnd -> backgroundColorStartToEnd
                    SwipeToDismissBoxValue.EndToStart -> backgroundColorEndToStart
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
                        if (iconStartToEnd != null) {
                            Icon(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.CenterStart)
                                    .padding(start = 16.dp),
                                imageVector = iconStartToEnd,
                                contentDescription = "edit"
                            )
                        }
                    }

                    SwipeToDismissBoxValue.EndToStart -> {
                        if (iconEndToStart != null) {
                            Icon(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp),
                                imageVector = iconEndToStart,
                                contentDescription = "delete"
                            )
                        }
                    }

                    SwipeToDismissBoxValue.Settled -> {
                        if (iconStartToEnd != null) {
                            Icon(
                                modifier = Modifier
                                    .size(38.dp)
                                    .align(Alignment.CenterStart)
                                    .padding(start = 16.dp),
                                imageVector = iconStartToEnd,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                        if (iconEndToStart != null) {
                            Icon(
                                modifier = Modifier
                                    .size(38.dp)
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp),
                                imageVector = iconEndToStart,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
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
                if (onEndToStart != null) {
                    onEndToStart()
                }
                // 6. Don't forget to reset the state value
                dismissState.snapTo(SwipeToDismissBoxValue.Settled) // or dismissState.reset()
            }

        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(dismissState.currentValue) {
                if (onStartToEnd != null) {
                    onStartToEnd()
                }
                dismissState.snapTo(SwipeToDismissBoxValue.Settled) // or dismissState.reset()
            }
        }

        SwipeToDismissBoxValue.Settled -> {
            // Nothing to do
        }
    }
}
