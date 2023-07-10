package microsleader.george.myvehiclesinfo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class MenuAction(
    @StringRes val label: Int,
    @DrawableRes val icon: Int) {

    object Share : MenuAction(R.string.log_out, R.drawable.ic_launcher_foreground)}