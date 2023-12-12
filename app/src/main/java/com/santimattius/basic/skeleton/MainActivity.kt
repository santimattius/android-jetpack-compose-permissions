package com.santimattius.basic.skeleton

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.santimattius.basic.skeleton.ui.component.BasicSkeletonContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicSkeletonContainer {
                MainRoute()
            }
        }
    }
}

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val currentContext = LocalContext.current
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
            if (isSuccessful) {
                //open viewer
            } else {
                Toast.makeText(currentContext, "Picture no taked", Toast.LENGTH_SHORT).show()
            }
        }
    val state by viewModel.state.collectAsStateWithLifecycle()
    MainScreen(
        state = state,
        onLaunchCamera = { cameraLauncher.launch(it) }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    state: MainUiState,
    onLaunchCamera: (Uri) -> Unit = {},
) {
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (cameraPermissionState.status.isGranted) {
                    onLaunchCamera(state.uri)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = stringResource(R.string.text_desc_main_action)
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            val textToShow = when {
                cameraPermissionState.status.isGranted -> {
                    "Camera permission Granted"
                }

                cameraPermissionState.status.shouldShowRationale -> {
                    "The camera is important for this app. Please grant the permission."
                }

                else -> {
                    "Camera permission required for this feature to be available. \n" +
                            "Please grant the permission"
                }
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = textToShow,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicSkeletonContainer {
        MainScreen(
            state = MainUiState(isLoading = false, message = "Hello Android"),
        )
    }
}