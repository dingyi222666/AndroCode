package io.dingyi222666.rewrite.androlua.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import io.dingyi222666.rewrite.androlua.api.AndroLua
import io.dingyi222666.rewrite.androlua.api.context.Context
import io.dingyi222666.rewrite.androlua.api.context.Service
import io.dingyi222666.rewrite.androlua.api.coroutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationBarService internal constructor(
    serviceRegistry: Context
) : Service {

    override val id = "navigationBar"

    override val ctx: Context = serviceRegistry

    private val navigationBarItemList = mutableListOf<NavigationBarItem>()

    private val _navigationBarItemFlow =
        MutableStateFlow<List<NavigationBarItem>>(navigationBarItemList)

    val navigationBarItemFlow = _navigationBarItemFlow.asStateFlow()

    fun registerItem(
        id: String,
        icon: ImageVector,
        description: String? = null,
        onClick: (() -> Unit)? = null,
        slot: NavigationBarSlot? = null
    ) {
        navigationBarItemList.add(
            NavigationBarItemImpl(
                id,
                icon,
                description,
                onClick,
                slot
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

interface NavigationBarSlot {
    @Composable
    fun panel()

    @Composable
    fun slot()
}

data class NavigationBarItemImpl(
    override val id: String,
    override val icon: ImageVector,
    override val description: String?,
    override val onClick: (() -> Unit)?,
    override val slot: NavigationBarSlot?
) : NavigationBarItem


interface NavigationBarItem {

    val description: String?

    val id: String

    val icon: ImageVector

    val onClick: (() -> Unit)?

    val slot: NavigationBarSlot?
}

fun createNavigationBarService(serviceRegistry: Context): NavigationBarService {
    return NavigationBarService(serviceRegistry)
}