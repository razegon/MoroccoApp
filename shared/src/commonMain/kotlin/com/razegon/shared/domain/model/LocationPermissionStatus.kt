package com.razegon.shared.domain.model


enum class LocationPermissionStatus {
    NOT_DETERMINED,

    GRANTED_APPROXIMATE,
    GRANTED_PRECISE,

    DENIED,
    DENIED_PERMANENTLY,

    RESTRICTED,
    SERVICES_DISABLED
}
