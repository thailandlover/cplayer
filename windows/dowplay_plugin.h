#ifndef FLUTTER_PLUGIN_DOWPLAY_PLUGIN_H_
#define FLUTTER_PLUGIN_DOWPLAY_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace dowplay {

class DowplayPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  DowplayPlugin();

  virtual ~DowplayPlugin();

  // Disallow copy and assign.
  DowplayPlugin(const DowplayPlugin&) = delete;
  DowplayPlugin& operator=(const DowplayPlugin&) = delete;

 private:
  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace dowplay

#endif  // FLUTTER_PLUGIN_DOWPLAY_PLUGIN_H_
