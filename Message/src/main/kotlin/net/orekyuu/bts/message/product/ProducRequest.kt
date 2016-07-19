package net.orekyuu.bts.message.product

class CreateProductRequest(
        val productName: String = ""
)

class DeleteProductRequest(
        val productId: Int = 0
)

class ModifyProductRequest(
        val productId: Int = 0,
        val newName: String = ""
)

class RegenerateTokenRequest(
        val productId: Int = 0
)
