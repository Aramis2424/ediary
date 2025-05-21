package org.srd.ediary_cli.util

import org.srd.ediary_cli.model.OwnerInfoDTO

object LocalStorage {
    var currentOwner: OwnerInfoDTO? = null
    var token : String? = null
}