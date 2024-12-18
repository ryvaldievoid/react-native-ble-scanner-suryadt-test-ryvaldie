package com.blescannersuryadttestryvaldie.dtos

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

data class DeviceDataDtoItem(
    val id: String,
    val name: String,
    val signal: String,
    val address: String
) {
  fun toWritableMap(): WritableMap {
    val map = Arguments.createMap()
    map.putString("id", this.id)
    map.putString("name", this.name)
    map.putString("signal", this.signal)
    map.putString("address", this.address)

    return map
  }
}
