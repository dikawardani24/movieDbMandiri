package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.ProductionCountryResponse

data class ProductionCountry(
        var iso31661: String,
        var name: String
) {
    companion object {
        fun from(productionCountryResponse: ProductionCountryResponse): ProductionCountry {
            return ProductionCountry(
                    iso31661 = productionCountryResponse.iso31661,
                    name = productionCountryResponse.name
            )
        }
    }
}