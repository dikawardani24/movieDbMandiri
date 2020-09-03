package wardani.dika.moviedbmandiri.model

import wardani.dika.moviedbmandiri.api.response.ProductionCompanyResponse

data class ProductionCompany(
        var id: Int,
        var name: String,
        var logoPath: String?,
        var originCountry: String
) {
    companion object {
        fun from(productionCompanyResponse: ProductionCompanyResponse): ProductionCompany {
            return ProductionCompany(
                    id = productionCompanyResponse.id,
                    name = productionCompanyResponse.name,
                    logoPath = productionCompanyResponse.logoPath,
                    originCountry = productionCompanyResponse.originCountry
            )
        }
    }
}