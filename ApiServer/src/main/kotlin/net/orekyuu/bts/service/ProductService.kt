package net.orekyuu.bts.service

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.message.product.ProductInfo
import net.orekyuu.bts.message.team.TeamInfo
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

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
     * チームからプロダクトの一覧を取得
     */
    fun showProductsFromTeam(requestUser: AppUser, teamId: String): List<ProductInfo>

    /**
     *  プロダクトの名前を変更
     */
    fun modifyProductName(requestUser: AppUser, teamId: String, productId: Int, productName: String): ProductInfo

    /**
     * プロダクトトークンの再生成を行う
     */
    fun regenerateProductToken(requestUser: AppUser, teamId: String, productId: Int): ProductInfo
}

class ProductServiceImpl : ProductService {

    override fun registerToTeam(requestUser: AppUser, teamId: String, productName: String): TeamInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.new {
            this.productName = productName
            this.team = team
        }
        ofTeamInfo(product.team)
    }

    override fun deleteFromTeam(requestUser: AppUser, teamId: String, productId: Int): TeamInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.findById(productId) ?: throw ProductNotFoundException(team, productId)
        product.delete()
        ofTeamInfo(team)
    }

    override fun showProductsFromTeam(requestUser: AppUser, teamId: String): List<ProductInfo> = transaction {
        logger.addLogger(StdOutSqlLogger())
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val products = Product.find { ProductTable.team.eq(team.id) }
        products.asSequence().map { ofProductInfo(it) }.toList()
    }

    override fun modifyProductName(requestUser: AppUser, teamId: String,
                                   productId: Int, productName: String): ProductInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.findById(productId) ?: throw ProductNotFoundException(team, productId)
        product.productName = productName
        ofProductInfo(product)
    }

    override fun regenerateProductToken(requestUser: AppUser, teamId: String, productId: Int): ProductInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        checkAuthority(team, requestUser)
        val product = Product.findById(productId) ?: throw ProductNotFoundException(team, productId)
        product.productToken = UUID.randomUUID()
        ofProductInfo(product)
    }

    private fun checkAuthority(team: Team, appUser: AppUser) {
        println("---start checkAuthority---")
    //if (!team.member.any { appUser.id == it.id })
      if (TeamUserTable.select { TeamUserTable.team.eq(team.id).and(TeamUserTable.user.eq(appUser.id)) }.empty())
            throw TeamAccessAuthorityNotException(appUser, team)
        println("---end checkAuthority---")
    }
}