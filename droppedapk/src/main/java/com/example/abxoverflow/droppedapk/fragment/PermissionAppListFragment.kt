package com.example.abxoverflow.droppedapk.fragment

import android.content.pm.PackageInfo
import android.view.View
import androidx.core.content.edit
import com.example.abxoverflow.droppedapk.Mods
import com.example.abxoverflow.droppedapk.R
import com.example.abxoverflow.droppedapk.databinding.PreferenceMaterialswitchBinding
import com.example.abxoverflow.droppedapk.utils.injectedPreferences
import com.google.android.material.materialswitch.MaterialSwitch

/**
 * System-only fragment that lists installed packages and allows disabling all permission checks for them.
 */
class PermissionAppListFragment : BaseAppListFragment() {
    override fun onAppClicked(target: View, pkg: String, position: Int) {
        context?.let {
            target.findViewById<MaterialSwitch>(R.id.switchWidget).let { switch ->
                switch.isChecked = !switch.isChecked
            }
        }
    }

    override fun bindToolButtons(holder: VH, pkg: String): List<View> = listOf(
        PreferenceMaterialswitchBinding.inflate(layoutInflater).root.apply {
            isChecked = injectedPreferences
                .getStringSet("permission_pkg_whitelist", emptySet())
                ?.contains(pkg) == true

            setOnCheckedChangeListener { _, bool ->
                val old = injectedPreferences.getStringSet("permission_pkg_whitelist", emptySet()) ?: emptySet()
                val new = if (bool) old + pkg else old - pkg
                injectedPreferences.edit(commit = true) {
                    putStringSet("permission_pkg_whitelist", new)
                }
                Mods.applyPermissionWhitelist()
            }
        }
    )

    override fun shouldShowPackage(info: PackageInfo) = true
    override fun queryPackageStatus(pkgName: String): String = ""
}
