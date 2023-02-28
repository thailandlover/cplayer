//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <dowplay/dowplay_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) dowplay_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "DowplayPlugin");
  dowplay_plugin_register_with_registrar(dowplay_registrar);
}
