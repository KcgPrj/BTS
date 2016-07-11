package net.orekyuu.bts.service

import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.Product
import net.orekyuu.bts.domain.Team
import net.orekyuu.bts.message.product.ProductInfo
import net.orekyuu.bts.message.team.TeamInfo
import org.jetbrains.exposed.sql.transactions.transaction

interface ProductService {

    /**
     * プロダクトをチームに登録
     */
    fun registerToTeam(requestUser: AppUser, teamId: String, productName: String): TeamInfo

    /**
     * プロダクトをチームから削除
     */
    fun deleteFromTeam(requestUser: AppUser, teamId: String, productId: Int): TeamInfo

    /**
     * プロダクトの情報を取得
     */
    fun showProductInfo(requestUser: AppUser, teamId: String, productId: Int): ProductInfo
}

class ProductServiceImpl : ProductService {

    override fun registerToTeam(requestUser: AppUser, teamId: String, productName: String): TeamInfo = transaction {
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.new {
            this.productName = productName
            this.team = team
        }
        ofTeamInfo(product.team)
    }

    override fun deleteFromTeam(requestUser: AppUser, teamId: String, productId: Int): TeamInfo = transaction {
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.findById(productId) ?: throw ProductNotFoundException(team, productId)
        product.delete()
        ofTeamInfo(team)
    }

    override fun showProductInfo(requestUser: AppUser, teamId: String, productId: Int): ProductInfo = transaction {
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.findById(productId) ?: throw ProductNotFoundException(team, productId)
        ofProductInfo(product)
    }

    private fun checkAuthority(team: Team, appUser: AppUser) {
        if (!team.member.any { it.id == appUser.id })
            throw TeamAccessAuthorityNotException(appUser, team)
    }
}