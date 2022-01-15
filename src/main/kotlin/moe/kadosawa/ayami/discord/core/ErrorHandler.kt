package moe.kadosawa.ayami.discord.core

import moe.kadosawa.ayami.discord.errors.BadArgument
import moe.kadosawa.ayami.discord.errors.CheckFailure
import moe.kadosawa.ayami.discord.errors.CommandError
import moe.kadosawa.ayami.discord.errors.CommandInvokeError
import moe.kadosawa.ayami.extensions.await
import mu.KotlinLogging
import net.dv8tion.jda.api.interactions.commands.CommandInteraction

object ErrorHandler {
    private val logger = KotlinLogging.logger {}

    suspend fun handle(e: CommandError, context: CommandInteraction) {
        if (!context.isAcknowledged) {
            context.deferReply(true).await()
        }

        when (e) {
            is CommandInvokeError -> {
                logger.error(e) { "Unhandled error during command invocation" }
                context.hook.sendMessage("Something went wrong..").await()
            }

            is CheckFailure -> {
                val msg = e.message ?: "You can't run this command."
                context.hook.sendMessage(msg).await()
            }

            is BadArgument -> {
                val msg = e.message ?: "You have provided an invalid input."
                context.hook.sendMessage(msg).await()
            }
        }
    }
}