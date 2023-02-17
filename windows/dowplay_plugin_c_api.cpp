#include "include/dowplay/dowplay_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "dowplay_plugin.h"

void DowplayPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  dowplay::DowplayPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
