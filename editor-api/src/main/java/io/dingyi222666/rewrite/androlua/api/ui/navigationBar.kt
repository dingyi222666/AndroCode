package io.dingyi222666.rewrite.androlua.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.api.service.IServiceRegistry
import io.dingyi222666.rewrite.androlua.api.service.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationBarService internal constructor(
    serviceRegistry: IServiceRegistry
) : Service {

    override val name = "navigationBar"

    override val registry: IServiceRegistry = serviceRegistry

    private val navigationBarItemList = mutableListOf<NavigationBarItem>()

    private val _navigationBarItemFlow =
        MutableStateFlow<List<NavigationBarItem>>(navigationBarItemList)

    val navigationBarItemFlow = _navigationBarItemFlow.asStateFlow()

    fun registerItem(
        id: String,
        icon: ImageVector,
        description: String? = null,
        onClick: (() -> Unit)? = null,
        content: (@Composable () -> Unit)? = null,
        panel: (@Composable () -> Unit)? = null
    ) {
        navigationBarItemList.add(
            NavigationBarItemImpl(
                id,
                icon,
                description,
                onClick,
                content,
                panel
            )
        )

        updateFlow()
    }

    fun removeItem(id: String) {
        val index = navigationBarItemList.indexOfFirst { it.id == id }

        if (index != -1) {
            navigationBarItemList.removeAt(index)
        }

        updateFlow()
    }

    private fun updateFlow() {
        val coroutineService = AndroLua.coroutine

        coroutineService.rootCoroutine.launch {
            _navigationBarItemFlow.emit(
                navigationBarItemList.toList()
            )
        }
    }
}

data class NavigationBarItemImpl(
    override val id: String,
    override val icon: ImageVector,
    override val description: String?,
    override val onClick: (() -> Unit)?,
    override val content: (@Composable () -> Unit)?,
    override val panel: (@Composable () -> Unit)?
) : NavigationBarItem


interface NavigationBarItem {

    val description: String?

    val id: String

    val icon: ImageVector

    val onClick: (() -> Unit)?

    val panel: (@Composable () -> Unit)?

    val content: (@Composable () -> Unit)?
}

fun createNavigationBarService(serviceRegistry: IServiceRegistry): NavigationBarService {
    return NavigationBarService(serviceRegistry)
}